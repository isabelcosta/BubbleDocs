package pt.tecnico.ulisboa.essd.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetSpreadSheetContentService;


public class GetSpreadSheetContentTest extends BubbleDocsServiceTest {
	
	
	private static final int SHEET_DOES_NOT_EXISTS = 999999;
	private static int DOC_ID = 0;
	private static int DOC_ID_EMPTY = 0;
	private static String USER_TOKEN;
	private static String USER_TOKEN_CANT_READ; 
	
	private FolhadeCalculo sheet;
	private FolhadeCalculo sheetEmpty;
	private String[][] matrix;

	 @Override
	 public void populate4Test() {
	    	
	    	//Limpa a base de dados
	    	unPopulate4Test();
	    	
	    	//Cria users
			Utilizador user = createUser("tep", "te#", "Teresa Palhoto");
			createUser("mit", "mi#", "Miguel Torrado");	
	    	    
	    	//Faz o login dos users
		
	    	USER_TOKEN = addUserToSession("tep");
	    	USER_TOKEN_CANT_READ = addUserToSession("mit");
	    	
	    	//cria folha
	    	sheet = createSpreadSheet(user, "teFolha", 10, 10);
	    	sheetEmpty = createSpreadSheet(user, "vaziaFolha", 10, 10);
			DOC_ID_EMPTY = sheetEmpty.getID();
			
			//Preenche a folha (sheet) do user "tep"
			DOC_ID = sheet.getID();
			String conteudoLiteral = "4";
			sheet.modificarCelula(3, 2, conteudoLiteral);
			
			String conteudoAdd = "=ADD(2,3;2)";
			sheet.modificarCelula(5,7,conteudoAdd);
			
	 }
	 
	   @Test
	    public void success() {
	    	
	        GetSpreadSheetContentService service = new GetSpreadSheetContentService(USER_TOKEN, DOC_ID);
	        service.execute();            
	    	
	        assertEquals("4", service.getResult()[3][2]);
	        assertEquals("6", service.getResult()[5][7]);
	    }
	   
	   @Test
	   public void contentNotSetEmpty() {
		   
		   GetSpreadSheetContentService service = new GetSpreadSheetContentService(USER_TOKEN, DOC_ID_EMPTY);
	       service.execute();
	       
	       matrix = new String[11][11];
	       matrix = service.getResult();
	  
		   for(int i=1; i < 11; i++){
			   for(int k=1; k < 11; k++){
				   assertEquals("()", matrix[i][k]);
			   }
		   }
		   
	   }
	   
	   @Test(expected = UserNotInSessionException.class)
	    public void userNotLogged() {
	    	
	    	 removeUserFromSession(USER_TOKEN);
	    	 GetSpreadSheetContentService service = new GetSpreadSheetContentService( USER_TOKEN, DOC_ID);
	         service.execute();
	    }
	   
	   @Test(expected = SpreadSheetDoesNotExistException.class)
	    public void SpreadSheetDoesNotExists() {

	    	GetSpreadSheetContentService service = new GetSpreadSheetContentService( USER_TOKEN, SHEET_DOES_NOT_EXISTS);
	    	service.execute();    	
	    }
	   
	   @Test(expected = UnauthorizedOperationException.class)
	    public void unauthorizedUserForReading() {
	    	
		   /*
		    *  Na criação de uma folha só o dono pode ler e escrever nela
		    *  		Tem de dar permissões posteriormente
		    */
	    	 GetSpreadSheetContentService service = new GetSpreadSheetContentService(USER_TOKEN_CANT_READ, DOC_ID);
	         service.execute();
	    }
	   
	   
}
