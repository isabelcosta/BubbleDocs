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
		 * 
		 *  Instância Remota
		 * 
		 */
		
		IDRemoteServices remote = new IDRemoteServices();
		Bubbledocs bd = getBubbleDocs();
		
		try {
			// invoke some method on remote
			remote.renewPassword(bd.getUsernameOfToken(_userToken));
			
			/*
			 * 
			 * tratar excepcoes
			 *		. caso receba RemoteInvocation Exception
			 *				. restaura a password antiga
			 * 				. lanca UnavailableBubbleDocsException
			 * 		. caso receba LoginBubbleDocsException
			 * 				. restaura a password antiga
			 *		
			 */
			
			} catch (RemoteInvocationException rie) {
				throw new UnavailableServiceException();
		
			} catch (LoginBubbleDocsException ex) {
				throw new LoginBubbleDocsException();
			}
		
		
		/*
		 *  Instância local
		 * 
		 */

//ter atencao as excepcoes
		
		_local = new RenewPasswordService(_userToken);
		_local.execute();
		
		
		
			}
	
	
}
