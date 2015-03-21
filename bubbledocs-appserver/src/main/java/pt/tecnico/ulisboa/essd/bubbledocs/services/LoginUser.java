package pt.tecnico.ulisboa.essd.bubbledocs.services;

import java.util.Random;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UtilizadorInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.WrongPasswordException;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
 

// add needed import declarations

public class LoginUser extends BubbleDocsService {

    private String userToken;
    

    public LoginUser(String username, String password){
	// add code here
    	
    	boolean existe = false;
    	
    	//verifica se o utilizador e pass sao validos
    	for(Utilizador user : Bubbledocs.getInstance().getUtilizadoresSet()){
    		if(user.getUsername().equals(username)){
    			existe = true;
    			if(!user.getPassword().equals(password)){
    				throw new WrongPasswordException(username);
    			} else {
        			//inicia sessao
        			getUserTok(user.getUsername());
    			}
    		} 
    	}
        
    	if(existe = false){
        	throw new UtilizadorInvalidoException(username);
        }
    }
        
    	
    private void getUserTok(String username) { //funcao criada para chamar a classe token funcao getusertoken
		
    	Token token = new Token(username);
    	
    	token.getUserToken(username);
	}


	@Override
    protected void dispatch() throws BubbleDocsException {
	// add code here
    }


    
    

    
}