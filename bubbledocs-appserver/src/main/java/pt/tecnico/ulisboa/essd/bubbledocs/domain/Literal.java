package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;

public class Literal extends Literal_Base {
    
    public Literal() {
        super();
    }
    
     public Literal(Integer literal) {
		this.setLiteral(literal);
	} 
    
    
    public Integer getValor(){
    	return getLiteral();
    }
	
	public Element exportToXML() {
		
		Element element = new Element("literal");
		
		element.setAttribute("valor", Integer.toString(getLiteral()));

		return element;
		
	}

	public void importToXML() {
		// TODO Auto-generated method stub
		
	}
	public void delete() {
    	setCelula(null);
    	setLiteral(null);
    	setFuncaobinaria1(null);
    	setFuncaobinaria2(null);
    	setValor(null);
    	deleteDomainObject();
    }
}
