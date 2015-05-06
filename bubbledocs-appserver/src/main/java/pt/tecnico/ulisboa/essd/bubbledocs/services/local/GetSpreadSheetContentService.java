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
		
		if(folha.podeLer(_bd.getUsernameOfToken(_userToken))){     //melhorar esta parte depois
			
		/* 
		 * Filling the matrix matrix
		 * 		
		 */
			int maxLinha = folha.getLinhas() + 1;
			int maxColuna = folha.getColunas() + 1;
			
			result = new String[maxLinha][maxColuna];
			
			for(int i=1; i < maxLinha; i++){
				for(int k=1; k < maxColuna; k++){
					result[i][k]= folha.contentToString(i, k);
				}
			}
			
		}
    }
    
    public String[][] getResult() {
        return result;
    }
	
	
}
