package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class GetSpreadSheetContentService extends ReadAndWritePermissionsService {  
	
	private String[][] result;
	private int folhaId;
	
	public GetSpreadSheetContentService(String userToken, int docId){
		
    	super(userToken, docId, false);
    	folhaId = docId;
	}
	
	//@Override
    protected void dispatch_read_and_write() throws BubbleDocsException {    
    		
		FolhadeCalculo folha = _bd.getFolhaOfId(folhaId);  
			
		/* 
		 * Filling the matrix matrix
		 * 		
		 */
			int maxLinha = folha.getLinhas() + 1;
			int maxColuna = folha.getColunas() + 1;
			
			result = new String[maxLinha][maxColuna];
			
			for(int i=1; i < maxLinha; i++){
				for(int k=1; k < maxColuna; k++){
					result[i][k]= folha.getCellContentValue(i, k);
				}
			}
			
    }
    
    public String[][] getResult() {
        return result;
    }
	
	
}
