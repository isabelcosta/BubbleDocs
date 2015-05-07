package pt.tecnico.ulisboa.essd.bubbledocs.services.remote;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;


public class StoreRemoteServices {

	public void storeDocument(String username, String docName, byte[] document)
			throws CannotStoreDocumentException, RemoteInvocationException {
		// TODO : the connection and invocation of the remote service
	}

	public byte[] loadDocument(String username, String docName)
			throws CannotLoadDocumentException, RemoteInvocationException {
		return null;
	}
}