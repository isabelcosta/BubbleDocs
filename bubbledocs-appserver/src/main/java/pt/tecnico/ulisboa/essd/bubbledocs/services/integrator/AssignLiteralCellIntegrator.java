package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignLiteralCellService;

public class AssignLiteralCellIntegrator extends BubbleDocsIntegrator{
	
	private String _userToken;
	private AssignLiteralCellService _local;
	private String _literalToAssign;
	private String _cellToFill;
	private int _folhaId;
    
	public AssignLiteralCellIntegrator(String userToken, int docId, String cellId, String literal) {
		this._userToken = userToken;
    	this._literalToAssign = literal;
        this._cellToFill = cellId;
        this._folhaId = docId;
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
		
		_local = new AssignLiteralCellService(_userToken, _folhaId, _cellToFill, _literalToAssign);
		_local.execute();
		
		/*
		 * 
		 * Nao existe Instância Remota
		 * 
		 */
		
	}
}
