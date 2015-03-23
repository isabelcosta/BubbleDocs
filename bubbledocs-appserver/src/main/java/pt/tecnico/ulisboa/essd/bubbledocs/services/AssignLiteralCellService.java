package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DontHavePermissionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.NotLiteralException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
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
    	
    	if(!validSession(tokenUserLogged)){
    		throw new DontHavePermissionException("Session for user " + tokenUserLogged.substring(0, tokenUserLogged.length()-1) + " is invalid" );
    	}else{
    		refreshToken(tokenUserLogged);
    	}
    	
    	FolhadeCalculo folha = null;
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	boolean existe = false;
    	for( FolhadeCalculo folhaIter : bd.getFolhasSet()  ){
    		if(folhaIter.getID() == folhaId){
    			folha = folhaIter;
    			existe = true;
    		}
    	}
    	
    	if(existe == false){
    		throw new SpreadSheetDoesNotExistException(folhaId + "");
    	}
    	
     	//obtem o objecto token para a seguir obter o username   	
    	Token token = null;

    	for(Token token2 : Bubbledocs.getInstance().getTokensSet()){
			if(token2.getToken().equals(tokenUserLogged)){
				token = token2;
			}
		}

    	if(folha.podeEscrever(token.getUsername())){
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