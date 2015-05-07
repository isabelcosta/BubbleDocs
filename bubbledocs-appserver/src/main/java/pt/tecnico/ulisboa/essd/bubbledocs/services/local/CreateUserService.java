package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;


public class CreateUserService extends IsRootService {

	private String newUsername;
	private String email;
	private String name;

	public CreateUserService(String userToken, String newUsername, String email, String name) {

		
		super(userToken);
		this.newUsername = newUsername;
		this.email = email;
		this.name = name;
	
	}

	@Override
	protected void dispatch_root() throws BubbleDocsException {
		
		if(this.newUsername == null || this.newUsername.equals("")){
			throw new InvalidUsernameException() ;
			
		}	
		if(this.email == null || this.email.equals("")){
			throw new InvalidEmailException();
		}
		
		for(Utilizador user : _bd.getUtilizadoresSet()){
			
			if(user.getEmail().equals(this.email)){
				throw new DuplicateEmailException();
				}
			
			
			
			if(user.getUsername().equals(this.newUsername)){
				throw new DuplicateUsernameException(newUsername);
				}
			
			
		}
		
		Utilizador user = new Utilizador(name, newUsername, email);
		_bd.addUtilizadores(user);
	}
}
