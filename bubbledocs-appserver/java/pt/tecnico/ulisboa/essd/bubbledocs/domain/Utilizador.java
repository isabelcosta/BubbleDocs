package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;


public class Utilizador extends Utilizador_Base {
    
    public Utilizador(String nomeUtilizador, String userName, String password) {
        super();
        setNome(nomeUtilizador);
        setUsername(userName);
        setPassword(password);
    }
    
    public void criaFolha(String nomeFolha,String nome, int linhas, int colunas, int id){
        
        FolhadeCalculo folha = new FolhadeCalculo();
        
        folha.setNomeFolha(nomeFolha);
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
    
    
    /* 
     * 
     * Atribuir e retirar permissoes nas suas folhas ou nas que pode escrever 
     * 
     * NOTA: permissao so recebe "leitura" ou "escrita"
     * 
     * */	
    public void darPermissoes(String permissao, String nomeUtilizadorAdicionar, String nomeFolha) {
    	
   
    	Utilizador utilizador=null;
    	for(Utilizador u : FenixFramework.getDomainRoot().getUtilizadoresSet()){
    		if (u.getNome().equals(nomeUtilizadorAdicionar))
    			utilizador=u;
    	}
    	
    	for(FolhadeCalculo folha : this.getFolhascriadasSet()){
    		if (folha.getNomeFolha().equals(nomeFolha)){
    			
    				switch(permissao){
    				case("escrita"):
    					folha.addUtilizadores_e(utilizador);
    				case("leitua"):
    					folha.addUtilizadores_l(utilizador);
    				}
    			
    		}
    	}
    
 	}
    
    
    public void retirarPermissoes(String permissao, String nomeUtilizadorAdicionar, String nomeFolha) {
    	
    	   
    	Utilizador utilizador=null;
    	for(Utilizador u : FenixFramework.getDomainRoot().getUtilizadoresSet()){
    		if (u.getNome().equals(nomeUtilizadorAdicionar))
    			utilizador=u;
    	}
    	
    	for(FolhadeCalculo folha : this.getFolhascriadasSet()){
    		if (folha.getNomeFolha().equals(nomeFolha)){
    			
    				switch(permissao){
    				case("escrita"):
    					folha.removeUtilizadores_e(utilizador);
    				case("leitua"):
    					folha.removeUtilizadores_l(utilizador);
    				}
    			
    		}
    	}
    }
    
    
}
    		
    		
    	
    
