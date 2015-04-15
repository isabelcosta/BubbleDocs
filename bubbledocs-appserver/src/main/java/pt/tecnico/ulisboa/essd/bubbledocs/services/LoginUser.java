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
 			remote.loginUser(_username,_password);	// nunca trata da LoginBubbleDocsException
 		}catch(RemoteInvocationException e){
 			
 			//verificacao da pass local
 			Utilizador utilizador = bd.getUserOfName(_username);
 			if(!checkLocalPassword(utilizador)) {
 				throw new UnavailableServiceException();
 			}
 		}
 		
 		// aqui, a verificao remota ocorreu sem problemas
 		// entao actualiza-se a password local se for diferente
 		Utilizador utilizador = bd.getUserOfName(_username);
 		
 		if (!checkLocalPassword(utilizador)) {
 			utilizador.setPassword(_password);
		}
 		
    	for(Utilizador user : bd.getUtilizadoresSet()){
			String temp;
			do {
				temp = _username + generateToken();
			} while (temp.equals(_result));
			_result = temp;
			refreshTokenTotal(_result);
			return ;
    	}
    } 

    public final String getUserToken() {
    	return _result;
    }
    
    
    public int generateToken(){		//	talvez passar para a bubble docs de forma a separa a lógica de negócio
        Random rand = new Random(); 
        int intToken = rand.nextInt(10);
        return intToken;
    }
    
    								//	talvez passar para a bubble docs de forma a separa a lógica de negócio
    public final void refreshTokenTotal (String token) {
    	for(Token tokenObject : Bubbledocs.getInstance().getTokensSet()){
    		if(tokenObject.getUsername().equals(_username)){
    			tokenObject.setTime(new LocalTime());
    			tokenObject.setToken(token);
    		}
    	}
    }
    
    public Boolean checkLocalPassword(Utilizador utilizador){
    	if (utilizador.getPassword()==null) {
    		return false;
    	}
    	return utilizador.getPassword().equals(_password);
    }
}