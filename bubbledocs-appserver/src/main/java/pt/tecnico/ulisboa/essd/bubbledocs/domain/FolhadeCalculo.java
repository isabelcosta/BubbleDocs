package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import org.jdom2.Element;
import org.joda.time.LocalDate;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.ArgColunaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ArgLinhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ProtectedCellException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;


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
		element.setAttribute("data", getDataCriacao().toString());
		element.setAttribute("id", Integer.toString(getID()));
		
		if (!getCelulaSet().isEmpty()){
			Element celulasElement = new Element("celulas");
			element.addContent(celulasElement);
			for (Celula celula : getCelulaSet()){		// passa pelas mesmas celulas varias vezes
				
				
				if( celula.getConteudo()!=null)
					celulasElement.addContent(celula.exportToXML());
			}
		}
		return element;
    }

    public void importFromXML(Element folhadecalculoElement) {
    	Element folhadecalculo = folhadecalculoElement.getChild("celulas");
    	
    	
    	
    	if (folhadecalculo != null)
	    	for (Element celula : folhadecalculo.getChildren("celula")) {
	
	    		Integer linha = Integer.parseInt(celula.getAttributeValue("linha"));
	    		Integer coluna = Integer.parseInt(celula.getAttributeValue("coluna"));
	    		
	    		Boolean entrou=false;
	
	    		
	    		for (Celula cel : getCelulaSet()){
					if (cel.getLinha().equals(linha) && cel.getColuna().equals(coluna)) {
				    	 cel.importFromXML(celula, folhadecalculoElement);
				    	 entrou = true;
					}
				}
				if (!entrou) {
					Celula c = new Celula(0, 0, null);
		    	    addCelula(c);
		    	    c.importFromXML(celula, folhadecalculoElement);
				}
	    	
	    	}
    }
    
    //FOLHA
    /* Apaga todas as associacoes ligadas a esta folha */
    public void delete(){
    	for (Celula toRemove : getCelulaSet()){
    		toRemove.delete();
    	}
    		
    }
    
    // EXCEPCOES_FOLHA
    //Linha invalida
    @Override
	public void setLinhas(Integer linhas) throws ArgLinhaInvalidoException{
    	if(linhas < 0 || linhas == null)
    		throw new ArgLinhaInvalidoException(linhas.toString());
    	
    	super.setLinhas(linhas);
    }
    
    //Coluna invalida
    @Override
	public void setColunas(Integer colunas) throws ArgColunaInvalidoException{
    	if(colunas < 0 || colunas == null)
    		throw new ArgColunaInvalidoException(colunas.toString());
    	
    	super.setColunas(colunas);
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
    public boolean podeEscrever (String username) throws UnauthorizedOperationException{
    	if(isDono(username)){
    		return true;
    	}
    	for (Utilizador existeUtilizador : this.getUtilizadores_eSet()){
    		if (existeUtilizador.getUsername().equals(username))
    			return true;
    	}
    	throw new UnauthorizedOperationException("O utilizador" + username + "nao pode escrever" );
    	
    }
    
    /* Verifica se o utilizador pode ler nesta folha */
    public boolean podeLer (String username) throws UnauthorizedOperationException{
    	if(isDono(username)){
    		return true;
    	}    	
    	for (Utilizador existeUtilizador : this.getUtilizadores_lSet()){
    		if (existeUtilizador.getUsername().equals(username))
    			return true;
        }
    	throw new UnauthorizedOperationException();
    }
    
    
    //CELULAS

    public void modificarCelula(int linha, int coluna, String conteudoAcriar) throws ProtectedCellException{
    	
    	//se a celula existir so vai alterar o conteudo
    	for (Celula existeCelula : this.getCelulaSet()){
    		if (existeCelula.getLinha() == linha && existeCelula.getColuna() == coluna){
    			if(!existeCelula.getProtegida()){
    		    	Conteudo conteudo = this.criaConteudo(conteudoAcriar);
	    			existeCelula.setConteudo(conteudo);
	    			return;
    			}	
				else {
					throw new ProtectedCellException("A celula esta protegida");
				}    			
			} 
    	}
    	Conteudo conteudo = this.criaConteudo(conteudoAcriar);
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
    	
    	if(conteudoAcriar != null){
    		c= Parser.parseConteudo(this, conteudoAcriar);    		
    	}
    	
    	return c;
    
	}
    
    public Celula getCell(int linha, int coluna){
    	for(Celula cell: this.getCelulaSet()){
    		if(cell.getLinha() == linha && cell.getColuna() == coluna){
    			return cell;
    		} 
    	}
		Celula novaCelula = new Celula(linha, coluna, null);
		this.addCelula(novaCelula);    	
		return novaCelula;
    }
    
    /* 
     * Devolver uma string com o conteudo de uma celula 
     * 
     * Recebe: dois inteiros (linha:coluna)
     **/
    public String getCellContentToString(int linha,int coluna){
    	Celula cell = this.getCell(linha, coluna);
    	
    	if(cell.getConteudo() != null){
    		return cell.getConteudo().toString();
    	} else {
        	return "()";
    	}
    }
    /* 
     * Devolver uma string com o valor do conteudo de uma celula 
     * 
     * Recebe: dois inteiros (linha:coluna)
     **/  
    public String getCellContentValue(int linha,int coluna){
    	Celula cell = this.getCell(linha, coluna);
    	
    	if(cell.getConteudo() != null){
    		return cell.getConteudo().getContentValue();
    	} else {
        	return "()";
    	}
    }
    
    
    
    // EXCEPCAO-CELULA
	/* Verifica se a celula respeita os limites da folha */
    public Boolean verificaLimite(int linha, int coluna) throws OutOfBoundsException{
    	if (linha > this.getLinhas() || coluna > this.getColunas() || linha < 0 || coluna < 0)
    		throw new OutOfBoundsException(linha, coluna);
    	return true;
    }
    
    
    public Celula getCelulaEspecifica(int linha, int coluna) throws OutOfBoundsException{
    	
    	if(verificaLimite(linha, coluna)){
	    	for(Celula cell: getCelulaSet()){
	    		
	    		int l = cell.getLinha();
				int c = cell.getColuna();
				
				if(linha==l && coluna==c ){
					return cell;
				}
	    	}
	    	
	    	Conteudo conteudo = this.criaConteudo(null);
			Celula novaCelula = new Celula(linha, coluna, conteudo);
			this.addCelula(novaCelula);
			return novaCelula;
    	} else{
    		throw new OutOfBoundsException(linha, coluna);
    	}
    }
}
