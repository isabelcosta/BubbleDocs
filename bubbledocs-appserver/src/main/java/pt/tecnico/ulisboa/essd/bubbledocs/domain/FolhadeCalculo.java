package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;


public class FolhadeCalculo extends FolhadeCalculo_Base {
    
    public FolhadeCalculo() {
    	 super();
    }
    
    public Element exportToXML() {
		Element element = new Element("folhadecalculo");
	
		element.setAttribute("dono", getDono());
		element.setAttribute("nome", getNomeFolha());
		element.setAttribute("linhas", Integer.toString(getLinhas()));
		element.setAttribute("colunas", Integer.toString(getColunas()));
		
		
		if (!getCelulaSet().isEmpty()){
			Element celulasElement = new Element("celulas");
			element.addContent(celulasElement);
			for (Celula celula : getCelulaSet())
				celulasElement.addContent(celula.exportToXML());
		}
		return element;
    }

    public void importFromXML(Element folhadecalculoElement) {
    	Element folhadecalculo = folhadecalculoElement.getChild("celulas");
    	
    	System.out.println(folhadecalculoElement.getChild("celulas") + " ASDASDASDASD");
    	
    	for (Element celula : folhadecalculo.getChildren("celula")) {
    		
    	    Celula c = new Celula(0, 0, null);
    	    c.importFromXML(celula);
    	    addCelula(c);
    	    System.out.println(c.getColuna() +" c");
    	    System.out.println(c.getLinha() +" l");
    	}
    }
    
    //FOLHA
    /* Apaga todas as associacoes ligadas a esta folha */
    public void delete(){
    	for (Celula toRemove : getCelulaSet()){
    		toRemove.delete();
    	}
    		
    }
    
    // UTILIZADORES
    /* Verifica se um utilizador e dono da folha */
    public boolean isDono (String nome){
    	if (this.getDono().equals(nome))
    		return true;
    	else
    		return false;
    }
    
    /* Verifica se o utilizador pode escrever nesta folha */
    public boolean podeEscrever (String nome){
    	for (Utilizador existeUtilizador : this.getUtilizadores_eSet()){
    		if (existeUtilizador.getNome().equals(nome))
    			return true;
    	}
    	return false; 
    }
    
    /* Verifica se o utilizador pode ler nesta folha */
    public boolean podeLer (String nome){
    	for (Utilizador existeUtilizador : this.getUtilizadores_lSet()){
    		if (existeUtilizador.getNome().equals(nome))
    			return true;
    	}
    	return false; 
    }
    
    
    //CELULAS

    public void modificarCelula(int linha, int coluna, String conteudoAcriar){
    	Conteudo conteudo = this.criaConteudo(conteudoAcriar);
    	
    	//se a celula existir so vai alterar o conteudo
    	for (Celula existeCelula : this.getCelulaSet()){
    		if (existeCelula.getLinha() == linha && existeCelula.getColuna() == coluna){
    		 existeCelula.setConteudo(conteudo);
    		}
    			
    	}
    	
    	if (this.verificaLimite(linha,coluna)){
    		Celula novaCelula = new Celula(linha, coluna, conteudo);
    		this.addCelula(novaCelula);
    	}
    }
    
    /* Dependendo da string que recebe cria o conteudo correspondente */
    private Conteudo criaConteudo(String conteudoAcriar) {
    	Conteudo c = null;
    	
    	try {
			 c= Parser.parseConteudo(this, conteudoAcriar);
//			 System.out.println(c.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return c;
    
	}

	/* Verifica se a celula respeita os limites da folha */
    public boolean verificaLimite(int linha, int coluna){
    	if (linha > this.getLinhas() || coluna > this.getColunas())
    			return false;

    	return true; 
    }

//	public static FolhadeCalculo getInstance() {
//		FolhadeCalculo pb = FenixFramework.getDomainRoot().getFolhasdecalculo();
//	if (pb == null)
//	    pb = new FolhadeCalculo();
//
//	return pb;
//    }
    
}
