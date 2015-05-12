package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class CreateSpreadSheetService extends ValidSessionsService {
	
	private int _rows;
	private int _columns;
	private String _name;
	private int _result;
    
    

    public CreateSpreadSheetService(String userToken, String name, int rows, int columns) {

    	super(userToken);
    	_name = name;
    	_rows = rows;
    	_columns = columns;
    }

    @Override
    protected void dispatch_session() throws BubbleDocsException {
    	
    	String dono = _bd.getUsernameOfToken(_userToken);
    	FolhadeCalculo folha = new FolhadeCalculo(_name, dono, _rows, _columns);
    	
    	_bd.addFolhas(folha);
    	
    	_result = _bd.getIdOfFolha(_name);
    }
    
    public int getResult() {
        return _result;
    }
}
