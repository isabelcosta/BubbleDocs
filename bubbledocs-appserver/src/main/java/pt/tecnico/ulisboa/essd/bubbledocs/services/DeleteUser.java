package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UtilizadorInvalidoException;
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
			//invoke some method on remote
			remote.removeUser(toDeleteUsername);
			//invoke same method locally, supposing no exceptions caught
			bd.removeUtilizadores(bd.getUserOfName(toDeleteUsername));

		} catch (RemoteInvocationException rie) {
			// Se esta excepção for apanhada vai verificar se o utilizador a apagar nao existe ou é vazio, ou se foi por erro de conexão
			try{
				bd.emptyUsername(toDeleteUsername);
				bd.existsUser(toDeleteUsername);
			} catch (EmptyUsernameException | UtilizadorInvalidoException exc){
				System.err.println("Couldn't delete User: " + exc);
				throw exc;
			}

			throw new UnavailableServiceException();
			
			
		} catch (LoginBubbleDocsException e) {
			// Se esta excepção for apanhada vai verificar se foi por nao ser root ou sessao nao valida
			try {
				bd.validSession(userToken);
				bd.isRoot(userToken);
				
			} catch (UnauthorizedOperationException | UserNotInSessionException ex) {
				System.err.println("Couldn't delete User: " + ex);
				throw ex;
			}
		}

	}	
}
