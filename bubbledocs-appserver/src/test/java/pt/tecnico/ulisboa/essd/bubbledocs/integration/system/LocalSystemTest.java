package pt.tecnico.ulisboa.essd.bubbledocs.integration.system;


import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import javax.transaction.SystemException;

import mockit.Mocked;
import mockit.StrictExpectations;

import org.jdom2.output.XMLOutputter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.dtos.UserDto;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.AssignLiteralCellIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.AssignRangeFunctionToCellIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.AssignReferenceCellIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.CreateSpreadSheetIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.CreateUserIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.ExportDocumentIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.ImportDocumentIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.LoginUserIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignLiteralCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignRangeFunctionToCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignReferenceCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.CreateUserService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetSpreadSheetContentService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetUserInfoService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.StoreRemoteServices;

public class LocalSystemTest {

    private static final String ROOT_USERNAME = "root";
    private static final String ROOT_PASSWORD = "rootroot";
    
    private static String USER_JOAO = "joao";
    private static String NOME_JOAO = "Joao Bravo";
    private static String MAIL_JOAO = "johnBravo@email.com";
    private static String PASS_JOAO = "ola123";
    private static String FOLHA_JOAO = "joaoFolha";
    private static Integer FOLHA_JOAO_COLUNAS = 10;
    private static Integer FOLHA_JOAO_LINHAS = 20;
    private static Integer FOLHA_JOAO_ID = 0; // inicializacao
    private static String TOKEN_JOAO = null; // inicializacao
    
    private static String USER_TIANA = "tiana";
    private static String NOME_TIANA = "Tiana Denver";
    private static String MAIL_TIANA = "tianaDenver@email.com";
    private static String PASS_TIANA = "comidadecao";
    private static String FOLHA_TIANA = "tianaFolha";
    private static Integer FOLHA_TIANA_COLUNAS = 25;
    private static Integer FOLHA_TIANA_LINHAS = 15;
    private static Integer FOLHA_TIANA_ID = 0; // inicializacao
    private static String TOKEN_TIANA = null; // inicializacao
    
//    private static final String FINALUSER_USERNAME = "finalUser";
//    private static final String FINALUSER_PASSWORD = "fu#";
//    private static final String FINALUSER_EMAIL = "finalUser@email.com";	
	
    @Mocked
    IDRemoteServices remoteID; 
	
    @Mocked
    StoreRemoteServices remoteStore; 

    @Before
    public void setUp(){
//    	unPopulate4Test();
    }
    
    @After
    public void tearDown() {
        unPopulate4Test();
    }
    
    @Atomic
    public void unPopulate4Test(){
    	Bubbledocs bd = Bubbledocs.getInstance();
		for (FolhadeCalculo folha : bd.getFolhasSet()){
			bd.removeFolhas(folha);
		}			
		for (Utilizador user : bd.getUtilizadoresSet()){
			bd.removeUtilizadores(user);
		}
		for (Token token : bd.getTokensSet()){
			bd.removeTokens(token);
		}
    }
    
   

/*
 * 	--------------------------------------------------------
 * | FUNCOES AUXILIARES										|
 * 	--------------------------------------------------------
 *
 */

    // Mostra o conteudo da folha
    public String showFancySpreadSheet(Integer sheetId, String userToken){
    	
		Bubbledocs bd = Bubbledocs.getInstance();
		
		// Folha de calculo associada ao ID
		FolhadeCalculo folha;
		folha = bd.getFolhaOfId(sheetId);
		Integer maxLinha = folha.getLinhas();
		Integer maxColuna = folha.getColunas();
		GetSpreadSheetContentService service = new GetSpreadSheetContentService(userToken, sheetId);
		String[][] matriz = service.getResult();
		String top = "";
		String linha = "";
		for (int u = 1; u < maxColuna; u++){
			top += "| " + u;
		}
		System.out.println(top + " |");
		for(int i=1; i < maxLinha; i++){
			for(int k=1; k < maxColuna; k++){
				linha += matriz[i][k] + " | ";
			}
			System.out.println("| " + i + " | " + linha);
		}
		return "/n";
    }
    
    // transforma uma folha em bytes para ser usado no mock de uma folha importada
	public byte[] folhaToByte4Mock(Integer sheetId, String userToken) {
		
		Bubbledocs bd = Bubbledocs.getInstance();
		
		// Folha de calculo associada ao ID
		FolhadeCalculo folha;
		folha = bd.getFolhaOfId(sheetId);
		
		// userName associado ao Token
		String userNameOfToken = bd.getUsernameOfToken(userToken);
    	
		// caso possa escrever ou ler passa á exportacao do doc
		if(folha.podeLer(userNameOfToken) || folha.podeEscrever(userNameOfToken)){
    		
    		// transformar a folha no jdomDoc
			org.jdom2.Document sheetDoc = bd.exportSheet(folha);
			
			// criar com a ajuda do XMLOutputter uma string apartir do jdomDoc
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(org.jdom2.output.Format.getPrettyFormat());
			String docString = xmlOutput.outputString(sheetDoc);
			
			// por fim transforma-la em bytes
			try {
				return docString.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				System.out.println("export falhou: " + e);
			}
    	}
		return null;
		
	}
	
