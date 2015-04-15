package pt.tecnico.ulisboa.essd.bubbledocs.exception;

<<<<<<< Upstream, based on origin/master
public class LoginBubbleDocsException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
}
=======
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
>>>>>>> 843b669 criação das excepções
