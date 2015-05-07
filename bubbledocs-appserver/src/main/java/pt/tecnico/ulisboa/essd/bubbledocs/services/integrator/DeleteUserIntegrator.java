package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.dtos.UserDto;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.CreateUserService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.DeleteUserService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetUserInfoService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

public class DeleteUserIntegrator extends BubbleDocsIntegrator {

	private String _userToken;
	private String _toDeleteUsername;
	private DeleteUserService _local;
	private UserDto backupUser;
	private GetUserInfoService userInfo;
	
	public DeleteUserIntegrator(String userToken, String toDeleteUsername) {
		
		_userToken = userToken;
		_toDeleteUsername = toDeleteUsername;
		
		//Generating backup user in case remote invocation fails
		userInfo = new GetUserInfoService(toDeleteUsername);
		userInfo.execute();												
		backupUser = userInfo.getResult();
		
	}
	@Override
	protected void dispatch() throws BubbleDocsException {
		
		/*
		 * 
		 *  Instância local
		 * 
		 * 
		 */
				
		_local = new DeleteUserService(_userToken,_toDeleteUsername);
		_local.execute();
		
		/*
		 * 
		 *  Instância Remota
		 * 
		 */
		
		IDRemoteServices remote = new IDRemoteServices();

		try {
			// invoke some method on remote
			remote.removeUser(_toDeleteUsername);
		
		} catch (RemoteInvocationException rie) {
			restoreUser(); 

			throw new UnavailableServiceException();
	
		} catch (LoginBubbleDocsException ex) {
			restoreUser();
			
			throw new LoginBubbleDocsException();
		}
	}
	
	
	private void restoreUser() {
		
		/*
		 * Pelo enunciado, nao e necessario a recuperacao das folhas apagadas do utilizador
		 */
		CreateUserService restore = new CreateUserService(_userToken,
															backupUser.getUsername(),
																backupUser.getEmail(),
																	backupUser.getName());
		restore.execute();
	
	}
	
}

