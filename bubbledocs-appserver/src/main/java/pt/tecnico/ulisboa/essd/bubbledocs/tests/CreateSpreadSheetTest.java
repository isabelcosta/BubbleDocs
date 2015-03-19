package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.services.CreateSpreadSheet;

public class CreateSpreadSheetTest  extends BubbleDocsServiceTest {
	
	 // the tokens
    private String root;
    private String ars;

    private static final String USERNAME = "ars";
    private static final String PASSWORD = "ars";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final int ROWS = 20;
    private static final int COLUMNS = 20;
    private static final String USER_TOKEN = "userToken";
    private static final String SPREADSHEET_NAME = "folha1";
    
    @Test
    public void success() {
    	
    	CreateSpreadSheet service = CreateSpreadSheet(USER_TOKEN,SPREADSHEET_NAME , ROWS, COLUMNS);
   
        service.execute();

	
    //Testa se a linha é inferior a 1
        
    @Test(expected = DuplicateUsernameException.class) // <--- tenho de arranjar excepcao
    public void argLinhaInvalido() {
    	
    }
    	CreateSpreadSheet service = CreateSpreadSheet(USER_TOKEN,SPREADSHEET_NAME , ROWS, COLUMNS);
    	
    	service.execute();
    }
    
  //Testa se a coluna é inferior a 1
    
    @Test(expected = EmptyUsernameException.class) // <--- tenho de arranjar excepcao
    public void argColunaInvalida() {
    	
    	CreateSpreadSheet service = CreateSpreadSheet(USER_TOKEN,SPREADSHEET_NAME , ROWS, COLUMNS);
    	
    	service.execute();
    }



}
