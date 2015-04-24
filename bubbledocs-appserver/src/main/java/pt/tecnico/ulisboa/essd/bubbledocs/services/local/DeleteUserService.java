package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;


public class DeleteUserService extends IsRootService {

	private String toDeleteUsername;

	public DeleteUserService(String userToken, String toDeleteUsername) {
		super(userToken);
		this.toDeleteUsername = toDeleteUsername;

	}

	@Override
	protected void dispatch_root() throws BubbleDocsException {
		
		IDRemoteServices remote = new IDRemoteServices();
		
		try {
			//invoke some method on remote if pass local verifications
			_bd.emptyUsername(toDeleteUsername);
			
			_bd.isRoot(_userToken);
			remote.removeUser(toDeleteUsername);
			
			//invoke same method locally, supposing no exceptions caught
			_bd.removeUtilizadores(_bd.getUserOfName(toDeleteUsername));
		
		} catch (EmptyUsernameException | UnauthorizedOperationException | UserNotInSessionException exc){
				throw exc;
			
		} catch (RemoteInvocationException rie) {
			// Se esta excepção for apanhada a conexão falhou

			throw new UnavailableServiceException();
				
		}

	}	
}

