package pt.tecnico.ulisboa.essd.bubbledocs;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetUsername4TokenService;

public class GetUsername4TokenServiceTest extends BubbleDocsServiceTest {

	// the tokens
	private String ars;

	private static final String USERNAME = "ars";
	private static final String EMPTY_TOKEN = "";
	private static final String EMAIL = "pedro@tecnico.ulisboa.pt";
	private static final String NAME = "António Rito Silva";


	private Utilizador user;

	public void populate4Test() {

		user = createUser(USERNAME, EMAIL, NAME);
		ars = addUserToSession(USERNAME);
	}

	@Test
	public void success() {

		GetUsername4TokenService userToken = new GetUsername4TokenService(ars);
		userToken.execute();

		assertEquals(user.getUsername(), userToken.getUsername());

	}


	//Token inválido

	@Test(expected = InvalidTokenException.class)
	public void tokenDoesNotExist() {

		GetUsername4TokenService userToken = new GetUsername4TokenService(EMPTY_TOKEN);
		userToken.execute();
	}
}