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
		// TODO Auto-generated method stub
		return null;
	}

	public void importToXML() {
		// TODO Auto-generated method stub
		
	}

    /*
     * 
     * tirei os metodos import e export
     * 
     * para ele chamar directamente nas classes filhas
     * 
     * */
}
