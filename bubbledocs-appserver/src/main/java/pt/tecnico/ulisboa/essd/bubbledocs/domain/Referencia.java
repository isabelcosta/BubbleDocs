package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;

public class Referencia extends Referencia_Base {
    
	public Referencia(Celula celula) {
		this.setCelula(celula);
    }
    
    public Element exportToXML() {
    	Element element = new Element("celula");
		
		element.setAttribute("linha", Integer.toString(getCelula().getLinha()));
		element.setAttribute("coluna", Integer.toString(getCelula().getColuna()));
    	return element;
		
	}
    
    
    
	public void importToXML() {
		// TODO Auto-generated method stub
		
	}
}
