package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class InvalidFunctionException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String cantDoFunction;
	
	public InvalidFunctionException(){
		
	}
	
	public InvalidFunctionException(String conflictingFunction) {
		this.cantDoFunction = conflictingFunction;
	}
	
	public String getConflictingLiteral() {
		return this.cantDoFunction;
	}
	
}