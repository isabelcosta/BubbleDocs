package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import java.util.List;

import org.jdom2.Element;

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
		
		element.setAttribute("protegida", Boolean.toString(getProtegida()));
		
		
		Element conteudoElement = new Element("conteudo");
		element.addContent(conteudoElement);
		conteudoElement.addContent(getConteudo().exportToXML());
		return element;
	}
	
	public void importFromXML(Element celula, Element base) {
		
		Celula cell;
		
		List<Element> c1 = celula.getChild("conteudo").getChildren();   // lista de children pode ter: "literal", "referencia" ou "div" ou "sum" etc
		Integer linha = Integer.parseInt(celula.getAttributeValue("linha"));
		Integer coluna = Integer.parseInt(celula.getAttributeValue("coluna"));
		Boolean protegida = Boolean.parseBoolean(celula.getAttributeValue("protegida"));
		
		setColuna(coluna);
		setLinha(linha);
		setProtegida(protegida);
		
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

			for(Celula c : getFolhadecalculoC().getCelulaSet()){
				if (c.getColuna() == colunaRef && c.getLinha() == linhaRef) {
					setConteudo(new Referencia(c));
					getFolhadecalculoC().addCelula(c);
					return;
				}
			}
			cell = new Celula(linhaRef,colunaRef,null);
			
			setConteudo(new Referencia(cell));
			getFolhadecalculoC().addCelula(cell);
			
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
				if(encCelArg1==0){
					Boolean found = false;
					
					for(Celula c : getFolhadecalculoC().getCelulaSet()){
						if (c.getColuna() == linhaRefArg1 && c.getLinha() == colunaRefArg1) {
							arg1 = new Referencia(c);
							getFolhadecalculoC().addCelula(c);
							found = true;
						}
					}
					
					if (!found) {
						cell = new Celula(linhaRefArg1,colunaRefArg1,null);
						arg1 = new Referencia(cell);
						getFolhadecalculoC().addCelula(cell);
					}
				}
			}
			if (isRefArg2==1)
			{
				for (Celula cel : getFolhadecalculoC().getCelulaSet()){
					if (cel.getColuna().equals(colunaRefArg2) && cel.getLinha().equals(linhaRefArg2)){
								arg2 = new Referencia(cel);
								encCelArg1 = 1;
								// se celula nao existir, cria
							}
						}
				if(encCelArg2==0){
					Boolean found = false;
					for(Celula c : getFolhadecalculoC().getCelulaSet()){
						if (c.getColuna() == colunaRefArg2 && c.getLinha() == linhaRefArg2) {
							arg2 = new Referencia(c);
							getFolhadecalculoC().addCelula(c);
							found = true;
						}
					}
					
					if (!found) {
					cell = new Celula(linhaRefArg2,colunaRefArg2,null);
					arg2 = new Referencia(cell);
					getFolhadecalculoC().addCelula(cell);
					}
				}
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
	
	public Integer getValor(){
		return getConteudo().getValor();
	}
	
	
}
