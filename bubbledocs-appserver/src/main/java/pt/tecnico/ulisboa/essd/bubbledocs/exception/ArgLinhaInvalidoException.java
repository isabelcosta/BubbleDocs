package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class ArgLinhaInvalidoException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	
	private String conflictingLine;
	
	
	public ArgLinhaInvalidoException(String conflictL) {
		
		this.conflictingLine = conflictL;
	}
	
	public String getMessage() {
		
		return " A linha " + conflictingLine + " e invalida. ";
		
	}
	
}