package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class Celula extends Celula_Base {
    
	public Celula(int linha, int coluna, Conteudo conteudo) {
		this.setLinha(linha);
		this.setColuna(coluna);
		this.setConteudo(conteudo);
	}

	public void delete() {
		
		getConteudo().delete();
		setConteudo(null);
		setFolhadecalculoC(null);
		deleteDomainObject();
		
	}

	public Element exportToXML() {
		Element element = new Element("celula");
		
		element.setAttribute("linha", Integer.toString(getLinha()));
		element.setAttribute("coluna", Integer.toString(getColuna()));
		
		System.out.println(getConteudo() + " dasdfasd");
		Element conteudoElement = new Element("conteudo");
		element.addContent(conteudoElement);
		conteudoElement.addContent(getConteudo().exportToXML());
		return element;
	}

	public void importFromXML(Element celula) {
		// TODO Auto-generated method stub
		/*
		List<Element> c1 = celula.getChild("celula").getChild("conteudo").getChildren();   // lista de children pode ter: "integral", "referencia" ou "div" ou "sum" etc
		
		if (c1.get(0).getValue().equals("literal")) {
			Literal conteudo = new Literal(c1.get(0).getAttribute("literal").getIntValue());
		}else if(c1.get(0).equals("referencia")){
			Referencia conteudo = new Referencia()
		}else{
			switch(c1.get(0).getValue()) { // nao sei se vem em string
				case "MUL":
				    MUL conteudo = new MUL(arg1,arg2);
				case "DIV":
					DIV conteudo = new DIV(arg1,arg2);
				case "SUB":
					SUB conteudo = new SUB(arg1,arg2);
				case "ADD":
					ADD conteudo = new ADD(arg1,arg2);
				}
			
		}
		
		Celula c = new Celula(celula.getChild("celula").getAttribute("linha").getIntValue(),
				  celula.getChild("celula").getAttribute("coluna").getIntValue(), 
				  conteudo);
		
		this.getConteudo().importToXML();
		 */
	}
	
    
}
