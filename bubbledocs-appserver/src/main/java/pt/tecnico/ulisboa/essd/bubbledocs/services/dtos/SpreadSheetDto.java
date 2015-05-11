package pt.tecnico.ulisboa.essd.bubbledocs.services.dtos;

public class SpreadSheetDto implements java.io.Serializable{
	

	private static final long serialVersionUID = 1L;
	
	private int _linhas;
    private int _colunas;
    private String _nomeFolha;    
    
    public SpreadSheetDto(int linhas, int colunas, String nomeFolha){
    	
    	_linhas = linhas;
    	_colunas = colunas; 
    	_nomeFolha = nomeFolha;
    }
    
    public int getLinhas(){
    	return _linhas;
    }
    
    public int getColunas(){
    	return _colunas;
    }
	
    public String getNome(){
    	return _nomeFolha;
    }
    
}