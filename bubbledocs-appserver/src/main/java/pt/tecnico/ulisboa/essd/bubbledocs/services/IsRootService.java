package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public abstract class IsRootService extends ValidSessionsService {
	
	
	public IsRootService(String userToken) {
		super(userToken);
	}

	protected final void dispatch_session() throws BubbleDocsException {
		_bd.isRoot(_userToken);
		dispatch_root();
	}
	
	protected abstract void dispatch_root() throws BubbleDocsException;
	
	public String getuserToken() {
		return _userToken;
	}

	public void setuserToken(String userToken) {
		_userToken = userToken;
	}

}
