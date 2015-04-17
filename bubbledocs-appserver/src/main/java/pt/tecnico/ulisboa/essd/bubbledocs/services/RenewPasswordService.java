package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;


public class RenewPasswordService extends BubbleDocsService {
    private String result;
    private String userToken;
    
    
    public RenewPasswordService(String tokenUser) {  	
    	this.userToken = tokenUser;
    }
 
    @Override
    protected void dispatch() throws BubbleDocsException {
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
		//Verifica se a pessoa aidna esta logada
		if(bd.validSession(userToken)){
			
			

			Utilizador user = bd.getUserFromToken(userToken);
			
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
		
		refreshToken(userToken);
		 
		}
    }
    public String getResult() {
        return result;
    }

}