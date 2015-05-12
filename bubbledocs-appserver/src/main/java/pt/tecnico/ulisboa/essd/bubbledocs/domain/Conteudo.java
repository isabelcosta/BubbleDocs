package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;

public class Conteudo extends Conteudo_Base {
    
    public Conteudo() {
        super();
    }

    public void delete() {
    	setCelula(null);
    	deleteDomainObject();
    }

	public Element exportToXML() {
		return null;
	}

	public void importToXML() {		
	}
	
	public Integer getValor(){
		return null;		
	}

    public String getContentValue(){
    	if (getValor() !=null) {
    		return Integer.toString(getValor());
    	}
    	return "#VALUE";
	}
		

}
