package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ArgColunaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ArgLinhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.CreateSpreadSheetService;



public class CreateSpreadSheetTest extends BubbleDocsServiceTest {

	private static final String SPREADSHEET_NAME = "myFolha";
	private static final int ROWS = 100;
	private static final int BAD_ROW = -1;
	private static final int COLUMNS = 100;
	private static final int BAD_COLUMN = -1;
	private static final String BAD_USER_TOKEN = "false-token";
	private static final String USERNAME = "frc";
	private static final String PASSWORD = "frc";

	private String token;
  
  @Override
  public void populate4Test() {
	  
	  	Utilizador user = createUser(USERNAME, PASSWORD, "Fabio Carvalho");     //so usado para ser adicionado a sessao
	  	token = addUserToSession(USERNAME);
  	
  }

  @Test
  public void success() {
	  
	
	  CreateSpreadSheetService service = new CreateSpreadSheetService( token, SPREADSHEET_NAME, ROWS, COLUMNS);
      service.execute();
      
      String sheetName = getSpreadSheet(SPREADSHEET_NAME).getNomeFolha();
   
      assertEquals(SPREADSHEET_NAME,sheetName);
  }
  
  @Test (expected = UserNotInSessionException.class)
  public void invalidToken(){
	  CreateSpreadSheetService service = new CreateSpreadSheetService( BAD_USER_TOKEN, SPREADSHEET_NAME, ROWS, COLUMNS);
      service.execute();
  } 
  
  @Test (expected = ArgColunaInvalidoException.class)
  public void invalidColumn(){
	  CreateSpreadSheetService service = new CreateSpreadSheetService( token, SPREADSHEET_NAME, ROWS, BAD_COLUMN);
      service.execute();
  }
  
  @Test (expected = ArgLinhaInvalidoException.class)
  public void invalidRow(){
	  CreateSpreadSheetService service = new CreateSpreadSheetService( token, SPREADSHEET_NAME, BAD_ROW, COLUMNS);
      service.execute();
  }
  
}
