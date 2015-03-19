package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class DontHavePermissionException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String cantDoName;
	
	public DontHavePermissionException(String conflictingName) {
		this.cantDoName = conflictingName;
	}
	
	public String getConflictingName() {
		return this.cantDoName;
	}
	
}