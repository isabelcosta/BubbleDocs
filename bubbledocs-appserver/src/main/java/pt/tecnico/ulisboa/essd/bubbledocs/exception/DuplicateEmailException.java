package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class DuplicateEmailException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private String conflictingEmail;
	
	public DuplicateEmailException(String conflictingEmail) {
		this.conflictingEmail = conflictingEmail;
	}
	
	public String getConflictingEmail() {
		return this.conflictingEmail;
	}
	
}