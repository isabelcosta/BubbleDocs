package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ProtectedCellException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.NotLiteralException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.AssignLiteralCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.CreateSpreadSheet;

// add needed import declarations

public class AssignLiteralCellTest extends BubbleDocsServiceTest {

    private static final String LITERAL = "94";
    private static final String CELL_ID = "3;2";
    private static final String CELL_ID_VAZIA = "1;1";
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
		Utilizador user1 = createUser("te", "te#", "Teresa Palhoto");
    	Utilizador user2 = createUser("mi", "mi#", "Miguel Torrado");	
    	    
    	//Faz o login dos users
	
    	USER_TOKEN = addUserToSession("te");
    	USER_TOKEN_PODE_ESCREVER = addUserToSession("mi");  	
    	
    	//cria duas folhas
    	FolhadeCalculo folha1 = createSpreadSheet(user1, "teFolha", 20, 30);
		FolhadeCalculo folha2 = createSpreadSheet(user2, "miFolha", 40, 11);
		
		//Preenche a folha (folha1) do user "ab"
		DOC_ID = folha1.getID();
		String conteudoLiteral = "4";
		folha1.modificarCelula(3, 2, conteudoLiteral);
		
		String conteudoAdd = "=ADD(2,3;2)";
		folha1.modificarCelula(5,7,conteudoAdd);
		
		//Protege celula 4;8
		folha1.protegeCelula(4, 8, true);

		//Preenche a folha (folha2) do user "pi"
		DOC_ID_SEM_PERMISSAO = folha2.getID();
		String conteudoRef = "=3;2";
    	folha2.modificarCelula(2,7,conteudoRef);   			
    	
    	//da "ab" da permissoes de escrita a "pi" para preencher a sua folha
    	bd.darPermissoes("escrita", "te", "mi", DOC_ID);
    	
    }

    @Test
    public void success() {
    	
        AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID, LITERAL);
        service.execute();
    	
        assertEquals(LITERAL, service.getResult());
    }
    
    @Test
    public void successAssignToEmptyCell() {
        AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN_PODE_ESCREVER, DOC_ID, CELL_ID_VAZIA, LITERAL);
        service.execute();
    	
        assertEquals(LITERAL, service.getResult());
    }   
    
    @Test
    public void successPodeEscrever() {
        AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN_PODE_ESCREVER, DOC_ID, CELL_ID, LITERAL);
        service.execute();

		assertEquals(LITERAL, service.getResult());
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
    public void ColumnNegative() {
    	
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, "4;-0", LITERAL);
         service.execute();
    }
     
    @Test(expected = ProtectedCellException.class)
    public void unauthorizedUserForProtectedCell() {
    	
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID_PROTEGIDA, LITERAL);
         service.execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void unauthorizedUserForWriting() {
    	
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID_SEM_PERMISSAO, CELL_ID, LITERAL);
         service.execute();
    }
    
    @Test(expected = UserNotInSessionException.class)
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

//    @Test(expected = ProtectedCellException.class)
//    public void ImportFuncionaProtegida() {
//    	org.jdom2.Document jdomDoc = new org.jdom2.Document();
//    	Bubbledocs bd = Bubbledocs.getInstance();
//    	for(FolhadeCalculo f : bd.getFolhasSet()){
//    		if(f.getID() == DOC_ID){
//    			jdomDoc.setRootElement(f.exportToXML());
//    		}
//    	}
//    	bd.eliminaFolha(DOC_ID);
//    	recoverFromBackup(jdomDoc,bd);
//    	
//    	AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, DOC_ID, CELL_ID_PROTEGIDA, LITERAL);
//    	service.execute();
//    	
//    }
//    
//    
//    private static void recoverFromBackup(org.jdom2.Document jdomDoc, Bubbledocs bd) {
//    	String donoFolha = jdomDoc.getRootElement().getAttributeValue("dono");
//    	String nomeFolha = jdomDoc.getRootElement().getAttributeValue("nome");
//    	int linhas = Integer.parseInt(jdomDoc.getRootElement().getAttributeValue("linhas"));
//    	int colunas = Integer.parseInt(jdomDoc.getRootElement().getAttributeValue("colunas"));
//    	int id = Integer.parseInt(jdomDoc.getRootElement().getAttributeValue("id"));
//    	String data = jdomDoc.getRootElement().getAttributeValue("data");
//    	
//    	
//    	for(FolhadeCalculo folha : bd.getFolhasSet())
//	    	if(folha.getNomeFolha().equals(nomeFolha)) {
//	    		folha.setDataCriacao(new LocalDate(data));
//    			folha.setID(id);
//	    		folha.importFromXML(jdomDoc.getRootElement());
//	    		return;
//	    	}
//    	
//    	//procura o token do pf
//    	String donoFolhaToken = null;
//        for(Token token : bd.getTokensSet()){
//        	if(token.getUsername().equals(donoFolha)){
//        		donoFolhaToken = token.getToken();
//        	}	
//        }
//    	
//    	
//    	//caso nao tenha encontrado a folha cria uma nova
// 		CreateSpreadSheet serviceFolha = new CreateSpreadSheet(donoFolhaToken, nomeFolha, linhas, colunas);
// 		serviceFolha.execute();
//    	
//    	for (FolhadeCalculo folha : bd.getFolhasSet())
//    		if(folha.getNomeFolha().equals(nomeFolha)){
//    			
//    			folha.setDataCriacao(new LocalDate(data));
//    			folha.setID(id);
//	    		folha.importFromXML(jdomDoc.getRootElement());
//    		}
//    	
//    }
}
