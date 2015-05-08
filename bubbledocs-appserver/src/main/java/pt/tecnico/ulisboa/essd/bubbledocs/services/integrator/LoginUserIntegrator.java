package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.LoginUserService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;

public class LoginUserIntegrator extends BubbleDocsIntegrator {
	
	
	/*
	 * 		- pensamento para decidir qual dos metodos escolher (remotamente primeiro ou localmente)
	 * 
	 * 
	 * 	|	Remotamente Primeiro  |
	 * 		
	 * 		Fazer login remoto 	
	 * 					1. 		-> chamada remota funciona
	 * 							-> faz-se a local que vai actualizar a password
	 * 					
	 * 					2.		-> caso a chamada remota falhe pela excepcao RemoteInvocation
	 * 							-> faz-se local que vai verificar a password local
	 * 
	 * 					Ter dois dispatchs??
	 * 							um que verifica se a password local é igual à recebida
	 * 							outro que apenas actualiza a password local
	 * 						
	 * 					PROBLEMA -> como destinguir quando o servico local verifica se a password é igual, do caso que verifica se 
	 * 
	 * 							-> valida os parametros remotamente
	 * 						   	-> caso venha remote(nao houve conexao), verifica password local, se falhar transforma em unavailable
	 * 							
	 * 					duvida	-> se user local não existir, não está definido comportamento para esse caso, que seria dos poucos em que funciona remotamente mas locamente nao
	 * 							
	 * 
	 * 
	 * 
	 * 	|	Localmente Primeiro		|
	 * 
	 * 		Fazer login local 
	 * 							
	 * 					1.		-> passa localmente
	 * 							-> chama remotamente e passa
	 * 							
	 * 					2.		-> passa localmente
	 *							-> chama remota e da RemoteExp
	 *							-> como já passou localmente esta tudo bem
	 *					
	 *					3. 		-> chumba localmente
	 *							-> nao faz sentido este caso, pois há um caso em que se chama remotamente e depois se actualiza a pw local
	 *					...		
	 *
	 */							

	
	private LoginUserService _local;
	private String _username;
	private String _password;
	
	
	
	public LoginUserIntegrator(String username, String password) {
		_username = username;
		_password = password;
		
		
	
	
	
	}

	@Override
	protected void dispatch() throws BubbleDocsException {

 		IDRemoteServices remote = new IDRemoteServices();
 		
     	
 		try {
 			remote.loginUser(_username,_password);
 		}catch(RemoteInvocationException e){
 			
 			_local = new LoginUserService(_username, _password);
 			_local.execute();
 			
 			
 			
 			
// 			
// 			//verificacao da pass local
// 			Utilizador utilizador = _bd.getUserOfName(_username);
// 			
// 			if (utilizador == null) {
// 				throw new LoginBubbleDocsException();
// 			}
 			
// 			if(!_bd.checkLocalPassword(utilizador, _password)) {
// 				throw new UnavailableServiceException();
// 			}
 			
// 			Get user info? 
// 						se nao encontrar lanca excepcao?
<<<<<<< Upstream, based on origin/master
 								
=======
// 								
>>>>>>> 7de76dc closes #101, #79, #78, #62
 		}

		

		
		
		
		
	}

	public final String getUserToken() {
    	return _local.getUserToken();
    }
	
}
