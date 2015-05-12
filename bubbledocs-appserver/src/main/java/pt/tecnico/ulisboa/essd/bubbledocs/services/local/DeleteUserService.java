package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class DeleteUserService extends IsRootService {

	private String toDeleteUsername;

	public DeleteUserService(String userToken, String toDeleteUsername) {
		super(userToken);
		this.toDeleteUsername = toDeleteUsername;

	}

	@Override
	protected void dispatch_root() throws BubbleDocsException {
				
		//invoke same method locally, supposing no exceptions caught
		_bd.removeUtilizadores(_bd.getUserOfName(toDeleteUsername));
	}
	
}

