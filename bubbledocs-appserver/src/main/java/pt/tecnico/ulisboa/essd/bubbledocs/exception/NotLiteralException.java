package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class NotLiteralException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String cantDoLiteral;
	
	public NotLiteralException(String conflictingLiteral) {
		this.cantDoLiteral = conflictingLiteral;
	}
	
	public String getConflictingLiteral() {
		return this.cantDoLiteral;
	}
	
}