package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;


public class RenewPasswordService extends ValidSessionsService {
    
    
    public RenewPasswordService(String tokenUser) {  	
    	_userToken = tokenUser;
    }
 
    @Override
    protected void dispatch() throws BubbleDocsException {
    	
    	super.dispatch();
		//Verifica se a pessoa aidna esta logada
		
		IDRemoteServices remote = new IDRemoteServices();

		try {
			// invoke some method on remote
			remote.renewPassword(_bd.getUsernameOfToken(_userToken));
			
			// invalidar a password
			_bd.userSetPasswordNull(_userToken);
			
		} catch (RemoteInvocationException rie) {
			throw new UnavailableServiceException();
		}
		
//		refreshToken(_userToken);
		 
    }

}