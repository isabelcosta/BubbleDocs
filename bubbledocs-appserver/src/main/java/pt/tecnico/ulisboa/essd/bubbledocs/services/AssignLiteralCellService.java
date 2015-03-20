package pt.tecnico.ulisboa.essd.bubbledocs.services;

// add needed import declarations

public class AssignLiteralCellService extends BubbleDocsService {
    private String result;
    private String literalToAssign;
    private String cellToFill;
    private int folhaId;
    private String tokenUserLogged;

    public AssignLiteralCellService(String tokenUser, int docId, String cellId, String literal) {
	// add code here	
    	this.literalToAssign = literal;
        this.cellToFill = cellId;
        this.folhaId = docId;
        this.tokenUserLogged = tokenUser;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
	// add code here
    	FolhadeCalculo folha = null;
    	for( FolhadeCalculo folhaIter : getBubbleDocs().getFolhasCriadasSet()  ){
    		if(folhaIter.getId() == folhaId){
    			folha = folhaIter;
    		}
    	}
    	    	
    	int[] linhaColuna = parseEndereco(cellToFill, folha);
    	
    	folha.modificarCelula( linhaColuna[0], linhaColuna[1], literalToAssign);
    	
    	result = folha.getCelula().getConteudo().toString();
    }

    public String getResult() {
        return result;
    }

}