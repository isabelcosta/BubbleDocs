package pt.tecnico.ulisboa.essd.bubbledocs.integration.component;

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
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignRangeFunctionToCellService;


public class AssignRangeFunctionToCellTest extends BubbleDocsServiceTest {

	// FUNCAO INTERVALO
    private static final String FUNCAO_INTERVALO_COM_INTERVALO_VALIDO = "=AVG(3;6:3;9)";
    private static final String FUNCAO_INTERVALO_COM_INTERVALO_INVALIDO = "=PRD(100000000;1:2;3)";
    private static final String FUNCAO_INTERVALO_INCOMPLETA = "=PRD(3;6:)";
    private static final String FUNCAO_INTERVALO_COM_INTERVALO_CELULA = "=AVG(3;6:3;6)";
    private static final String FUNCAO_INTERVALO_INEXISTENTE = "=ABB(3;3:3;5)"; 
    private static final String FUNCAO_INTERVALO_BINARIA = "=MUL(3;3:3;5)"; 
    
    // CELULAS
    private static final String CELL_ID = "4;1";
    private static final String CELL_ID_PREENCHIDA = "3;6";
    private static final String CELL_ID_VAZIA = "1;1";
    private static final String CELL_ID_PROTEGIDA = "4;8";
    
    private static int FOLHA_ID;
    private static int FOLHA_ID_SEM_PERMISSAO;    
    private static String USER_TOKEN;
    private static String USER_TOKEN_PODE_ESCREVER;    
    
    @Override
    public void populate4Test() {
    	
    	
    	unPopulate4Test();
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	//Cria utilizadores
    	Utilizador user1 = createUser("mas", "marias", "Maria Santos");
    	Utilizador user2 = createUser("jos", "joaos", "Joao Santos");
    
    	    
    	//Inicia sessao 
    	USER_TOKEN = addUserToSession("mas");
    	USER_TOKEN_PODE_ESCREVER = addUserToSession("jos");; 	
    	
    	
    	//Cria duas folhas
    	FolhadeCalculo folha1 = createSpreadSheet(user1, "msFolha", 50, 20 );
    	FolhadeCalculo folha2 = createSpreadSheet(user2, "jsFolha", 40, 20 );
    	
    	
    	//Adiciona conteudo a folha da ms
    	FOLHA_ID = folha1.getID();
    			
    	String conteudoLiteral = "2";
    	folha1.modificarCelula(3, 6, conteudoLiteral);
    	
    	String conteudoLiteral1 = "4";
    	folha1.modificarCelula(3, 7, conteudoLiteral1);
    	
    	String conteudoLiteral2 = "10";
    	folha1.modificarCelula(3, 8, conteudoLiteral2);
    	
    	String conteudoLiteral3 = "4";
    	folha1.modificarCelula(3, 9, conteudoLiteral3);
    	
    	String conteudoAdd = "=ADD(2,3;2)";
    	folha1.modificarCelula(4,1,conteudoAdd);
    			
    	folha1.protegeCelula(4, 8, true);
		
    	
    	//Adiciona conteudo a folha do js
    	FOLHA_ID_SEM_PERMISSAO = folha2.getID();
    			
		String conteudoLit = "3";
		folha2.modificarCelula(2,7,conteudoLit); 
  			
    	
		bd.darPermissoes("escrita", "mas", "jos", FOLHA_ID);
		
		bd.darPermissoes("leitura", "jos", "mas", FOLHA_ID_SEM_PERMISSAO);
    	
    }

    
    @Test
    public void successAssignToFullCell() {
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService(USER_TOKEN_PODE_ESCREVER, FOLHA_ID, CELL_ID_PREENCHIDA, FUNCAO_INTERVALO_COM_INTERVALO_VALIDO);
        service.execute();
    	
        assertEquals(FUNCAO_INTERVALO_COM_INTERVALO_VALIDO, service.getResult());
    }
    
    
    @Test
    public void successCelulaComoIntervalo() {
    	
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService(USER_TOKEN, FOLHA_ID, CELL_ID_PREENCHIDA, FUNCAO_INTERVALO_COM_INTERVALO_CELULA);
        service.execute();
    	
        assertEquals(FUNCAO_INTERVALO_COM_INTERVALO_CELULA, service.getResult());
    }
    
    @Test
    public void successAssignToEmptyCell() {
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService(USER_TOKEN_PODE_ESCREVER, FOLHA_ID, CELL_ID_VAZIA, FUNCAO_INTERVALO_COM_INTERVALO_VALIDO);
        service.execute();
    	
        assertEquals(FUNCAO_INTERVALO_COM_INTERVALO_VALIDO, service.getResult());
    }   
    
