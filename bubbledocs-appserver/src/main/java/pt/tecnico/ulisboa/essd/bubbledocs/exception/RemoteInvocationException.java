package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class RemoteInvocationException extends RemoteException{
	private static final long serialVersionUID = 1L;

	private String remInvExp;
	
	public RemoteInvocationException() {
    }
	
	public RemoteInvocationException(String remInvExp) {
		this.remInvExp= remInvExp;
	}
	
	public String getconflictingRemoteInvocation() {
		return this.remInvExp;
	}
	
}