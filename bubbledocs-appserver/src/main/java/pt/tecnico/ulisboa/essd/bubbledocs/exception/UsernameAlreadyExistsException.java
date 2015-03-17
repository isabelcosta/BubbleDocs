package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class UsernameAlreadyExistsException extends BubbleDocsException {

private static final long serialVersionUID = 1L;

	private String conflictingName;
	
	public UsernameAlreadyExistsException(String conflictingName) {
		this.conflictingName = conflictingName;
	}
	
	public String getConflictingName() {
		return this.conflictingName;
	}
	
}