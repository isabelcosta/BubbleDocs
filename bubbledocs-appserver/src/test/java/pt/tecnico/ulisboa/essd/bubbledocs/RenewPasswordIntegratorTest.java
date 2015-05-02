package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.*;
import mockit.Mocked;
import mockit.StrictExpectations;

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
	
    @Test
    public void success() {
    	
    	new StrictExpectations(){
	    	
	    	{
	    		remote = new IDRemoteServices();
	    		remote.renewPassword(USERNAME);
	    	}
	    };
    
	    RenewPasswordIntegrator local = new RenewPasswordIntegrator(USER_TOKEN);
	    local.execute();
    	

	    Utilizador user = getUserFromSession(USER_TOKEN);

	    assertNull("Password do User", user.getPassword());
	    
    	
    }
    

    //aFALTA TESTE PARA RESTAURO DA PPASS NO CASO DA EXCEPCAO DO LOGIN
    
	//@Test(expected = UnavailableServiceException.class)
	public void remoteFails() {

    	
    	/*
    	 * <QUESTION> 
    	 * 		!!		Verificar se existe uma maneira melhor de verificar 
    	 * 		!!		que a password foi restaurada e que lancou a excecao
    	 * 
    	 */
    	
    	
		new StrictExpectations(){
	    	
	    	{
	    		remote = new IDRemoteServices();
	    		remote.renewPassword(USERNAME);
	    		result = new RemoteInvocationException();
	    	}
	    };
	    
	    Utilizador user = getUserFromSession(USER_TOKEN);
	    
	    String oldPassword = user.getPassword();
	    String newPassword;
	    boolean flag = false;
	    try {
	    	RenewPasswordIntegrator local = new RenewPasswordIntegrator(USER_TOKEN);
	    	local.execute();
	    }catch(UnavailableServiceException e) {
	    	flag = true;
		    //throw new UnavailableServiceException();
	    } finally {
		    newPassword = user.getPassword();
		    assertEquals(oldPassword, newPassword);	 
		    assertEquals(flag, true);
	    } 
    }
    
    
    @Test(expected = UserNotInSessionException.class)
    public void userNotLogged() {
    	    
    	removeUserFromSession(USER_TOKEN);	   	    
	    
	    RenewPasswordIntegrator local = new RenewPasswordIntegrator(USER_TOKEN);
	    local.execute();
    	
    }    
    
 
    @Test(expected = InvalidTokenException.class)
    public void userTokenInvalid() {
    	
    	RenewPasswordIntegrator local = new RenewPasswordIntegrator(USER_TOKEN_INVALID);
	    local.execute();
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void userNotFoundInRemoteService() {
    
    	
    	
    	
    	/*
    	 * <QUESTION> 
    	 * 		!!		Verificar se existe uma maneira melhor de verificar 
    	 * 		!!		que a password foi restaurada e que lancou a excecao
    	 * 
    	 */
    	
    	
    	
    	
    	new StrictExpectations(){
	    	
	    	{
	    		remote = new IDRemoteServices();
	    		remote.renewPassword(USER_LOCAL);
	    		result = new LoginBubbleDocsException();
	    	}
	    };
	    
	    Utilizador user = getUserFromSession(USER_LOCAL_TOKEN);
	    
	    String oldPassword = user.getPassword();
	    String newPassword = null;
	    
	    try {
	    	RenewPasswordIntegrator local = new RenewPasswordIntegrator(USER_LOCAL_TOKEN);
	    	local.execute();
	    } catch (LoginBubbleDocsException e) {
	    	newPassword = user.getPassword();
	    	assertEquals(oldPassword, newPassword);
	    	throw new LoginBubbleDocsException();
	    }
	    
    }
    
    
    
    
    
    
    
    
    
}
