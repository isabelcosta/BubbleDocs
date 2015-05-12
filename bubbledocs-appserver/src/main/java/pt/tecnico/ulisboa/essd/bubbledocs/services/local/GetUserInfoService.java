package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.dtos.UserDto;

public class GetUserInfoService extends BubbleDocsService {

	private Bubbledocs _bd = getBubbleDocs();
	private UserDto _result;
	private String _username;
	
	
	public GetUserInfoService(String username) {
		_username=username;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		Utilizador user = _bd.getUserfromUsername(_username);
		
		_result = new UserDto(user.getUsername(), user.getEmail(), user.getNome());
		
	}
	
	 public final UserDto getResult() {
	        return _result;
	    }


}
