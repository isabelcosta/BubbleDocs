package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class CreateSpreadSheet extends ValidSessionsService {
	
	private int rows;
	private int columns;
	private String name;
	private int result;
    
    

    public CreateSpreadSheet(String userToken, String name, int rows, int columns) {
    	
    	_userToken = userToken;
    	this.name = name;
    	this.rows = rows;
    	this.columns = columns;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	
		super.dispatch();
		
    	String dono = _bd.getUsernameOfToken(_userToken);
    	
    	FolhadeCalculo folha = new FolhadeCalculo(name, dono, rows, columns);
    	
    	_bd.addFolhas(folha);
    	
    	result = _bd.getIdOfFolha(name);
		
    }
    
    public int getResult() {
        return result;
    }
}
