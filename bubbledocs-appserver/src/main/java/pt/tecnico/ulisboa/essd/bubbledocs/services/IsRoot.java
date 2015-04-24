package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public class IsRoot extends BubbleDocsService {
	
	protected Bubbledocs _bd = Bubbledocs.getInstance();
	protected String _userToken;
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		if(_bd.validSession(getuserToken()) && _bd.isRoot(_userToken)){
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
