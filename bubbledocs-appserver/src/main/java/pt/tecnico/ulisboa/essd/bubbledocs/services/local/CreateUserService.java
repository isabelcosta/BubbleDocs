package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidUsernameException;

public class CreateUserService extends IsRootService {

	private String _newUsername;
	private String _email;
	private String _name;

	public CreateUserService(String userToken, String newUsername, String email, String name) {

		
		super(userToken);
		_newUsername = newUsername;
		_email = email;
		_name = name;
	
	}

	@Override
	protected void dispatch_root() throws BubbleDocsException {
		
		if(_newUsername == null || _newUsername.equals("")){
			throw new InvalidUsernameException() ;	
		}
		
		if(_email == null || _email.equals("")){
			throw new InvalidEmailException();
		}
		
		for(Utilizador user : _bd.getUtilizadoresSet()){
			
			if(user.getEmail().equals(_email)){
				throw new DuplicateEmailException();
			}
			
			
			
			if(user.getUsername().equals(_newUsername)){
				throw new DuplicateUsernameException(_newUsername);
			}
			
			
		}
		
		Utilizador user = new Utilizador(_name, _newUsername, _email);
		_bd.addUtilizadores(user);
	}
}
