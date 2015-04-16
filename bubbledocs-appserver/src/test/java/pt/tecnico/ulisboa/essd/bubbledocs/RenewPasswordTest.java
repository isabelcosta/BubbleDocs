package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.StrictExpectations;

import org.joda.time.LocalDate;
import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ProtectedCellException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.NotLiteralException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.DeleteUser;
import pt.tecnico.ulisboa.essd.bubbledocs.services.RenewPasswordService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.CreateSpreadSheet;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

// add needed import declarations

public class RenewPasswordTest extends BubbleDocsServiceTest {

	private static String USER_TOKEN;
	private static final String USER_TOKEN_NOT_EXISTS="bla";
	private static  String USER_TOKEN_NOT_LOGGED;
	
	
	 public void populate4Test() {

	    unPopulate4Test();
	   	
	   	Bubbledocs bd = Bubbledocs.getInstance();
	    	
	    	
	  	//Cria utilizador
	   	Utilizador user = createUser("mas", "maria@email.com", "Maria Santos");
	
	   	USER_TOKEN = addUserToSession(user.getUsername());
	   	
	 }
	 
	@Mocked
	IDRemoteServices remote;
	
//    @Test
//    public void success() {
//    	
//        RenewPasswordService service = new RenewPasswordService( USER_TOKEN);
//        service.execute();
//    }
//    
    
	@Test(expected = UserNotInSessionException.class)
	public void remoteFails() {
	    
		new StrictExpectations(){
	    	
	    	{
	    		remote = new IDRemoteServices();
	    		remote.renewPassword(USER_TOKEN);
	    		result = new RemoteInvocationException();
	    	}
	    };
    }
    
    
//    @Test(expected = UserNotInSessionException.class)
//    public void userNotLogged() {
//    	
//    	new MockUp<IDRemoteServices>() {
//    		@Mock
//   			public void renewPassword(String username){
//   				throw new LoginBubbleDocsException();
//   			}
//    	};
//    	
//   		new RenewPasswordService(USER_TOKEN).execute();
//    	
//    }    
    
 
//    @Test(expected = UserNotInSessionException.class)
//    public void userTokenInvalid() {
//    	
//    	new MockUp<IDRemoteServices>() {
//    		@Mock
//   			public void renewPassword(String username){
//   				throw new RemoteInvocationException();
//   			}
//    	};
//    	
//   		new RenewPasswordService(USER_TOKEN).execute();
//    }     
    
}
