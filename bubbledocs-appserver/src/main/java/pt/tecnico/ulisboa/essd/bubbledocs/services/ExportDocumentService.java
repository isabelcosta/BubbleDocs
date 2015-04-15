package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ReferenciaInvalidaException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;

public class ExportDocumentService extends BubbleDocsService{
    private org.jdom2.Document _result;
    private int _sheetId;
    private String _userToken;
    private FolhadeCalculo _folha;
    
    
    
    public ExportDocumentService(int sheetId, String userToken) {
		_sheetId = sheetId;
		_userToken = userToken;
	}
    
	@Override
	protected void dispatch() throws UserNotInSessionException {

		Bubbledocs bd = Bubbledocs.getInstance();
		try {

//VERIFICAR SE A SESSÃO É VÁLIDA
			if(bd.validSession(_userToken)){
				refreshToken(_userToken);
				
//VERIFICAR SE O USER TEM PERMISSÕES PARA EXPORTAR		
			_folha = bd.getFolhaOfId(_sheetId);
			
	    	if(_folha.podeLer(bd.getUsernameOfToken(_userToken)) || _folha.podeEscrever(bd.getUsernameOfToken(_userToken))){
//EXPORTAR A FOLHA    	
				_result = bd.exportSheet(_folha);
	    		}
			}
		} catch (ReferenciaInvalidaException | OutOfBoundsException e) {
			System.err.println("Couldn't export Sheet: " + e);
		} catch (UnauthorizedOperationException e ){
			throw new UnauthorizedOperationException();
		} catch (SpreadSheetDoesNotExistException e ){
			throw new SpreadSheetDoesNotExistException();
		}
	}
	
	public org.jdom2.Document getResult() {
        return _result;
    }

}
