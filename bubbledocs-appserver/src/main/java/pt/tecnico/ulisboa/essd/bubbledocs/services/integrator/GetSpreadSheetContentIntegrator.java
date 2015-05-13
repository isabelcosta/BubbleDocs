package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetSpreadSheetContentService;

public class GetSpreadSheetContentIntegrator extends BubbleDocsIntegrator{
	
	private String _userToken;
	private GetSpreadSheetContentService _local;
	private int _folhaId;
    
	public GetSpreadSheetContentIntegrator(String userToken, int docId) {
		_userToken = userToken;
        _folhaId = docId;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		/*
		 * 
		 *  Instância local
		 * 
		 * 
		 */
		
		_local = new GetSpreadSheetContentService(_userToken, _folhaId);
		_local.execute();

	}
}
