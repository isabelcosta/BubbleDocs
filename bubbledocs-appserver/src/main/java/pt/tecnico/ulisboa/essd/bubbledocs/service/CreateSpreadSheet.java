package pt.tecnico.ulisboa.essd.bubbledocs.service;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.service.dtos.SpreadSheetDto;

// add needed import declarations

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
    	
    	FolhadeCalculo folha = new FolhadeCalculo();
    	
    	folha.setDono(userToken);                        //Guardar o userToken
    	folha.setLinhas(rows);
    	folha.setColunas(columns);
    	folha.setNomeFolha(name);
    	
    	
    	for(Utilizador user : FenixFramework.getDomainRoot().getUtilizadoresSet()){
    		
    		if(user.getUsername().equals(userToken)){
    			
    			
    			
    			
    	
    		}	
    	}	
    }
}
