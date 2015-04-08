package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class UnknownBubbleDocsUserException  extends BubbleDocsException {
	
   
	private static final long serialVersionUID = 1L;
    
	private String unkownUser;
    
    
    public UnknownBubbleDocsUserException() {
    }

    
    
    public UnknownBubbleDocsUserException(String username) {
        
        this.unkownUser = username;
    }
    
    
    
	 public String getMessage(){
		 return "O utilizador " + unkownUser + " não existe.";
	 
	 }

}
