package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetSpreadsheetName4IdService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetUsername4TokenService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.ImportDocumentService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.StoreRemoteServices;

public class ImportDocumentIntegrator extends BubbleDocsIntegrator {

	private int _sheetId;
	private String _username;
	private String _userToken;
	private byte[] _doc;
	
	public ImportDocumentIntegrator(int sheetId, String userToken) {
		_sheetId = sheetId;
		_userToken = userToken;
		
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		//  alem de ir buscar o username associado ao token, verifica se a sessao desse token é valida
		GetUsername4TokenService userNameService = new GetUsername4TokenService(_userToken);
		userNameService.execute();
		
		_username = userNameService.getUsername();
		
		
		GetSpreadsheetName4IdService spreadsheetNameService = new GetSpreadsheetName4IdService(_sheetId);
		spreadsheetNameService.execute();
		
		StoreRemoteServices remote = new StoreRemoteServices();
		
		/*
		 *  <QUESTION>
		 *  falta verificar se o username tem permissão para importar (se foi ele que exportou, atributo no user)
		 */
		
		try {
			
		/*NULL*/
			_doc = remote.loadDocument(_username, spreadsheetNameService.getResult());
		/*NULL <QUESTION>*/
			
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
		
		
														// _doc esta sempre a NULL
		ImportDocumentService local = new ImportDocumentService(_doc);
		local.execute();
	}
}
