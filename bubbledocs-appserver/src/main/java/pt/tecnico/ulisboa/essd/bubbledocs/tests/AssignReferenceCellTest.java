package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ArgColunaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ArgLinhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.IdFolhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ReferenciaInvalidaException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.AssignLiteralCellService;
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
    
    @Override
    public void populate4Test() {
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
    	for(FolhadeCalculo folhacalc : Bubbledocs.getInstance().getFolhasSet()){

    		System.out.println("ID DA FOLHAS:      " + folhacalc.getID());
    	}
    }

    @Test
    public void success() {
    	
    	FolhadeCalculo folhaTest = null;
        String conteudo = null;
        
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, REFERENCE);
        service.execute();

        
        //-----------------------------------------------------------------------------
        // Verifica se o conteudo da celula e o esperado.
        //------------------------------------------------------------------------------
        
        for(FolhadeCalculo folhaIter : Bubbledocs.getInstance().getFolhasSet()){
			if(folhaIter.getID() == FOLHA_ID)
        		folhaTest = folhaIter;
        }
        	
        
    	int[] linhaEColuna = null;        
		try {
			linhaEColuna = Parser.parseEndereco(CELL_ID, folhaTest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        for(Celula cell : folhaTest.getCelulaSet()){
        	if(cell.getLinha() == linhaEColuna[0] && cell.getColuna() == linhaEColuna[1])
        		conteudo = cell.getConteudo().toString();	
        }
        

        assertEquals("=5;6", conteudo);

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
    
//  @Test(expected = UserNaoAutorizadoException.class) // 
//  public void userNaoAutorizado() {
//  	
//  	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, REFERENCE);
//      service.execute();
//  	
//  }
//
    @Test(expected = OutOfBoundsException.class) 
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
    	
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, FOLHA_ID, "-9;1", REFERENCE);
    	 service.execute();
    }
    

    @Test(expected = OutOfBoundsException.class) 
    public void argColunaInvalido() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, "1;900000", REFERENCE);
        service.execute();
    }
    
    
    @Test(expected = OutOfBoundsException.class)
    public void ColumnNegativeSpreadSheet() {
    	
    	 AssignLiteralCellService service = new AssignLiteralCellService( USER_TOKEN, FOLHA_ID, "1;-1", REFERENCE);
         service.execute();
    }

    
    @Test(expected = OutOfBoundsException.class) 
    public void NotRefernce() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, "9" );
        service.execute();
    }
    
    @Test(expected = OutOfBoundsException.class) 
    public void ReferenciaInvalida() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, "=29999;33" );
        service.execute();
    }
    
//    @Test(expected = UserNotInSessionException.class) 
//    public void rootEstaAutorizado() {
//    	
//    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, REFERENCE);
//        service.execute();
//        
//    }
}