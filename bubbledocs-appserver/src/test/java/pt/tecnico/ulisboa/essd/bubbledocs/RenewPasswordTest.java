package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.*;
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
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidTokenException;
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
	private static String PASSWORD = "";
	private static String USERNAME;
	private static String USERNAME1= "ghj"; 
	private static String USER_TOKEN_INVALID=""; 
	private static String USER_LOCAL;
	private static String USER_LOCAL_TOKEN;
	
	 public void populate4Test() {

	    unPopulate4Test();
	   	
	   	Bubbledocs bd = Bubbledocs.getInstance();
	    	
	    	
	  	//Cria utilizador
	   	Utilizador user = createUser("mas", "maria@email.com", "Maria Santos");
	   	Utilizador userLocal = createUser("dip", "diogo@email.com", "Diogo Pires");
	
	   	USER_TOKEN = addUserToSession(user.getUsername());
	   	USERNAME = user.getUsername();
	   
	   	
	   	USER_LOCAL = userLocal.getUsername();
	   	USER_LOCAL_TOKEN = addUserToSession(userLocal.getUsername());
	   	
	   	USER_TOKEN_NOT_LOGGED = addUserToSession("dip");
    	turnTokenInvalid(USER_TOKEN_NOT_LOGGED);
	 }
	 
	@Mocked
	IDRemoteServices remote;
	
    @Test
    public void success() {
    	
    	new StrictExpectations(){
	    	
	    	{
	    		remote = new IDRemoteServices();
	    		remote.renewPassword(USERNAME);
	    	}
	    };
	    
	    RenewPasswordService service = new RenewPasswordService(USER_TOKEN);
	    service.execute();
	    
	    assertNull("O serviço retorna", service.getResult());
    }
    
    
	@Test(expected = UnavailableServiceException.class)
	public void remoteFails() {
	    
		new StrictExpectations(){
	    	
	    	{
	    		remote = new IDRemoteServices();
	    		remote.renewPassword(USERNAME);
	    		result = new RemoteInvocationException();
	    	}
	    };
	    
	    RenewPasswordService service = new RenewPasswordService(USER_TOKEN);
	    service.execute();
    }
    
    
    @Test(expected = UserNotInSessionException.class)
    public void userNotLogged() {
    	    
    	removeUserFromSession(USER_TOKEN);	   	    
	    RenewPasswordService service = new RenewPasswordService(USER_TOKEN);
	    service.execute();
    	
    }    
    
 
    @Test(expected = InvalidTokenException.class)
    public void userTokenInvalid() {
    	
	    RenewPasswordService service = new RenewPasswordService(USER_TOKEN_INVALID);
	    service.execute(); 
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void userNotFoundInRemoteService() {
    	
    	new StrictExpectations(){
	    	
	    	{
	    		remote = new IDRemoteServices();
	    		remote.renewPassword(USER_LOCAL);
	    		result = new LoginBubbleDocsException();
	    	}
	    };
	    
	    RenewPasswordService service = new RenewPasswordService(USER_LOCAL_TOKEN);
	    service.execute();
	    
    }
}
