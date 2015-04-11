package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;


public class CreateUser extends BubbleDocsService {


	private String userToken;
	private String newUsername;
	private String email;
	private String name;

	public CreateUser(String userToken, String newUsername, String email, String name) {

		this.userToken = userToken;
		this.newUsername = newUsername;
		this.email = email;
		this.name = name;
	
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		Bubbledocs bd = Bubbledocs.getInstance();
		
		try {
			if(bd.validSession(userToken) && bd.isRoot(userToken)){
				refreshToken(userToken);
		    	
				if (bd.emptyUsername(newUsername) && bd.duplicatedUsername(newUsername)) {
					Utilizador user = new Utilizador(name, newUsername, email);
					bd.addUtilizadores(user);
				}
	    	}
		} catch (DuplicateUsernameException | EmptyUsernameException | UnauthorizedOperationException | UserNotInSessionException e) {
			System.err.println("Couldn't create User: " + e);
		}
			
		
	}
}

