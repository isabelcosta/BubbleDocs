package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mockit.Mocked;
import mockit.StrictExpectations;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.DeleteUser;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

// add needed import declarations

public class DeleteUserTest extends BubbleDocsServiceTest {

    private static final String USERNAME_TO_DELETE = "smf";
    private static final String USERNAME = "ars";
    private static final String PASSWORD = "ars";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String SPREADSHEET_NAME = "spread";

    // the tokens for user root
    private String root;
    
    // the mock class
    @Mocked
    IDRemoteServices remote;
    
    @Override
    public void populate4Test() {
        createUser(USERNAME, PASSWORD, "António Rito Silva");
        Utilizador smf = createUser(USERNAME_TO_DELETE, "smf", "Sérgio Fernandes");
        createSpreadSheet(smf, USERNAME_TO_DELETE, 20, 20);

        root = addUserToSession(ROOT_USERNAME);
    };

    public void success() {
    	
    	new StrictExpectations() {{
    		remote = new IDRemoteServices();
    		remote.removeUser(USERNAME_TO_DELETE);
    	}};
    	
        DeleteUser service = new DeleteUser(root, USERNAME_TO_DELETE);
        service.execute();

        boolean deleted = getUserFromUsername(USERNAME_TO_DELETE) == null;

        assertTrue("user was not deleted", deleted);

        assertNull("Spreadsheet was not deleted",
                getSpreadSheet(SPREADSHEET_NAME));
    }

    /*
     * accessUsername exists, is in session and is root toDeleteUsername exists
     * and is not in session
     */
   @Test
    public void successToDeleteIsNotInSession() {
        success();
    }

    /*
     * accessUsername exists, is in session and is root toDeleteUsername exists
     * and is in session Test if user and session are both deleted
     */
    @Test
    public void successToDeleteIsInSession() {
        String token = addUserToSession(USERNAME_TO_DELETE);
        success();
	assertNull("Removed user but not removed from session", getUserFromSession(token));
    }
    
   /*
    * remote invocation fails for some reason: connection, etc
    * 
    */
    @Test(expected = UnavailableServiceException.class)
    public void remoteInvocationFails() throws Exception{
  	
     	new StrictExpectations() {{
    		remote = new IDRemoteServices();
    		remote.removeUser(USERNAME_TO_DELETE);
    		result = new RemoteInvocationException();
    	}};
    
    	new DeleteUser(root, USERNAME_TO_DELETE).execute();	
    }

   
    /*
     * the user that we want delete doesn't exist remotely
     */
    @Test(expected = LoginBubbleDocsException.class)
    public void userToDeleteDoesNotExist() {
   
     	new StrictExpectations() {{
    		remote = new IDRemoteServices();
    		remote.removeUser(USERNAME_DOES_NOT_EXIST);
    		result = new LoginBubbleDocsException();
    	}};
    	
        new DeleteUser(root, USERNAME_DOES_NOT_EXIST).execute();
    }


    @Test(expected = UnauthorizedOperationException.class)
    public void notRootUser() {
    	
        String ars = addUserToSession(USERNAME);
        new DeleteUser(ars, USERNAME_TO_DELETE).execute();
    }

    
    @Test(expected = UserNotInSessionException.class)
    public void rootNotInSession() {
        removeUserFromSession(root);
        
        new DeleteUser(root, USERNAME_TO_DELETE).execute();
    }
    
    
    @Test(expected = UserNotInSessionException.class)
    public void notInSessionAndNotRoot() {
        String ars = addUserToSession(USERNAME);
        removeUserFromSession(ars);
        
        new DeleteUser(ars, USERNAME_TO_DELETE).execute();
    }

    
    @Test(expected = UserNotInSessionException.class)
    public void accessUserDoesNotExist() {
    	
        new DeleteUser(USERNAME_DOES_NOT_EXIST, USERNAME_TO_DELETE).execute();
    }
    
    @Test(expected = EmptyUsernameException.class)
    public void userToDeleteIsEmpty(){
    	
        new DeleteUser(root, "").execute();
    }
}
