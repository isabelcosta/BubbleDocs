package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;


import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.CreateUserService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.DeleteUserService;

public class CreateUserIntegrator extends BubbleDocsIntegrator{
	
	private String userToken;
	private String newUsername;
	private String email;
	private String name;
	private CreateUserService local;

	public CreateUserIntegrator(String userToken, String newUsername, String email, String name) {

		this.newUsername = newUsername;
		this.email = email;
		this.name = name;
		this.userToken = userToken;

	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		/*Instancia local*/

		local = new CreateUserService(this.userToken, this.newUsername, this.email, this.name);
		local.execute();

		/*  Instância remota*/

		IDRemoteServices remote = new IDRemoteServices();

		try {

			remote.createUser(this.newUsername, this.email);


		} catch (RemoteInvocationException rie) {
			deleteUser();
			throw new UnavailableServiceException();
			
		} catch (InvalidEmailException iee) {
			deleteUser();
			throw new InvalidEmailException ();
			
		} catch (DuplicateUsernameException due) {
			deleteUser();
			throw new DuplicateUsernameException(this.newUsername);
			
		} catch (UserNotInSessionException unise) {
			deleteUser();
			
			throw new UserNotInSessionException (this.newUsername);
			
		} catch (EmptyUsernameException eue) {
			deleteUser();
			throw new EmptyUsernameException ();
		}
	}
	
	public void deleteUser(){
		
		DeleteUserService user = new DeleteUserService(this.userToken, this.newUsername);
		user.execute();
		
	}

}
