package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import java.util.Random;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.IdFolhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;


public class Bubbledocs extends Bubbledocs_Base {
    
	
	public static Bubbledocs getInstance() {
		Bubbledocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		
		if (bd==null){
			bd = new Bubbledocs();
		}
		
		boolean existe = false;
		for (Utilizador u : bd.getUtilizadoresSet()){
			if(u.getUsername().equals("root"))
				existe = true;
		}
		
		if (!existe){
			Utilizador user =new Utilizador("Super User", "root", "root");
			bd.addUtilizadores(user);
		}
	
		return bd;
	}
	
    private Bubbledocs() {
    	FenixFramework.getDomainRoot().setBubbledocs(this);
    }
    
    public void criaFolha(String nomeFolha, String username, int linhas, int colunas){
        
        FolhadeCalculo folha = new FolhadeCalculo(nomeFolha, username, linhas, colunas);
        
        addFolhas(folha);
    }
    
    public void eliminaFolha(int idFolha){
        
        for(FolhadeCalculo folha : getFolhasSet()){
            if(folha.getID() == idFolha){
                folha.delete();
                removeFolhas(folha);   //retira da lista
            }
        }
    }
    
   
    public void apagaFolhas(String nomeUtilizador){
        
        for(FolhadeCalculo folha : getFolhasSet()){
            if (nomeUtilizador.equals(folha.getDono()))
            	eliminaFolha(folha.getID()); 
        }   
    }


    /* 
     * 
     * Atribuir e retirar permissoes nas suas folhas ou nas que pode escrever 
     * 
     * NOTA: permissao so recebe "leitura" ou "escrita"
     * 
     * */	
    public void darPermissoes(String permissao, String utilizadorDador, String utilizadorReceptor, int folhaID)  {
    	
    	// chama utilizador
    	Utilizador utilizador=null;
    	for(Utilizador u : getUtilizadoresSet()){
    		if (u.getUsername().equals(utilizadorReceptor))
    			utilizador=u;
    	}
    	
    	// chama folha
    	FolhadeCalculo folha=null;
    	for(FolhadeCalculo f : getFolhasSet()){
    		if (f.getID() == folhaID)
    			folha=f;
    	}
    	
    	if (folha.getID() == folhaID && 
			(utilizadorDador.equals(folha.getDono()) || folha.podeEscrever(utilizadorDador))){
																//LanÃ§a excepcao
			
				switch(permissao){
    				case("escrita"):
    					folha.addUtilizadores_e(utilizador);
    				case("leitura"):
    					folha.addUtilizadores_l(utilizador);
				}
    	}
    	
    
 	}
    
    public void retirarPermissoes(String permissao, String utilizadorRetira, String utilizadorReceptor, int folhaID) {
    	
    	// chama utilizador
    	Utilizador utilizador=null;
    	for(Utilizador u : getUtilizadoresSet()){
    		if (u.getUsername().equals(utilizadorReceptor))
    			utilizador=u;
    	}
    	
    	// chama folha
    	FolhadeCalculo folha=null;
    	for(FolhadeCalculo f : getFolhasSet()){
    		if (f.getID() == folhaID)
    			folha=f;
    	}
    	
    	if (folha.getID() == folhaID && 
			(utilizadorRetira.equals(folha.getDono()) || folha.podeEscrever(utilizadorRetira))){
																//LanÃ§a excepcao
			
				switch(permissao){
    				case("escrita"):
    					folha.removeUtilizadores_e(utilizador);
    				case("leitura"):
    					folha.removeUtilizadores_l(utilizador);
				}
    	}	
    	

 	}
    
    
    public Boolean validSession(String token) throws UserNotInSessionException, InvalidTokenException{
    	
    	if (token == null || token == ""){
    		throw new InvalidTokenException("Token is invalid");
    	}
    	
    	for(Token token2 : getTokensSet()){
    		if(token2.getToken().equals(token)){
    			int minutes = Minutes.minutesBetween(token2.getTime(), new LocalTime()).getMinutes();
    			if(minutes >= 120){
    				getTokensSet().remove(token2);
    				throw new UserNotInSessionException("Session for user " + token.substring(0, token.length()-1) + " is invalid" );
    			}else{
    				return true;
    			}
    		}
    	}
    	throw new UserNotInSessionException("Session for user " + token.substring(0, token.length()-1) + " is invalid" );
    }
    
    
    public Boolean isRoot(String token) throws UnauthorizedOperationException{
    	for(Token tokenObjecto : getTokensSet()){
    		if(tokenObjecto.getUsername().equals("root") && tokenObjecto.getToken().equals(token)){
    			return true;
    		}
    	}
    	
    	throw new UnauthorizedOperationException("Session for user " + token.substring(0, token.length()-1) + " is not root");
    	
    }
    
    
    public Boolean emptyUsername(String newUsername) throws EmptyUsernameException{
    	
	    if(newUsername == "" || newUsername == null){
			throw new EmptyUsernameException("User empty!");
		}
	    return true;
    }

