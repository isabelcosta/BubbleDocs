package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.RenewPasswordService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

public class RenewPasswordIntegrator extends BubbleDocsIntegrator{
	
	private String _userToken;
	private RenewPasswordService _local;
	
	public RenewPasswordIntegrator(String userToken) {
		_userToken = userToken;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		/*
		 *  Instância Remota
		 * 
		 */
		
		IDRemoteServices remote = new IDRemoteServices();
		Bubbledocs bd = getBubbleDocs();
		
		try {
			remote.renewPassword(bd.getUsernameOfToken(_userToken));
			
			} catch (RemoteInvocationException rie) {
				throw new UnavailableServiceException();
		
			} catch (LoginBubbleDocsException ex) {
				throw new LoginBubbleDocsException();
			}
		
		
		/*
		 *  Instância local
		 *  
		 * 
		 */

		_local = new RenewPasswordService(_userToken);
		_local.execute();
		
		
		
			}
	
	
}
