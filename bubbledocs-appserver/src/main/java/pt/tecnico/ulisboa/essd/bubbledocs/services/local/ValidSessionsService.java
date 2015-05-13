package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public abstract class ValidSessionsService extends BubbleDocsService {
	
//	protected Bubbledocs _bd = getBubbleDocs();
	protected String _userToken;
	
	public ValidSessionsService(String userToken) {
		_userToken = userToken;
	}

	protected void dispatch() throws BubbleDocsException {
		
		Bubbledocs bd = getBubbleDocs();
		
		if(bd.validSession(_userToken)){
			refreshToken(_userToken);
		}
		
		dispatch_session();
	}
	
	protected abstract void dispatch_session() throws BubbleDocsException;

}
