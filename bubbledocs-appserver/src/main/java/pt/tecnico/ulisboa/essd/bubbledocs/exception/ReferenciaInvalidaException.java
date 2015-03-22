package pt.tecnico.ulisboa.essd.bubbledocs.exception;


public class ReferenciaInvalidaException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private String referenciaInvalida;
	
	
	public ReferenciaInvalidaException(String referencia) {
		
		this.referenciaInvalida = referencia;
		
	}
	
	public String getMessage() {
		
		return " A referencia " + referenciaInvalida + "nao e valida.";
		
	}
	
}