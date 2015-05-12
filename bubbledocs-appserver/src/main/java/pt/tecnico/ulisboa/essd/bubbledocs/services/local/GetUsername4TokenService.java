package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;

public class GetUsername4TokenService extends ValidSessionsService {

	private String _result;
	
	public GetUsername4TokenService(String userToken) {
		super(userToken);
	}

	@Override
	protected void dispatch_session(){
		_result = Bubbledocs.getInstance().getUsernameOfToken(_userToken);
		
	}

	public String getResult() {
		return _result;
	}
	
}
