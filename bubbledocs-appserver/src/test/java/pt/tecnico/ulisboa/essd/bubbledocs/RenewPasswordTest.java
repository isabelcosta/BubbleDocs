package pt.tecnico.ulisboa.essd.bubbledocs;

import java.sql.ResultSet;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.RenewPasswordService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

// add needed import declarations

public class RenewPasswordTest extends BubbleDocsServiceTest {

	private static final String USER_TOKEN = "bla bla bla";
	private static final String USER_TOKEN_NOT_LOGGED = "bla bla bla";

	@Mocked
	IDRemoteServices myService;
	
    @Test
    public void success() {
    	
        RenewPasswordService service = new RenewPasswordService( USER_TOKEN);
        service.execute();
    }
   
    @Test
    public void remoteFail(){        
    	
    	new Expectations(){{
    		myService.renewPassword(USER_TOKEN); result = new RemoteInvocationException();
    		
    	}};
    	
    	RenewPasswordService service = new RenewPasswordService( USER_TOKEN);
    	service.execute();
    }
    
    
    @Test(expected = UserNotInSessionException.class)
    public void userNotLogged() {
    	
    	 removeUserFromSession(USER_TOKEN);
    	 RenewPasswordService service = new RenewPasswordService( USER_TOKEN);
         service.execute();
    }    
 
    @Test(expected = UserNotInSessionException.class)
    public void userTokenInvalid() {
    	
    	 removeUserFromSession(USER_TOKEN);
    	 RenewPasswordService service = new RenewPasswordService( USER_TOKEN_NOT_LOGGED);
         service.execute();
    }   
    

//    @Test
//    public <M extends Runnable & ResultSet> void someTest() {
//        M mock = new MockUp<M>() {
//           @Mock void run() { ...do something... }
//           @Mock boolean next() { return true; }
//        }.getMockInstance();
//
//        mock.run();
//        assertTrue(mock.next());
//    }
//    
//
//    @Test(expected = UnavailableServiceException.class)
//    public <RenewPasswordService extends Runnable> void userTokenInvalidTest() {
//    	RenewPasswordService mock = new MockUp<RenewPasswordService>() {
//           @Mock void execute() {  }
//           @Mock String getResult() { ; }
//        }.getMockInstance();
//
//        mock.run();
//        assertTrue(mock.next());
//    }
    
}
