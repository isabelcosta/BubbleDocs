/*package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
    private static final int DOC_ID = 3;
    private static final String CELL_ID = "5;6";
    private static final String SPREADSHEET_NAME = "folha1";
    
    @Override
    public void populate4Test() {
		
    	//-->Referencia para a celula (5, 6) na posicao (1, 1)
		String conteudoReferencia = "=5;6";
		//folhaIter.modificarCelula(7,7, conteudoReferencia);
       
		root = addUserToSession("root");
        ars = addUserToSession("ars");
    }

    @Test
    public void success() {
        AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, DOC_ID, CELL_ID, REFERENCE);
        service.execute();

	// User is the domain class that represents a User
        User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

        assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
        assertEquals("jose", user.getPassword());
        assertEquals("José Ferreira", user.getName());
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