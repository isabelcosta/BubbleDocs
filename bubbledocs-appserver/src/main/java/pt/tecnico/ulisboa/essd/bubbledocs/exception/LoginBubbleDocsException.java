package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class LoginBubbleDocsException extends BubbleDocsException{
	private static final long serialVersionUID = 1L;

	private String loginExp;
	
	public LoginBubbleDocsException() {
    }
	
	public LoginBubbleDocsException(String loginExp) {
		this.loginExp = loginExp;
	}
	
	public String getconflictingSpreadSheet() {
		return this.loginExp;
	}	
}