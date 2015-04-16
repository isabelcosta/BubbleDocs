package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;


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
		IDRemoteServices remote = new IDRemoteServices();
		
		try {
			//invoke some method on remote
			remote.createUser(newUsername, email);;
			
			if(bd.validSession(userToken) && bd.isRoot(userToken)){
				refreshToken(userToken);
		    	
				if (bd.emptyUsername(newUsername) && bd.duplicatedUsername(newUsername)) {
					Utilizador user = new Utilizador(name, newUsername, email);
					bd.addUtilizadores(user);
				}
	    	}
		} catch (DuplicateUsernameException | EmptyUsernameException |
				UnauthorizedOperationException | UserNotInSessionException e) {
			System.err.println("Couldn't create User: " + e);
			
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
				
			
		}catch (LoginBubbleDocsException e) {
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

