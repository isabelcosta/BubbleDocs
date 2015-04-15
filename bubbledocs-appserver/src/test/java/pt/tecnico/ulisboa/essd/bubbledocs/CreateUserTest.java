package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;

import mockit.Mock;
import mockit.MockUp;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
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
import pt.tecnico.ulisboa.essd.bubbledocs.services.CreateUser;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

// add needed import declarations

public class CreateUserTest extends BubbleDocsServiceTest {

    // the tokens
    private String root;
    private String ars;

    private static final String USERNAME = "ars";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String EMAIL = "pedro@tecnico.ulisboa.pt";

    @Override
    public void populate4Test() {
        createUser(USERNAME, EMAIL, "António Rito Silva");
        root = addUserToSession("root");
        ars = addUserToSession("ars");
    }

    @Test
    public void success() {
        CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST, EMAIL,
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
    	
    	new MockUp<IDRemoteServices>() {

    		@Mock
    		public String createUser(String username, String email){
    			throw new  DuplicateUsernameException(username);
    		}
    	};

        CreateUser service = new CreateUser(root, USERNAME, EMAIL,"José Ferreira");
        service.execute();
    }

    @Test(expected = EmptyUsernameException.class)
    public void emptyUsername() {
    	
    	new MockUp<IDRemoteServices>() {

    		@Mock
    		public String createUser(String username, String email){
    			throw new  EmptyUsernameException();
    		}
    	};
    	
        CreateUser service = new CreateUser(root, "", EMAIL,"José Ferreira");
        service.execute();
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void unauthorizedUserCreation() {
    	
    	new MockUp<IDRemoteServices>() {

    		@Mock
    		public String createUser(String username, String email){
    			throw new  UnauthorizedOperationException();
    		}
    	};
    	
        CreateUser service = new CreateUser(ars, USERNAME_DOES_NOT_EXIST, EMAIL,
                "José Ferreira");
        service.execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void accessUsernameNotExist() {
    	
    	new MockUp<IDRemoteServices>() {

    		@Mock
    		public String createUser(String username, String email){
    			throw new UserNotInSessionException(username) ;
    		}
    	};
    	
        removeUserFromSession(root);
        CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST, EMAIL,
                "José Ferreira");
        service.execute();
    }
    

    @Test(expected = InvalidUsernameException.class)
    public void InvalidUsername() {
    	
    	new MockUp<IDRemoteServices>() {

    		@Mock
    		public String createUser(String username, String email){
    			throw new InvalidUsernameException() ;
    		}
    	};
    	
        CreateUser service = new CreateUser(root,"pedro",EMAIL,
                "José Ferreira");
        service.execute();
    }
    
    @Test(expected = InvalidEmailException.class)
    public void InvalidEmail() {
    	
    	new MockUp<IDRemoteServices>() {

    		@Mock
    		public String createUser(String username, String email){
    			throw new InvalidEmailException() ;
    		}
    	};
    	
        CreateUser service = new CreateUser(root,USERNAME,"fabio@tecnico.ulisboa.pt",
                "José Ferreira");
        service.execute();
    }
    
    @Test(expected = DuplicateEmailException.class)
    public void DuplicateEmail() {
    	
    	new MockUp<IDRemoteServices>() {

    		@Mock
    		public String createUser(String username, String email){
    			throw new DuplicateEmailException();
    		}
    	};
    	
        CreateUser service = new CreateUser(root,USERNAME,"pedro@tecnico.ulisboa.pt",
                "José Ferreira");
        service.execute();
    }
    
    @Test(expected = RemoteInvocationException.class)
    public void RemoteInvocation() {
    	
    	new MockUp<IDRemoteServices>() {

    		@Mock
    		public String createUser(String username, String email){
    			throw new UnavailableServiceException();
    		}
    	};
    	
        CreateUser service = new CreateUser(root,USERNAME,EMAIL,
                "José Ferreira");
        service.execute();
    }

}
