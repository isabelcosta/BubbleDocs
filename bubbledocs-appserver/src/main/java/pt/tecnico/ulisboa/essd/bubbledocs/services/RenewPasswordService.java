package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;


public class RenewPasswordService extends ValidSessionsService {
    private String result;
    
    
    public RenewPasswordService(String tokenUser) {  	
    	_userToken = tokenUser;
    }
 
    @Override
    protected void dispatch() throws BubbleDocsException {
    	
    	super.dispatch();
		//Verifica se a pessoa aidna esta logada
		Utilizador user = _bd.getUserFromToken(_userToken);
		
		IDRemoteServices remote = new IDRemoteServices();

		try {
			// invoke some method on remote
			remote.renewPassword(user.getUsername());
			
			// invalidar a password
			user.setPassword(null);
			result = user.getPassword();
			
		} catch (RemoteInvocationException rie) {
			throw new UnavailableServiceException();
		}
		
//		refreshToken(_userToken);
		 
    }
    public String getResult() {
        return result;
    }

}