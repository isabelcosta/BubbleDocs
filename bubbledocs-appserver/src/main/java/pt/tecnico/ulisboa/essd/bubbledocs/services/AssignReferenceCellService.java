/*package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;

// add needed import declarations

public class AssignReferenceCell extends BubbleDocsService {
    
    private String result;
    private String tokenDoUser;
    private int idFolha;
    private String idCelula;
    private String referencia;



    public AssignReferenceCell(String tokenUser, int docId, String cellId, String reference) {
	// add code here
        this.tokenDoUser = tokenUser;
        this.idFolha = docId;
        this.idCelula = cellId;
        this.referencia = reference;

    }

    @Override
    protected void dispatch() throws BubbleDocsException {
	// add code here

    	FolhadeCalculo folha = null;
    	
    	for(FolhadeCalculo folhacalc : Bubbledocs.getInstance().getFolhasSet()){
    		if(folha.getID() == idFolha){
    			folha = folhacalc;
    		}	
    	}
    	
    	int[] linhaEcoluna = parseEndereco(idCelula, folha);
    	folha.modificarCelula( linhaEcoluna[0], linhaEcoluna[1], referencia);
 	
    	result = 

    }

    public final String getResult() {
        return result;
    }
}*/
