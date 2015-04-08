package pt.tecnico.ulisboa.essd.bubbledocs.services.dtos;

public class SpreadSheetDto implements java.io.Serializable{
	

	private static final long serialVersionUID = 1L;
	
	private int linhas;
    private int colunas;
    private String nomeFolha;    
    public SpreadSheetDto(int linhas, int colunas, String nomeFolha){
    	
    	this.linhas = linhas;
    	this.colunas = colunas; 
    	this.nomeFolha = nomeFolha;
    }
    
    public int getLinhas(){
    	return linhas;
    }
    
    public int getColunas(){
    	return colunas;
    }
	
    public String getNome(){
    	return nomeFolha;
    }
	
}