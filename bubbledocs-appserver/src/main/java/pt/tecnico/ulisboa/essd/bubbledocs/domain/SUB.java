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

    @Override
    public String toString(){
    	String arg1 = getArgumento1().toString();
    	String arg2 = getArgumento2().toString();
    			
    	if(arg1.contains("=")){
    		arg1 =  arg1.substring(1);
    	}
    	if(arg2.contains("=")){
    		arg2 =  arg2.substring(1);
    	}
    			
		return "=" + "SUB" + "(" + arg1 + "," + arg2 + ")";
    }
    

    @Override
    public Integer getValor(){
    	
    	if (getArgumento1().getValor() == null || getArgumento2().getValor() == null){
    		return null;
    	}
    	
    	Integer res = getArgumento1().getValor() - getArgumento2().getValor();
    	
    	return res;
    }
}
