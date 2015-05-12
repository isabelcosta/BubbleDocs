package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;


public class FuncaoBinaria extends FuncaoBinaria_Base {
    
    public FuncaoBinaria() {
        super();
    }
    
    public Element exportToXML() {
		return null;
		// TODO Auto-generated method stub
		
	}

	public void importToXML() {
		// TODO Auto-generated method stub
		
	}
	
	 public void delete() {
		 setArgumento1(null);
		 setArgumento2(null);
		 setCelula(null);
		 deleteDomainObject();
		 
	 }
	 
}
