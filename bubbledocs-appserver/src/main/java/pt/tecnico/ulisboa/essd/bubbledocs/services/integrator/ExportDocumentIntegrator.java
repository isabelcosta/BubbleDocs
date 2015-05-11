package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.ExportDocumentService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetUsername4TokenService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.StoreRemoteServices;

public class ExportDocumentIntegrator extends BubbleDocsIntegrator {

	    private int _sheetId;
	    private String _userToken;
	    private byte[] _result;
	    
	    public ExportDocumentIntegrator(int sheetId, String userToken) {
	    	_sheetId = sheetId;
	    	_userToken = userToken;
	    }

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		
		ExportDocumentService local = new ExportDocumentService(_sheetId, _userToken);
		local.execute();
		
		GetUsername4TokenService userNameService = new GetUsername4TokenService(_userToken);
		userNameService.execute();

		String userName = userNameService.getResult();
		
		StoreRemoteServices remote = new StoreRemoteServices();
		
		
		try{
			remote.storeDocument(userName, Integer.toString(_sheetId), local.getResult());
		} catch (RemoteInvocationException e){
			throw new UnavailableServiceException();
		}
		
		_result = local.getResult();
		
	}
	
	public byte[] getResult() {
		return _result;
	}
	
}
