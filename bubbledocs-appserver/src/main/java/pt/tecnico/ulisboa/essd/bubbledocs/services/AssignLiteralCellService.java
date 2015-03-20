package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;


public class AssignLiteralCellService extends BubbleDocsService {
    private String result;
    private String literalToAssign;
    private String cellToFill;
    private int folhaId;
    private String tokenUserLogged;

    //SO FALATA A QUESTAO DO USER LOGADO----------------------------------------
    
    public AssignLiteralCellService(String tokenUser, int docId, String cellId, String literal) {
	
    	this.literalToAssign = literal;
        this.cellToFill = cellId;
        this.folhaId = docId;
        this.tokenUserLogged = tokenUser;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
	
    	FolhadeCalculo folha = null;
    	for( FolhadeCalculo folhaIter : Bubbledocs.getInstance().getFolhasSet()  ){
    		if(folhaIter.getID() == folhaId){
    			folha = folhaIter;
    		}
    	}
    	    	
    	int[] linhaColuna = null;
		try {
			linhaColuna = Parser.parseEndereco(cellToFill, folha);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		
    	folha.modificarCelula( linhaColuna[0], linhaColuna[1], literalToAssign);
    	
    	for(Celula cell: folha.getCelulaSet()){
    		if(cell.getLinha() == linhaColuna[0] && cell.getColuna() == linhaColuna[1]){
    			result = cell.getConteudo().getValor().toString();
    		}
    	}
    	
    }

    public String getResult() {
        return result;
    }

}