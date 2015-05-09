package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public abstract class ReadAndWritePermissionsService extends ValidSessionsService {
	
	protected Bubbledocs _bd = getBubbleDocs();
	protected String _userToken;
	protected int _docId;
	protected boolean _flag;
	
	public ReadAndWritePermissionsService(String userToken, int docId, boolean flag) {
		
		super(userToken);
		_userToken = userToken;
 	    _docId = docId;
 	    _flag = flag;
 	   
	}

	protected void dispatch_session() throws BubbleDocsException {
		if(!_flag){ //pode ler
			_bd.canRead(_userToken,_docId);
		} else { //pode escrever
			_bd.canWrite(_userToken,_docId);
		}
		dispatch_read_and_write();
		
	}
	
	protected abstract void dispatch_read_and_write() throws BubbleDocsException;
}
