package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class IdFolhaInvalidoException extends BubbleDocsException {
	
	   
	private static final long serialVersionUID = 1L;
    
	private int idInvalido;
    
    
    public IdFolhaInvalidoException() {
    }

    
    
    public IdFolhaInvalidoException(int folhaID) {
        
        this.idInvalido = folhaID;
    }
    
    
    
	 public String getMessage(){
		 return "O id da folha: " + idInvalido + "nao e valido.";
	 
	 }
}