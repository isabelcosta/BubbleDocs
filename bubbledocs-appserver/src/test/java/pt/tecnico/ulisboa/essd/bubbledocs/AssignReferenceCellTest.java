package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ProtectedCellException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ReferenciaInvalidaException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignReferenceCellService;



public class AssignReferenceCellTest extends BubbleDocsServiceTest {


    private static final String REFERENCE = "=5;7";
    private static int FOLHA_ID;
    private static int FOLHA_ID_SEM_PERMISSAO;
    private static final String CELL_ID = "4;2";
    private static final String CELL_ID_PROTEGIDA = "4;8";
    private static String USER_TOKEN;
    private static String USER_TOKEN_PODE_ESCREVER;
    private static final String CELL_ID_VAZIA = "10;10";
    
    @Override
    public void populate4Test() {

    	unPopulate4Test();
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	
    	//Cria utilizadores
    	Utilizador user1 = createUser("mas", "marias", "Maria Santos");

    	Utilizador user2 = createUser("jos", "joaos", "Joao Santos");
    
    	
    	//Inicia sessao para o utilizador ms
    	USER_TOKEN = addUserToSession("mas");

    	
    	//Inicia sessao para o utilizador js
    	USER_TOKEN_PODE_ESCREVER = addUserToSession("jos");; 

    	
    	//cria duas folhas
    	
    	FolhadeCalculo folha1 = createSpreadSheet(user1, "msFolha", 50, 20 );
    	FolhadeCalculo folha2 = createSpreadSheet(user2, "jsFolha", 40, 20 );
    	
    			
    	//adiciona conteudo a folha da ms
    	FOLHA_ID = folha1.getID();
    			
    	String conteudoReferencia = "7";
    	folha1.modificarCelula(4, 2, conteudoReferencia);
    			
    	String conteudoAdd = "=ADD(2,3;2)";
    	folha1.modificarCelula(5,7,conteudoAdd);
    			
    	folha1.protegeCelula(4, 8, true);

    	
    	//adiciona conteudo a folha do js
    	FOLHA_ID_SEM_PERMISSAO = folha2.getID();
    			
		String conteudoRef = "3";
		folha2.modificarCelula(2,7,conteudoRef);   			

    	

    	bd.darPermissoes("escrita", "mas", "jos", FOLHA_ID);
    }

    @Test
    public void success() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, REFERENCE);
        service.execute();

        assertEquals(REFERENCE, service.getResult());
        
    }
    
    
    @Test
    public void successPodeEscrever() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService( USER_TOKEN_PODE_ESCREVER, FOLHA_ID, CELL_ID, REFERENCE);
        service.execute();

        assertEquals(REFERENCE, service.getResult());

    }

    @Test
    public void successAssignToEmptyCell() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService( USER_TOKEN_PODE_ESCREVER, FOLHA_ID, CELL_ID_VAZIA, REFERENCE);
        service.execute();

        assertEquals(REFERENCE, service.getResult());

    }
    
    
    @Test(expected = UserNotInSessionException.class) 
    public void loginInvalido() {
    	
    	removeUserFromSession(USER_TOKEN);
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, REFERENCE);
        service.execute();
         
    	
    }
    
    @Test(expected = UnauthorizedOperationException.class)
    public void unauthorizedUserForWriting() {
    	
      	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID_SEM_PERMISSAO, CELL_ID, REFERENCE);
        service.execute();
    }
    
    @Test(expected = ProtectedCellException.class)
    public void unauthorizedUserForProtectedCell() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService( USER_TOKEN, FOLHA_ID, CELL_ID_PROTEGIDA, REFERENCE);
         service.execute();
    }
    


    @Test(expected = SpreadSheetDoesNotExistException.class) 
    public void IdFolhaInvalido() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, 93423, CELL_ID, REFERENCE);
        service.execute();
        
    }


    @Test(expected = OutOfBoundsException.class) 
    public void argLinhaInvalido() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, "40000;1", REFERENCE);
        service.execute();
    }
    
    
    @Test(expected = OutOfBoundsException.class)
    public void LineNegative() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService( USER_TOKEN, FOLHA_ID, "-9;1", REFERENCE);
    	 service.execute();
    }
    

    @Test(expected = OutOfBoundsException.class) 
    public void argColunaInvalido() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, "1;900000", REFERENCE);
        service.execute();
    }
    
    
    @Test(expected = OutOfBoundsException.class)
    public void ColumnNegativeSpreadSheet() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService( USER_TOKEN, FOLHA_ID, "1;-1", REFERENCE);
         service.execute();
    }

    
    @Test(expected = ReferenciaInvalidaException.class) 
    public void NotReference() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, "9" );
        service.execute();
    }
    
    
    @Test(expected = ReferenciaInvalidaException.class) 
    public void ReferenciaInvalida() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, "=29999;33" );
        service.execute();
    }
    
}
