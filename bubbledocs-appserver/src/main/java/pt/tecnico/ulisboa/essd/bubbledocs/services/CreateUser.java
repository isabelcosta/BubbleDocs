package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
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
		
		if(bd.validSession(userToken) && bd.isRoot(userToken)){
			refreshToken(userToken);
		
			try {
				remote.createUser(newUsername, email);
			}catch (RemoteInvocationException e) {
				throw new UnavailableServiceException();	
				
			}	
			Utilizador user = new Utilizador(name, newUsername, email);
			bd.addUtilizadores(user);
		}
	}
}
