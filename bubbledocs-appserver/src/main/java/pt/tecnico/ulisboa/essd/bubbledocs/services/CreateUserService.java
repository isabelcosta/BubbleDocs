package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;


public class CreateUserService extends IsRootService {

	private String newUsername;
	private String email;
	private String name;

	public CreateUserService(String userToken, String newUsername, String email, String name) {

		_userToken = userToken;
		this.newUsername = newUsername;
		this.email = email;
		this.name = name;
	
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		super.dispatch();
		
		IDRemoteServices remote = new IDRemoteServices();
		_bd.isRoot(_userToken);
		
		try {
			remote.createUser(newUsername, email);
		}catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();	
			
		}	
		Utilizador user = new Utilizador(name, newUsername, email);
		_bd.addUtilizadores(user);
	}
}
