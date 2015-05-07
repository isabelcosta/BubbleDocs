package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidFunctionException;

public class AssignRangeFunctionToCellService extends ValidSessionsService {
    private String _result;
    private String _functionToAssign;
    private String _cellToFill;
    private int _folhaId;
    
    public AssignRangeFunctionToCellService(String userToken, int docId, String idCelula, String rangeFunction) {
	
    	super(userToken);
    	this._functionToAssign = rangeFunction;
        this._cellToFill = idCelula;
        this._folhaId = docId;
    }
    
    @Override
    protected void dispatch_session() throws BubbleDocsException {
    		
			FolhadeCalculo folha = _bd.getFolhaOfId(_folhaId);
			
			if(folha.podeEscrever(_bd.getUsernameOfToken(_userToken))){
		    	int[] linhaColuna = null;
		
				linhaColuna = Parser.parseEndereco(_cellToFill, folha);
				
				try{
//					Parser.parseBinaryFunction(folha, _functionToAssign);
					folha.modificarCelula( linhaColuna[0], linhaColuna[1], _functionToAssign);		//=AVG(1;2 : 1;10)		
				}catch(Exception e){
					throw new InvalidFunctionException(_functionToAssign);
				}
			
				/*
				 * 
				 * criar uma funcao pra buscar o conteudo pra nao expor a logica de negocio
				 * 
				 */
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