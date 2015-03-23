package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;

// add needed import declarations

public class BubbleDocsServiceTest {

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
    }
//    // auxiliary methods that access the domain layer and are needed in the test classes
//    // for defining the initial state and checking that the service has the expected behavior
    public Utilizador createUser(String username, String password, String name) {
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	//Cria user
		Utilizador user = new Utilizador(username, password, name);
    	bd.addUtilizadores(user);
    		
    	return user;
    }
//
    public FolhadeCalculo createSpreadSheet(Utilizador user, String name, int row,
            int column) {
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	//cria folha
    	FolhadeCalculo folha = new FolhadeCalculo();
    	
    	String dono = null;
    	for (Utilizador u : bd.getUtilizadoresSet()){
			if(user.getNome().equals(u.getNome()))
				dono=u.getNome();
		}
    	
    	folha.setDono(dono);              
		folha.setLinhas(row);
		folha.setColunas(column);
		folha.setNomeFolha(name);
    	
    	return folha;
    }
//
//    // returns a spreadsheet whose name is equal to name
    public FolhadeCalculo getSpreadSheet(String name) {
		  for (FolhadeCalculo f : Bubbledocs.getInstance().getFolhasSet()){
			   if (f.getNomeFolha().equals(name))
			   		return f;
		  }
		//Expected dont reach this point
    	return null;
    }
//
//    // returns the user registered in the application whose username is equal to username
    public Utilizador getUserFromUsername(String username) {
    	for (Utilizador u : Bubbledocs.getInstance().getUtilizadoresSet()){
    		if (u.getUsername().equals(username))
    			return u;
    	}
    	//Expected dont reach this point
		return null;
 }
//    }
//
//    // put a user into session and returns the token associated to it
    public String addUserToSession(String username) {
//	// add code here
    	return null;
    }

    // remove a user from session given its token
    void removeUserFromSession(String token) {
	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	for(Token userToken : bd.getTokensSet()){
			if(userToken.getToken().equals(token)){
				bd.removeTokens(userToken);
			}
    	}
    }
//
//    // return the user registered in session whose token is equal to token
//    Utilizador getUserFromSession(String token) {
//	// add code here
//    }

}
