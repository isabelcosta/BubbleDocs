package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;


public class RenewPasswordService extends ValidSessionsService {
    
    private String _oldPassword;
    
    public RenewPasswordService(String tokenUser) {  	
    	super(tokenUser);
    }
 
    @Override
    protected void dispatch_session() throws BubbleDocsException {
    	
			_bd.invalidateUserPassword(_userToken);
		 
    }

}