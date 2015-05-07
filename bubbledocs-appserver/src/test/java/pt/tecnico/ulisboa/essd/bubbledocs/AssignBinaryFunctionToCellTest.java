package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidFunctionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ProtectedCellException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignBinaryFunctionToCellService;


public class AssignBinaryFunctionToCellTest extends BubbleDocsServiceTest {

	// FUNCAO BINARIA
    private static final String FUNCAO_BINARIA_COM_LITERAIS_VALIDOS = "=MUL(3,4)";
    private static final String FUNCAO_BINARIA_COM_OPERANDOS_INVALIDOS = "=ADD( " + ",4)";
    private static final String FUNCAO_BINARIA_COM_REFERENCIA_VALIDAS = "=ADD(3;3,2)";    
    private static final String FUNCAO_BINARIA_INEXISTENTE = "=FILADELFIA(3;3,2)"; 
    
    // CELULAS
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
		Utilizador user1 = createUser("tep", "te#", "Teresa Palhoto");
    	Utilizador user2 = createUser("mit", "mi#", "Miguel Torrado");	
    	    
    	//Faz o login dos users
	
    	USER_TOKEN = addUserToSession("tep");
    	USER_TOKEN_PODE_ESCREVER = addUserToSession("mit");  	
    	
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
    	bd.darPermissoes("escrita", "tep", "mit", DOC_ID);
    	
    }

    /*
     * Testes de sucesso
     */
    @Test
    public void success() {
    	
    	AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService( USER_TOKEN, DOC_ID, CELL_ID, FUNCAO_BINARIA_COM_LITERAIS_VALIDOS);
        service.execute();
    	
        assertEquals(FUNCAO_BINARIA_COM_LITERAIS_VALIDOS, service.getResult());
    }
    
    @Test
    public void successAssignToEmptyCell() {
    	AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService( USER_TOKEN_PODE_ESCREVER, DOC_ID, CELL_ID_VAZIA, FUNCAO_BINARIA_COM_LITERAIS_VALIDOS);
        service.execute();
    	
        assertEquals(FUNCAO_BINARIA_COM_LITERAIS_VALIDOS, service.getResult());
    }   
    
    @Test
    public void successPodeEscrever() {
    	AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService( USER_TOKEN_PODE_ESCREVER, DOC_ID, CELL_ID, FUNCAO_BINARIA_COM_LITERAIS_VALIDOS);
        service.execute();

		assertEquals(FUNCAO_BINARIA_COM_LITERAIS_VALIDOS, service.getResult());
    }
    
    /*
     * Testes falhados limites da folha
     */
    @Test(expected = OutOfBoundsException.class)
    public void LineOutOfBoundOfSpreadSheet() {
    	
    	AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService( USER_TOKEN, DOC_ID, "7000;2", FUNCAO_BINARIA_COM_LITERAIS_VALIDOS);
    	 service.execute();
    }
    
    @Test(expected = OutOfBoundsException.class)
    public void LineNegative() {
    	
    	AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService( USER_TOKEN, DOC_ID, "-2;2", FUNCAO_BINARIA_COM_LITERAIS_VALIDOS);
    	 service.execute();
    }

    @Test(expected = OutOfBoundsException.class)
    public void ColumnOutOfBoundOfSpreadSheet() {
    	
    	AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService( USER_TOKEN, DOC_ID, "4;3457", FUNCAO_BINARIA_COM_LITERAIS_VALIDOS);
         service.execute();
    }
    
    @Test(expected = OutOfBoundsException.class)
    public void ColumnNegative() {
    	
    	AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService( USER_TOKEN, DOC_ID, "4;-0", FUNCAO_BINARIA_COM_LITERAIS_VALIDOS);
         service.execute();
    }
     
    /*
     * Testes de falha nas permissoes e no login
     */
    
    @Test(expected = ProtectedCellException.class)
    public void unauthorizedUserForProtectedCell() {
    	
    	AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService( USER_TOKEN, DOC_ID, CELL_ID_PROTEGIDA, FUNCAO_BINARIA_COM_LITERAIS_VALIDOS);
         service.execute();
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void unauthorizedUserForWriting() {
    	
    	AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService( USER_TOKEN, DOC_ID_SEM_PERMISSAO, CELL_ID, FUNCAO_BINARIA_COM_LITERAIS_VALIDOS);
         service.execute();
    }
    
    @Test(expected = UserNotInSessionException.class)
    public void userNotLogged() {
    	
    	 removeUserFromSession(USER_TOKEN);
    	 AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService( USER_TOKEN, DOC_ID, CELL_ID, FUNCAO_BINARIA_COM_LITERAIS_VALIDOS);
         service.execute();
    }
    /*
     * Teste de folha inexistente
     */
    @Test(expected = SpreadSheetDoesNotExistException.class)
    public void SpreadSheetDoesNotExists() {

    	AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService( USER_TOKEN, 34534, CELL_ID, FUNCAO_BINARIA_COM_LITERAIS_VALIDOS);
    	service.execute();    	
    }
    
    /*
     * Testes do argumento invalido
     */
    @Test(expected = InvalidFunctionException.class)
    public void binaryFunctionWithInvalidArgs() {
    	
    	AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService( USER_TOKEN, DOC_ID, CELL_ID, FUNCAO_BINARIA_COM_OPERANDOS_INVALIDOS);
        service.execute();
    }
    
    @Test(expected = InvalidFunctionException.class)
    public void binaryFunctionUndefined() {
    	
    	AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService( USER_TOKEN, DOC_ID, CELL_ID, FUNCAO_BINARIA_INEXISTENTE);
        service.execute();
    }
    
}
