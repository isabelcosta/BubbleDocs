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
    	setCelularef(null);
    	setCelula(null);
    	deleteDomainObject();
    }
    
	
	public String toString(){
		String res = "=" + getCelularef().getLinha().toString() + ";" + getCelularef().getColuna().toString();
		
		return res;
	}
		
	
	  @Override
	  public Integer getValor(){
	    	
		  //primeiro verifica se tem conteudo
	    if (getCelularef().getConteudo() == null || getCelularef().getConteudo().getValor() == null){
	    	return null;
	    }
	    	
	    Integer res = getCelularef().getConteudo().getValor();
	    	
	    return res;
	  }
	  
	  public String contentValue(){
		  if(getValor() == null){
			  return "#VALUE";
		  } else {
			  return getValor() + "";
		  }
	  }
}
