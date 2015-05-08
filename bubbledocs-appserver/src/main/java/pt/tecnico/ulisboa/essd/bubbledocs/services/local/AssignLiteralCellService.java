package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.NotLiteralException;


public class AssignLiteralCellService extends ValidSessionsService {
    private String _result;
    private String _literalToAssign;
    private String _cellToFill;
    private int _folhaId;
    
    public AssignLiteralCellService(String userToken, int docId, String cellId, String literal) {
	
    	super(userToken);
    	this._literalToAssign = literal;
        this._cellToFill = cellId;
        this._folhaId = docId;
    }

    @Override
    protected void dispatch_session() throws BubbleDocsException {
    		
			FolhadeCalculo folha = _bd.getFolhaOfId(_folhaId);
			
			if(folha.podeEscrever(_bd.getUsernameOfToken(_userToken))){
		    	int[] linhaColuna = null;
		
				linhaColuna = Parser.parseEndereco(_cellToFill, folha);
				
				//Verifica se o literal e um inteiro
				try{
					Integer.parseInt(_literalToAssign);
				}catch(Exception e){
					throw new NotLiteralException(_literalToAssign);
				}
				
		    	folha.modificarCelula( linhaColuna[0], linhaColuna[1], _literalToAssign);
				
    			_result = folha.getCellContentToString(linhaColuna[0], linhaColuna[1]);
		    }
				
    }

    public String getResult() {
        return _result;
    }

}