package pt.tecnico.ulisboa.essd.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.IdFolhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetExportedSpreadsheetName4IdService;

public class GetExportedSpreadsheetName4IdServiceTest extends BubbleDocsServiceTest{



	private static final int SHEET_DOES_NOT_EXISTS = 999999;
	private static int DOC_ID;
	private static String USERNAME = "PSDD";
	private static String EMAIL = "pedro@ist.utl.pt";
	private static String NAME = "Pedro";
	private static String SPREADSHEET_NAME = "folha";
	private static String USER_TOKEN;
	private static String USER_TOKEN_INVALID = "nada";
	
	private FolhadeCalculo sheet;


	@Override
	public void populate4Test() {

		//Limpa a base de dados
		unPopulate4Test();

		//Cria o utilizador
		Utilizador user = createUser(USERNAME, EMAIL, NAME);
		USER_TOKEN = addUserToSession("PSDD");
		
		
		//cria a folha
		sheet = createSpreadSheet(user,SPREADSHEET_NAME, 10, 10);
		DOC_ID=sheet.getID();

		//exporta a folha
		exportFolhadeCalculo(DOC_ID, USER_TOKEN);
    	
		
	}
	
	@Test
    public void successTest() {
        GetExportedSpreadsheetName4IdService service = new GetExportedSpreadsheetName4IdService(DOC_ID);
        service.execute();
      
        assertEquals(SPREADSHEET_NAME, service.getResult());
    }
	 
	 /*
	  * Folha com ID inexistente
	  * 
	  */
	  
	@Test(expected = IdFolhaInvalidoException.class)
    public void folhaInexistenteTeste() {
        GetExportedSpreadsheetName4IdService service = new GetExportedSpreadsheetName4IdService(SHEET_DOES_NOT_EXISTS);
        service.execute();
    } 
}
