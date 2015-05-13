package pt.tecnico.ulisboa.essd.bubbledocs.integration.component;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.jdom2.output.XMLOutputter;
import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ReferenciaInvalidaException;

public class BubbleDocsServiceTest {
	//protected Bubbledocs _bd = Bubbledocs.getInstance();
	
	
    @Before
    public void setUp() throws Exception {

        try {
            FenixFramework.getTransactionManager().begin(false);
            //unPopulate4Test();
            populate4Test();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
    }

    // should redefine this method in the subclasses if it is needed to specify
    // some initial state
    public void populate4Test() {
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
//    // auxiliary methods that access the domain layer and are needed in the test classes
//    // for defining the initial state and checking that the service has the expected behavior
    public Utilizador createUser(String username,String password, String name  ) {
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	//Cria user
		Utilizador user = new Utilizador(name, username, password);
    	bd.addUtilizadores(user);
    	return user;
    }
//
    public FolhadeCalculo createSpreadSheet(Utilizador user, String name, int row,
            int column) {
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	//cria folha
    	String dono = null;
    	for (Utilizador u : bd.getUtilizadoresSet()){
			if(user.getUsername().equals(u.getUsername()))
				dono=u.getUsername();
		}
    	
    	FolhadeCalculo folha = new FolhadeCalculo(name,dono,row,column);
    	bd.addFolhas(folha);
    	
    	return folha;
    }

    // returns a spreadsheet whose name is equal to name
    public FolhadeCalculo getSpreadSheet(String name) {
		  for (FolhadeCalculo f : Bubbledocs.getInstance().getFolhasSet()){
			   if (f.getNomeFolha().equals(name))
			   		return f;
		  }
		//Expected dont reach this point
    	return null;
    }

    // returns the user registered in the application whose username is equal to username
    public Utilizador getUserFromUsername(String username) {
    	for (Utilizador u : Bubbledocs.getInstance().getUtilizadoresSet()){
    		if (u.getUsername().equals(username))
    			return u;
    	}
    	//Expected dont reach this point
		return null;
    }

    // put a user into session and returns the token associated to it
    public String addUserToSession(String username) {
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	//cria o token
    	Random rand = new Random(); 
        int intToken = rand.nextInt(10);
        String userToken = username + intToken;
        
        //poe em sessao
        Token token = new Token(username, userToken);
        bd.addTokens(token);
        
    	return userToken;
    }

    // remove a user from session given its token
    public void removeUserFromSession(String token) {
	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	for(Token userToken : bd.getTokensSet()){
			if(userToken.getToken().equals(token)){
				bd.removeTokens(userToken);
			}
    	}
    }

    // return the user registered in session whose token is equal to token
    public Utilizador getUserFromSession(String token) {
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	Utilizador user = null;
    	String userStr = null;
    	
    	for(Token t: bd.getTokensSet()){
			if(t.getToken().equals(token)){
				userStr = t.getUsername();
			}
    	}
    	
    	for(Utilizador u: bd.getUtilizadoresSet()){
			if(u.getUsername().equals(userStr)){
				user = u;
			}
    	}
    	
    	return user;
    }
    
    public void turnTokenInvalid(String token){
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	for(Token t: bd.getTokensSet()){
			if(t.getToken().equals(token)){
				t.setTime(t.getTime().minusHours(2));
			}
    	}
    }
    
    public byte[] exportFolhadeCalculo(Integer sheetId, String userToken){
    	byte[] resultTemp = null;
    	Bubbledocs bd = Bubbledocs.getInstance();
    	FolhadeCalculo folha;
    	
		try {

				
//VERIFICAR SE O USER TEM PERMISSÃ•ES PARA EXPORTAR		
			folha = bd.getFolhaOfId(sheetId);
			
			String userNameOfToken = bd.getUsernameOfToken(userToken);
	    	if(folha.podeLer(userNameOfToken) || folha.podeEscrever(userNameOfToken)){
	    		
//CONVERTER A FOLHA EM BYTES
				org.jdom2.Document sheetDoc = bd.exportSheet(folha);
				
				XMLOutputter xmlOutput = new XMLOutputter();
				xmlOutput.setFormat(org.jdom2.output.Format.getPrettyFormat());
				String docString = xmlOutput.outputString(sheetDoc);
				
				
				try {
					resultTemp = docString.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					System.out.println("export falhou: " + e);
				}
//ADICIONAR AO USER ID DA FOLHA QUE EXPORTOU
				bd.addFolhaExportadas(sheetId);
				
	    	}
		} catch (ReferenciaInvalidaException | OutOfBoundsException e) {
			System.err.println("Couldn't export Sheet: " + e);
		}
		return resultTemp;
    }
    
    public void removeSpreadsheet(Integer sheetId) {
    	Bubbledocs bd = Bubbledocs.getInstance();
    	FolhadeCalculo folha = bd.getFolhaOfId(sheetId);
    	bd.removeFolhas(folha);
	}
}
