package pt.tecnico.ulisboa.essd.bubbledocs.services;

import java.util.Random;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
//import javax.servlet.http.HttpSessionEvent;
//import javax.servlet.http.HttpSessionListener;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UtilizadorInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.WrongPasswordException;

 

// add needed import declarations

public class LoginUser extends BubbleDocsService {

    private String result;
    private String _username;
    private String _password;
    

    public LoginUser(String username, String password) throws WrongPasswordException, UtilizadorInvalidoException {
		_username = username;
		_password = password;
    	
    }
        
    	
    @Override
    protected void dispatch() throws BubbleDocsException {
    	
    	//verifica se o utilizador e pass sao validos
    	for(Utilizador user : Bubbledocs.getInstance().getUtilizadoresSet()){
    		if(user.getUsername().equals(_username)){
    			if(!user.getPassword().equals(_password)){
    				//throw new WrongCredencialsException("Password incorrecta");
    			} else {
        			//inicia sessao
    				result = setUserToken(user.getUsername());
    				return ;
    			}
    		} 
    	}
       //throw new WrongCredencialsException("Utilizador nao existe");	   	
    }

    public final String setUserToken(String user) {
    	String userToken = user + generateToken();
    	return userToken;
    }
    
    
    public int generateToken(){
    	
        Random rand = new Random(); 
        //This will give value from 0 to 9.
        int intToken = rand.nextInt(10);
        return intToken;
    }
    
    public String getResult() {
    	return result;
    }
}