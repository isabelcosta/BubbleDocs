package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignReferenceCellService;

public class AssignReferenceCellIntegrator extends BubbleDocsIntegrator{
	
	private String _userToken;
	private int _docId;
    private String _idCelula;
    private String _referencia;
	private AssignReferenceCellService _local;
	
	public AssignReferenceCellIntegrator(String userToken, int docId, String idCelula, String referencia) {
		_userToken = userToken;
		_docId = docId;
		_idCelula = idCelula;
		_referencia = referencia;  
		
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		/*Instancia local*/
		
		_local = new AssignReferenceCellService(_userToken, _docId, _idCelula, _referencia);
		_local.execute();
		
	}
	
}
