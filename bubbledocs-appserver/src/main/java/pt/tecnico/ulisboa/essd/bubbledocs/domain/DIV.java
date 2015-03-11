package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;

public class DIV extends DIV_Base {
    
	public DIV(Argumento arg1, Argumento arg2) {
		this.setArgumento1(arg1);
		this.setArgumento2(arg2);
    }
    
    @Override
    public Element exportToXML() {
    	
    	Element element = new Element("DIV");							// DIV
    																	// --------
    	Element argumento1Element = new Element("arg1");				// |arg1
    	element.addContent(argumento1Element);							// |	.Arg1ExportedToXML
    																	// |arg2
    	Element argumento2Element = new Element("arg2");				// |	.Arg2ExportedToXML
    	element.addContent(argumento2Element);							// |
    																	// ---------
//    	System.out.println(getArgumento1(). + " ARG1");
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
