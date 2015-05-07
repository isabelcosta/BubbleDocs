package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public abstract class ReadingPermissionService extends ValidSessionsService {
	
	protected Bubbledocs _bd = getBubbleDocs();
	protected String _userToken;
	protected int _docId;
	
	public ReadingPermissionService(String userToken, int docId) {
		
		super(userToken);
		_userToken = userToken;
 	    _docId = docId;
 	   
	}

	protected void dispatch_session() throws BubbleDocsException {
		_bd.canRead(_userToken,_docId);

		dispatch_read();
	}
	
	protected abstract void dispatch_read() throws BubbleDocsException;

}
