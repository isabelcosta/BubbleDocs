package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DontHavePermissionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UsernameAlreadyExistsException;


public class DeleteUser extends BubbleDocsService {

	private String userToken;
	private String toDeleteUsername;

	public DeleteUser(String userToken, String toDeleteUsername) {

		this.userToken = userToken;
		this.toDeleteUsername = toDeleteUsername;

	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		for(Token token : Bubbledocs.getInstance().getTokensSet()){

			if(token.getToken().equals(userToken)){

				for(Utilizador user : Bubbledocs.getInstance().getUtilizadoresSet()){

					if(user.getUsername().equals(toDeleteUsername)){

						Bubbledocs.getInstance().removeUtilizadores(user);

					}
				}

			}
		}
	}
}
