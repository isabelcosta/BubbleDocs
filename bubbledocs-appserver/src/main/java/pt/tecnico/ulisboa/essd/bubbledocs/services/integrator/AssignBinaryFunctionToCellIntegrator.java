package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignBinaryFunctionToCellService;

public class AssignBinaryFunctionToCellIntegrator extends BubbleDocsIntegrator{
	
	private String _userToken;
	private AssignBinaryFunctionToCellService _local;
	private String _binaryFunctionToAssign;
	private String _cellToFill;
	private int _folhaId;
    
	public AssignBinaryFunctionToCellIntegrator(String userToken, int docId, String cellId, String binaryFunction) {
		_userToken = userToken;
    	_binaryFunctionToAssign = binaryFunction;
        _cellToFill = cellId;
        _folhaId = docId;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		/*
		 * 
		 *  Instância local
		 * 
		 * 
		 */

//ter atencao as excepcoes ???
		
		_local = new AssignBinaryFunctionToCellService(_userToken, _folhaId, _cellToFill, _binaryFunctionToAssign);
		_local.execute();
		
		/*
		 * 
		 * Nao existe metodo Remoto
		 * 
		 */
		
	}
}
