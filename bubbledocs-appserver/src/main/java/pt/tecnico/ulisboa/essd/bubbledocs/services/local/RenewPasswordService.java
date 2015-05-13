package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class RenewPasswordService extends ValidSessionsService {
    
    public RenewPasswordService(String tokenUser) {  	
    	super(tokenUser);
    }
 
    @Override
    protected void dispatch_session() throws BubbleDocsException {
    	Bubbledocs _bd = getBubbleDocs();
			_bd.invalidateUserPassword(_userToken);
		 
    }

}