package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;


public class CreateUser extends BubbleDocsService {


	private String userToken;
	private String newUsername;
	private String password;
	private String name;

	public CreateUser(String userToken, String newUsername, String password, String name) {

		this.userToken = userToken;
		this.newUsername = newUsername;
		this.password = password;
		this.name = name;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		Bubbledocs bd = Bubbledocs.getInstance();
		if(!validSession(userToken)){
    		throw new UserNotInSessionException("Session for user " + userToken.substring(0, userToken.length()-1) + " is invalid" );
    	}
		if(!isRoot(userToken)){
			throw new UnauthorizedOperationException("Session for user " + userToken.substring(0, userToken.length()-1) + " is not root");
		}
		refreshToken(userToken);
    	
		
		if(newUsername == "" || newUsername == null){
			throw new EmptyUsernameException("User empty!");
		}
		
		for(Utilizador user : bd.getUtilizadoresSet()){
			if(user.getUsername().equals(newUsername)){
				throw new DuplicateUsernameException(newUsername);
			}
		}
		
		Utilizador user = new Utilizador(name, newUsername, password);
		bd.addUtilizadores(user);
	}
}

