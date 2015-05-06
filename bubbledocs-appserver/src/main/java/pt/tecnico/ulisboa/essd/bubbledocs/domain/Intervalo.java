package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import java.util.ArrayList;

public class Intervalo extends Intervalo_Base {
    
	ArrayList<Celula> _listaCelulas;
	Bubbledocs _bd = Bubbledocs.getInstance();
	FolhadeCalculo _folha;
	
    public Intervalo(Celula upperLeftCell, Celula lowerRightCell) {
    
    	_folha = upperLeftCell.getFolhadecalculoC();
    	
    	setLowerRightCell(lowerRightCell);
    	setUpperLeftCell(upperLeftCell);
   
    	// calcular celulas
    	int upperRow = upperLeftCell.getLinha();
    	int upperColumn = upperLeftCell.getColuna();
    	int lowerRow = lowerRightCell.getLinha();
    	int lowerColumn = lowerRightCell.getColuna();
    	   	
    	
    	for(Celula c : _folha.getCelulaSet()){
    		if (c.getLinha() >= upperRow && c.getColuna() >= upperColumn 
 				&& c.getLinha() <= lowerRow && c.getColuna() <= lowerColumn) {
    				_listaCelulas.add(c);
    		}
    	}
    	
    }
    
    
    public ArrayList<Celula> getListaCelulas() {
    	
    	return _listaCelulas;
    	
    }
    
}
