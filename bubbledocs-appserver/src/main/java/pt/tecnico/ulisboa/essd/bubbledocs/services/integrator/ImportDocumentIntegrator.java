package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetExportedSpreadsheetName4IdService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetUsername4TokenService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.ImportDocumentService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.StoreRemoteServices;

public class ImportDocumentIntegrator extends BubbleDocsIntegrator {

	private Integer _sheetId;
	private String _username;
	private String _userToken;
	private byte[] _doc;
	private Integer _result;
	
	public ImportDocumentIntegrator(Integer sheetId, String userToken) {
		_sheetId = sheetId;
		_userToken = userToken;
		
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		// verifica se o user exportou a folha, pois so assim pode importa-la.  E verifica se a sessao é valida
		//CanImportDocumentService permission = new CanImportDocumentService(_sheetId, _userToken);
		//permission.execute();
		
	//  vai buscar username associado ao token
		GetUsername4TokenService userNameService = new GetUsername4TokenService(_userToken);
		userNameService.execute();
		
		
		_username = userNameService.getResult();
		
		GetExportedSpreadsheetName4IdService spreadsheetNameService = new GetExportedSpreadsheetName4IdService(_sheetId);
		spreadsheetNameService.execute();
		
		StoreRemoteServices remote = new StoreRemoteServices();
		
		
		try {
			
			_doc = remote.loadDocument(_username, spreadsheetNameService.getResult());
			
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
		
		
		ImportDocumentService local = new ImportDocumentService(_doc, _userToken);
		local.execute();
		_result = local.getResult();
	}
	
	public Integer getResult() {
		return _result;
	}
}
