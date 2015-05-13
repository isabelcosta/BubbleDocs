package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class DeleteUserService extends IsRootService {

	private String _toDeleteUsername;

	public DeleteUserService(String userToken, String toDeleteUsername) {
		super(userToken);
		_toDeleteUsername = toDeleteUsername;

	}

	@Override
	protected void dispatch_root() throws BubbleDocsException {
		Bubbledocs _bd = getBubbleDocs();	
		//invoke same method locally, supposing no exceptions caught
		_bd.removeUtilizadores(_bd.getUserOfName(_toDeleteUsername));
	}
	
}

