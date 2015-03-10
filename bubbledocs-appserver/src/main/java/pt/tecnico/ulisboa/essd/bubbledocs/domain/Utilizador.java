package pt.tecnico.ulisboa.essd.bubbledocs.domain;


public class Utilizador extends Utilizador_Base {
    
    public Utilizador(String nomeUtilizador, String userName, String password) {
        super();
        setNome(nomeUtilizador);
		setUsername(userName);
		setPassword(password);
    }
    
    public void criaFolha(String nome, int linhas, int colunas, int id){
    	
    	FolhadeCalculo folha = new FolhadeCalculo();
    	
    	folha.setDono(nome);
    	folha.setLinhas(linhas);
    	folha.setColunas(colunas);
    	folha.setID(id);
    	
    	this.addFolhascriadas(folha);
    	
    }
    
    
    public void removeFolha(String nome){
    	
    	for(FolhadeCalculo folha : getFolhascriadasSet()){
    		if(folha.getNomeFolha().equals(nome)){
    			folha.delete();
    			this.removeFolhascriadas(folha);
    		}
    	}
    }
    
   
    public void apagaFolhas(){
    	
    	String nome;
    	
    	for(FolhadeCalculo folha : getFolhascriadasSet()){
    		nome = folha.getNomeFolha();
    		this.removeFolha(nome);	
    	}	
    }
}
