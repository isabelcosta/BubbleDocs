package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;

public class Celula extends Celula_Base {
    
	public Celula(int linha, int coluna, Conteudo conteudo) {
		this.setLinha(linha);
		this.setColuna(coluna);
		this.setConteudo(conteudo);
	}

	public void delete() {
		
//		getConteudo().delete();
		setConteudo(null);
		setFolhadecalculoC(null);
		setReferencia(null);
		deleteDomainObject();
		
	}

	public Element exportToXML() {
		Element element = new Element("celula");
		
		element.setAttribute("linha", Integer.toString(getLinha()));
		element.setAttribute("coluna", Integer.toString(getColuna()));
		
		System.out.println(getConteudo() + " conteudo");
		Element conteudoElement = new Element("conteudo");
		element.addContent(conteudoElement);
		conteudoElement.addContent(getConteudo().exportToXML());
		return element;
	}

	public void importFromXML(Element celula) {
		// TODO Auto-generated method stub
		
		List<Element> c1 = celula.getChild("conteudo").getChildren();   // lista de children pode ter: "integral", "referencia" ou "div" ou "sum" etc
		FolhadeCalculo folha = getFolhadecalculoC();
		Utilizador user1 = null;
		Conteudo conteudo = null;
		Integer linha = Integer.parseInt(celula.getAttributeValue("linha"));
		Integer coluna = Integer.parseInt(celula.getAttributeValue("coluna"));
		setColuna(coluna);
		setLinha(linha);
		
		
		for (Utilizador user: FenixFramework.getDomainRoot().getUtilizadoresSet())
			if(user.getUsername().equals("pf")){
				user1 = user;
				break;
			}
    		
		
		
		
		if (c1.get(0).getName().equals("literal")) {
			setConteudo(new Literal(Integer.parseInt(c1.get(0).getAttributeValue("valor"))));
		}else if(c1.get(0).getName().equals("referencia")){
			Integer linhaRef = Integer.parseInt(c1.get(0).getAttributeValue("linha"));
			Integer colunaRef = Integer.parseInt(c1.get(0).getAttributeValue("coluna"));
			
//			System.out.println(linha + " linhAA");
//			System.out.println(coluna + " colunAA");
			
			for(FolhadeCalculo folhaIter : user1.getFolhascriadasSet())
				if(folhaIter.getNomeFolha().equals("Notas ES")) {
					for (Celula cel : folhaIter.getCelulaSet()){
						if (cel.getColuna().equals(linhaRef) && cel.getLinha().equals(colunaRef)){
							setConteudo(new Referencia(cel));
							return;
						}
					}
				}
			setConteudo(new Referencia(new Celula(linhaRef,colunaRef,null)));
			
		}else{
			Argumento arg1 = null;
			Argumento arg2 = null;
			Integer linhaRef = Integer.parseInt(c1.get(0).getAttributeValue("linha"));
			Integer colunaRef = Integer.parseInt(c1.get(0).getAttributeValue("coluna"));
			
			if (c1.get(0).getChild("arg1").getName().equals("referencia")) {
				for(FolhadeCalculo folhaIter : user1.getFolhascriadasSet())
					if(folhaIter.getNomeFolha().equals("Notas ES")) {
						for (Celula cel : folhaIter.getCelulaSet()){
							if (cel.getColuna().equals(linhaRef) && cel.getLinha().equals(colunaRef)){
								arg1 = new Referencia(cel);
							}
						}
					}
			}else if(c1.get(0).getChild("arg1").getName().equals("Literal")) {
				setConteudo(new Literal(Integer.parseInt(c1.get(0).getChild("arg1").getAttributeValue("valor"))));
			arg1 = new Referencia(new Celula(linhaRef,colunaRef,null));
				switch(c1.get(0).getName()) {
				case "MUL":
					setConteudo(new MUL(arg1,arg2));
				case "DIV":
					setConteudo(new DIV(arg1,arg2));
				case "SUB":
					setConteudo(new SUB(arg1,arg2));
				case "ADD":
					setConteudo(new ADD(arg1,arg2));
				}
			
			}
		}
	}
}
