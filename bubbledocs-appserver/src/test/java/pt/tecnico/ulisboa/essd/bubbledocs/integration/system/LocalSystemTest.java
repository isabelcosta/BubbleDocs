package pt.tecnico.ulisboa.essd.bubbledocs.integration.system;


import static org.junit.Assert.assertEquals;

import javax.transaction.SystemException;

import mockit.Mocked;
import mockit.StrictExpectations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.dtos.UserDto;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.CreateSpreadSheetIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.ExportDocumentIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.ImportDocumentIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.LoginUserIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignLiteralCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignRangeFunctionToCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignReferenceCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.CreateUserService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetUserInfoService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

public class LocalSystemTest {

    private static final String ROOT_USERNAME = "root";
    private static final String ROOT_PASSWORD = "rootroot";	
//    private static final String FINALUSER_USERNAME = "finalUser";
//    private static final String FINALUSER_PASSWORD = "fu#";
//    private static final String FINALUSER_EMAIL = "finalUser@email.com";	
	
    @Mocked
    IDRemoteServices remote; 
	
    @Before
    public void setUp(){
    	unPopulate4Test();
    }
    
    @After
    public void tearDown() {
            unPopulate4Test();
    }
    
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
     * Procedure:
     * 	Logar a root
     * 	A root cria dois users: johnny e tiana
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
    public void finalLocalSystemTest() {
    
		
		System.out.println("Welcome to Bubble application!!");
		
		//FAZ LOGIN DA ROOT
    	new StrictExpectations() {
 		   
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(ROOT_USERNAME, ROOT_PASSWORD);
		    }
		};
    	
		System.out.println("---------------------------------------------------------------------------------");
		
		System.out.println("Johnny e Tiana fizeram login...");
    	LoginUserIntegrator serviceLogin = new LoginUserIntegrator(ROOT_USERNAME, ROOT_PASSWORD);
    	serviceLogin.execute();
    	
    	//ROOT CRIA UM NOVO UTILIZADOR        
    	new StrictExpectations() {
    		{
    			remote = new IDRemoteServices();
    			remote.createUser("john231", "johnBravo@email.com");
    			remote = new IDRemoteServices();
    			remote.createUser("tiana123", "tianaDenver@email.com");
    		}
    	};
    	

        CreateUserService serviceCreateUser1 = new CreateUserService("root", "john231", "johnBravo@email.com",
                "Johnny Bravo");
        serviceCreateUser1.execute();

        CreateUserService serviceCreateUser2 = new CreateUserService("root", "tiana123", "tianaDenver@email.com",
                "Tiana Denver");
        serviceCreateUser2.execute();
        System.out.println("A Root criou os users Johnny e Tiana...");
        
	    GetUserInfoService userInfo1 = new GetUserInfoService("john231");
	    userInfo1.execute();
	    UserDto user1 = userInfo1.getResult();
	    GetUserInfoService userInfo2 = new GetUserInfoService("tiana123");
	    userInfo2.execute();
	    UserDto user2 = userInfo2.getResult();
	    
	    System.out.println("---------------------------------------------------------------------------------");
	    System.out.println("Lista de Utilizadores:");
	    System.out.println("-> Nome: " + user1.getName() + "	Email:" + user1.getEmail() + ";" );
	    System.out.println("-> Nome: " + user2.getName() + "	Email:" + user2.getEmail() + ";" );
	    
    	new StrictExpectations() {
  		   
    		{// A PASSWORD E ALEATORIA???
    			remote = new IDRemoteServices();
    			remote.loginUser("john231", "????");
		    }
		};
    	
    	//OS UTILIZADORES LOGAM NA BUBBLEDOCS
    	LoginUserIntegrator loginService1 = new LoginUserIntegrator("john231", "????");
    	loginService1.execute();

    	LoginUserIntegrator loginService2 = new LoginUserIntegrator("tiana123", "????");
    	loginService2.execute();
    	
        String userToken1 = loginService1.getUserToken();
        String userToken2 = loginService2.getUserToken();      
        System.out.println("Johnny e Tiana fizeram login...");
        
        
        //OS UTILIZADORES CRIAM AS SUAS FOLHAS
        CreateSpreadSheetIntegrator createSpreadsheetService1 = new CreateSpreadSheetIntegrator(userToken1, "johnnys_folha", 50, 60);
        createSpreadsheetService1.execute();

        CreateSpreadSheetIntegrator createSpreadsheetService2 = new CreateSpreadSheetIntegrator(userToken2, "tianas_folha", 456, 2345);
        createSpreadsheetService2.execute();
        //createSpreadsheetService.getPassword();
        
        System.out.println("Johnny e Tiana criaram as suas folhas...");        
        
        
        //O UTILIZADOR1 PREENCHE A FOLHA COM FUNCOES REFERENCIAS E LITERAIS
        
		AssignRangeFunctionToCellService serviceRange1 = new AssignRangeFunctionToCellService(userToken1, FOLHA_ID, "45;6", "=AVG(3;2:5;6)";
		serviceRange1.execute();
		AssignLiteralCellService serviceLiteral1 = new AssignLiteralCellService( userToken1, DOC_ID, "3;2", "78");
		serviceLiteral1.execute();
		AssignLiteralCellService serviceLiteral2 = new AssignLiteralCellService( userToken1, DOC_ID, "3;34", "3564");
		serviceLiteral2.execute();
    	AssignReferenceCellService serviceReference1 = new AssignReferenceCellService( userToken1, FOLHA_ID, "13;5", "=3;2");
    	serviceReference1.execute();
        
//        AssignLiteralCellIntegrator
//        AssignReferenceCellIntegrator
//        AssignBinaryFunctionToCellIntegrator
//        AssignRangeFunctionToCellIntegrator
//        ExportDocumentIntegrator
//        ImportDocumentIntegrator
        
        //O UTILIZADOR1 DA PERMISSOES AO UTILIZADOR2 PARA MEXER NA FOLHA
        
        //O UTILIZADOR1 E REMOVIDO PELA ROOT (NAO TENHO A CERTEZA)
        
        //O UTILIZADOR2 EXPORTA A FOLHA1
		ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(FOLHA_ID, USER_TOKEN);
		exportDocument.execute();
		
        //O UTLIZADOR1 IMPORTA A FOLHA1
		ImportDocumentIntegrator importDocument = new ImportDocumentIntegrator(FOLHA1_ID, USER_TOKEN);
		importDocument.execute();
        
		//IDEM IDEM
    }
	 
    
	
}
