package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;

public class Celula extends Celula_Base {
    
	public Celula(int linha, int coluna, Conteudo conteudo) {
		this.setLinha(linha);
		this.setColuna(coluna);
		this.setConteudo(conteudo);
	}

	public void apagarConteudo() {
		// TODO Auto-generated method stub
		
	}

	public Element exportToXML() {
		Element element = new Element("celula");
		
		element.setAttribute("linha", Integer.toString(getLinha()));
		element.setAttribute("coluna", Integer.toString(getColuna()));
		
		Element conteudoElement = new Element("conteudo");
		element.addContent(conteudoElement);
		conteudoElement.addContent(this.getConteudo().exportToXML());
		
		return element;
	}

	public void importFromXML(Element celula) {
		// TODO Auto-generated method stub
		
//		this.getConteudo().exportToXML();
		this.getConteudo().importToXML();
	}
	
    
}
