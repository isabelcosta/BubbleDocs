package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;

public class Utilizador extends Utilizador_Base {
    
    public Utilizador(String nomeUtilizador, String userName, String password) {
        super();
        setNome(nomeUtilizador);
		setUsername(userName);
		setPassword(password);
    }
    
    
    //Cria uma folha
    
    public void criaFolha(String criador, String nomeFolha, int linha , int coluna){
    	
    		FolhadeCalculo folha = new FolhadeCalculo(criador, nomeFolha, linha, coluna);
    		
    		this.addFolhascriadas(folha);
    }
    
    //Remove uma folha 
    
    public void removeFolha(String nome){
    	
    	for(FolhadeCalculo folha : this.getFolhascriadasSet()){
    		if(folha.getNomeFolha().equals(nome)){
    			folha.apagarFolha();
    			this.removeFolhascriadas(folha);
    		}	
    	}	
    }
    
    //Inserir numa folha um conteudo
    
    public void insereCelula(String nomefolha, int linha, int coluna, String conteudo, String nomeUtilizador){
    	
    	for(FolhadeCalculo folha : this.getFolhascriadasSet()){
    		if(folha.getNomeFolha().equals(nomefolha)){
    			folha.criaCelula(linha, coluna, conteudo);
   	
    		}
    	}	
    }
}
