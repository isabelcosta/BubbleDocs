package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.CreateSpreadSheetService;

public class CreateSpreadSheetIntegrator extends BubbleDocsIntegrator {
	
	private String userToken;
	private int rows;
	private int columns;
	private String name;
	private CreateSpreadSheetService local;
	private Integer result;
	
	public CreateSpreadSheetIntegrator(String userToken,String name,int rows,int columns){
		
		this.userToken = userToken;
		this.name = name;
		this.rows = rows;
		this.columns = columns;
	
		
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		/*Instancia local*/

		local = new CreateSpreadSheetService(this.userToken, this.name, this.rows, this.columns);
		local.execute();
		result = local.getResult();
		
	}
	
	public Integer getResult() {
		return result;
	}
}
