package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DontHavePermissionException;

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
	protected void dispatch() throws DontHavePermissionException {

//VERIFICAR SE A SESSÃO É VÁLIDA
		if(_userToken == null){
			throw new DontHavePermissionException("Session for user is invalid" );
		}
		if(!validSession(_userToken)){
    		throw new DontHavePermissionException("Session for user " + _userToken.substring(0, _userToken.length()-1) + " is invalid" );
    	}else{
    		refreshToken(_userToken);
    	}
//VERIFICAR SE O USER TEM PERMISSÕES PARA EXPORTAR		
		for(FolhadeCalculo folhaIter : Bubbledocs.getInstance().getFolhasSet()){
			if(folhaIter.getID() == _sheetId ){
				_folha = folhaIter;
			}
		}
		
		Token token = null;

    	for(Token tokenObject : Bubbledocs.getInstance().getTokensSet()){
			if(tokenObject.getToken().equals(_userToken)){
				token = tokenObject;
			}
		}
    	
    	if(_folha.podeEscrever(token.getUsername()) || _folha.podeEscrever(token.getUsername())){
//EXPORTAR A FOLHA    	
    		org.jdom2.Document jdomDoc = new org.jdom2.Document();
			jdomDoc.setRootElement(_folha.exportToXML());
			_result = jdomDoc;
    	}else{
    		throw new DontHavePermissionException("User " + _userToken.substring(0, _userToken.length()-1) + " have no write, read or owner permissions" ); 
    	}
		
	}
	
	public org.jdom2.Document getResult() {
        return _result;
    }

}
