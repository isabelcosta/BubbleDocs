package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.UtilizadorInvalidoException;


public class Utilizador extends Utilizador_Base {
    
    public Utilizador(String nomeUtilizador, String userName, String email) {
        super();
        
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
    	
}
    		
    		
    	
    
