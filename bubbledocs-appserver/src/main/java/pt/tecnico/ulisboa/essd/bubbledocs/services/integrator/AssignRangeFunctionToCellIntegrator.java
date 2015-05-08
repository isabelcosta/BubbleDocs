package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidFunctionException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.ProtectedCellException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignBinaryFunctionToCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignRangeFunctionToCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.ValidSessionsService;

public class AssignRangeFunctionToCellIntegrator extends BubbleDocsIntegrator {
	
	private AssignRangeFunctionToCellService _local;
	private String _userToken;
    private String _functionToAssign;
    private String _cellToFill;
    private int _folhaId;
    
    public AssignRangeFunctionToCellIntegrator(String userToken, int docId, String idCelula, String rangeFunction) {
    	this._userToken=userToken;
    	this._functionToAssign = rangeFunction;
        this._cellToFill = idCelula;
        this._folhaId = docId;
    }
    
    @Override
	protected void dispatch() throws BubbleDocsException {
	
    	
		_local = new AssignRangeFunctionToCellService(_userToken, _folhaId, _cellToFill, _functionToAssign);
		_local.execute();
		
		/*
		 * 
		 * Nao existe metodo Remoto
		 * 
		 */
		
	}

}