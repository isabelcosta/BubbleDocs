package pt.tecnico.ulisboa.essd.bubbledocs.services;

import java.util.Random;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
//import javax.servlet.http.HttpSessionEvent;
//import javax.servlet.http.HttpSessionListener;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.WrongCredencialsException;
 

// add needed import declarations

public class LoginUser extends BubbleDocsService {

    private String userToken;
    

    public LoginUser(String username, String password) throws WrongCredencialsException {
	// add code here
    	boolean existe = false;
    	//verifica se o utilizador e pass sao validos
    	for(Utilizador user : Bubbledocs.getInstance().getUtilizadoresSet()){
    		if(user.getUsername().equals(username)){
    			existe = true;
    			if(!user.getPassword().equals(password)){
    				//throw new WrongCredencialsException("Password incorrecta");
    			} else {
        			//inicia sessao
        			getUserToken(user);
    			}
    		} 
    	}
        
    	if(existe = false){
        	//throw new WrongCredencialsException("Utilizador nao existe");
        }
    }
        
        
    	//quando expira a sessao no fim das duas horas 
    	
   /* public class SessionListener implements HttpSessionListener {
    	 
    	@Override
    	public void sessionCreated(HttpSessionEvent event) {
    		System.out.println("==== Session is created ====");
    			event.getSession().setMaxInactiveInterval(120*60);
    	}
    	 
    	@Override
    	public void sessionDestroyed(HttpSessionEvent event) {
    		System.out.println("==== Session is destroyed ====");
    	}
    }*/
    
    	  /*@Override
    	    public void onStartup(ServletContext servletContext) throws ServletException {
    	        super.onStartup(servletContext);
    	        servletContext.addListener(new SessionListener());
    	    }*/
    	
    @Override
    protected void dispatch() throws BubbleDocsException {
	// add code here
    }

    public final String getUserToken(Utilizador user) {
	
    	String userToken = user.getUsername() + generateToken();

    	return userToken;
    }
    
    
    public int generateToken(){
    	
        Random rand = new Random(); 
        //This will give value from 0 to 9.
        int intToken = rand.nextInt(10);
        
        return intToken;
    }
    
}