package pt.tecnico.ulisboa.essd.bubbledocs.domain;

import java.util.HashMap;
import java.util.Random;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.IdFolhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.CreateSpreadSheetService;


public class Bubbledocs extends Bubbledocs_Base {
    
	private static HashMap<Integer,String> _folhasExportadas = new HashMap<Integer, String>();
	
	public static Bubbledocs getInstance() {
		Bubbledocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		
		if (bd==null){
			_folhasExportadas = new HashMap<Integer, String>();
			bd = new Bubbledocs();
			Utilizador user =new Utilizador("Super User", "root", "root");
			bd.addUtilizadores(user);
		}

		return bd;
	}
	
    private Bubbledocs() {
    	FenixFramework.getDomainRoot().setBubbledocs(this);
    }
    
    private void addFolhasExportadasPriv(Integer id, String nome) {
    	
    	Bubbledocs.getInstance()._folhasExportadas.put(id, nome);
    }
    
    private HashMap<Integer, String> getFolhasExportadasPriv() {
    	return Bubbledocs.getInstance()._folhasExportadas;
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
    
    public void addFolhaExportada(Integer id, String nome) {
    	
    	if (id== null || id < 0) {
    		throw new IdFolhaInvalidoException();
    	}
    	Bubbledocs.getInstance().addFolhasExportadasPriv(id, nome);
    }
    
    public HashMap<Integer, String> getFolhasExportadas() {
    	return Bubbledocs.getInstance().getFolhasExportadasPriv();
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
    	FolhadeCalculo folha= getFolhaOfId(folhaID);
    
    	
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

    public String getUsernameOfToken(String token) throws UserNotInSessionException{
    	if(token == null || token == ""){
    		throw new InvalidTokenException();
    	}
    	for(Token tokenObjecto : getTokensSet()){
    		if (tokenObjecto.getToken().equals(token))
    			return tokenObjecto.getUsername();
    	}
    	throw new UserNotInSessionException(token);
    }

    public Utilizador getUserFromToken(String token) throws UserNotInSessionException{
    	if(token == null || token == ""){
    		throw new InvalidTokenException();
    	}
    	for(Token tokenObjecto : getTokensSet()){
    		if (tokenObjecto.getToken().equals(token)){
    	    	for(Utilizador userObjecto : getUtilizadoresSet()){
    	    		if (userObjecto.getUsername().equals(tokenObjecto.getUsername()))    			
    	    			return userObjecto;
    	    	}
    		}
    	}
    	throw new UserNotInSessionException(token);
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
    
	 public String getExportedSpreadsheetName4Id(Integer id) throws IdFolhaInvalidoException, SpreadSheetDoesNotExistException{
	    	
	    	if(id < 0 || id == null){
				throw new IdFolhaInvalidoException();
			}
	    	
	    	String nomeFolha = getFolhasExportadas().get(id);
	    	
	    	if (nomeFolha != null) {
	    		return nomeFolha;
	    	}
	    	
	    	throw new IdFolhaInvalidoException();
	    }
    

	 public String getSpreadsheetName4Id(Integer id) throws IdFolhaInvalidoException, SpreadSheetDoesNotExistException{
	    	
	    	if(id < 0 || id == null){
				throw new IdFolhaInvalidoException();
			}

	     	
	    	for( FolhadeCalculo folhaIter : getFolhasSet()  ){
	    		if(folhaIter.getID() == id){
	    			return folhaIter.getNomeFolha();
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

	public void invalidateUserPassword(String userToken) {
		getUserFromToken(userToken).setPassword(null);
	}

	public void setUserPassword(String username, String password) {
		getUserOfName(username).setPassword(password);
	}
	
	public String getUserPassword(String userToken){
		return getUserFromToken(userToken).getPassword();
	}

	public Utilizador getUserfromUsername(String username) throws LoginBubbleDocsException{
		
		//Verifica primeiro se é vazio
		emptyUsername(username);
		
		for(Utilizador user : getUtilizadoresSet()){
			if(user.getUsername().equals(username)){
				return user;
			}
    	}
		throw new LoginBubbleDocsException(username);
	}
	
	/*
	 *  Funcoes que permitem verificar nos servicos a permissao de leitura e escrita
	 * 		sem ter de aceder directamente a folha
	 */
	public boolean canRead(String token, int docID) throws UnauthorizedOperationException{
		
		
		FolhadeCalculo folha = getFolhaOfId(docID);
		String username = getUsernameOfToken(token);
		
		return folha.podeLer(username);	
	}
	
	public boolean canWrite(String token, int docID) throws UnauthorizedOperationException{
		
		FolhadeCalculo folha = getFolhaOfId(docID);
		String username = getUsernameOfToken(token);
		return folha.podeEscrever(username);	

	}
 
	public String renewUserToken(String username, String password) {
		String token = "";
		
		try{
			token = getUserToken(username);			
		} catch (UserNotInSessionException e ) {
			// para o caso de ser a primeira vez que é chamada a renewToken
		}
		
		//ciclo que impede a repeticao do novo token com o token antigo
		String temp;
		do {
			temp = username + generateToken();
		} while (temp.equals(token));
		token  = temp;
		refreshTokenTotal(token, username);
		addTokens(new Token(username, token));
		
		return token;
	}
	
	@Atomic
    public Integer recoverFromBackup(org.jdom2.Document jdomDoc, String userToken) {
    	String nomeFolha = jdomDoc.getRootElement().getAttributeValue("nome");
    	int linhas = Integer.parseInt(jdomDoc.getRootElement().getAttributeValue("linhas"));
    	int colunas = Integer.parseInt(jdomDoc.getRootElement().getAttributeValue("colunas"));
    	String data = jdomDoc.getRootElement().getAttributeValue("data");
    	Integer sheetId = null;
    	  	
    	
    	//caso nao tenha encontrado a folha cria uma nova
 		CreateSpreadSheetService serviceFolha = new CreateSpreadSheetService(userToken, nomeFolha, linhas, colunas);
 		serviceFolha.execute();
    	
    	for (FolhadeCalculo folha : getFolhasSet())
    		if(folha.getID() == serviceFolha.getResult()){
    			
    			folha.setDataCriacao(new LocalDate(data));
	    		folha.importFromXML(jdomDoc.getRootElement());
	    		sheetId = folha.getID();
    		}
    	return sheetId;
    }
	
	public void addFolhaExportadas (Integer id) {
		String nomeFolha = getSpreadsheetName4Id(id);
		
		addFolhaExportada(id, nomeFolha);
	}
	
//	public Boolean checkFolhaExportada4User (Integer id, String token) {
//		HashMap<Integer, String> listaFolhasExportadas = getUserFromToken(token).getFolhasExportadas();
//		return listaFolhasExportadas.containsKey(id);
//	}
	/*
	public Boolean wasExported (Integer id) {
		/*
		 *  procura nos users se alguem exportou aquela folha
		 *  para ver se o id existe
		 * 
		for (Utilizador user : getUtilizadoresSet()) {
			if (user.getFolhasExportadas().containsKey(id)) {
				return true;
			}
		}
		return false;
	}
	 */
	public String getUserToken(String username) {
		
		if(username == null || username == ""){
    		throw new InvalidUsernameException();
    	}
    	for(Token tokenObjecto : getTokensSet()){
    		if (tokenObjecto.getUsername().equals(username))
    			return tokenObjecto.getToken();
    	}
		
		throw new UserNotInSessionException(username);
	}
}
