package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetSpreadsheetName4IdService;

public class GetSpreadsheetName4IdServiceTest extends BubbleDocsServiceTest{



	private static final int SHEET_DOES_NOT_EXISTS = 999999;
	private static int DOC_ID;
	private static String USERNAME = "PSDD";
	private static String EMAIL = "pedro@ist.utl.pt";
	private static String NAME = "Pedro";
	private static String SPREADSHEET_NAME = "folha";

	private FolhadeCalculo sheet;


	@Override
	public void populate4Test() {

		//Limpa a base de dados
		unPopulate4Test();

		//Cria users
		Utilizador user = createUser(USERNAME, EMAIL, NAME);

		//cria folha
		sheet = createSpreadSheet(user,SPREADSHEET_NAME, 10, 10);
		DOC_ID=sheet.getID();
		
	}
	
	 @Test
	    public void success() {
	        
	        GetSpreadsheetName4IdService service = new GetSpreadsheetName4IdService(DOC_ID);
	        service.execute();
	      
	        assertEquals(SPREADSHEET_NAME, service.getResult());
	    }
	 
	 /*
	  * Folha com ID inexistente
	  * 
	  */
	  
	 @Test(expected = SpreadSheetDoesNotExistException.class)
	    public void folhaInexistente() {
	        
	        GetSpreadsheetName4IdService service = new GetSpreadsheetName4IdService(SHEET_DOES_NOT_EXISTS);
	        service.execute();
	    } 
}
