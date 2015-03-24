/*
package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
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
    	Token tk1 = new Token("ms", login1.getUserToken());
    	Bubbledocs.getInstance().addTokens(tk1);
    	
    	USER_TOKEN = tk1.getToken();
    	
    	//Inicia sessao para o utilizador js
    	LoginUser login2 = new LoginUser("js", "joaos");
    	login2.execute();
    	Token tk2 = new Token("js", login2.getUserToken());
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
	public void successExport () {
		// add code here 
		// 		- exportar a folha e usar o XPATH para verificar se exportou bem
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
*/