package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;

public abstract class ReadAndWritePermissionsService extends ValidSessionsService {
	
	protected Bubbledocs _bd = getBubbleDocs();
	protected String _userToken;
	protected int _docId;
	protected boolean _flag;
	
	public ReadAndWritePermissionsService(String userToken, int docId, boolean flag) {
		
		super(userToken);
		_userToken = userToken;
 	    _docId = docId;
 	    _flag = flag;
 	   
	}

	protected void dispatch_session() throws BubbleDocsException {
		
		if(_flag){ //pode ler
			_bd.canWrite(_userToken,_docId);
			
		} else { //pode escrever ou ler
			
			/* e é isso
			 * como o canWrite lanca uma excecao quando o user nao tem premissao de escrita, e ainda queremos testar a de leitura, temos que fazer catch dessa excecao
			 * para que possamos testar a leitura, so caso falhe a leitura é que a excessao entao é de facto lancada, dai so ter 1 try catch
			 */
			try {
				_bd.canWrite(_userToken,_docId);
				// caso nao lance excessao quer dizer que pode ler, logo saimos do metodo
				return;
			}
			catch (UnauthorizedOperationException ex) {
			}

			_bd.canRead(_userToken, _docId);
				
			}		
		dispatch_read_and_write();
		}
		
	
	protected abstract void dispatch_read_and_write() throws BubbleDocsException;
}
