package pt.tecnico.ulisboa.essd.bubbledocs.services;

import java.util.Random;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.NotLiteralException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
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
			
			refreshToken(userToken);

			Utilizador user = bd.getUserFromToken(userToken);
			
			IDRemoteServices remote = new IDRemoteServices();

			try {
				// invoke some method on remote
				remote.renewPassword(user.getUsername());
				
				// invalidar a password
				user.setPassword(null);
				
			} catch (RemoteInvocationException rie) {
				throw new UnavailableServiceException();
			}
			
		} else {
			throw new UserNotInSessionException(userToken);
		}
    }

    public String getResult() {
        return result;
    }

}