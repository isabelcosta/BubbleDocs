package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;



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
    	
		try {
			if(bd.validSession(userToken)){
				refreshToken(userToken);
		    	
				String dono = bd.getUsernameOfToken(userToken);
				
				FolhadeCalculo folha = new FolhadeCalculo(name, dono, rows, columns);
				
				bd.addFolhas(folha);
				
				result = bd.getIdOfFolha(name);
				
	    	}
		} catch (SpreadSheetDoesNotExistException | UserNotInSessionException e) {
			System.err.println("Couldn't create SpreadSheet: " + e);
		}
		
    }
    
    public int getResult() {
        return result;
    }
}
