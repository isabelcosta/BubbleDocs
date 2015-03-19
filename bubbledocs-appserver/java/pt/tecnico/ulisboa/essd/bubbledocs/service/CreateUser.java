package pt.tecnico.ulisboa.essd.bubbledocs.service;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;


public class CreateUser extends BubbleDocsService {
	

	private String userToken;
	private String newUsername;
	private String password;
	private String name;
	
    public CreateUser(String userToken, String newUsername, String password, String name) {
    	
    	this.userToken = userToken;
    	this.newUsername = newUsername;
    	this.password = password;
    	this.name = name;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	
    	//Acrescentar a Verificação do Login usando o token
    	
    	Utilizador user = new Utilizador(newUsername, password, name);
    
    	
    	FenixFramework.getDomainRoot().addUtilizadores(user);
    	
    		
    	}
    }