    public Boolean duplicatedUsername(String newUsername) throws DuplicateUsernameException{
    	
	    for(Utilizador user : getUtilizadoresSet()){
			if(user.getUsername().equals(newUsername)){
				throw new DuplicateUsernameException(newUsername);
			}
	    }
	    return true;
    }

    public String getUsernameOfToken(String token) throws UnauthorizedOperationException{
    	for(Token tokenObjecto : getTokensSet()){
    		if (tokenObjecto.getToken().equals(token))
    			return tokenObjecto.getUsername();
    	}
    	throw new UnauthorizedOperationException();
    }

    public Utilizador getUserFromToken(String token) throws UnauthorizedOperationException{
    	for(Token tokenObjecto : getTokensSet()){
    		if (tokenObjecto.getToken().equals(token)){
    	    	for(Utilizador userObjecto : getUtilizadoresSet()){
    	    		if (userObjecto.getUsername().equals(tokenObjecto.getUsername()))    			
    	    			return userObjecto;
    	    	}
    		}
    	}
    	throw new UnauthorizedOperationException();
    }
    
    public Integer getIdOfFolha(String folha) throws SpreadSheetDoesNotExistException{
    	for(FolhadeCalculo f : getFolhasSet()){
			if (f.getNomeFolha().equals(folha))
				return f.getID();		
		}
    	throw new SpreadSheetDoesNotExistException();
    }
    
    public Utilizador getUserOfName(String name) throws LoginBubbleDocsException{
    	for(Utilizador user : getUtilizadoresSet()){
			if(user.getUsername().equals(name)){
				return user;
			}
    	}
    	throw new LoginBubbleDocsException();
    }
    
    
    public Boolean existsUser(String name) throws UserNotInSessionException{
    	for(Utilizador user : getUtilizadoresSet()){
			if(user.getUsername().equals(name)){
				return true;
			}
    	}
    	throw new UserNotInSessionException(name);
    }

    public FolhadeCalculo getFolhaOfId(Integer id) throws IdFolhaInvalidoException, SpreadSheetDoesNotExistException{
    	
    	if(id < 0 || id == null){
			throw new IdFolhaInvalidoException();
		}
    	
    	for( FolhadeCalculo folhaIter : getFolhasSet()  ){
    		if(folhaIter.getID() == id){
    			return folhaIter;
    		}
    	}
    	throw new SpreadSheetDoesNotExistException();
    }
    
    
    public org.jdom2.Document exportSheet(FolhadeCalculo folha){
    	org.jdom2.Document jdomDoc = new org.jdom2.Document();
    	jdomDoc.setRootElement(folha.exportToXML());
    	return jdomDoc;
    }
    
    public int generateToken(){		//	talvez passar para a bubble docs de forma a separa a lÃ³gica de negÃ³cio
        Random rand = new Random(); 
        int intToken = rand.nextInt(10);
        return intToken;
    }
    
    public final void refreshTokenTotal (String token, String username) {
    	for(Token tokenObject : Bubbledocs.getInstance().getTokensSet()){
    		if(tokenObject.getUsername().equals(username)){
    			tokenObject.setTime(new LocalTime());
    			tokenObject.setToken(token);
    		}
    	}
    }
    
    
    public Boolean checkLocalPassword(Utilizador utilizador, String password){
    	if (utilizador.getPassword()==null) {
    		return false;
    	}
    	return utilizador.getPassword().equals(password);
    }

	public void userSetPasswordNull(String _userToken) {
		getUserFromToken(_userToken).setPassword(null);;
	}

	public void setUserPassword(String _username, String _password) {
		getUserOfName(_username).setPassword(_password);
	}
}
