package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.*;
import mockit.*;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.RenewPasswordIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.RenewPasswordService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;


public class RenewPasswordIntegratorTest extends BubbleDocsServiceTest {

	private static String USER_TOKEN;
	private static String USER_TOKEN_NOT_LOGGED;
	private static String USERNAME;
	private static String USER_TOKEN_INVALID=""; 
	private static String USER_LOCAL;
	private static String USER_LOCAL_TOKEN;
	
	 public void populate4Test() {

	    unPopulate4Test();
	   	
	  	//Cria utilizador
	   	Utilizador user = createUser("mas", "maria@email.com", "Maria Santos");
	   	Utilizador userLocal = createUser("dip", "diogo@email.com", "Diogo Pires");
	
	   	USER_TOKEN = addUserToSession(user.getUsername());
	   	USERNAME = user.getUsername();
	   
	   	
	   	USER_LOCAL = userLocal.getUsername();
	   	USER_LOCAL_TOKEN = addUserToSession(userLocal.getUsername());
	   	
	   	USER_TOKEN_NOT_LOGGED = addUserToSession("dip");
    	turnTokenInvalid(USER_TOKEN_NOT_LOGGED);
    	
    	user.setPassword("123");
	 }
	 
	@Mocked
	IDRemoteServices remote;
	
	
	//1
    @Test
    public void success() {
    	
    	new StrictExpectations(){
	    	
	    	{
	    		remote = new IDRemoteServices();
	    		remote.renewPassword(USERNAME);
	    	}
	    };
    
	    RenewPasswordIntegrator service = new RenewPasswordIntegrator(USER_TOKEN);
	    service.execute();
    	

	    Utilizador user = getUserFromSession(USER_TOKEN);

	    assertNull("Password do User", user.getPassword());
	    
    	
    }
    
    //2
    @Test(expected = UserNotInSessionException.class)
    public void userNotLogged() {
    	    
    	removeUserFromSession(USER_TOKEN);	   	    
	    
	    RenewPasswordIntegrator service = new RenewPasswordIntegrator(USER_TOKEN);
	    service.execute();
    	
    }    
    
    //3
    @Test(expected = InvalidTokenException.class)
    public void userTokenInvalid() {
    	
    	RenewPasswordIntegrator service = new RenewPasswordIntegrator(USER_TOKEN_INVALID);
    	service.execute();
    }
    
    //4
	@Test
	public void remoteFails() {

		new StrictExpectations(){
	    	{
	    		remote = new IDRemoteServices();
	    		remote.renewPassword(USERNAME);
	    		result = new RemoteInvocationException();
	    	}
	    };
	    
	    Utilizador user = getUserFromSession(USER_TOKEN);
	    user.setPassword("asdfasdf");
	    String oldPassword = user.getPassword();
	    String newPassword;

	    RenewPasswordIntegrator service = new RenewPasswordIntegrator(USER_TOKEN);
	    try {
	    	service.execute();
	    	fail();
	    } catch(UnavailableServiceException exs) {
	    	newPassword = user.getPassword();
	    	assertEquals(oldPassword, newPassword);	 
	    }
	    
	}
 
   
    
    //5
    @Test
    public void userNotFoundInRemoteService() {
    
    	new StrictExpectations(){
	    	
	    	{
	    		remote = new IDRemoteServices();
	    		remote.renewPassword(USER_LOCAL);
	    		result = new LoginBubbleDocsException();
	    	}
	    };
	    
	    Utilizador user = getUserFromSession(USER_LOCAL_TOKEN);
	    user.setPassword("asdfasdf");
	    String oldPassword = user.getPassword();
	    String newPassword;
	    
	    try {
	    	RenewPasswordIntegrator service = new RenewPasswordIntegrator(USER_LOCAL_TOKEN);
	    	service.execute();
	    	fail();
	    } catch (LoginBubbleDocsException e) {
	    	newPassword = user.getPassword();
	    	assertEquals(oldPassword, newPassword);	 
	    }
	    
    }
    
}
