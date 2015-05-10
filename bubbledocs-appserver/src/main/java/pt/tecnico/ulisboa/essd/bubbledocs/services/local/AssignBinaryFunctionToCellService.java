package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class AssignBinaryFunctionToCellService extends ReadAndWritePermissionsService {
	
    private String _result;
    private String _functionToAssign;
    private String _cellToFill;
    private int _folhaId;
    
    public AssignBinaryFunctionToCellService(String userToken, int docId, String cellId, String binaryFunction) {
	
    	super(userToken, docId, true);
    	_functionToAssign = binaryFunction;
        _cellToFill = cellId;
        _folhaId = docId;
    }

    @Override
    protected void dispatch_read_and_write() throws BubbleDocsException {
    		
		FolhadeCalculo folha = _bd.getFolhaOfId(_folhaId);

		int[] linhaColuna = null;

		linhaColuna = Parser.parseEndereco(_cellToFill, folha);

		folha.modificarCelula( linhaColuna[0], linhaColuna[1], _functionToAssign);				

		_result = folha.getCellContentToString(linhaColuna[0], linhaColuna[1]);
				
    }

    public String getResult() {
        return _result;
    }

}