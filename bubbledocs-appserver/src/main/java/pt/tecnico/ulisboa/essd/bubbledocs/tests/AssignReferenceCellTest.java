package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ArgColunaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ArgLinhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DontHavePermissionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.IdFolhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ReferenciaInvalidaException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.AssignLiteralCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.AssignReferenceCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.BubbleDocsService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.LoginUser;

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
    private static final String REFERENCE = "=4;2";
    private static final String SPREADSHEET_NAME = "Notas ES";
    private static int FOLHA_ID;
    private static int FOLHA_ID_SEM_PERMISSAO;
    private static final String CELL_ID = "5;7";
    private static final String CELL_ID_PROTEGIDA = "4;8";
    private static String USER_TOKEN;
    private static String USER_TOKEN_PODE_ESCREVER;
    
    @Override
    public void populate4Test() {

    	unPopulate4Test();
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	//Cria utilizadores
    	Utilizador user1 = new Utilizador("Maria Santos", "ms", "marias");
    	bd.addUtilizadores(user1);
    	
    	Utilizador user2 = new Utilizador("Joao Santos", "js", "joaos");
    	bd.addUtilizadores(user2);	
    	
    	//Inicia sessao para o utilizador ms
    	LoginUser login1 = new LoginUser("ms", "marias");
    	login1.execute();
    	Token tk1 = new Token("ms", login1.getResult());
    	Bubbledocs.getInstance().addTokens(tk1);
    	
    	USER_TOKEN = tk1.getToken();
    	
    	//Inicia sessao para o utilizador js
    	LoginUser login2 = new LoginUser("js", "joaos");
    	login2.execute();
    	Token tk2 = new Token("js", login2.getResult());
    	Bubbledocs.getInstance().addTokens(tk2);
    	
    	USER_TOKEN_PODE_ESCREVER = tk2.getToken();   
    	
    	//cria duas folhas
		bd.criaFolha("msFolha", "ms", 50, 20);
		bd.criaFolha("jsFolha", "js", 40, 20);
		
		//preenche a folha
    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
    		if(folhaIter.getNomeFolha().equals("msFolha")){
    			FOLHA_ID = folhaIter.getID();
    			String conteudoReferencia = "7";
    			folhaIter.modificarCelula(4, 2, conteudoReferencia);
    			
    			String conteudoAdd = "=ADD(2,3;2)";
    			folhaIter.modificarCelula(5,7,conteudoAdd);
    			
    			folhaIter.protegeCelula(4, 8, true);

    		} else if (folhaIter.getNomeFolha().equals("jsFolha")){
    			FOLHA_ID_SEM_PERMISSAO = folhaIter.getID();
    			String conteudoRef = "3";
    			folhaIter.modificarCelula(2,7,conteudoRef);   			
    		}
    	}
    	
    	// "ms" da permissoes de escrita a "js" para preencher a sua folha
    	bd.darPermissoes("escrita", "ms", "js", FOLHA_ID);
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
        

        assertEquals(REFERENCE, conteudo);

    }
    
    @Test
    public void successPodeEscrever() {
    	AssignReferenceCellService service = new AssignReferenceCellService( USER_TOKEN_PODE_ESCREVER, FOLHA_ID, CELL_ID, REFERENCE);
        service.execute();

    	FolhadeCalculo folhaTest = null;
        String conteudo = null;
		
    	for( FolhadeCalculo folhaIter : BubbleDocsService.getBubbleDocs().getFolhasSet() ){
			if(folhaIter.getID() == FOLHA_ID){
				folhaTest = folhaIter;
			}
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
    	
        assertEquals(REFERENCE, conteudo);
    }

    @Test(expected = DontHavePermissionException.class) 
    public void loginInvalido() {
    	
    	removeUserFromSession(USER_TOKEN);
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, REFERENCE);
        service.execute();
         
    	
    }
    
    @Test(expected = DontHavePermissionException.class)
    public void unauthorizedUserForWriting() {
    	
      	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID_SEM_PERMISSAO, CELL_ID, REFERENCE);
        service.execute();
    }
    
    @Test(expected = DontHavePermissionException.class)
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

    
    @Test(expected = OutOfBoundsException.class) 
    public void NotReference() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, "9" );
        service.execute();
    }
    
    
    @Test(expected = OutOfBoundsException.class) 
    public void ReferenciaInvalida() {
    	
    	AssignReferenceCellService service = new AssignReferenceCellService(USER_TOKEN, FOLHA_ID, CELL_ID, "=29999;33" );
        service.execute();
    }
    
}