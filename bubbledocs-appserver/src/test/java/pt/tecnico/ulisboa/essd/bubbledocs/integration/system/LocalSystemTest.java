package pt.tecnico.ulisboa.essd.bubbledocs.integration.system;


import static org.junit.Assert.assertEquals;

import javax.transaction.SystemException;

import mockit.Mocked;
import mockit.StrictExpectations;

import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.dtos.UserDto;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.LoginUserIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.CreateUserService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetUserInfoService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

public class LocalSystemTest {

    private static final String ROOT_USERNAME = "root";
    private static final String ROOT_PASSWORD = "rootroot";	
    private static final String FINALUSER_USERNAME = "finalUser";
    private static final String FUNALUSER_PASSWORD = "fu#";
    private static final String FUNALUSER_EMAIL = "finalUser@email.com";	
	
    @Mocked
    IDRemoteServices remote; 
	
    public void setUp(){
    	unPopulate4Test();
    }
    
    public void tearDown() {
        try {
            
        	//FenixFramework.getTransactionManager().rollback();
            unPopulate4Test();
        } catch (IllegalStateException | SecurityException e) {
            e.printStackTrace();
        }
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
    
    //testar tudo
    //TESTAR TUDOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
	@Test
    public void success() {
    
		
		
		//FAZ LOGIN DA ROOT
    	new StrictExpectations() {
 		   
    		{
    			remote = new IDRemoteServices();
    			remote.loginUser(ROOT_USERNAME, ROOT_PASSWORD);
		    }
		};
    	
    	
    	LoginUserIntegrator serviceLogin = new LoginUserIntegrator(ROOT_USERNAME, ROOT_PASSWORD);
    	serviceLogin.execute();
    	
    	//ROOT CRIA UM NOVO UTILIZADOR        
    	new StrictExpectations() {
    		{
    			remote = new IDRemoteServices();
    			remote.createUser(FINALUSER_USERNAME, FUNALUSER_EMAIL);
    		}
    	};
    	

        CreateUserService serviceCreateUser = new CreateUserService("root", FINALUSER_USERNAME, FUNALUSER_EMAIL,
                "José Ferreira");
        serviceCreateUser.execute();

        
    	new StrictExpectations() {
  		   
    		{// A PASSWORD E ALEATORIA???
    			remote = new IDRemoteServices();
    			remote.loginUser(FINALUSER_USERNAME, FUNALUSER_PASSWORD);
		    }
		};
    	
    	
    	LoginUserIntegrator service = new LoginUserIntegrator(FINALUSER_USERNAME, FUNALUSER_PASSWORD);
        service.execute();
        
        //O NOVO UTILIZADOR CRIA UMA FOLHA
        
        //A ROOT CRIA OUTRO UTILIZADOR
        
        //O UTILIZADOR2 CRIA OUTRA FOLHA
        
        //O UTILIZADOR1 PREENCHE A FOLHA COM FUNCOES REFERENCIAS E LITERAIS
        
        //O UTILIZADOR1 DA PERMISSOES AO UTILIZADOR2 PARA MEXER NA FOLHA
        
        //O UTILIZADOR1 E REMOVIDO PELA ROOT (NAO TENHO A CERTEZA)
        
        //O UTILIZADOR2 EXPORTA A FOLHA1
        
        //O UTLIZADOR1 IMPORTA A FOLHA1
        
        //IDEM IDEM
    }
	 
    
	
}
