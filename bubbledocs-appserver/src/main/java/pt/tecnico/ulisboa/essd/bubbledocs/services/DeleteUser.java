package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;


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
		IDRemoteServices remote = new IDRemoteServices();
		
		try {
			//invoke some method on remote if pass local verifications
			bd.emptyUsername(toDeleteUsername);
			bd.validSession(userToken);
			bd.isRoot(userToken);
			remote.removeUser(toDeleteUsername);
			
			//invoke same method locally, supposing no exceptions caught
			bd.removeUtilizadores(bd.getUserOfName(toDeleteUsername));
		
		} catch (EmptyUsernameException | UnauthorizedOperationException | UserNotInSessionException exc){
				throw exc;
			
		} catch (RemoteInvocationException rie) {
			// Se esta excepção for apanhada a conexão falhou

			throw new UnavailableServiceException();
				
		}

	}	
}

