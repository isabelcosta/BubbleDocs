package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public abstract class RemoteException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
    public RemoteException() {
    }

    public RemoteException(String msg) {
        super(msg);
    }
}