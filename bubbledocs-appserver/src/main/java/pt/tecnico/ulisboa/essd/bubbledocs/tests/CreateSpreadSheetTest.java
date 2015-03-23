package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ArgColunaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ArgLinhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UtilizadorInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.CreateSpreadSheet;



public class CreateSpreadSheetTest extends BubbleDocsServiceTest {

	private static final String SPREADSHEET_NAME = "myFolha";
	private static final int ROWS = 100;
	private static final int BAD_ROW = -1;
	private static final int COLUMNS = 100;
	private static final int BAD_COLUMN = -1;
	private static final String BAD_USER_TOKEN = "false-token";

	private String token;
  
  @Override
  public void populate4Test() {
	  	user = createUser(USERNAME, PASSWORD, "Fabio Carvalho");
	  	token = addUserToSession("frc");
  	
  }

  @Test
  public void success() {
	  
	
	  CreateSpreadSheet service = new CreateSpreadSheet( token, SPREADSHEET_NAME, ROWS, COLUMNS);
      service.execute();
      
      String sheetName = getSpreadSheet(SPREADSHEET_NAME).getNomeFolha();
   
      assertEquals(SPREADSHEET_NAME,sheetName);
  }
  
  @Test (expected = UtilizadorInvalidoException.class)
  public void invalidToken(){
	  CreateSpreadSheet service = new CreateSpreadSheet( BAD_USER_TOKEN, SPREADSHEET_NAME, ROWS, COLUMNS);
      service.execute();
  } 
  
  @Test (expected = ArgColunaInvalidoException.class)
  public void invalidColumn(){
	  CreateSpreadSheet service = new CreateSpreadSheet( BAD_USER_TOKEN, SPREADSHEET_NAME, ROWS, BAD_COLUMN);
      service.execute();
  }
  
  @Test (expected = ArgLinhaInvalidoException.class)
  public void invalidRow(){
	  CreateSpreadSheet service = new CreateSpreadSheet( BAD_USER_TOKEN, SPREADSHEET_NAME, BAD_ROW, COLUMNS);
      service.execute();
  }
  
}
