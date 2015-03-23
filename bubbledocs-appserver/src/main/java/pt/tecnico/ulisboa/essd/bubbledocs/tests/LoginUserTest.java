package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.joda.time.LocalTime;
import org.joda.time.Seconds;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.WrongPasswordException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.LoginUser;
import pt.tecnico.ulisboa.essd.bubbledocs.tests.BubbleDocsServiceTest;

// add needed import declarations

public class LoginUserTest extends BubbleDocsServiceTest {

    private String jp; // the token for user jp
    private String root; // the token for user root

    private static final String USERNAME = "jp";
    private static final String PASSWORD = "jp#";

    @Override
    public void populate4Test() {
        createUser(USERNAME, PASSWORD, "João Pereira");
    }

    // returns the time of the last access for the user with token userToken.
    // It must get this data from the session object of the application
    private LocalTime getLastAccessTimeInSession(String userToken) {
    	for (Token token : Bubbledocs.getInstance().getTokensSet()) {
			if (token.getUsername().equals(userToken)) {
				return token.getTime();
			}
		}
		return null;
    }

    @Test
    public void success() {
        LoginUser service = new LoginUser(USERNAME, PASSWORD);
        service.execute();
        String token = service.getUserToken();
        Bubbledocs.getInstance().addTokens(new Token(USERNAME, token));
		
        LocalTime currentTime = new LocalTime();
		
        String ss = service.getUserToken();
        Utilizador user = getUserFromSession(service.getUserToken());
        assertEquals(USERNAME, user.getUsername());

        System.out.println("asdf");
		int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();
	
		assertTrue("Access time in session not correctly set", difference >= 0);
		assertTrue("diference in seconds greater than expected", difference < 2);
    }

    @Test
    public void successLoginTwice() {
        LoginUser service = new LoginUser(USERNAME, PASSWORD);

        service.execute();
        Bubbledocs.getInstance().addTokens(new Token(USERNAME, service.getUserToken()));
        String token1 = service.getUserToken();

        service.execute();
        String token2 = service.getUserToken();

        Utilizador user = getUserFromSession(token1);
        assertNull(user);
        user = getUserFromSession(token2);
        assertEquals(USERNAME, user.getUsername());
    }
    
    /*
    @Test(expected = UnknownBubbleDocsUserException.class)
    public void loginUnknownUser() {
        LoginUser service = new LoginUser("jp2", "jp");
        service.execute();
    }
    */

    @Test(expected = WrongPasswordException.class)
    public void loginUserWithinWrongPassword() {
        LoginUser service = new LoginUser(USERNAME, "jp2");
        service.execute();
    }
}
