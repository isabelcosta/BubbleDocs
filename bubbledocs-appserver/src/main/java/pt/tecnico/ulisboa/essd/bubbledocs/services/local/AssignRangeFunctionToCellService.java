package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class AssignRangeFunctionToCellService extends ValidSessionsService {
    private String _result;
    private String _functionToAssign;
    private String _cellToFill;
    private int _folhaId;
    
    public AssignRangeFunctionToCellService(String userToken, int docId, String idCelula, String rangeFunction) {
	
    	super(userToken);
    	_functionToAssign = rangeFunction;
        _cellToFill = idCelula;
        _folhaId = docId;
    }
    
    @Override
    protected void dispatch_session() throws BubbleDocsException {
    		
			FolhadeCalculo folha = _bd.getFolhaOfId(_folhaId);
			
			if(folha.podeEscrever(_bd.getUsernameOfToken(_userToken))){
		    	int[] linhaColuna = null;
		
				linhaColuna = Parser.parseEndereco(_cellToFill, folha);
				
				folha.modificarCelula( linhaColuna[0], linhaColuna[1], _functionToAssign);
				
				_result = folha.getCellContentToString(linhaColuna[0], linhaColuna[1]);
		    }
								
		}

    public String getResult() {
    	return _result;
	}

}