//package pt.tecnico.ulisboa.essd.bubbledocs.tests;
//
//import static org.junit.Assert.assertEquals;
//import AssignLiteralCellService;
//
//import org.junit.Test;
//
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
//
//// add needed import declarations
//
//public class AssignLiteralCellTest extends BubbleDocsServiceTest {
//
//    // the tokens
//    private String root;
//    private String ars;
//
//    private static final String USERNAME = "ars";
//    private static final String PASSWORD = "ars";
//    private static final String ROOT_USERNAME = "root";
//    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
//    private static final String LITERAL = "94";
//    private static final String SPREADSHEET_NAME = "myFolha";
//    private static final String CELL_ID = "5;4";
//    private static final int DOC_ID = 2;
//    private static final String USER_TOKEN = "JonhyBravo234";
//    
//    @Override
//    public void populate4Test() {
//        createUser(USERNAME, PASSWORD, "Ant�nio Rito Silva");
//        root = addUserToSession("root");
//        ars = addUserToSession("ars");
//    }
//
//    @Test
//    public void success() {
//        AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID, LITERAL);
//        service.execute();
//
//	// User is the domain class that represents a User
//        User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);
//
//        assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
//        assertEquals("jose", user.getPassword());
//        assertEquals("Jos� Ferreira", user.getName());
//    }
//
//    @Test(expected = LineNotInSpreadSheetException.class) // <--- tenho de arranjar excepcao
//    public void LineNotInSpreadSheet() {
//    	
//    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, "235;2", LITERAL);
//    	 service.execute();
//    }
//    
//    @Test(expected = LineNotPositiveException.class) // <--- tenho de arranjar excepcao
//    public void LineNotPositive() {
//    	
//    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, "-2;2", LITERAL);
//    	 service.execute();
//    }
//
//    @Test(expected = ColumnNotInSpreadSheetException.class) // <--- tenho de arranjar excepcao
//    public void ColumnNotInSpreadSheet() {
//    	
//    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, "4;345", LITERAL);
//         service.execute();
//    }
//
//    @Test(expected = UnauthorizedUserException.class) // <--- tenho de arranjar excepcao
//    public void unauthorizedUser() {
//    	
//    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID, LITERAL);
//         service.execute();
//    }
//
//    @Test(expected = UserNotLogged.class) // <--- tenho de arranjar excepcao
//    public void userNotLogged() {
//    	
//    	 removeFromSession(USER_TOKEN);
//    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID, LITERAL);
//         service.execute();
//    }    
//
//    @Test(expected = NotLiteralException.class) // <--- tenho de arranjar excepcao
//    public void notLiteral() {
//    	
//    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID, "AB4");
//         service.execute();
//    }
//
//    @Test(expected = SpreadSheetDoesNotExistsException.class) // <--- tenho de arranjar excepcao
//    public void SpreadSheetDoesNotExists() {
//
//    	AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, 34534, CELL_ID, LITERAL);
//    	service.execute();    	
//    }
//    
////    @Test(expected = rootAuthorizedException.class) // <--- tenho de arranjar excepcao
////    public void rootAuthorized() {
////    	// TODO ESTE TESTE
////    }
//}
