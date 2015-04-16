package pt.tecnico.ulisboa.essd.bubbledocs.services;

import java.io.UnsupportedEncodingException;

import org.jdom2.output.XMLOutputter;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ReferenciaInvalidaException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.StoreRemoteServices;

public class ExportDocumentService extends BubbleDocsService{
    private byte[] _result;
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
		StoreRemoteServices remote = new StoreRemoteServices();
		byte[] resultTemp = null;
		try {

//VERIFICAR SE A SESSÃƒO Ã‰ VÃ�LIDA
			if(bd.validSession(_userToken)){
				refreshToken(_userToken);
			}
				
//VERIFICAR SE O USER TEM PERMISSÃ•ES PARA EXPORTAR		
			_folha = bd.getFolhaOfId(_sheetId);
			
			String userNameOfToken = bd.getUsernameOfToken(_userToken);
	    	if(_folha.podeLer(userNameOfToken) || _folha.podeEscrever(userNameOfToken)){
//EXPORTAR A FOLHA    	
	    		//fazer chamada remota
	    		
//CONVERTER A FOLHA EM BYTES
				org.jdom2.Document sheetDoc = bd.exportSheet(_folha);
				
				XMLOutputter xmlOutput = new XMLOutputter();
				xmlOutput.setFormat(org.jdom2.output.Format.getPrettyFormat());
				String docString = xmlOutput.outputString(sheetDoc);
				
				
				try {
					resultTemp = docString.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					System.out.println("export falhou: " + e);
				}
				
// CHAMAR O SERVICO REMOTO, SO ACTULIZANDO O RESULT CASO A CHAMADA REMOTA SEJA VALIDA
				try{
					remote.storeDocument(userNameOfToken, _folha.getNomeFolha(), resultTemp);
				} catch (RemoteInvocationException e){
					throw new UnavailableServiceException();
				}
				_result = resultTemp;
	    	}
		} catch (ReferenciaInvalidaException | OutOfBoundsException e) {
			System.err.println("Couldn't export Sheet: " + e);
		} catch (UnauthorizedOperationException e ){
			throw new UnauthorizedOperationException();
		} catch (SpreadSheetDoesNotExistException e ){
			throw new SpreadSheetDoesNotExistException();
		}
	}
	
	public byte[] getResult() {
        return _result;
    }

}
