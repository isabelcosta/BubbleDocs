package pt.tecnico.ulisboa.essd.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import mockit.Mocked;
import mockit.StrictExpectations;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.CreateUserIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.CreateUserService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

public class CreateUserIntegratorTest extends BubbleDocsServiceTest {

    // the tokens
    private String root;
    private String ars;

    private static final String USERNAME = "ars";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String EMAIL = "pedro@tecnico.ulisboa.pt";
    private static final String EMAIL_INEXISTENTE = "este@mail.nao.existe";


    @Override
    public void populate4Test() {
        createUser(USERNAME, EMAIL, "António Rito Silva");
        root = addUserToSession("root");
        ars = addUserToSession("ars");
    }
    
    @Mocked
    IDRemoteServices remote; 
    
    @Test
    public void success() {
    	
    	new StrictExpectations() {
    		{
    			remote = new IDRemoteServices();
    			remote.createUser(USERNAME_DOES_NOT_EXIST, EMAIL_INEXISTENTE);
    			
    		}
    	}; 

    	CreateUserIntegrator integrator = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, EMAIL_INEXISTENTE, "José Ferreira");
        integrator.execute();
        
        Utilizador user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);
        assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
        assertEquals(EMAIL_INEXISTENTE, user.getEmail());
        assertEquals("José Ferreira", user.getNome());
    }
    
    /*
     * Testar localmente se o username é Duplicado
     * 
     */

    @Test(expected = DuplicateUsernameException.class)
    public void duplicatedUserNameLocalTest() {
    	
        CreateUserIntegrator integrator= new CreateUserIntegrator(root, USERNAME, EMAIL_INEXISTENTE,"José Ferreira");
        integrator.execute();
        
        Utilizador user = getUserFromUsername(USERNAME);
        assertNull(user);
        
        
    }
    
    /*
     * Testar localmente se o username vazio
     * 
     */

    @Test(expected = InvalidUsernameException.class)
    public void emptyUsernameLocalTest() {
    	
    	 CreateUserIntegrator integrator= new CreateUserIntegrator(root,"", EMAIL_INEXISTENTE,"José Ferreira");
         integrator.execute();
         
         Utilizador user = getUserFromUsername("");
         assertNull(user);
    }
    
    /*
     * Testar remotamente se o username vazio
     * 
     */

    
    @Test(expected = DuplicateEmailException.class)
    public void DuplicateEmailRemoteTest() {
    	
    	new StrictExpectations() {
    		{
    			remote = new IDRemoteServices();
    			remote.createUser(USERNAME_DOES_NOT_EXIST, EMAIL_INEXISTENTE);
    			result = new DuplicateEmailException();
    		}
    	}; 
    	
    	 CreateUserIntegrator integrator= new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, EMAIL_INEXISTENTE,"José Ferreira");
         integrator.execute();
         
         Utilizador user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);
         assertNull(user);
         
    }
    
    @Test(expected = DuplicateUsernameException.class)
    public void DuplicateUserNameRemoteTest() {
    	
    	new StrictExpectations() {
    		{
    			remote = new IDRemoteServices();
    			remote.createUser(USERNAME_DOES_NOT_EXIST, EMAIL_INEXISTENTE);
    			result = new DuplicateUsernameException(USERNAME_DOES_NOT_EXIST);
    		}
    	}; 
    	
    	 CreateUserIntegrator integrator= new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, EMAIL_INEXISTENTE,"José Ferreira");
         integrator.execute();
         
         Utilizador user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);
         assertNull(user);
         
    }
    
    @Test(expected = UnauthorizedOperationException.class)
    public void unauthorizedUserCreation() {
    	
    	 CreateUserIntegrator integrator= new CreateUserIntegrator(ars, USERNAME, EMAIL,"José Ferreira");
         integrator.execute();
         
         Utilizador user = getUserFromUsername(USERNAME);
         assertNull(user);
      
    }
    
    @Test(expected = UserNotInSessionException.class)
    public void accessUsernameNotExist() {
    	
        removeUserFromSession(root);
        CreateUserService service = new CreateUserService(root, USERNAME_DOES_NOT_EXIST, EMAIL,
                "José Ferreira");
        service.execute();
        
        Utilizador user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);
        assertNull(user);
    }
    
    /*
     * Testar localmente se o username é inválido 
     * 
     */
    

    @Test(expected = InvalidUsernameException.class)
    public void InvalidUsernameLocalTest() {
    	
 
    	
        CreateUserIntegrator integrator= new CreateUserIntegrator(root,"","braz@ist.utl.pt","José Ferreira");
        integrator.execute();
        
        Utilizador user = getUserFromUsername("");
        assertNull(user);
        
    }
    
    /*
     * Testar localmente se o email é inválido 
     * 
     */
    
    @Test(expected = InvalidEmailException.class)
    public void InvalidEmailLocalTest() {
    	
        CreateUserIntegrator integrator = new CreateUserIntegrator(root,USERNAME_DOES_NOT_EXIST,"","José Ferreira");
        integrator.execute();
        
        Utilizador user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);
        assertNull(user);
    }
    
    /*
     * Testar localmente se o email é Duplicado
     * 
     */
    
    
    @Test(expected = DuplicateEmailException.class)
    public void DuplicateEmailLocalTest() {
    	
    	CreateUserIntegrator integrator = new CreateUserIntegrator(root, "Pedro", EMAIL,"José Ferreira");
        integrator.execute();
        
        Utilizador user = getUserFromUsername("Pedro");
        assertNull(user);
    }
    
    /*
     * Invocação remota falha
     * 
     */
    
    @Test(expected = UnavailableServiceException.class)
    public void RemoteInvocation() {
    	
    	
    	new StrictExpectations() {
    		{
    			remote = new IDRemoteServices();
    			remote.createUser(USERNAME_DOES_NOT_EXIST, EMAIL_INEXISTENTE);
    			result = new RemoteInvocationException();
    		}
    	}; 
    	
        CreateUserIntegrator integrator = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST,EMAIL_INEXISTENTE,"José Ferreira");
        integrator.execute();
        
        Utilizador user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);
        assertNull(user);
    }
}