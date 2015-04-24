package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class ValidSessionsService extends BubbleDocsService {
	
	protected Bubbledocs _bd = Bubbledocs.getInstance();
	protected String _userToken;
	
	public ValidSessionsService() {
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		if(_bd.validSession(getuserToken())){
			refreshToken(getuserToken());
		}
	}

	public String getuserToken() {
		return _userToken;
	}

	public void setuserToken(String userToken) {
		_userToken = userToken;
	}

}
