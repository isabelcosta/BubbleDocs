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
    private Bubbledocs _bd;
    

    public LoginUserService(String username, String password) throws LoginBubbleDocsException, UnavailableServiceException {
		_username = username;
		_password = password;
    	
    }
    
    
    @Override
    protected void dispatch() throws BubbleDocsException {
    	
 		_bd = Bubbledocs.getInstance();
 		IDRemoteServices remote = new IDRemoteServices();
 		
     	
 		try {
 			remote.loginUser(_username,_password);
 		}catch(RemoteInvocationException e){
 			
 			//verificacao da pass local
 			Utilizador utilizador = _bd.getUserOfName(_username);
 			
 			if (utilizador == null) {
 				throw new LoginBubbleDocsException();
 			}
 			
 			if(!_bd.checkLocalPassword(utilizador, _password)) {
 				throw new UnavailableServiceException();
 			}
 		}
 		
 		// aqui, a verificao remota ocorreu sem problemas
 		// entao actualiza-se a password local se for diferente
 		Utilizador utilizador = _bd.getUserOfName(_username);
 		
 		if (utilizador == null) {
				throw new LoginBubbleDocsException();
		}
 		
 		if (!_bd.checkLocalPassword(utilizador, _password)) {
 			_bd.setUserPassword(_username,_password);
		}
 		
 		//eliminei o ciclo getUtilizadoresSet()
 		
		String temp;
		do {
			temp = _username + _bd.generateToken();
		} while (temp.equals(_result));
		_result = temp;
		_bd.refreshTokenTotal(_result, _username);
		_bd.addTokens(new Token(_username, _result));
		return ;
    } 

    public final String getUserToken() {
    	return _result;
    }
   
}