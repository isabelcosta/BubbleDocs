package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class InvalidUsernameException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private String invalidUsername;
    
    
    public InvalidUsernameException() {
    }
    
    public InvalidUsernameException(String username) {
        
        this.invalidUsername = username;
    }
    
    
    
	 public String getMessage(){
		 return "O utilizador" + invalidUsername + "nao e valido.";
	 
	 }
	
}