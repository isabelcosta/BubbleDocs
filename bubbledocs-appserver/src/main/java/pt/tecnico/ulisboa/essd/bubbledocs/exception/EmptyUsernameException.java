package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class EmptyUsernameException extends BubbleDocsException{
	
	private static final long serialVersionUID = 1L;
    
	private String emptyUser;
    
    
    public EmptyUsernameException() {
    }

    
    
    public EmptyUsernameException(String username) {
        
        this.emptyUser = username;
    }
    
    
    
	 public String getMessage(){
		 return "O utilizador" + emptyUser + "nao e valido.";
	 
	 }
}
