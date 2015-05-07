package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public abstract class WrittingPermissionService extends ValidSessionsService {
	
	protected Bubbledocs _bd = getBubbleDocs();
	protected String _userToken;
	protected int _docId;
	
	public WrittingPermissionService(String userToken, int docId) {
		
		super(userToken);
		_userToken = userToken;
 	    _docId = docId;
 	   
	}

	protected void dispatch_session() throws BubbleDocsException {
		_bd.canWrite(_userToken,_docId);

		dispatch_write();
	}
	
	protected abstract void dispatch_write() throws BubbleDocsException;
}
