package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;

public class GetUsername4TokenService extends BubbleDocsService {

	private String _result;
	private String _userToken;
	
	public GetUsername4TokenService(String userToken) {
		_userToken = userToken;
	}

	@Override
	protected void dispatch(){
		_result = Bubbledocs.getInstance().getUsernameOfToken(_userToken);
		
	}

	public String getUsername() {
		return _result;
	}
	
}