	 public void removeSpreadsheet(Integer sheetId) {
	    	Bubbledocs bd = Bubbledocs.getInstance();
	    	FolhadeCalculo folha = bd.getFolhaOfId(sheetId);
	    	bd.removeFolhas(folha);
	}
	 
/*
 * 	--------------------------------------------
 * | TESTES										|
 * 	--------------------------------------------
 */
	
	 /*
     * Procedure:
     * 	Logar a root
     * 	A root cria dois users: joao e tiana
     * 	Os dois users logam
     * 	Os dois users criam as suas folhas
     * 	Cada user preenche as suas folhas
     * 	Vizualiza-se as folhas
     * 	Exporta-se as folhas
     * 	Apagam-se as folhas
     * 	Importam-se as folhas
     * 	Vizualizam-se as folhas
     */
	
	
	@Test
    public void finalLocalSystemIT() {
    
		
		System.out.println("Welcome to Bubble application!!");
		
    /*
     *  --------------------------------------------
     * | LOGIN DA ROOT								|
     *  --------------------------------------------
     * 
     * 
     */
	
    	new StrictExpectations() {
 		   
    		{
    			remoteID = new IDRemoteServices();
    			remoteID.loginUser(ROOT_USERNAME, ROOT_PASSWORD);
    			remoteID = new IDRemoteServices();
    			remoteID.createUser(USER_JOAO, MAIL_JOAO);
    			remoteID = new IDRemoteServices();
    			remoteID.createUser(USER_TIANA, MAIL_TIANA);
    			remoteID = new IDRemoteServices();
    			remoteID.loginUser(USER_JOAO, PASS_JOAO);
    			remoteID = new IDRemoteServices();
    			remoteID.loginUser(USER_TIANA, PASS_TIANA);
    			remoteStore = new StoreRemoteServices();
    			remoteStore.storeDocument(USER_JOAO, FOLHA_JOAO, (byte[]) any);
    			remoteStore = new StoreRemoteServices();
    			remoteStore.loadDocument(USER_JOAO, FOLHA_JOAO);
//    			result = folhaByte;
    		
		    }
		};
    	
		System.out.println("---------------------------------------------------------------------------------");
		
		
    	LoginUserIntegrator serviceLogin = new LoginUserIntegrator(ROOT_USERNAME, ROOT_PASSWORD);
    	serviceLogin.execute();
    	

    /*
     *  --------------------------------------------
     * | CRIACAO DE USERS							|
     *  --------------------------------------------
     * 		
     * 		- TIANA
     * 		- JOAO
     * 
     */
		
        CreateUserIntegrator serviceCreateUser1 = new CreateUserIntegrator(serviceLogin.getUserToken(), USER_JOAO, MAIL_JOAO,
                NOME_JOAO);
        serviceCreateUser1.execute();        
        
        CreateUserIntegrator serviceCreateUser2 = new CreateUserIntegrator(serviceLogin.getUserToken(), USER_TIANA, MAIL_TIANA,
                NOME_TIANA);
        serviceCreateUser2.execute();
        System.out.println("A Root criou os users Joao e Tiana...");
        
        
	    GetUserInfoService userInfo1 = new GetUserInfoService(USER_JOAO);
	    userInfo1.execute();
	    UserDto user1 = userInfo1.getResult();
	    GetUserInfoService userInfo2 = new GetUserInfoService(USER_TIANA);
	    userInfo2.execute();
	    UserDto user2 = userInfo2.getResult();
	    
	    System.out.println("---------------------------------------------------------------------------------");
	    System.out.println("Lista de Utilizadores:");
	    System.out.println("-> Nome: " + user1.getName() + "	Email:" + user1.getEmail() + ";" );
	    System.out.println("-> Nome: " + user2.getName() + "	Email:" + user2.getEmail() + ";" );
	    
    /*
     *  --------------------------------------------
     * | LOGIN DOS USERS							|
     *  --------------------------------------------
     *  
     * 		- TIANA
     * 		- JOAO
     * 
     */
	    

    	LoginUserIntegrator loginService1 = new LoginUserIntegrator(USER_JOAO, PASS_JOAO);
    	loginService1.execute();

    	TOKEN_JOAO =  loginService1.getUserToken();
    	
	
    	LoginUserIntegrator loginService2 = new LoginUserIntegrator(USER_TIANA, PASS_TIANA);
    	loginService2.execute();
    	
    	TOKEN_TIANA =  loginService2.getUserToken();
    	
    	
        System.out.println("Joao e Tiana fizeram login...");
        
    /*
     *  --------------------------------------------
     * | USERS CRIAM FOLHAS							|
     *  --------------------------------------------
     *  
     * 		- FOLHA_TIANA
     * 		- FOLHA_JOAO
     * 
     */
        
        CreateSpreadSheetIntegrator createSpreadsheetService1 = new CreateSpreadSheetIntegrator(TOKEN_JOAO, FOLHA_JOAO , FOLHA_JOAO_LINHAS, FOLHA_JOAO_COLUNAS);
        createSpreadsheetService1.execute();
        
        FOLHA_JOAO_ID = createSpreadsheetService1.getResult();
        
        
        CreateSpreadSheetIntegrator createSpreadsheetService2 = new CreateSpreadSheetIntegrator(TOKEN_TIANA, FOLHA_TIANA , FOLHA_TIANA_LINHAS, FOLHA_TIANA_COLUNAS);
        createSpreadsheetService2.execute();
        
        FOLHA_TIANA_ID = createSpreadsheetService2.getResult();
        
        
        System.out.println("Joao e Tiana criaram as suas folhas...");        
        
    
    /*
     * 
     *  --------------------------------------------
     * | JOAO PREENCHE FOLHA						|
     *  --------------------------------------------
     *  
     * 		- Literal 78 na celula 3:2
     * 		- Literal 3564 na celula 3,4
     * 		- AVG das celulas 3,2 a 5,6 na celula 5,7
     * 		- Referencia para a celula 3,2 na celula 13,5
     * 
     */

		AssignLiteralCellIntegrator serviceLiteralJoao1 = new AssignLiteralCellIntegrator( TOKEN_JOAO, FOLHA_JOAO_ID, "3;2", "78");
		serviceLiteralJoao1.execute();
		AssignLiteralCellIntegrator serviceLiteralJoao2 = new AssignLiteralCellIntegrator( TOKEN_JOAO, FOLHA_JOAO_ID, "3;4", "3564");
		serviceLiteralJoao2.execute();
		AssignRangeFunctionToCellIntegrator serviceRangeJoao1 = new AssignRangeFunctionToCellIntegrator(TOKEN_JOAO, FOLHA_JOAO_ID, "5;7", "=AVG(3;4:3;4)");
		serviceRangeJoao1.execute();
    	AssignReferenceCellIntegrator serviceReferenceJoao1 = new AssignReferenceCellIntegrator( TOKEN_JOAO, FOLHA_JOAO_ID, "13;5", "=3;2");
    	serviceReferenceJoao1.execute();

        System.out.println("Joao preenche a sua folha....");
        
    	showFancySpreadSheet(FOLHA_JOAO_ID, TOKEN_JOAO);
    	
    /*
     * 
     *  --------------------------------------------
     * | TIANA PREENCHE FOLHA						|
     *  --------------------------------------------
     *  
     * 		- Literal 78 na celula 3:2
     * 		- Literal 3564 na celula 3,4
     * 		- AVG das celulas 3,2 a 5,6 na celula 5,7
     * 		- Referencia para a celula 3,2 na celula 13,5
     * 
     */
        
		AssignLiteralCellIntegrator serviceLiteralTiana1 = new AssignLiteralCellIntegrator( TOKEN_TIANA, FOLHA_TIANA_ID, "3;2", "78");
		serviceLiteralTiana1.execute();
		AssignLiteralCellIntegrator serviceLiteralTiana2 = new AssignLiteralCellIntegrator( TOKEN_TIANA, FOLHA_TIANA_ID, "3;4", "3564");
		serviceLiteralTiana2.execute();
		AssignRangeFunctionToCellIntegrator serviceRangeTiana1 = new AssignRangeFunctionToCellIntegrator(TOKEN_TIANA, FOLHA_TIANA_ID, "5;6", "=AVG(3;2:5;6)");
		serviceRangeTiana1.execute();
    	AssignReferenceCellIntegrator serviceReferenceTiana1 = new AssignReferenceCellIntegrator( TOKEN_TIANA, FOLHA_TIANA_ID, "13;5", "=3;2");
    	serviceReferenceTiana1.execute();
    	
        System.out.println("Tiana preenche a sua folha....");
        
        //O UTILIZADOR1 DA PERMISSOES AO UTILIZADOR2 PARA MEXER NA FOLHA
        
        //O UTILIZADOR1 E REMOVIDO PELA ROOT (NAO TENHO A CERTEZA)
        
    	/*
    	 *  --------------------------------------------
    	 * | JOAO EXPORTA A SUA FOLHA						|
    	 *  --------------------------------------------
    	 *  
    	 * 		- Literal 78 na celula 3:2
    	 * 
    	 */


		ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(FOLHA_JOAO_ID, TOKEN_JOAO);
		exportDocument.execute();
		
		byte[] folhaByte;
		folhaByte = folhaToByte4Mock(FOLHA_JOAO_ID, TOKEN_JOAO); //guardar a folha em bytes para usar no mock do import
		removeSpreadsheet(FOLHA_JOAO_ID); 						//remover a folha para importar sem estar na bd
		
		
        //O UTLIZADOR2 IMPORTA A FOLHA1
		ImportDocumentIntegrator importDocument = new ImportDocumentIntegrator(FOLHA_JOAO_ID, TOKEN_JOAO);
		importDocument.execute();
        
		//IDEM IDEM
    }
	 
    
	
}
