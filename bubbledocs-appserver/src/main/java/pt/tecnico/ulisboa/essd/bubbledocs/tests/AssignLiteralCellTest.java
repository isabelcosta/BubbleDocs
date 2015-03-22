package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DontHavePermissionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.NotLiteralException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.AssignLiteralCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.BubbleDocsService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.LoginUser;

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
    private static final String CELL_ID = "3;2";
    private static final int DOC_ID = 13;
    private static final String USER_TOKEN = "JonhyBravo234";
    
    @Override
    public void populate4Test() {
//        createUser(USERNAME, PASSWORD, "Antï¿½nio Rito Silva");
//        root = addUserToSession("root");
//        ars = addUserToSession("ars");
    	
    	//------------------------------------------------------------------------------------
        // setup the initial state if bubbledocs is empty
    	unPopulate4Test();
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
		Utilizador user1 = new Utilizador("abelha maya", "ab", "maya");
    	bd.addUtilizadores(user1);
    	
    	Utilizador user2 = new Utilizador("pipi meias altas", "pi", "altas");
    	bd.addUtilizadores(user2);			
    	    	
    	
//    	for(Token token : Bubbledocs.getInstance().getTokensSet()){
//    		if(!token.getUsername().equals("Paul Door")){
//    			LoginUser login = new LoginUser("Paul Door", "sub");
//    	 		login.execute(); //	-> cria o result
//    	 		Bubbledocs.getInstance().addTokens(new Token("Paul Door", login.getResult()));
//    		}
//		}
//    	
//    	
// 		for(Token token : Bubbledocs.getInstance().getTokensSet()){
//			int minutes = Minutes.minutesBetween(token.getTime(), new LocalTime()).getMinutes();
//			System.out.println(minutes);
//			if(minutes > 120){
//				Bubbledocs.getInstance().getTokensSet().remove(token);
//			}
//		}
		
    	 
		bd.criaFolha("abFolha","ab",20, 30);
		bd.criaFolha("piFolha","pi",40, 11);
    	
    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
    		if(folhaIter.getNomeFolha().equals("abFolha")){
    			
    			String conteudoLiteral = "4";
    			folhaIter.modificarCelula(3, 2, conteudoLiteral);
    			
    			
    			String conteudoAdd = "=ADD(2,3;2)";
    			folhaIter.modificarCelula(5,7,conteudoAdd);
    			
    		} else if (folhaIter.getNomeFolha().equals("piFolha")){
    			
    			String conteudoRef = "=3;2";
    			folhaIter.modificarCelula(2,7,conteudoRef);   			
    		}
    	}
    	//------------------------------------------------------------------------------------
    	
    	//Substituir as funcoes por servicos?!?!
    	System.out.println("ENCONTREI FOLHA");
    	for( FolhadeCalculo folhaIter : BubbleDocsService.getBubbleDocs().getFolhasSet() ){
				System.out.println("Nome: " + folhaIter.getNomeFolha() + "    Id: " + folhaIter.getID() + "    Dono: " + folhaIter.getDono());
			   	for( Celula cell : folhaIter.getCelulaSet() ){
					System.out.println("Linha: " + cell.getLinha() + "    Coluna: " + cell.getColuna() + "    Conteudo: " + cell.getConteudo());
			   	}
    	}
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
     
    @Test(expected = DontHavePermissionException.class)
    public void unauthorizedUser() {
    	
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID, LITERAL);
         service.execute();
    }
    
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
