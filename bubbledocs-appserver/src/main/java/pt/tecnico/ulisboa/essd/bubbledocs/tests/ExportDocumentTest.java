package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.IdFolhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.ExportDocumentService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.LoginUser;

public class ExportDocumentTest extends BubbleDocsServiceTest{
	
	// the tokens
    private String root;
    private String ars;

    private static final String USERNAME = "pf";
    private static final String PASSWORD = "sub";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String NOT_REFERENCE = "noReference";
    private static final String REFERENCE = "=5;7";
    private static final String SPREADSHEET_NAME = "Notas ES";
    private static int FOLHA_ID;
    private static int FOLHA_ID_SEM_PERMISSAO;
    private static final String CELL_ID = "4;2";
    private static String USER_TOKEN;
    private static String USER_TOKEN_PODE_ESCREVER;
    private static String USER_TOKEN_NO_ACCESS;
    private static String USER_TOKEN_NOT_IN_SESSION;
    private static String EMPTY_TOKEN;
    private static int FOLHA_ID_INEXISTENT = 100;
    private static int FOLHA_ID_NEGATIVE = -100;
	
	public void populate4Test() {

		//Limpa a base de dados
    	unPopulate4Test();
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	//Cria users
		Utilizador user1 = createUser("te", "te#", "Teresa Palhoto");
    	Utilizador user2 = createUser("mi", "mi#", "Miguel Torrado");
    	Utilizador user3 = createUser("re", "re#", "Isabel Costa");
    	    
    	//Faz o login dos users
    	EMPTY_TOKEN = "";
    	USER_TOKEN_NOT_IN_SESSION = addUserToSession("re");
    	turnTokenInvalid(USER_TOKEN_NOT_IN_SESSION);
    	USER_TOKEN = addUserToSession("te");
    	USER_TOKEN_NO_ACCESS = addUserToSession("mi");
    	
    	//cria duas folhas
    	FolhadeCalculo folha1 = createSpreadSheet(user1, "teFolha", 20, 30);
		FolhadeCalculo folha2 = createSpreadSheet(user2, "miFolha", 40, 11);
		
		//Preenche a folha (folha1) do user "ab"
		FOLHA_ID = folha1.getID();
		String conteudoLiteral = "4";
		folha1.modificarCelula(3, 2, conteudoLiteral);
		
		String conteudoAdd = "=ADD(2,3;2)";
		folha1.modificarCelula(5,7,conteudoAdd);
		
		//Protege celula 4;8
		folha1.protegeCelula(4, 8, true);

		//Preenche a folha (folha2) do user "pi"
		FOLHA_ID_SEM_PERMISSAO = folha2.getID();
		String conteudoRef = "=3;2";
    	folha2.modificarCelula(2,7,conteudoRef);   			
    	
    	//da "ab" da permissoes de escrita a "pi" para preencher a sua folha
    	//bd.darPermissoes("escrita", "te", "mi", FOLHA_ID);
    	
    }
	/*
	@Test(expected = UnauthorizedOperationException.class)
	public void unauthorizedExport() {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, USER_TOKEN_NO_ACCESS);
		exportDocument.execute();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void emptyTokenExport () {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, EMPTY_TOKEN);
		exportDocument.execute();
	}
	 */
	@Test(expected = UserNotInSessionException.class)
	public void invalidSessionExport () {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, USER_TOKEN_NOT_IN_SESSION);
		exportDocument.execute();
	}
	/*
	
	@Test(expected = SpreadSheetDoesNotExistException.class)
	public void idDoesNotExistExport() {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID_INEXISTENT, USER_TOKEN);
		exportDocument.execute();
	}
	@Test(expected = IdFolhaInvalidoException.class)
	public void invalidIdExport() {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID_NEGATIVE, USER_TOKEN);
		exportDocument.execute();
	}
	
	@Test
	public void successExport () {
		// add code here 
		// 		- exportar a folha e usar o XPATH para verificar se exportou bem
	}
	
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
