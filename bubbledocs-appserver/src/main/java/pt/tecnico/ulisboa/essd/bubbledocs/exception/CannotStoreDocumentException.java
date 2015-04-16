package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class CannotStoreDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;


	private String conflictingDocument;
	
	
	public CannotStoreDocumentException(String conflictingDocument) {
		this.conflictingDocument = conflictingDocument;
	}
	
	
	public String conflictingDocument() {
		return this.conflictingDocument;
	}
}