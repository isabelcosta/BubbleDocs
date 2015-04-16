package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class InvalidEmailException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private String invalidEmail;
    
    public InvalidEmailException() {
    }
    
    public InvalidEmailException(String invalidEmail) {
        
        this.invalidEmail = invalidEmail;
    }
    
	public String getMessage(){
		return this.invalidEmail;
	}

}