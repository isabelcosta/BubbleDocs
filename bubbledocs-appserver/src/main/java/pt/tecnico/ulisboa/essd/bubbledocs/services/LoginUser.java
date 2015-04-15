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
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UtilizadorInvalidoException;


 

// add needed import declarations

public class LoginUser extends BubbleDocsService {

    private String _result;
    private String _username;
    private String _password;
    

    public LoginUser(String username, String password) throws LoginBubbleDocsException, UtilizadorInvalidoException {
		_username = username;
		_password = password;
    	
    }
        
    	
    @Override
    protected void dispatch() throws BubbleDocsException {
    	/*
    	 * 		Bubbledocs bd = Bubbledocs.getInstance();
    	 * 
    	 *     	IDRemoteServices remote = bd.getIDRemoteService();
    	 *     
    	 *		remote.login(_username,_password);
    	 *
    	 *		
    	 */
    	
    	
    	for(Utilizador user : Bubbledocs.getInstance().getUtilizadoresSet()){
    			
    			/* INICIO
    			* 	
    			* 	Retirar verificacao da password e user, é o servico remoto que se responsabiliza por essa verificacao
    			* 	
    			* 	Usar apenas a comparacao da password local com a remota, se for igual
    			* 	
    			* 	Password Remota: quando a que recebemos é igual á remota (chamada remota não lanca exceção).
    			* 
    			*
    			*/
    		if(user.getUsername().equals(_username)){							// retirar
    			if(1==2){ //!user.getPassword().equals(_password)				// retirar
    				throw new LoginBubbleDocsException("Password incorrecta!");	// retirar
    			} else {
    			/*
    			*
    			*
    			* FIM
    			*/
    				
    				String temp;
    				do {
						temp = _username + generateToken();
					} while (temp.equals(_result));
    				_result = temp;
    				refreshTokenTotal(_result);
    				return ;
    			}
    		}
    		
    	}
    	/*
    	 * retirar
    	 */
    	throw new UtilizadorInvalidoException("Password incorrecta!");  	//retirar
    	/*
    	 * retirar
    	 */
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
}