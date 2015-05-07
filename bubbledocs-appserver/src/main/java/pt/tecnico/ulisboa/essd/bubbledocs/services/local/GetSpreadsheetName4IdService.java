package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class GetSpreadsheetName4IdService extends BubbleDocsService {

	
	private Bubbledocs _bd = Bubbledocs.getInstance();
	private int _sheetId;
	private String _result;
	
	public GetSpreadsheetName4IdService(int sheetId) {
		_sheetId = sheetId;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		_bd.getSpreadsheetName4Id(_sheetId);
	}

	public String getResult () {
		return _result;
	}
	
}
