package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;

public class SUB extends SUB_Base {
    
	public SUB(Argumento arg1, Argumento arg2) {
		this.setArgumento1(arg1);
		this.setArgumento2(arg2);
    }

    @Override
    public Element exportToXML() {
    	
    	Element element = new Element("SUB");							// SUB
    																	// --------
    	Element argumento1Element = new Element("arg1");				// |arg1
    	element.addContent(argumento1Element);							// |	.Arg1ExportedToXML
    																	// |arg2
    	Element argumento2Element = new Element("arg2");				// |	.Arg2ExportedToXML
    	element.addContent(argumento2Element);							// |
    																	// ---------
    	argumento1Element.addContent(getArgumento1().exportToXML());	//
    	argumento2Element.addContent(getArgumento2().exportToXML());	//
    	
		return element;
	}
    
    public void delete() {
    	setCelula(null);
    	setArgumento1(null);
    	setArgumento2(null);
    	deleteDomainObject();
    	
    }
}
