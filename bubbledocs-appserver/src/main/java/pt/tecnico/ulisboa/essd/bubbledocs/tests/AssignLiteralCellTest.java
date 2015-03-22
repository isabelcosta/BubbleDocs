package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DontHavePermissionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.NotLiteralException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.AssignLiteralCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.BubbleDocsService;

// add needed import declarations

public class AssignLiteralCellTest extends BubbleDocsServiceTest {

//    // the tokens
//    private String root;
//    private String ars;

    private static final String USERNAME = "ars";
    private static final String PASSWORD = "ars";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String LITERAL = "94";
    private static final String SPREADSHEET_NAME = "myFolha";
    private static final String CELL_ID = "3;4";
    private static final int DOC_ID = 10;
    private static final String USER_TOKEN = "JonhyBravo234";
    
    @Override
    public void populate4Test() {
//        createUser(USERNAME, PASSWORD, "Antï¿½nio Rito Silva");
//        root = addUserToSession("root");
//        ars = addUserToSession("ars");
    	
    	//Substituir as funcoes por servicos?!?!
//    	System.out.println("ENCONTREI FOLHA");
//    	for( FolhadeCalculo folhaIter : BubbleDocsService.getBubbleDocs().getFolhasSet() ){
//			if(folhaIter.getNomeFolha().equals("Notas ES")){
//				System.out.println("---------    " + folhaIter.getID());
//			}
//		}
    }

    @Test
    public void success() {
    	
        AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID, LITERAL);
        service.execute();

//	// User is the domain class that represents a User
//        Utilizador user = Bubbledocs.getUserFromUsername(USERNAME_DOES_NOT_EXIST);

		FolhadeCalculo folha = null;
		String literalInCell = null;
		
    	for( FolhadeCalculo folhaIter : BubbleDocsService.getBubbleDocs().getFolhasSet() ){
			if(folhaIter.getID() == DOC_ID){
				folha = folhaIter;
			}
		}
    	int[] linhaColuna = null;        
		try {
			linhaColuna = Parser.parseEndereco(CELL_ID, folha);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	for(Celula cell: folha.getCelulaSet()){
    		if(cell.getLinha() == linhaColuna[0] && cell.getColuna() == linhaColuna[1]){
    			literalInCell = cell.getConteudo().getValor().toString();
    		}
    	}
    	
        assertEquals(LITERAL, literalInCell);
    }

    @Test(expected = OutOfBoundsException.class)
    public void LineOutOfBoundOfSpreadSheet() {
    	
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, "7000;2", LITERAL);
    	 service.execute();
    }
    
    @Test(expected = OutOfBoundsException.class)
    public void LineNegative() {
    	
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, "-2;2", LITERAL);
    	 service.execute();
    }

    @Test(expected = OutOfBoundsException.class)
    public void ColumnOutOfBoundOfSpreadSheet() {
    	
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, "4;3457", LITERAL);
         service.execute();
    }
    
    @Test(expected = OutOfBoundsException.class)
    public void ColumnNegativeSpreadSheet() {
    	
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, "4;-1", LITERAL);
         service.execute();
    }
    
//	FALTA TESTARRRRRRRRRRRRRRRRRRRRRRRRR 
//    @Test(expected = DontHavePermissionException.class) // <--- tenho de arranjar excepcao
//    public void unauthorizedUser() {
//    	
//    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID, LITERAL);
//         service.execute();
//    }
    
//		FALTA TESTARRRRRRRRRRRRRRRRRRRRRRRRR 
//    @Test(expected = UserNotLogged.class) // <--- tenho de arranjar excepcao
//    public void userNotLogged() {
//    	
//    	 removeFromSession(USER_TOKEN);
//    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID, LITERAL);
//         service.execute();
//    }    
    
    @Test(expected = NotLiteralException.class)
    public void notLiteral() {
    	
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID, "AB4");
         service.execute();
    }
    
    @Test(expected = SpreadSheetDoesNotExistException.class)
    public void SpreadSheetDoesNotExists() {

    	AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, 34534, CELL_ID, LITERAL);
    	service.execute();    	
    }
    
}
