package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;

public class Referencia extends Referencia_Base {
    
	public Referencia(Celula celula) {
		this.setCelula(celula);
    }
    
    public Element exportToXML() {
    	
    	return getCelula().exportToXML();
		
	}

	public void importToXML() {
		// TODO Auto-generated method stub
		
	}
}
