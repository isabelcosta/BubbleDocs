package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Conteudo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Referencia;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.IdFolhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;

// add needed import declarations

public class AssignReferenceCellService extends BubbleDocsService {
    
    private String result;
    private String tokenDoUser;
    private int idFolha;
    private String idCelula;
    private String referencia;



    public AssignReferenceCellService(String tokenUser, int docId, String cellId, String reference) {
	// add code here
        this.tokenDoUser = tokenUser;
        this.idFolha = docId;
        this.idCelula = cellId;
        this.referencia = reference;

    }

    @Override
    protected void dispatch() throws OutOfBoundsException {
    	
    	if(!validSession(tokenDoUser)){
    		throw new UserNotInSessionException("Session for user " + tokenDoUser.substring(0, tokenDoUser.length()-1) + " is invalid" );
    	}else{
    		refreshToken(tokenDoUser);
    	}
    	
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    		
    	boolean existe = false;
    	FolhadeCalculo folha = null;
    	int[] linhaEcoluna = null;
    	Conteudo referenciaValida = null;
    	
    	for(FolhadeCalculo folhacalc : Bubbledocs.getInstance().getFolhasSet()){
    		if(folhacalc.getID() == idFolha){
    			folha = folhacalc;
    			existe = true;
    		}	
    	}
    	
    	if(existe == false){
    		throw new SpreadSheetDoesNotExistException(folha + "");
    	}
    	
    	Token token = null;

     	//obtem o objecto token para a seguir obter o username  
    	for(Token token2 : Bubbledocs.getInstance().getTokensSet()){
			if(token2.getToken().equals(tokenDoUser)){
				token = token2;
			}
		}
    	
    	if(folha.podeEscrever(token.getUsername())){
			
    		linhaEcoluna = Parser.parseEndereco(idCelula, folha);
			
			if (referencia.contains("=")){
				
				referenciaValida = Parser.parseConteudo(folha, referencia);

			} else
				throw new OutOfBoundsException(1,1);
	
			folha.modificarCelula( linhaEcoluna[0], linhaEcoluna[1], referencia);
			
			for(Celula celula : folha.getCelulaSet()){
				if(celula.getLinha() ==  linhaEcoluna[0] && celula.getColuna() == linhaEcoluna[1])
					result = celula.getConteudo().toString();
				}
    		}
    	}
  

    public final String getResult() {
        return result;
    }
}
