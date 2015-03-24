package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnknownBubbleDocsUserException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;


public class DeleteUser extends BubbleDocsService {

	private String userToken;
	private String toDeleteUsername;

	public DeleteUser(String userToken, String toDeleteUsername) {

		this.userToken = userToken;
		this.toDeleteUsername = toDeleteUsername;

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
    	
		
		if(toDeleteUsername == "" || toDeleteUsername == null){
			throw new EmptyUsernameException("User empty!");
		}
		
		//vai buscar o token da root
		String rootToken = null;
		for (Token t : bd.getTokensSet()){
			if (t.getUsername().equals("root"))
				rootToken = t.getToken();
		}

		for(Token token : Bubbledocs.getInstance().getTokensSet()){
			if(token.getToken().equals(rootToken)){
				for(Utilizador user : bd.getUtilizadoresSet()){
					if(user.getUsername().equals(toDeleteUsername)){
						bd.removeUtilizadores(user);
						return;
					}
				}
			}
		}
		throw new UnknownBubbleDocsUserException(toDeleteUsername);
	}
}
