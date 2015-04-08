package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class UnauthorizedOperationException extends BubbleDocsException {
	
   
	private static final long serialVersionUID = 1L;
    
	private String invalidRoot;
    
    
    public UnauthorizedOperationException() {
    }

    
    
    public UnauthorizedOperationException(String username) {
        
        this.invalidRoot = username;
    }
    
    
    
	 public String getMessage(){
		 return "O utilizador" + invalidRoot + "não tem permissões de root.";
	 
	 }
}
