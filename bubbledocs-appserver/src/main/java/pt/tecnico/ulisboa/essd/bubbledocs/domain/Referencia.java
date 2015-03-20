package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;

public class Referencia extends Referencia_Base {
    
	public Referencia(Celula celula) {
		this.setCelularef(celula);
//		Celula c = getCelularef();
//		System.out.println("|| Va1 " + c.getConteudo().getValor()+ " ||");
//		System.out.println("|| c1 " + c.getColuna()+ " ||");
	}
    
    public Element exportToXML() {
    	Element element = new Element("celula");
    	
//   	Celula c = getCelularef();
//		System.out.println("|| Ce " + c + " ||");
		
		element.setAttribute("linha", Integer.toString(getCelularef().getLinha()));
		element.setAttribute("coluna", Integer.toString(getCelularef().getColuna()));
    	return element;
		
	}
    
    public void delete() {
    	setCelularef(null);
    	setCelula(null);
    	deleteDomainObject();
    }
    
	public void importToXML() {
		// TODO Auto-generated method stub
		
	}
}
