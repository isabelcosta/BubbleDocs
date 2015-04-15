package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ProtectedCellException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.NotLiteralException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.RenewPasswordService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.CreateSpreadSheet;

// add needed import declarations

public class RenewPasswordTest extends BubbleDocsServiceTest {

	private static final String USER_TOKEN = "bla bla bla";
	private static final String USER_TOKEN_NOT_LOGGED = "bla bla bla";

    @Test
    public void success() {
    	
        RenewPasswordService service = new RenewPasswordService( USER_TOKEN);
        service.execute();
    }
   
    
    @Test(expected = UserNotInSessionException.class)
    public void userNotLogged() {
    	
    	 removeUserFromSession(USER_TOKEN);
    	 RenewPasswordService service = new RenewPasswordService( USER_TOKEN);
         service.execute();
    }    
 
    @Test(expected = UserNotInSessionException.class)
    public void userTokenInvalid() {
    	
    	 removeUserFromSession(USER_TOKEN);
    	 RenewPasswordService service = new RenewPasswordService( USER_TOKEN_NOT_LOGGED);
         service.execute();
    }     
    
}
