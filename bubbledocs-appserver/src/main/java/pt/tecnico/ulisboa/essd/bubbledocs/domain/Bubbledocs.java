package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;


public class Bubbledocs extends Bubbledocs_Base {
    
	
	public static Bubbledocs getInstance() {
		Bubbledocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		
		if (bd==null)
			bd = new Bubbledocs();
		
		return bd;
	}
	
    private Bubbledocs() {
    	FenixFramework.getDomainRoot().setBubbledocs(this);
    }
    
    public void criaFolha(String nomeFolha, String username, int linhas, int colunas){
        
        FolhadeCalculo folha = new FolhadeCalculo();
        
        folha.setNomeFolha(nomeFolha);
        folha.setDono(username);
        folha.setLinhas(linhas);
        folha.setColunas(colunas);
        int id = getProxID();
        folha.setID(id++);
        
        //actualiza ID
        setProxID(id++);
        
        this.addFolhas(folha);
        
    }
    
    public void eliminaFolha(String nomeFolha){
        
        for(FolhadeCalculo folha : getFolhasSet()){
            if(folha.getNomeFolha().equals(nomeFolha)){
                folha.delete();
                removeFolhas(folha);   //retira da lista
            }
        }
    }
    
   
    public void apagaFolhas(String nomeUtilizador){
        
        for(FolhadeCalculo folha : getFolhasSet()){
            if (nomeUtilizador.equals(folha.getDono()))
            	eliminaFolha(folha.getDono()); 
        }   
    }


    /* 
     * 
     * Atribuir e retirar permissoes nas suas folhas ou nas que pode escrever 
     * 
     * NOTA: permissao so recebe "leitura" ou "escrita"
     * 
     * */	
    public void darPermissoes(String permissao, String utilizadorDador, String utilizadorReceptor, String nomeFolha) {
    	
    	// chama utilizador
    	Utilizador utilizador=null;
    	for(Utilizador u : getUtilizadoresSet()){
    		if (u.getNome().equals(utilizadorReceptor))
    			utilizador=u;
    	}
    	
    	// chama folha
    	FolhadeCalculo folha=null;
    	for(FolhadeCalculo f : getFolhasSet()){
    		if (f.getNomeFolha().equals(nomeFolha))
    			folha=f;
    	}
    	
    	if (folha.getNomeFolha().equals(nomeFolha) && 
			(utilizadorDador.equals(folha.getDono()) || folha.podeEscrever(utilizadorDador))){
																//Lança excepcao
			
				switch(permissao){
    				case("escrita"):
    					folha.addUtilizadores_e(utilizador);
    				case("leitura"):
    					folha.addUtilizadores_l(utilizador);
				}
    	}	
    
 	}
    
    public void retirarPermissoes(String permissao, String utilizadorRetira, String utilizadorReceptor, String nomeFolha) {
    	
    	// chama utilizador
    	Utilizador utilizador=null;
    	for(Utilizador u : getUtilizadoresSet()){
    		if (u.getNome().equals(utilizadorReceptor))
    			utilizador=u;
    	}
    	
    	// chama folha
    	FolhadeCalculo folha=null;
    	for(FolhadeCalculo f : getFolhasSet()){
    		if (f.getNomeFolha().equals(nomeFolha))
    			folha=f;
    	}
    	
    	if (folha.getNomeFolha().equals(nomeFolha) && 
			(utilizadorRetira.equals(folha.getDono()) || folha.podeEscrever(utilizadorRetira))){
																//Lança excepcao
			
				switch(permissao){
    				case("escrita"):
    					folha.removeUtilizadores_e(utilizador);
    				case("leitura"):
    					folha.removeUtilizadores_l(utilizador);
				}
    	}	
    

 	}
    

}
