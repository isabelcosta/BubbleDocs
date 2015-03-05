package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;

public class Utilizador extends Utilizador_Base {
    
    public Utilizador() {
        super();
    }
    
    
    
    public void removeFolha(String nome){
    	FolhadeCalculo toRemove = getNomeFolha(nome);
    	
    	if(toRemove ==  null){
    		System.out.println("Folha inexistente");
    	}
    	
    	removeFolha(toRemove);
    	
    	
    	
    	
    }
    
   
    
}
