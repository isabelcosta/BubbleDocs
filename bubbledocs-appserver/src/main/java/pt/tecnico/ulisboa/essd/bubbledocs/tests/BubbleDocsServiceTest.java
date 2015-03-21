package pt.tecnico.ulisboa.essd.bubbledocs.tests;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;

// add needed import declarations

public class BubbleDocsServiceTest {

    @Before
    public void setUp() throws Exception {

        try {
            FenixFramework.getTransactionManager().begin(false);
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

//    // auxiliary methods that access the domain layer and are needed in the test classes
//    // for defining the initial state and checking that the service has the expected behavior
//    Utilizador createUser(String username, String password, String name) {
//	// add code here
//    }
//
//    public FolhadeCalculo createSpreadSheet(Utilizador user, String name, int row,
//            int column) {
//	// add code here
//    }
//
//    // returns a spreadsheet whose name is equal to name
//    public FolhadeCalculo getSpreadSheet(String name) {
//	// add code here
//    }
//
//    // returns the user registered in the application whose username is equal to username
//    Utilizador getUserFromUsername(String username) {
//	// add code here
//    }
//
//    // put a user into session and returns the token associated to it
//    String addUserToSession(String username) {
//	// add code here
//    }
//
//    // remove a user from session given its token
//    void removeUserFromSession(String token) {
//	// add code here
//    }
//
//    // return the user registered in session whose token is equal to token
//    Utilizador getUserFromSession(String token) {
//	// add code here
//    }

}
