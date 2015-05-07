package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import java.util.ArrayList;

public class PRD extends PRD_Base {
    
    public PRD(Intervalo intervalo) {
        this.setIntervalo(intervalo);
    }

    
    @Override
    public Integer getValor(){
    	
    	int sum = 0;
    	ArrayList<Integer> lista = getIntervalo().getValorListaCelulas();
    	
    	 if (getIntervalo().getValorListaCelulas() == null || getIntervalo().getValorListaCelulas() == null){
     		return null;
     	}

    	for(int i=0; i< lista.size();i++){
    		sum*=i;
    	}
    	return sum;
    	
    }
    
	public String toString(){
		String res = "=PRD(" + getIntervalo().toString() + ")";
		
		return res;
	}
    
    
}
