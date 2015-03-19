package pt.tecnico.ulisboa.essd.bubbledocs.service;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;



public class DeleteUser extends BubbleDocsService {
	
	private String userToken;
	private String toDeleteUsername;
	
    public DeleteUser(String userToken, String toDeleteUsername) {
    	
    	this.userToken = userToken;
    	this.toDeleteUsername = toDeleteUsername;
    	
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	
    	//Acrescentar a Verificação do Login usando o token
   
    	for(Utilizador user : FenixFramework.getDomainRoot().getUtilizadoresSet()){
    		
    		if(Util)
    	}
    	
    	
    	
    	
    }

}
