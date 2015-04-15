package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
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
		
		try {
			if(bd.validSession(userToken) && bd.isRoot(userToken)){
				refreshToken(userToken);
		    	
				if (bd.emptyUsername(toDeleteUsername)) {
					bd.removeUtilizadores(bd.getUserOfName(toDeleteUsername));
				}
	    	}
		} catch (EmptyUsernameException | UnauthorizedOperationException | UserNotInSessionException | LoginBubbleDocsException e) {
			System.err.println("Couldn't delete User: " + e);
		}
	}	
}
