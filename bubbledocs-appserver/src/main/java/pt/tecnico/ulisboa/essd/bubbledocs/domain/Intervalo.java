package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;

public class Intervalo extends Intervalo_Base {
    
	ArrayList<Celula> _listaCelulas;
	Bubbledocs _bd = Bubbledocs.getInstance();
	FolhadeCalculo _folha;
	
    public Intervalo(Celula upperLeftCell, Celula lowerRightCell, FolhadeCalculo folha) {
    
    	//se a celula pertence ao intervalo mas nao esta no getcelulaset, cria se a celula vazia e adicionas a lista
    	
    	//_folha = upperLeftCell.getFolhadecalculoC();
    	
    	setLowerRightCell(lowerRightCell);
    	setUpperLeftCell(upperLeftCell);
   
    	// calcular celulas
    	int upperRow = upperLeftCell.getLinha();
    	int upperColumn = upperLeftCell.getColuna();
    	int lowerRow = lowerRightCell.getLinha();
    	int lowerColumn = lowerRightCell.getColuna();
    	   	
    	_listaCelulas = new ArrayList<Celula>();
    	for(Celula c : folha.getCelulaSet()){
    		if (c.getLinha() >= upperRow && c.getColuna() >= upperColumn 
 				&& c.getLinha() <= lowerRow && c.getColuna() <= lowerColumn) {
    				_listaCelulas.add(c);
    		}
    	}
    	
    }
    
    
    public ArrayList<Celula> getListaCelulas() {
    	
    	return _listaCelulas;
    	
    }
    
    public ArrayList<Integer> getValorListaCelulas() {		
    	ArrayList<Integer> listaValores = new ArrayList<Integer>();
    	Integer conteudo;
    	
    	
    	for(Celula cell : getListaCelulas()){     		//Partindo do pressuposto que as celulas tem conteudos inteiros
    		if(cell.getConteudo().getValor()== null){
    			return null;
    		}
    		conteudo = cell.getConteudo().getValor();
    		listaValores.add(conteudo);
    	}	
    	return listaValores;
    }
    
	public String toString(){
		String res = getUpperLeftCell().getLinha() + ";" + getUpperLeftCell().getColuna() + ":" + getLowerRightCell().getLinha() + ";" + getLowerRightCell().getColuna();
		
		return res;
	}
    
    
}
