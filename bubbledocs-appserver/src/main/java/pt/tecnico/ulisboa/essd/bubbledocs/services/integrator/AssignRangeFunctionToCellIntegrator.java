package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignRangeFunctionToCellService;

public class AssignRangeFunctionToCellIntegrator extends BubbleDocsIntegrator {
	
	private AssignRangeFunctionToCellService _local;
	private String _userToken;
    private String _functionToAssign;
    private String _cellToFill;
    private int _folhaId;
    
    public AssignRangeFunctionToCellIntegrator(String userToken, int docId, String idCelula, String rangeFunction) {
    	_userToken=userToken;
    	_functionToAssign = rangeFunction;
        _cellToFill = idCelula;
        _folhaId = docId;
    }
    
    @Override
	protected void dispatch() throws BubbleDocsException {
	
    	
		_local = new AssignRangeFunctionToCellService(_userToken, _folhaId, _cellToFill, _functionToAssign);
		_local.execute();
		
	}

}