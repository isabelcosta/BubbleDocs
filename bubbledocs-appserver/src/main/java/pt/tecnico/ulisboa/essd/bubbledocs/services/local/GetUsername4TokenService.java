package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;

public class GetUsername4TokenService extends ValidSessionsService {

	private String _result;
	
	
	public GetUsername4TokenService(String userToken) {
		super(userToken);
	}

	@Override
	protected void dispatch_session() throws BubbleDocsException {
		_result = Bubbledocs.getInstance().getUsernameOfToken(_userToken);
		
	}

	public String getUsername() {
		return _result;
	}
	
}
