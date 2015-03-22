package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import java.util.List;

import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;

public class Celula extends Celula_Base {
    
	public Celula(int linha, int coluna, Conteudo conteudo) {
		this.setLinha(linha);
		this.setColuna(coluna);
		this.setConteudo(conteudo);
		this.setProtegida(false);
	}

	public void delete() {
		getConteudo().delete();
		setConteudo(null);
		setFolhadecalculoC(null);
		
		for(Referencia ref : getReferenciaSet()){
			this.removeReferencia(ref);
		}
		deleteDomainObject();
		
	}

	public Element exportToXML() {
		Element element = new Element("celula");
		
		element.setAttribute("linha", Integer.toString(getLinha()));
		element.setAttribute("coluna", Integer.toString(getColuna()));
		
//		System.out.println(getConteudo() + " conteudo");
		Element conteudoElement = new Element("conteudo");
		element.addContent(conteudoElement);
		conteudoElement.addContent(getConteudo().exportToXML());
		return element;
	}
	
public void importFromXML(Element celula, Element base) {
		
		List<Element> c1 = celula.getChild("conteudo").getChildren();   // lista de children pode ter: "literal", "referencia" ou "div" ou "sum" etc
		Integer linha = Integer.parseInt(celula.getAttributeValue("linha"));
		Integer coluna = Integer.parseInt(celula.getAttributeValue("coluna"));
		setColuna(coluna);
		setLinha(linha);
//		getConteudo().importToXML(celula.getChild("conteudo"));
		
//		String donoFolha = base.getAttributeValue("dono");
//    	String nomeFolha = base.getAttributeValue("nome");
    	
    	
    	//ir buscar o 
		 
		
		
		
		if (c1.get(0).getName().equals("literal")) {
			setConteudo(new Literal(Integer.parseInt(c1.get(0).getAttributeValue("valor"))));
		}else if(c1.get(0).getName().equals("celula")){
			Integer linhaRef = Integer.parseInt(c1.get(0).getAttributeValue("linha"));
			Integer colunaRef = Integer.parseInt(c1.get(0).getAttributeValue("coluna"));
			for (Celula cel : getFolhadecalculoC().getCelulaSet()){
				if (cel.getColuna().equals(linhaRef) && cel.getLinha().equals(colunaRef)){
					setConteudo(new Referencia(cel));
					return;
				}
			}
			setConteudo(new Referencia(new Celula(linhaRef,colunaRef,null)));
			
		}else{
			Argumento arg1 = null;
			Argumento arg2 = null;
			int isRefArg1 = 0; // 1 para referencia, 0 para literal
			int isRefArg2 = 0;
			
			
			Integer linhaRefArg1 = null;
			Integer colunaRefArg1 = null;

			Integer linhaRefArg2 = null;
			Integer colunaRefArg2 = null;
			
			Integer literalArg1 = null;
			Integer literalArg2 = null;

			int encCelArg1 = 0; //  verificar se encontrou celula para a referencia
			int encCelArg2 = 0;
			
			
			if (c1.get(0).getChild("arg1").getChildren().get(0).getName().equals("celula")) {
				linhaRefArg1 = Integer.parseInt(c1.get(0).getChild("arg1").getChildren().get(0).getAttributeValue("linha"));
				colunaRefArg1 = Integer.parseInt(c1.get(0).getChild("arg1").getChildren().get(0).getAttributeValue("coluna"));
				isRefArg1= 1;
				
			}else {
				literalArg1 = Integer.parseInt(c1.get(0).getChild("arg1").getChild("literal").getAttributeValue("valor"));
				arg1 = new Literal(literalArg1);
			}
			
			if (c1.get(0).getChild("arg2").getChildren().get(0).getName().equals("celula")) {
				
				linhaRefArg2 = Integer.parseInt(c1.get(0).getChild("arg2").getChildren().get(0).getAttributeValue("linha"));
				colunaRefArg2 = Integer.parseInt(c1.get(0).getChild("arg2").getChildren().get(0).getAttributeValue("coluna"));
				isRefArg2= 1;
				
			}else{
				literalArg2 = Integer.parseInt(c1.get(0).getChild("arg2").getChild("literal").getAttributeValue("valor"));
				arg2 = new Literal(literalArg2);
			}
			
			if (isRefArg1==1) 
			{
				for (Celula cel : getFolhadecalculoC().getCelulaSet()){
					if (cel.getColuna().equals(linhaRefArg1) && cel.getLinha().equals(colunaRefArg1)){
						arg1 = new Referencia(cel);																// se celula nao existir, cria
						encCelArg1 = 1;
					}
				}
				//<WARNING>
				//Referência não existe 
				//Célula com conteúdo nulo
				//Solução pode ser usar o observer
				if(encCelArg1==0)
					arg1 = new Referencia(new Celula(linhaRefArg1,colunaRefArg1,null));
			}
			if (isRefArg2==1)
			{
				for (Celula cel : getFolhadecalculoC().getCelulaSet()){
					if (cel.getColuna().equals(linhaRefArg2) && cel.getLinha().equals(colunaRefArg2)){
								arg2 = new Referencia(cel);																// se celula nao existir, cria
							}
						}
				//<WARNING>
				//Referência não existe 
				//Célula com conteúdo nulo
				//Solução pode ser usar o observer
				if(encCelArg2==0)
					arg2 = new Referencia(new Celula(linhaRefArg2,colunaRefArg2,null));
			}
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
