package pt.tecnico.ulisboa.essd.bubbledocs.services;

import java.util.Random;

import org.joda.time.LocalTime;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
//import javax.servlet.http.HttpSessionEvent;
//import javax.servlet.http.HttpSessionListener;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UtilizadorInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

 

// add needed import declarations

public class LoginUser extends BubbleDocsService {

    private String _result;
    private String _username;
    private String _password;
    

    public LoginUser(String username, String password) throws LoginBubbleDocsException, UnavailableServiceException {
		_username = username;
		_password = password;
    	
    }
        
    	
    @Override
    protected void dispatch() throws BubbleDocsException {
    	
 		Bubbledocs bd = Bubbledocs.getInstance();
 		IDRemoteServices remote = new IDRemoteServices();
 		
 		
     	
 		try {
 			remote.loginUser(_username,_password);
 		}catch(RemoteInvocationException e){
 			
 			//verificacao da pass local
 			Utilizador utilizador = bd.getUserOfName(_username);
 			if(!bd.checkLocalPassword(utilizador, _password)) {
 				throw new UnavailableServiceException();
 			}
 		}
 		
 		// aqui, a verificao remota ocorreu sem problemas
 		// entao actualiza-se a password local se for diferente
 		Utilizador utilizador = bd.getUserOfName(_username);
 		
 		if (!bd.checkLocalPassword(utilizador, _password)) {
 			utilizador.setPassword(_password);
		}
 		
    	for(Utilizador user : bd.getUtilizadoresSet()){
			String temp;
			do {
				temp = _username + bd.generateToken();
			} while (temp.equals(_result));
			_result = temp;
			bd.refreshTokenTotal(_result, _username);
			bd.addTokens(new Token(_username, _result));
			return ;
    	}
    } 

    public final String getUserToken() {
    	return _result;
    }
   
}