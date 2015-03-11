package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;

public class Referencia extends Referencia_Base {
    
	public Referencia(Celula celula) {
		this.setCelularef(celula);
	}
    
    public Element exportToXML() {
    	Element element = new Element("celula");
		
		element.setAttribute("linha", Integer.toString(getCelularef().getLinha()));
		element.setAttribute("coluna", Integer.toString(getCelularef().getColuna()));
    	return element;
		
	}
    
    public void delete() {
    	setCelula(null);
    	setCelularef(null);
    	deleteDomainObject();
    }
    
	public void importToXML() {
		// TODO Auto-generated method stub
		
	}
}
