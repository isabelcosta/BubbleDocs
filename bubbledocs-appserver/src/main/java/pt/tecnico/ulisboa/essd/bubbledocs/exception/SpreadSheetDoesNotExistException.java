package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class SpreadSheetDoesNotExistException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;

	private String cantDoSpreadSheet;
	
	public SpreadSheetDoesNotExistException(String conflictingSpreadSheet) {
		this.cantDoSpreadSheet = conflictingSpreadSheet;
	}
	
	public String getconflictingSpreadSheet() {
		return this.cantDoSpreadSheet;
	}
	
}