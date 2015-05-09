package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import java.io.UnsupportedEncodingException;

import org.jdom2.output.XMLOutputter;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ReferenciaInvalidaException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.StoreRemoteServices;

public class ExportDocumentService extends ValidSessionsService{
    private byte[] _result;
    private int _sheetId;
    private FolhadeCalculo _folha;
    
    
    public ExportDocumentService(int sheetId, String userToken) {
    	super(userToken);
		_sheetId = sheetId;
	}
    
	@Override
	protected void dispatch_session() throws UserNotInSessionException {

		byte[] resultTemp = null;
		try {

				
//VERIFICAR SE O USER TEM PERMISSÃ•ES PARA EXPORTAR		
			_folha = _bd.getFolhaOfId(_sheetId);
			
			String userNameOfToken = _bd.getUsernameOfToken(_userToken);
	    	if(_folha.podeLer(userNameOfToken) || _folha.podeEscrever(userNameOfToken)){
	    		
//CONVERTER A FOLHA EM BYTES
				org.jdom2.Document sheetDoc = _bd.exportSheet(_folha);
				
				XMLOutputter xmlOutput = new XMLOutputter();
				xmlOutput.setFormat(org.jdom2.output.Format.getPrettyFormat());
				String docString = xmlOutput.outputString(sheetDoc);
				System.out.println(docString);
				
				try {
					resultTemp = docString.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					System.out.println("export falhou: " + e);
				}
//ADICIONAR AO USER ID DA FOLHA QUE EXPORTOU
				_bd.addFolhaExportada4User(_sheetId, _userToken);
				
				_result = resultTemp;
	    	}
		} catch (ReferenciaInvalidaException | OutOfBoundsException e) {
			System.err.println("Couldn't export Sheet: " + e);
		}
	}
	
	public byte[] getResult() {
        return _result;
    }

}
