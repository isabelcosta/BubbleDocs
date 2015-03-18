package pt.tecnico.ulisboa.essd.bubbledocs.service.dtos;

public class SpreadSheetDto implements java.io.Serializable{
	
	
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