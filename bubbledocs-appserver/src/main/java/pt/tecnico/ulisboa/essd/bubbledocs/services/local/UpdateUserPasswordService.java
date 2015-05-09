package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class UpdateUserPasswordService extends BubbleDocsService {
	
	private String _username;
	private String _password;
	private Bubbledocs _bd; 
	
	public UpdateUserPasswordService(String username, String password) {
		_username = username;
		_password = password;
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		
		_bd = Bubbledocs.getInstance();
		_bd.setUserPassword(_username, _password);

	}

}