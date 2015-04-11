package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.NotLiteralException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;


public class AssignLiteralCellService extends BubbleDocsService {
    private String result;
    private String literalToAssign;
    private String cellToFill;
    private int folhaId;
    private String tokenUserLogged;
    
    public AssignLiteralCellService(String tokenUser, int docId, String cellId, String literal) {
	
    	this.literalToAssign = literal;
        this.cellToFill = cellId;
        this.folhaId = docId;
        this.tokenUserLogged = tokenUser;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	try {
			if(bd.validSession(tokenUserLogged)){
				refreshToken(tokenUserLogged);
				FolhadeCalculo folha = bd.getFolhaOfId(folhaId);
				
				if(folha.podeEscrever(bd.getUsernameOfToken(tokenUserLogged))){
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
		} catch (UnauthorizedOperationException | NotLiteralException | OutOfBoundsException | SpreadSheetDoesNotExistException e) {
			System.err.println("Couldn't assign Literal: " + e);
		}
    	
    }

    public String getResult() {
        return result;
    }

}