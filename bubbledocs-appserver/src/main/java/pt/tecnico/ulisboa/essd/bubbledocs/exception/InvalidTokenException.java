package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class InvalidTokenException extends BubbleDocsException {
	
   
	private static final long serialVersionUID = 1L;
    
	private String invalidToken;
    
    public InvalidTokenException() {
    }
    
    public InvalidTokenException(String username) {
        
        this.invalidToken = username;
    }
    
	public String getMessage(){
		return "O token é null ou vazio!";
	}

}
