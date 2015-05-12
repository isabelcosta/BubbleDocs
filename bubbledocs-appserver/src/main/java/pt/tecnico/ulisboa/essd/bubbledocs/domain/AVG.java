package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import java.util.ArrayList;

public class AVG extends AVG_Base {
    
    public AVG(Intervalo intervalo) {
        setIntervalo(intervalo);
    }
    
    
    @Override
    public Integer getValor(){
    	
    	int sum = 0;
    	ArrayList<Integer> lista = getIntervalo().getValorListaCelulas();
    	
    	 if (getIntervalo().getListaCelulas() == null || getIntervalo().getListaCelulas() == null){
     		return null;
     	}

    	for(int i=0; i< lista.size();i++){
    		sum+=i;
    	}
    	return sum / lista.size();
    	
    }
    
    @Override
	public String toString() {
		String res = "=AVG(" + getIntervalo().toString() + ")";
		
		return res;
	}
}
