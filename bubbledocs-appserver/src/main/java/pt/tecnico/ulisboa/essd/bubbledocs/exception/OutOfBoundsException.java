package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class OutOfBoundsException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private int conflictingLine;
	private int conflictingColumn;
	
	public OutOfBoundsException(int conflictL, int conflictC) {
		this.conflictingLine = conflictL;
		this.conflictingColumn = conflictC;
	}
	
	public String getConflictingLine_Column() {
		String res = Integer.toString(this.conflictingLine) + ":" + Integer.toString(this.conflictingColumn);
		
		return res;
	}
	
}
	
