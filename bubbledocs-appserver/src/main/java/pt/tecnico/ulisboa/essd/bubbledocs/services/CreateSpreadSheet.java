package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.dtos.SpreadSheetDto;



public class CreateSpreadSheet extends BubbleDocsService {
	
	private int rows;
	private int columns;
	private String name;
	private String userToken;
	
    private int sheetId;  // id of the new sheet

    public int getSheetId() {
        return sheetId;
    }
    
    

    public CreateSpreadSheet(String userToken, String name, int rows, int columns) {
    	
    	this.userToken = userToken;
    	this.name = name;
    	this.rows = rows;
    	this.columns = columns;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	
    	//obter username a partir do token
    	//obter o LOgin
    	
    	FolhadeCalculo folha = new FolhadeCalculo();
    	
    	folha.setDono(userToken);              //Guardar o userToken
    	folha.setLinhas(rows);
    	folha.setColunas(columns);
    	folha.setNomeFolha(name);
    	
    	Bubbledocs.getInstance().addFolhas(folha);
    		
    }
}
