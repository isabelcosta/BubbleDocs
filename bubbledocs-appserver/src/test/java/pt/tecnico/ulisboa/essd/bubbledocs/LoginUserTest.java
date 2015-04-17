package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mockit.Mocked;
import mockit.StrictExpectations;

import org.joda.time.LocalTime;
import org.joda.time.Seconds;
import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.LoginUser;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

public class LoginUserTest extends BubbleDocsServiceTest {

    private static final String USERNAME = "jpa";
    private static final String PASSWORD = "jp#";

    @Override
    public void populate4Test() {
        createUser(USERNAME, PASSWORD, "João Pereira");
    }

    // returns the time of the last access for the user with token userToken.
    // It must get this data from the session object of the application
    private LocalTime getLastAccessTimeInSession(String userToken) {
    	for (Token token : Bubbledocs.getInstance().getTokensSet()) {
			if (token.getToken().equals(userToken)) {
				return token.getTime();
			}
		}
		return null;
    }
    @Mocked
	IDRemoteServices remote;
	
    //1
    @Test
    public void success() {

    	
    	new StrictExpectations() {
    		   
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, PASSWORD);
		    }
		};
    	
    	
    	LoginUser service = new LoginUser(USERNAME, PASSWORD);
        service.execute();
        
        
        String token = service.getUserToken();
		
        LocalTime currentTime = new LocalTime();
		
        Utilizador user = getUserFromSession(service.getUserToken());
        assertEquals(USERNAME, user.getUsername());

		int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();
		
		
		assertTrue("Access time in session not correctly set", difference >= 0);
		assertTrue("diference in seconds greater than expected", difference < 2);
    }
    
    //3
    @Test
    public void successLoginTwiceLocal() {
    	
    	new StrictExpectations() {
 		   
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, PASSWORD);
		    }
		};
    	
    	
    	
        LoginUser service = new LoginUser(USERNAME, PASSWORD);
        service.execute();
        
        String token1 = service.getUserToken();
        
    	
    	new StrictExpectations() {
 		   
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, PASSWORD);
		    }
		};
    		
    		
    		
        service.execute();
        
        String token2 = service.getUserToken();

        Utilizador user = getUserFromSession(token1);
        assertNull(user);
        user = getUserFromSession(token2);
        assertEquals(USERNAME, user.getUsername());
    }
    
    //2
    @Test
    public void successLoginTwice() {
    	
    	new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, PASSWORD);

    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, PASSWORD);
			    }
    		};
    		
    		
        LoginUser service = new LoginUser(USERNAME, PASSWORD);
        service.execute();
        
        String token1 = service.getUserToken();

        service.execute();
        
        String token2 = service.getUserToken();

        Utilizador user = getUserFromSession(token1);
        assertNull(user);
        user = getUserFromSession(token2);
        assertEquals(USERNAME, user.getUsername());
    }
    
    //4
    @Test(expected = LoginBubbleDocsException.class)	// dar um USER invalido
    public void loginUnknownUser() {
    	
    	new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser("jp2", PASSWORD);
    			result = new LoginBubbleDocsException();
		    }
		};
    		
    		
        LoginUser service = new LoginUser("jp2", PASSWORD);
        service.execute();	// o remote tem que lancar a excessao, logo -> mockit
    }
    
    //7
    @Test(expected = LoginBubbleDocsException.class)		// dar a PASSWORD errada
    public void loginUserWithinWrongPassword() {
    	
    	new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, "jp2");
    			result = new LoginBubbleDocsException();
		    }
		};
    	
    	
    	
    	
        LoginUser service = new LoginUser(USERNAME, "jp2");
        service.execute();
    }
    
    //5
    @Test(expected = UnavailableServiceException.class)		// quando faz a verificao da password usando a local e é diferente
    public void loginWrongPasswordLocal() {
    	
    	new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, PASSWORD);
		    }
		};
    		
    	
    	
        LoginUser service = new LoginUser(USERNAME, PASSWORD);
        service.execute();
        
        new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, "errada");
    			result = new RemoteInvocationException();
		    }
		};
    	LoginUser service1 = new LoginUser(USERNAME, "errada");
        service1.execute();
    }

    
    
    @Test(expected = UnavailableServiceException.class)		// quando faz a verificao da password e nao tem password local
    public void loginNoLocalPassword() {
    	
    	new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, PASSWORD);
    			result = new RemoteInvocationException();
		    }
		};
        LoginUser service = new LoginUser(USERNAME, PASSWORD);
        service.execute();
    }
    
    
    
}
