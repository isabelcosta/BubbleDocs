package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.LoginUserService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.UpdateUserPasswordService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

public class LoginUserIntegrator extends BubbleDocsIntegrator {
	
	
	private LoginUserService _local;
	private String _username;
	private String _password;
	
	
	
	public LoginUserIntegrator(String username, String password) {
		_username = username;
		_password = password;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {

 		IDRemoteServices remote = new IDRemoteServices();
 		
     	
 		try {
 			remote.loginUser(_username,_password);
 			UpdateUserPasswordService updatePassword = new UpdateUserPasswordService(_username, _password);
 			updatePassword.execute();
 		}catch(RemoteInvocationException e){
 			_local = new LoginUserService(_username, _password);
 			_local.execute();

 		}
 		
	}

	public final String getUserToken() {
    	return _local.getUserToken();
    }
	
}
