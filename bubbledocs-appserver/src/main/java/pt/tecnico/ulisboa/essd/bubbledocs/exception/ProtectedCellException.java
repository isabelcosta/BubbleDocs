package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class ProtectedCellException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private String conflictingCellID;
	
	public ProtectedCellException(String conflictC) {
		this.conflictingCellID = conflictC;
	}
	
	public String getConflictingLine_Column() {
		String res = conflictingCellID;
		
		return res;
	}
	
}
	
