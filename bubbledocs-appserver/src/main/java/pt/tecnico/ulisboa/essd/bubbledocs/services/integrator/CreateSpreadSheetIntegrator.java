package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.CreateSpreadSheetService;

public class CreateSpreadSheetIntegrator extends BubbleDocsIntegrator {
	
	private String _userToken;
	private int _rows;
	private int _columns;
	private String _name;
	private CreateSpreadSheetService _local;
	private Integer _result;
	
	public CreateSpreadSheetIntegrator(String userToken,String name,int rows,int columns){
		
		_userToken = userToken;
		_name = name;
		_rows = rows;
		_columns = columns;
	
		
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		/*Instancia local*/

		_local = new CreateSpreadSheetService(_userToken, _name, _rows, _columns);
		_local.execute();
		_result = _local.getResult();
		
	}
	
	public Integer getResult() {
		return _result;
	}
}
