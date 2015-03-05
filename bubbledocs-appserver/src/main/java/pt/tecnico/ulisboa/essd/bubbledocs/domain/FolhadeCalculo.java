package pt.tecnico.ulisboa.essd.bubbledocs.domain;


public class FolhadeCalculo extends FolhadeCalculo_Base {
    
    public FolhadeCalculo(String dono,String nome, int linhas, int colunas) {
    	this.setDono(dono);
    	this.setNomeFolha(nome);
    	this.setLinhas(linhas);
    	this.setColunas(colunas);
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

    public void criarCelula(int linha, int coluna, String conteudoAcriar){
    	Conteudo conteudo = this.criaConteudo(conteudoAcriar);
    	
    	//se a celula existir so vai alterar o conteudo
    	for (Celula existeCelula : this.getCelulaSet()){
    		if (existeCelula.getLinha() == linha && existeCelula.getColuna() == coluna){
    		 // TODO
    		}
    			
    	}
    	Celula novaCelula = new Celula(linha, coluna, conteudo);
    	if (this.verificaLimite(novaCelula))
    		this.addCelula(novaCelula);
    }
    
    /* Dependendo da string que recebe cria o conteudo correspondente */
    private Conteudo criaConteudo(String conteudoAcriar) {
    	Conteudo conteudo = null;
    	
    	if(this.isInteger(conteudoAcriar)){
    		int i = Integer.parseInt(conteudoAcriar);
    		conteudo = new Literal(i);
    	}
    	
		return conteudo;
	}

	/* Verifica se a celula respeita os limites da folha */
    public boolean verificaLimite(Celula celula){
    	if (celula.getLinha() > this.getLinhas() || celula.getColuna() > this.getColunas())
    			return false;

    	return true; 
    }
    
    
    
    //APOIOS
    /* Verificar se a string e um inteiro */
    private boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        }
        // only got here if we didn't return false
        return true;
    }
    
}
