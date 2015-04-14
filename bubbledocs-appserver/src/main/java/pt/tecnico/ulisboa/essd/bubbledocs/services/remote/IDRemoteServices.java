package pt.tecnico.ulisboa.essd.bubbledocs.services.remote;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;

public class IDRemoteServices {

	public void createUser(String username, String email)
			throws InvalidUsernameException, DuplicateUsernameException,
			DuplicateEmailException, InvalidEmailException,
			RemoteInvocationException {
		// TODO : the connection and invocation of the remote service
	}

	public void loginUser(String username, String password)
			throws LoginBubbleDocsException, RemoteInvocationException {
		// TODO : the connection and invocation of the remote service
	}

	public void removeUser(String username)
			throws LoginBubbleDocsException, RemoteInvocationException {
		// TODO : the connection and invocation of the remote service
	}
	
	public void renewPassword(String username)
			throws LoginBubbleDocsException, RemoteInvocationException {
		// TODO : the connection and invocation of the remote service
	}
	
}