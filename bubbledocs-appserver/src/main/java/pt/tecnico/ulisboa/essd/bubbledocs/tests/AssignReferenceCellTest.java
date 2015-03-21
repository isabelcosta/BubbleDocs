/*package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.services.AssignReferenceCellService;

// add needed import declarations

public class AssignReferenceCellTest extends BubbleDocsServiceTest {

    // the tokens
    private String root;
    private String ars;

    private static final String USERNAME = "ars";
    private static final String PASSWORD = "ars";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String NOT_REFERENCE = "noReference";
    private static final String REFERENCE = "=5;6";
    private static final String USER_TOKEN = "ars8";
    private static final int FOLHA_ID = 3;
    private static final String CELL_ID = "5;6";
    private static final String SPREADSHEET_NAME = "folha1";
    
    @Override
    public void populate4Test() {
		
    	FolhadeCalculo folha = new FolhadeCalculo();
		
    	//-->Referencia para a celula (5, 6) na posicao (1, 1)
		//String conteudoReferencia = "=5;6";
		
    	folha.modificarCelula(1,1, REFERENCE);
       
		root = addUserToSession("root");
        ars = addUserToSession("ars");
    }

    @Test
    public void success() {
    	
        FolhadeCalculo folha;
        String conteudo;
        
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, REFERENCE);
        service.execute();
        
       //for(Utilizador user : Bubbledocs.getInstance().getUtilizadoresSet()){
    	//  if(user.getBubbledocs().getTokensSet().equals(USER_TOKEN))	   
        //}

        //assertEquals(USER_TOKEN, user.getBubbledocs());
        
        for(FolhadeCalculo folhaIter : Bubbledocs.getInstance().getFolhasSet()){
        	if(folhaIter.getID() == FOLHA_ID)
        		for(Celula cell : folhaIter.getCelulaSet()){
        			if(cell.equals(CELL_ID))
        				conteudo = cell.getConteudo().toString();	
        		}			
        }
       
        assertEquals("=5;6", conteudo);
        assertEquals(CELL_ID, user.getName());
    }

    @Test(expected = DuplicateUsernameException.class) // <--- tenho de arranjar excepcao
    public void argLinhaInvalido() {
    	// TODO ESTE TESTE
    }

    @Test(expected = EmptyUsernameException.class) // <--- tenho de arranjar excepcao
    public void argColunaInvalida() {
    	// TODO ESTE TESTE
    }

    @Test(expected = UnauthorizedOperationException.class) // <--- tenho de arranjar excepcao
    public void userNaoAutorizado() {
    	// TODO ESTE TESTE
    }

    @Test(expected = UserNotInSessionException.class) // <--- tenho de arranjar excepcao
    public void accessUsernameNotExist() {
    	// TODO ESTE TESTE
    }

    @Test(expected = UserNotInSessionException.class) // <--- tenho de arranjar excepcao
    public void isReference() {
    	// TODO ESTE TESTE
    }

    @Test(expected = UserNotInSessionException.class) // <--- tenho de arranjar excepcao
    public void folhaInvalida() {
    	// TODO ESTE TESTE
    }
    
    @Test(expected = UserNotInSessionException.class) // <--- tenho de arranjar excepcao
    public void rootEstaAutorizado() {
    	// TODO ESTE TESTE
    }
}*/