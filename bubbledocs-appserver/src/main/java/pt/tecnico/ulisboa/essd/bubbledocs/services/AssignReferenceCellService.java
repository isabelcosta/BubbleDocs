package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

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
    protected void dispatch() throws BubbleDocsException {

    	//falta verificar o token
    	
    	boolean existe = false;
    	
    	for(Token token : Bubbledocs.getInstance().getTokensSet()){
    		if(token.equals(tokenDoUser)){
    			
    		}
    	}
    		

    	FolhadeCalculo folha = null;
    	int[] linhaEcoluna = null;
    	
    	for(FolhadeCalculo folhacalc : Bubbledocs.getInstance().getFolhasSet()){
    		if(folha.getID() == idFolha){
    			folha = folhacalc;
    		}	
    	}
    	

		try {
			linhaEcoluna = Parser.parseEndereco(idCelula, folha);
			folha.modificarCelula( linhaEcoluna[0], linhaEcoluna[1], referencia);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	for(Celula celula : folha.getCelulaSet()){
    		if(celula.getLinha() ==  linhaEcoluna[0] && celula.getColuna() == linhaEcoluna[1])
    			result = celula.getConteudo().toString();
    	}

    }

    public final String getResult() {
        return result;
    }
}
