package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;

public class Literal extends Literal_Base {
    
    public Literal() {
        super();
    }
    
     public Literal(Integer literal) {
		this.setLiteral(literal);
	} 

	
	public Element exportToXML() {
		
		Element element = new Element("literal");
		
<<<<<<< Upstream, based on origin/master
		element.setAttribute("valor", Integer.toString(getLiteral()));
=======
		element.setAttribute("valor", Integer.toString(getValor()));
>>>>>>> 0c33ff7 erro
		
		return element;
		
	}

	public void importToXML() {
		// TODO Auto-generated method stub
		
	}
}
