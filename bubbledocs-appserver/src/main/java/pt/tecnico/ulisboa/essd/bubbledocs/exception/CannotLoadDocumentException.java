package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class CannotLoadDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private String conflictingDocument;
	
	
	public CannotLoadDocumentException(String conflictingDocument) {
		this.conflictingDocument = conflictingDocument;
	}
	
	
	public String conflictingDocument() {
		return this.conflictingDocument;
	}

}