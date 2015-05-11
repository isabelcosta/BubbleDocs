package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.IdFolhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UtilizadorInvalidoException;


public class Utilizador extends Utilizador_Base {
	
	private HashMap<Integer, String> _folhasExportadas = new HashMap<Integer, String>();
	
	
    public Utilizador(String nomeUtilizador, String userName, String email) {
        
        try {
        	if (nameIsValid(userName)){
        		setNome(nomeUtilizador);
                setUsername(userName);
                setEmail(email);
        	}	
        }catch (UtilizadorInvalidoException ex) {
			System.err.println("O username tem de ter entre 3 e 8 caracteres");
        }
    }
    
    private boolean nameIsValid(String username) throws UtilizadorInvalidoException{
    	int size = username.length();
    	
    	if (size >=3 && size <=8)
    		return true;
    	else
    		throw new UtilizadorInvalidoException();
    }
    
    @Override
    public void setUsername (String username){
    	if (nameIsValid(username)) {
    		super.setUsername(username);
    	}
    }
    
    public void addFolhaExportada(Integer id, String nome) {
    	
    	if (id== null || id < 0) {
    		throw new IdFolhaInvalidoException();
    	}
    	
    	
    	_folhasExportadas.put(id, nome);
    }
    
    public HashMap<Integer, String> getFolhasExportadas() {
    	return _folhasExportadas;
    }
    
}
    		
    	
    
