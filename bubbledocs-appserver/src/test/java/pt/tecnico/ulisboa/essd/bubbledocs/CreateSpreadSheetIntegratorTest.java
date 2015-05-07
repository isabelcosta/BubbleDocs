package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ArgColunaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ArgLinhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.CreateSpreadSheetIntegrator;

public class CreateSpreadSheetIntegratorTest extends BubbleDocsServiceTest{
	

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
	  
	
	  CreateSpreadSheetIntegrator integrator = new CreateSpreadSheetIntegrator( token, SPREADSHEET_NAME, ROWS, COLUMNS);
      integrator.execute();
      
      String sheetName = getSpreadSheet(SPREADSHEET_NAME).getNomeFolha();
   
      assertEquals(SPREADSHEET_NAME,sheetName);
  }
  
  @Test (expected = UserNotInSessionException.class)
  public void invalidToken(){
	  CreateSpreadSheetIntegrator integrator = new CreateSpreadSheetIntegrator( BAD_USER_TOKEN, SPREADSHEET_NAME, ROWS, COLUMNS);
      integrator.execute();
  } 
  
  @Test (expected = ArgColunaInvalidoException.class)
  public void invalidColumn(){
	  CreateSpreadSheetIntegrator integrator = new CreateSpreadSheetIntegrator( token, SPREADSHEET_NAME, ROWS, BAD_COLUMN);
      integrator.execute();
  }
  
  @Test (expected = ArgLinhaInvalidoException.class)
  public void invalidRow(){
	  CreateSpreadSheetIntegrator integrator = new CreateSpreadSheetIntegrator( token, SPREADSHEET_NAME, BAD_ROW, COLUMNS);
      integrator.execute();
  }
  
}
