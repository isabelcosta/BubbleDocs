package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class ArgColunaInvalidoException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	
	private String conflictingColumn;
	
	
	public ArgColunaInvalidoException(String conflictC) {
		
		this.conflictingColumn = conflictC;
	}
	
	public String getMessage() {
		
		return " A coluna " + conflictingColumn + " e invalida. ";
		
	}
	
}