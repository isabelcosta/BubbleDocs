package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class CreateSpreadSheetService extends ValidSessionsService {
	
	private int rows;
	private int columns;
	private String name;
	private int result;
    
    

    public CreateSpreadSheetService(String userToken, String name, int rows, int columns) {

    	super(userToken);
    	this.name = name;
    	this.rows = rows;
    	this.columns = columns;
    }

    @Override
    protected void dispatch_session() throws BubbleDocsException {
    	
    	String dono = _bd.getUsernameOfToken(_userToken);
    	FolhadeCalculo folha = new FolhadeCalculo(name, dono, rows, columns);
    	
    	_bd.addFolhas(folha);
    	
    	result = _bd.getIdOfFolha(name);
    }
    
    public int getResult() {
        return result;
    }
}
