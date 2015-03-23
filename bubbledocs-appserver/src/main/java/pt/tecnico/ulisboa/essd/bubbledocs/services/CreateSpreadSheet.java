package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DontHavePermissionException;



public class CreateSpreadSheet extends BubbleDocsService {
	
	private int rows;
	private int columns;
	private String name;
	private String userToken;
	private int result;
    
    

    public CreateSpreadSheet(String userToken, String name, int rows, int columns) {
    	
    	this.userToken = userToken;
    	this.name = name;
    	this.rows = rows;
    	this.columns = columns;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	if(!validSession(userToken)){
    		throw new DontHavePermissionException("Session for user " + userToken.substring(0, userToken.length()-1) + " is invalid" );
    	}else{
    		refreshToken(userToken);
    	}

    	for(Token token : Bubbledocs.getInstance().getTokensSet()){

    		if(token.getToken().equals(userToken)){

    			FolhadeCalculo folha = new FolhadeCalculo();
    			
    			String dono = userToken.substring(0, userToken.length()-1);
    			
    			folha.setDono(dono);              
    			folha.setLinhas(rows);
    			folha.setColunas(columns);
    			folha.setNomeFolha(name);

    			bd.addFolhas(folha);
    			
    			//apos a folha criada, ir buscar o ID
    			for(FolhadeCalculo f : bd.getFolhasSet()){
    				if (f.getNomeFolha().equals(name))
    					result = f.getID();		
    			}
    		}
    		
    		else{		
    			throw new DontHavePermissionException(userToken);	
    		}	
    	}
    }
    
    public int getResult() {
        return result;
    }
}
