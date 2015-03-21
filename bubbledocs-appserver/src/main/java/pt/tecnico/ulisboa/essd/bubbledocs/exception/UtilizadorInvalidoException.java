package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class UtilizadorInvalidoException extends BubbleDocsException {
	
   
	private static final long serialVersionUID = 1L;
    
	private String invalidUser;
    
    
    public UtilizadorInvalidoException() {
    }

    
    
    public UtilizadorInvalidoException(String username) {
        
        this.invalidUser = username;
    }
    
    
    
	 public String getMessage(){
		 return "O utilizador" + invalidUser + "nao e valido.";
	 
	 }
    



}