    @Test
    public void successAssignOneIntToEmptyCell() {
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService(USER_TOKEN_PODE_ESCREVER, FOLHA_ID, CELL_ID_VAZIA, FUNCAO_INTERVALO_COM_INTERVALO_CELULA);
        service.execute();
    	
        assertEquals(FUNCAO_INTERVALO_COM_INTERVALO_CELULA, service.getResult());
    }  
    
    @Test
    public void successPodeEscrever() {
    	AssignBinaryFunctionToCellService service = new AssignBinaryFunctionToCellService(USER_TOKEN_PODE_ESCREVER, FOLHA_ID, CELL_ID, FUNCAO_INTERVALO_COM_INTERVALO_VALIDO);
        service.execute();

		assertEquals(FUNCAO_INTERVALO_COM_INTERVALO_VALIDO, service.getResult());
    }
    
    
    @Test(expected = OutOfBoundsException.class)
    public void lineOutOfBoundOfSpreadSheet() {
    	
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService(USER_TOKEN, FOLHA_ID, "10000;10", FUNCAO_INTERVALO_COM_INTERVALO_VALIDO);
    	 service.execute();
    }
    
    @Test(expected = OutOfBoundsException.class)
    public void lineNegative() {
    	
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService(USER_TOKEN, FOLHA_ID, "-9;2", FUNCAO_INTERVALO_COM_INTERVALO_VALIDO);
    	 service.execute();
    }

    @Test(expected = OutOfBoundsException.class)
    public void columnOutOfBoundOfSpreadSheet() {
    	
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService( USER_TOKEN, FOLHA_ID, "4;3457", FUNCAO_INTERVALO_COM_INTERVALO_VALIDO);
         service.execute();
    }
    
    @Test(expected = OutOfBoundsException.class)
    public void columnNegative() {
    	
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService( USER_TOKEN, FOLHA_ID, "4;-0", FUNCAO_INTERVALO_COM_INTERVALO_VALIDO);
         service.execute();
    }

    
    @Test(expected = ProtectedCellException.class)
    public void unauthorizedUserForProtectedCell() {
    	
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService(USER_TOKEN, FOLHA_ID, CELL_ID_PROTEGIDA, FUNCAO_INTERVALO_COM_INTERVALO_VALIDO);
         service.execute();
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void unauthorizedUserForWriting() {
    	
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService( USER_TOKEN, FOLHA_ID_SEM_PERMISSAO, CELL_ID, FUNCAO_INTERVALO_COM_INTERVALO_VALIDO);
         service.execute();
    }
    
    @Test(expected = UnauthorizedOperationException.class)
    public void userCanOnlyRead() {
    	
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService( USER_TOKEN, FOLHA_ID_SEM_PERMISSAO, CELL_ID, FUNCAO_INTERVALO_COM_INTERVALO_VALIDO);
         service.execute();
    } 
    
    
    @Test(expected = UserNotInSessionException.class)
    public void userNotLogged() {
    	
    	 removeUserFromSession(USER_TOKEN);
    	 AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService(USER_TOKEN, FOLHA_ID, CELL_ID, FUNCAO_INTERVALO_COM_INTERVALO_VALIDO);
         service.execute();
    }

    
    @Test(expected = SpreadSheetDoesNotExistException.class)
    public void spreadSheetDoesNotExists() {

    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService(USER_TOKEN, 70002, CELL_ID, FUNCAO_INTERVALO_COM_INTERVALO_VALIDO);
    	service.execute();    	
    }
    

    @Test(expected = InvalidFunctionException.class)
    public void rangeFunctionWithInvalidArgs() {
    	
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService( USER_TOKEN, FOLHA_ID, CELL_ID, FUNCAO_INTERVALO_COM_INTERVALO_INVALIDO);
        service.execute();
    }
    
    @Test(expected = InvalidFunctionException.class)
    public void rangeFunctionUndefined() {
    	
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService( USER_TOKEN, FOLHA_ID, CELL_ID, FUNCAO_INTERVALO_INEXISTENTE);
        service.execute();
    }
    
    @Test(expected = InvalidFunctionException.class)
    public void rangeFunctionIncomplete() {
    	
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService( USER_TOKEN, FOLHA_ID, CELL_ID, FUNCAO_INTERVALO_INCOMPLETA);
        service.execute();
    }
    
    @Test(expected = InvalidFunctionException.class)
    public void notRangeFunction() {
    	
    	AssignRangeFunctionToCellService service = new AssignRangeFunctionToCellService( USER_TOKEN, FOLHA_ID, CELL_ID, FUNCAO_INTERVALO_BINARIA);
        service.execute();
    }
}