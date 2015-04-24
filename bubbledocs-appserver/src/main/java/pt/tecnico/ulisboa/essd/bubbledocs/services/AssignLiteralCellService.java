package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.NotLiteralException;


public class AssignLiteralCellService extends ValidSessionsService {
    private String result;
    private String literalToAssign;
    private String cellToFill;
    private int folhaId;
    
    public AssignLiteralCellService(String userToken, int docId, String cellId, String literal) {
	
    	super(userToken);
    	this.literalToAssign = literal;
        this.cellToFill = cellId;
        this.folhaId = docId;
    }

    @Override
    protected void dispatch_session() throws BubbleDocsException {
    		
			FolhadeCalculo folha = _bd.getFolhaOfId(folhaId);
			
			if(folha.podeEscrever(_bd.getUsernameOfToken(_userToken))){
		    	int[] linhaColuna = null;
		
				linhaColuna = Parser.parseEndereco(cellToFill, folha);
				
				//Verifica se o literal e um inteiro
				try{
					Integer.parseInt(literalToAssign);
				}catch(Exception e){
					throw new NotLiteralException(literalToAssign);
				}
				
		    	folha.modificarCelula( linhaColuna[0], linhaColuna[1], literalToAssign);
				
		    	for(Celula cell: folha.getCelulaSet()){
		    		if(cell.getLinha() == linhaColuna[0] && cell.getColuna() == linhaColuna[1]){
		    			result = cell.getConteudo().getValor().toString();
		    		} 
		    	}
		    }
				
    }

    public String getResult() {
        return result;
    }

}