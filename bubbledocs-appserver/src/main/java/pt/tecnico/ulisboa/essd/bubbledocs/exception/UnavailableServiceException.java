package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class UnavailableServiceException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private String serviceExp;
	
	public UnavailableServiceException() {
    }
	
	public UnavailableServiceException(String serviceExp) {
		this.serviceExp = serviceExp;
	}
	
	public String getUnavailableService() {
		return this.serviceExp;
	}
}