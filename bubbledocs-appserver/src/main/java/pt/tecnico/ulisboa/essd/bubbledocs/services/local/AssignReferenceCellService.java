package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ReferenciaInvalidaException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;

public class AssignReferenceCellService extends ReadAndWritePermissionsService {
    
    private String _result;
    private int _idFolha;
    private String _idCelula;
    private String _referencia;
    

    public AssignReferenceCellService(String userToken, int docId, String cellId, String reference) {
    	super(userToken, docId, true);
        _idFolha = docId;
        _idCelula = cellId;
        _referencia = reference;

    }

    @Override
    protected void dispatch_read_and_write() throws OutOfBoundsException, UnauthorizedOperationException  {
    	Bubbledocs _bd = getBubbleDocs();
    		
			FolhadeCalculo folha = _bd.getFolhaOfId(_idFolha);
			
	    	int[] linhaEcoluna = null;
	    	
    		linhaEcoluna = Parser.parseEndereco(_idCelula, folha);	// lanca OutOfBounds
			
    		try{
    			if(_referencia.contains("=")){
    				Parser.parseConteudo(folha, _referencia);
    			} else {
    				throw new ReferenciaInvalidaException(folha,_referencia);
    			}
    		} catch(Exception e){
				throw new ReferenciaInvalidaException(folha, _referencia);
			}
    		
			folha.modificarCelula( linhaEcoluna[0], linhaEcoluna[1], _referencia);
			
			_result = folha.getCellContentToString(linhaEcoluna[0], linhaEcoluna[1]);
	}
  

    public final String getResult() {
        return _result;
    }
}
