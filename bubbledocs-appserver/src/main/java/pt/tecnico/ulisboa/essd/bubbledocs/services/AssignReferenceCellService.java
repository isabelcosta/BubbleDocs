package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ReferenciaInvalidaException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;

// add needed import declarations

public class AssignReferenceCellService extends ValidSessionsService {
    
    private String result;
    private int idFolha;
    private String idCelula;
    private String referencia;



    public AssignReferenceCellService(String tokenUser, int docId, String cellId, String reference) {
	
        setuserToken(tokenUser);
        this.idFolha = docId;
        this.idCelula = cellId;
        this.referencia = reference;

    }

    @Override
    protected void dispatch() throws OutOfBoundsException, UnauthorizedOperationException  {

    		super.dispatch();
			FolhadeCalculo folha = _bd.getFolhaOfId(idFolha);
			
			
	    	int[] linhaEcoluna = null;
	    	
	    	if(folha.podeEscrever(_bd.getUsernameOfToken(_userToken))){
	    		linhaEcoluna = Parser.parseEndereco(idCelula, folha);	// lanca OutOfBounds
				
	    		try{
	    			if(referencia.contains("=")){
	    				Parser.parseConteudo(folha, referencia);
	    			}
	    			else
	    				throw new ReferenciaInvalidaException(folha,referencia);
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
  

    public final String getResult() {
        return result;
    }
}
