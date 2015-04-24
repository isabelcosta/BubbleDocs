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
    	
		//Verifica se a pessoa ainda esta logada
		
//		IDRemoteServices remote = new IDRemoteServices();

//		try {
			// invoke some method on remote
//			remote.renewPassword(_bd.getUsernameOfToken(_userToken));
			
		
    	
    		// invalidar a password
    		_oldPassword = _bd.getUserPassword(_userToken);
			_bd.invalidateUserPassword(_userToken);
			
			
			
//		} catch (RemoteInvocationException rie) {
//			throw new UnavailableServiceException();
//		}
		
//n		refreshToken(_userToken);
		 
    }

	public String getOldPassword() {
		return _oldPassword;
	}

}