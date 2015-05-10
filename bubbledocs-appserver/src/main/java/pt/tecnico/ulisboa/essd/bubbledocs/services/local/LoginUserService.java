package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;


public class LoginUserService extends BubbleDocsService {

    private String _result;
    private String _username;
    private String _password;
    private Bubbledocs _bd = Bubbledocs.getInstance();
    

    public LoginUserService(String username, String password) throws LoginBubbleDocsException, UnavailableServiceException {
		_username = username;
		_password = password;
    	
    }
    
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	Utilizador utilizador;
    	
    	try {
    		utilizador = _bd.getUserOfName(_username);
		} catch (LoginBubbleDocsException e) {
			throw new UnavailableServiceException();
		}
 		
 		
 		if (utilizador == null) {
				throw new UnavailableServiceException();
		}
 		
 		if (!_bd.checkLocalPassword(utilizador, _password)) {
 			throw new UnavailableServiceException();
		}
 		
		_result = _bd.renewUserToken(_username, _password);
	
    } 

    public final String getUserToken() {
    	return _result;
    }
   
}