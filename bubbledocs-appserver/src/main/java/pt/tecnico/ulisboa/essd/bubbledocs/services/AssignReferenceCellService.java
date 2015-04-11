package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ReferenciaInvalidaException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;

// add needed import declarations

public class AssignReferenceCellService extends BubbleDocsService {
    
    private String result;
    private String tokenDoUser;
    private int idFolha;
    private String idCelula;
    private String referencia;



    public AssignReferenceCellService(String tokenUser, int docId, String cellId, String reference) {
	
        this.tokenDoUser = tokenUser;
        this.idFolha = docId;
        this.idCelula = cellId;
        this.referencia = reference;

    }

    @Override
    protected void dispatch() throws OutOfBoundsException {

    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	try {
			if(bd.validSession(tokenDoUser)){
				refreshToken(tokenDoUser);
				FolhadeCalculo folha = bd.getFolhaOfId(idFolha);
				
				
		    	int[] linhaEcoluna = null;
		    	
		    	if(folha.podeEscrever(bd.getUsernameOfToken(tokenDoUser))){
		    		linhaEcoluna = Parser.parseEndereco(idCelula, folha);	// lanca OutOfBounds
					
		    		try{
		    			Parser.parseConteudo(folha, referencia);
					}catch(Exception e){
						throw new ReferenciaInvalidaException(folha, referencia);
					}
		    		
		    		
	    			folha.modificarCelula( linhaEcoluna[0], linhaEcoluna[1], referencia);
	    			
	    			for(Celula celula : folha.getCelulaSet()){
	    				if(celula.getLinha() ==  linhaEcoluna[0] && celula.getColuna() == linhaEcoluna[1])
	    					result = celula.getConteudo().toString();
	    			}
					
    			}
			}
		} catch (UnauthorizedOperationException | ReferenciaInvalidaException | SpreadSheetDoesNotExistException | OutOfBoundsException e) {
			System.err.println("Couldn't assign Reference: " + e);
		}
	}
  

    public final String getResult() {
        return result;
    }
}
