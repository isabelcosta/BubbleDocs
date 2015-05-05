package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class GetSpreadSheetContentService extends ValidSessionsService {  
	
	private String[][] result;
	private int folhaId;
	
	public GetSpreadSheetContentService(String userToken, int docId){
		
		super(userToken);
    	folhaId = docId;
	}
	
	//@Override
    protected void dispatch_session() throws BubbleDocsException {    
    		
		FolhadeCalculo folha = _bd.getFolhaOfId(folhaId);     
			
		/* 
		 * Filling the matrix matrix
		 * 		
		 */
			int maxLinha = folha.getLinhas();
			int maxColuna = folha.getColunas();
			
			result = new String[maxLinha][maxColuna];
			
			for(int i=0; i < maxLinha+1; i++){
				for(int k=0; k < maxColuna+1; k++){
					result[i][k]= folha.contentToString(i, k);
				}
			}  	
    }
    
    public String[][] getResult() {
        return result;
    }
	
	
}
