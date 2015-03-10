package pt.tecnico.ulisboa.essd.bubbledocs.domain;


public class FolhadeCalculo extends FolhadeCalculo_Base {
    
    public FolhadeCalculo() {
    	 super();
    }
    
    
    //FOLHA
    /* Apaga todas as associacoes ligadas a esta folha */
    public void apagarFolha(){
    	for (Celula toRemove : this.getCelulaSet()){
    		toRemove.apagarConteudo();
    		this.removeCelula(toRemove);
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
    
}
