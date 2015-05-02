package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidFunctionException;

public class AssignBinaryFunctionToCellService extends ValidSessionsService {
    private String _result;
    private String _functionToAssign;
    private String _cellToFill;
    private int _folhaId;
    
    public AssignBinaryFunctionToCellService(String userToken, int docId, String cellId, String binaryFunction) {
	
    	super(userToken);
    	this._functionToAssign = binaryFunction;
        this._cellToFill = cellId;
        this._folhaId = docId;
    }

    @Override
    protected void dispatch_session() throws BubbleDocsException {
    		
			FolhadeCalculo folha = _bd.getFolhaOfId(_folhaId);
			
			if(folha.podeEscrever(_bd.getUsernameOfToken(_userToken))){
		    	int[] linhaColuna = null;
		
				linhaColuna = Parser.parseEndereco(_cellToFill, folha);
				
				try{
					Parser.parseBinaryFunction(folha, _functionToAssign);
				}catch(Exception e){
					throw new InvalidFunctionException(_functionToAssign);
				}
				
		    	folha.modificarCelula( linhaColuna[0], linhaColuna[1], _functionToAssign);
				
		    	for(Celula cell: folha.getCelulaSet()){
		    		if(cell.getLinha() == linhaColuna[0] && cell.getColuna() == linhaColuna[1]){
		    			_result = cell.getConteudo().getValor().toString();
		    		} 
		    	}
		    }
				
    }

    public String getResult() {
        return _result;
    }

}