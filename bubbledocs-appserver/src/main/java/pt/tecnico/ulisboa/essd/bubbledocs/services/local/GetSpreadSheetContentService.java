package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class GetSpreadSheetContentService extends ReadAndWritePermissionsService {  
	
	private String[][] _result;
	private int _folhaId;
	
	public GetSpreadSheetContentService(String userToken, int docId){
		
    	super(userToken, docId, false);
    	_folhaId = docId;
	}
	
	@Override
    protected void dispatch_read_and_write() throws BubbleDocsException {   
			
		FolhadeCalculo folha = _bd.getFolhaOfId(_folhaId);  
			
		/* 
		 * Filling the matrix matrix
		 * 		
		 */
		
		int maxLinha = folha.getLinhas() + 1;
		int maxColuna = folha.getColunas() + 1;
		
		_result = new String[maxLinha][maxColuna];
		
		for(int i=1; i < maxLinha; i++){
			for(int k=1; k < maxColuna; k++){
				_result[i][k]= folha.getCellContentValue(i, k);
			}
		}
			
    }
    
    public String[][] getResult() {
        return _result;
    }
	
	
}
