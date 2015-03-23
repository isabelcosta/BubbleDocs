package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;
import org.joda.time.LocalDate;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.DontHavePermissionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;


public class FolhadeCalculo extends FolhadeCalculo_Base {
    
    public FolhadeCalculo(String nomeFolha, String username, int linhas, int colunas) {
    	 super();
    	 setNomeFolha(nomeFolha);
         setDono(username);
         setLinhas(linhas);
         setColunas(colunas);
         int id = Bubbledocs.getInstance().getProxID();
         setID(id++);
         setDataCriacao(new LocalDate());
         
         //actualiza ID
         Bubbledocs.getInstance().setProxID(id++);
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
			for (Celula celula : getCelulaSet()){		// passa pelas mesmas celulas varias vezes
				
//				System.out.println(i + " coluna: " + celula.getColuna());
//				System.out.println("referencia: " + celula.getReferencia());
//				System.out.println("");
				
				
				if( celula.getConteudo()!=null)
					celulasElement.addContent(celula.exportToXML());
			}
		}
		return element;
    }

    public void importFromXML(Element folhadecalculoElement) {
    	Element folhadecalculo = folhadecalculoElement.getChild("celulas");
    	

    	
    	for (Element celula : folhadecalculo.getChildren("celula")) {
    		
    	    Celula c = new Celula(0, 0, null);
    	    addCelula(c);
    	    c.importFromXML(celula, folhadecalculoElement);
    	    

    	
    	}
    }
    
    //FOLHA
    /* Apaga todas as associacoes ligadas a esta folha */
    public void delete(){
    	for (Celula toRemove : getCelulaSet()){
    		toRemove.delete();
    	}
    		
    }
    
    // EXCEPCOES-UTILIZADORES
    /* Verifica se um utilizador e dono da folha */
    public boolean isDono (String nome) {
    	if (this.getDono().equals(nome))
    		return true;
    	else
    		return false;
    }
    
    /* Verifica se o utilizador pode escrever nesta folha */
    public boolean podeEscrever (String username) throws DontHavePermissionException{
    	if(isDono(username)){
    		return true;
    	}
    	for (Utilizador existeUtilizador : this.getUtilizadores_eSet()){
    		if (existeUtilizador.getUsername().equals(username))
    			return true;
    	}
    	throw new DontHavePermissionException(username);
    	
    }
    
    /* Verifica se o utilizador pode ler nesta folha */
    public boolean podeLer (String username) throws DontHavePermissionException{
    	if(isDono(username)){
    		return true;
    	}    	
    	for (Utilizador existeUtilizador : this.getUtilizadores_lSet()){
    		if (existeUtilizador.getUsername().equals(username))
    			return true;
        }
    	throw new DontHavePermissionException(username);
        
    }
    
    
    //CELULAS

    public void modificarCelula(int linha, int coluna, String conteudoAcriar){
    	Conteudo conteudo = this.criaConteudo(conteudoAcriar);
    	
    	//se a celula existir so vai alterar o conteudo
    	for (Celula existeCelula : this.getCelulaSet()){
    		if (existeCelula.getLinha() == linha && existeCelula.getColuna() == coluna){
    			if(!existeCelula.getProtegida()){
	    			existeCelula.setConteudo(conteudo);
	    			return;
    			}	
				else {
					throw new DontHavePermissionException("A celula esta protegida");
				}    			
			} 
    	}
    	
		Celula novaCelula = new Celula(linha, coluna, conteudo);
		this.addCelula(novaCelula);

    }
    
    //recebe ou True para bloquear ou False para desbloquear a celula 
    public void protegeCelula(int linha, int coluna, boolean protege){
    	for (Celula existeCelula : this.getCelulaSet()){
    		if (existeCelula.getLinha() == linha && existeCelula.getColuna() == coluna){
    			existeCelula.setProtegida(protege);
    			return;
    		}
    	}
		Celula novaCelula = new Celula(linha, coluna, null);
		novaCelula.setProtegida(protege);
		this.addCelula(novaCelula);
    }
    
    /* Dependendo da string que recebe cria o conteudo correspondente */
    private Conteudo criaConteudo(String conteudoAcriar) {
    	Conteudo c = null;
    	
    	try {
			 c= Parser.parseConteudo(this, conteudoAcriar);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return c;
    
	}
    
    
    // EXCEPCAO-CELULA
	/* Verifica se a celula respeita os limites da folha */
    public void verificaLimite(int linha, int coluna) throws OutOfBoundsException{
    	if (linha > this.getLinhas() || coluna > this.getColunas())
    		throw new OutOfBoundsException(linha, coluna);

    }

    
}
