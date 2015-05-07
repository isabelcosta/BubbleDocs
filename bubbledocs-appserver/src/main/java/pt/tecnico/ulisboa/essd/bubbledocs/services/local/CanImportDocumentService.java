package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.IdFolhaInvalidoException;

public class CanImportDocumentService extends ValidSessionsService {

	private Integer _sheetId;
	
	
	
	public CanImportDocumentService(Integer sheetId, String userToken) {
		super(userToken);
	}

	@Override
	protected void dispatch_session() throws BubbleDocsException {
		
		if (_sheetId == null || _sheetId < 0) {
			throw new IdFolhaInvalidoException();
		}
		
		if (!_bd.checkFolhaExportada4User(_sheetId, _userToken)) {
			throw new CannotLoadDocumentException(Integer.toString(_sheetId));
		}
	
	}

}
