package pt.tecnico.ulisboa.essd.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.LoginUserIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

public class LoginUserIntegratorTest extends BubbleDocsServiceTest {

    private static final String USERNAME = "jpa";
    private static final String PASSWORD = "jp#";
    private static final String NAME = "João Pereira";

    @Override
    public void populate4Test() {
        createUser(USERNAME, PASSWORD, NAME);
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
    	
    	
    	LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
        service.execute();
        
        
        String token = service.getUserToken();
		
        LocalTime currentTime = new LocalTime();
		
        Utilizador user = getUserFromSession(service.getUserToken());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(PASSWORD, user.getPassword());

		int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();
		
		
		assertTrue("Access time in session not correctly set", difference >= 0);
		assertTrue("diference in seconds greater than expected", difference < 2);
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
    		
    		
        LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
        service.execute();
        
        String token1 = service.getUserToken();

        service.execute();
        
        String token2 = service.getUserToken();

        Utilizador user = getUserFromSession(token1);
        assertNull(user);
        user = getUserFromSession(token2);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(PASSWORD, user.getPassword());
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
    	
    	
    	
        LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
        service.execute();
        
        String token1 = service.getUserToken();
        
        Utilizador user = getUserFromSession(token1);
        user = getUserFromSession(token1);
        assertEquals(PASSWORD, user.getPassword());
        
        
    	
    	new StrictExpectations() {
 		   
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, PASSWORD);
    			result = new RemoteInvocationException();
		    }
		};
    		
    		
    		
        service.execute();
        
        String token2 = service.getUserToken();

        user = getUserFromSession(token1);
        assertNull(user);
        user = getUserFromSession(token2);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(PASSWORD, user.getPassword());
        
    }
    
    //4
    @Test												 	// remote em baixo e não consegue
    public void remoteExceptionOnce() {						// fazer login pois a pw nunca foi updated
    	
    	new StrictExpectations() {
 		   
    	
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, PASSWORD);
    			result = new RemoteInvocationException();
		    }
		};
    	
    	
    	
        LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
        try {
        	service.execute();
        	fail();
        }catch (UnavailableServiceException e) {
        	String token1 = service.getUserToken();
            Utilizador user = getUserFromSession(token1);
            assertNull("Verificar que o user não ficou logado", user);
        }
        
        
        
        
    }
    
    //5
    @Test		// quando faz a verificao da password usando a local e é diferente
    public void loginWrongPasswordLocal() {
    	
    	new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, PASSWORD);
		    }
		};
    		
    	
    	
        LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, PASSWORD);
        service.execute();
        
        String token1 = service.getUserToken();
        
        Utilizador user = getUserFromSession(token1);
        user = getUserFromSession(token1);
        assertEquals(PASSWORD, user.getPassword());
        
        new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, "errada");
    			result = new RemoteInvocationException();
		    }
		};
    	LoginUserIntegrator service1 = new LoginUserIntegrator(USERNAME, "errada");

    	try {
        	service1.execute();
        	fail();
        }catch (UnavailableServiceException e) {
        	token1 = service1.getUserToken();
            user = getUserFromSession(token1);
            assertNull("Verificar que o user não ficou logado", user);
        }
    }
    
    
    
    //6
    @Test												// dar a PASSWORD null
    public void loginUserWithinNullPassword() {
    	
    	new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, null);
    			result = new LoginBubbleDocsException();
		    }
		};
    	
    	
    	
 
        LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, null);
        
        try {
        	service.execute();
        	fail();
        }catch (LoginBubbleDocsException e) {
        	String token = service.getUserToken();
            Utilizador user = getUserFromSession(token);
            assertNull("Verificar que o user não ficou logado", user);
        }
    }
    
    //7
    @Test												// dar a PASSWORD errada
    public void loginUserWithinWrongPassword() {
    	
    	new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(USERNAME, "jp2");
    			result = new LoginBubbleDocsException();
		    }
		};
    	
    	
    	
 
        LoginUserIntegrator service = new LoginUserIntegrator(USERNAME, "jp2");
        
        try {
        	service.execute();
        	fail();
        }catch (LoginBubbleDocsException e) {
        	String token = service.getUserToken();
            Utilizador user = getUserFromSession(token);
            assertNull("Verificar que o user não ficou logado", user);
        }
    }
    
    //8
    @Test											// dar um USER invalido
    public void loginInvalidUserLocal() {
    	
    	new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(null, PASSWORD);
    			result = new RemoteInvocationException();
		    }
		};
    		
    		
        LoginUserIntegrator service = new LoginUserIntegrator(null, PASSWORD);
        
        try {
        	service.execute();
        	fail();
        }catch (UnavailableServiceException e) {
        	String token = service.getUserToken();
            Utilizador user = getUserFromSession(token);
            assertNull("Verificar que não encontrou o user", user);
        }
    }
    
    
    //9
    @Test											// dar um USER inexistente
    public void loginUnknownUserLocal() {
    	
    	new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser("jp2", PASSWORD);
    			result = new RemoteInvocationException();
		    }
		};
    		
    		
        LoginUserIntegrator service = new LoginUserIntegrator("jp2", PASSWORD);
        
        try {
        	service.execute();
        	fail();
        }catch (UnavailableServiceException e) {
        	String token = service.getUserToken();
            Utilizador user = getUserFromSession(token);
            assertNull("Verificar que não encontrou o user", user);
        }
    }
    
  
    
    //10
    @Test											// dar um USER inválido
    public void loginInvalidUser() {
    	
    	new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(null, PASSWORD);
    			result = new LoginBubbleDocsException();
		    }
		};
    		
    		
        LoginUserIntegrator service = new LoginUserIntegrator(null, PASSWORD);
        
        try {
        	service.execute();
        	fail();
        }catch (LoginBubbleDocsException e) {
        	String token = service.getUserToken();
            Utilizador user = getUserFromSession(token);
            assertNull("Verificar que não encontrou o user", user);
        }
    }
    
    //11
    @Test											// dar um USER invalido
    public void loginUnknownUser() {
    	
    	new StrictExpectations() {
    		
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser("jp2", PASSWORD);
    			result = new LoginBubbleDocsException();
		    }
		};
    		
    		
        LoginUserIntegrator service = new LoginUserIntegrator("jp2", PASSWORD);
        
        try {
        	service.execute();
        	fail();
        }catch (LoginBubbleDocsException e) {
        	String token = service.getUserToken();
            Utilizador user = getUserFromSession(token);
            assertNull("Verificar que não encontrou o user", user);
        }
    }
    
}
