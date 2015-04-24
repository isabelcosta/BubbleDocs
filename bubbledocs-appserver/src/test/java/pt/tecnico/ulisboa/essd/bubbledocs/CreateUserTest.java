package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.assertEquals;
import mockit.Mocked;
import mockit.StrictExpectations;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.CreateUserService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

public class CreateUserTest extends BubbleDocsServiceTest {

    // the tokens
    private String root;
    private String ars;

    private static final String USERNAME = "ars";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String EMAIL = "pedro@tecnico.ulisboa.pt";

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
        CreateUserService service = new CreateUserService(root, USERNAME_DOES_NOT_EXIST, EMAIL,
                "José Ferreira");
        service.execute();

	// User is the domain class that represents a User
        
        Utilizador user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);
        assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
        assertEquals(EMAIL, user.getEmail());
        assertEquals("José Ferreira", user.getNome());
    }

    @Test(expected = DuplicateUsernameException.class)
    public void usernameExists() {
    	
    	new StrictExpectations() {
    		{
    			remote = new IDRemoteServices();
    			remote.createUser(USERNAME, EMAIL);
    			result = new DuplicateUsernameException(USERNAME);
    		}
    	}; 
    	
    	
        CreateUserService service = new CreateUserService(root, USERNAME, EMAIL,"José Ferreira");
        service.execute();
    }

    @Test(expected = EmptyUsernameException.class)
    public void emptyUsername() {
    	
    	new StrictExpectations() {
    		{
    			remote = new IDRemoteServices();
    			remote.createUser("", EMAIL);
    			result = new EmptyUsernameException();
    			
    		}
    	}; 
    	
        CreateUserService service = new CreateUserService(root, "", EMAIL,"José Ferreira");
        service.execute();
    }
    
    
    @Test(expected = UnauthorizedOperationException.class)
    public void unauthorizedUserCreation() {
    	
    	
        CreateUserService service = new CreateUserService(ars, USERNAME, EMAIL,
                "José Ferreira");
        service.execute();
    }
    
    @Test(expected = UserNotInSessionException.class)
    public void accessUsernameNotExist() {
    	
        removeUserFromSession(root);
        CreateUserService service = new CreateUserService(root, USERNAME_DOES_NOT_EXIST, EMAIL,
                "José Ferreira");
        service.execute();
    }
    

    @Test(expected = InvalidUsernameException.class)
    public void InvalidUsername() {
    	
    	new StrictExpectations() {
    		{
    			remote = new IDRemoteServices();
    			remote.createUser("fabio", EMAIL);
    			result = new InvalidUsernameException();
    		}
    	}; 
    	
        CreateUserService service = new CreateUserService(root,"fabio",EMAIL,
                "José Ferreira");
        service.execute();
    }
    
    @Test(expected = InvalidEmailException.class)
    public void InvalidEmail() {
    
    	
    	new StrictExpectations() {
    		{
    			remote = new IDRemoteServices();
    			remote.createUser(USERNAME, "júlio");
    			result = new InvalidEmailException();
    		}
    	}; 
    	
        CreateUserService service = new CreateUserService(root,USERNAME,"júlio",
                "José Ferreira");
        service.execute();
    }
    
    @Test(expected = DuplicateEmailException.class)
    public void DuplicateEmail() {
    	
    	new StrictExpectations() {
    		{
    			remote = new IDRemoteServices();
    			remote.createUser(USERNAME, EMAIL);
    			result = new DuplicateEmailException();
    		}
    	}; 
    	
        CreateUserService service = new CreateUserService(root,USERNAME, EMAIL,
                "José Ferreira");
        service.execute();
    }
    
    @Test(expected = UnavailableServiceException.class)
    public void RemoteInvocation() {
    	
    	
    	new StrictExpectations() {
    		{
    			remote = new IDRemoteServices();
    			remote.createUser(USERNAME, EMAIL);
    			result = new RemoteInvocationException();
    		}
    	}; 
    	
        CreateUserService service = new CreateUserService(root,USERNAME,EMAIL,
                "José Ferreira");
        service.execute();
    }
}
