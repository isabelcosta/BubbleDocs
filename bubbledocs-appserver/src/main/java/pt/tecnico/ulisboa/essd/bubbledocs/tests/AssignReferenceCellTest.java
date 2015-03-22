/*package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.IdFolhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ReferenciaInvalidaException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.AssignReferenceCellService;

// add needed import declarations

public class AssignReferenceCellTest extends BubbleDocsServiceTest {

    // the tokens
    private String root;
    private String ars;

    private static final String USERNAME = "pf";
    private static final String PASSWORD = "sub";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String NOT_REFERENCE = "noReference";
    private static final String REFERENCE = "=5;6";
    private static final String USER_TOKEN = "pf8";
    private static final int FOLHA_ID = 3;
    private static final String CELL_ID = "1;1";
    private static final String SPREADSHEET_NAME = "Notas ES";
    
//    @Override
////    public void populate4Test() {
////		
////    	FolhadeCalculo folha = new FolhadeCalculo();
////		
////    	//-->Referencia para a celula (5, 6) na posicao (1, 1)
////		//String conteudoReferencia = "=5;6";
////		
////    	folha.modificarCelula(1,1, REFERENCE);
////       
////		root = addUserToSession("root");
////        ars = addUserToSession("ars");
////    }

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
        
        
        //-----------------------------------------------------------------------------
        // Verifica se o conteudo da celula e o esperado.
        //------------------------------------------------------------------------------
        
        for(FolhadeCalculo folhaIter : Bubbledocs.getInstance().getFolhasSet()){
        	if(folhaIter.getID() == FOLHA_ID)
        		for(Celula cell : folhaIter.getCelulaSet()){
        			if(cell.equals(CELL_ID))
        				conteudo = cell.getConteudo().toString();	
        		}			
        }
       
        assertEquals("=5;6", conteudo);
        //assertEquals(CELL_ID, user.getName());
    }

//    @Test(expected = LoginInvalidoException.class) 
//    public void loginInvalido() {
//    	
//    	AssignReferenceCellService service = new AssignReferenceCellService("ars4", FOLHA_ID, CELL_ID, REFERENCE);
//        service.execute();
//         
//        //////////////////AINDA NAO SEI O TOKEN//////////////////////////////////////
//    	
//    }

    @Test(expected = IdFolhaInvalidoException.class) 
    public void IdFolhaInvalido() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, 9, CELL_ID, REFERENCE);
        service.execute();
        
    }

//    @Test(expected = UserNaoAutorizadoException.class) // <--- Nao sei se este é preciso!!!!!
//    public void userNaoAutorizado() {
//    	
//    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, REFERENCE);
//        service.execute();
//    	
//    }

    @Test(expected = ArgLinhaInvalidoException.class) 
    public void argLinhaInvalido() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, "4;1", REFERENCE);
        service.execute();
    }

    @Test(expected = ArgColunaInvalidoException.class) 
    public void argColunaInvalido() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, "1;9", REFERENCE);
        service.execute();
    }

    @Test(expected = ReferenciaInvalidaException.class) 
    public void referenciaInvalida() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, "=5;9" );
        service.execute();
    }
    
//    @Test(expected = UserNotInSessionException.class) //////// <--- Nao sei se este é preciso!!!!!
//    public void rootEstaAutorizado() {
//    	
//    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, REFERENCE);
//        service.execute();
//        
//    }
}*/