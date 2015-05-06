//package pt.tecnico.ulisboa.essd.bubbledocs.services.local;
//
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
//import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
//
//public abstract class WrittingPermissionService extends ValidSessionsService {
//	
//	protected Bubbledocs _bd = getBubbleDocs();
//	protected String _userToken;
//	
//	public ValidSessionsService(String userToken, FolhadeCalculo folha, String cellId) {
//		super(_userToken);
//		_userToken = userToken;
//	}
//
//	protected void dispatch() throws BubbleDocsException {
//		if(_bd.validSession(_userToken)){
//			refreshToken(_userToken);
//		}
//		dispatch_session();
//	}
//	
//	protected abstract void dispatch_session() throws BubbleDocsException;
//
//}
