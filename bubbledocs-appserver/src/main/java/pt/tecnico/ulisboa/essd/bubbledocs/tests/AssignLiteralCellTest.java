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
    private static final String CELL_ID_PROTEGIDA = "4;8";
    private static int DOC_ID;
    private static int DOC_ID_SEM_PERMISSAO;    
    private static String USER_TOKEN;
    private static String USER_TOKEN_PODE_ESCREVER;    
    
    @Override
    public void populate4Test() {
    	
    	//Limpa a base de dados
    	unPopulate4Test();
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	//Cria users
		Utilizador user1 = new Utilizador("abelha maya", "ab", "maya");
    	bd.addUtilizadores(user1);
    	
    	Utilizador user2 = new Utilizador("pipi meias altas", "pi", "altas");
    	bd.addUtilizadores(user2);			
    	    
    	//Faz o login dos users
		LoginUser login1 = new LoginUser("ab", "maya");
    	login1.execute();
    	Token tk1 = new Token("ab", login1.getResult());
    	Bubbledocs.getInstance().addTokens(tk1);
	
    	USER_TOKEN = tk1.getToken();

		LoginUser login2 = new LoginUser("pi", "altas");
    	login2.execute();
    	Token tk2 = new Token("pi", login2.getResult());
    	Bubbledocs.getInstance().addTokens(tk2);
    	
    	USER_TOKEN_PODE_ESCREVER = tk2.getToken();   	
    	
    	//cria duas folhas
		bd.criaFolha("abFolha", "ab", 20, 30);
		bd.criaFolha("piFolha", "pi", 40, 11);

		//preenche a folha
    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
    		if(folhaIter.getNomeFolha().equals("abFolha")){
    			DOC_ID = folhaIter.getID();
    			String conteudoLiteral = "4";
    			folhaIter.modificarCelula(3, 2, conteudoLiteral);
    			
    			String conteudoAdd = "=ADD(2,3;2)";
    			folhaIter.modificarCelula(5,7,conteudoAdd);
    			
    			folhaIter.protegeCelula(4, 8, true);

    		} else if (folhaIter.getNomeFolha().equals("piFolha")){
    			DOC_ID_SEM_PERMISSAO = folhaIter.getID();
    			String conteudoRef = "=3;2";
    			folhaIter.modificarCelula(2,7,conteudoRef);   			
    		}
    	}
    	
    	//da "ab" da permissoes de escrita a "pi" para preencher a sua folha
    	bd.darPermissoes("escrita", "ab", "pi", DOC_ID);
    	
    }

    @Test
    public void success() {
    	
        AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID, LITERAL);
        service.execute();

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
    
    @Test
    public void successPodeEscrever() {
        AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN_PODE_ESCREVER, DOC_ID, CELL_ID, LITERAL);
        service.execute();

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
    public void unauthorizedUserForProtectedCell() {
    	
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID_PROTEGIDA, LITERAL);
         service.execute();
    }

    @Test(expected = DontHavePermissionException.class)
    public void unauthorizedUserForWriting() {
    	
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID_SEM_PERMISSAO, CELL_ID, LITERAL);
         service.execute();
    }
    
    @Test(expected = DontHavePermissionException.class) // <--- tenho de arranjar excepcao
    public void userNotLogged() {
    	
    	 removeUserFromSession(USER_TOKEN);
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID, LITERAL);
         service.execute();
    }    
    
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
