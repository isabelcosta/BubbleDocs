package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.StrictExpectations;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.joda.time.LocalDate;
import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.IdFolhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.CreateSpreadSheet;
import pt.tecnico.ulisboa.essd.bubbledocs.services.ExportDocumentService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.LoginUser;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.StoreRemoteServices;

public class ExportDocumentTest extends BubbleDocsServiceTest{
	
	// the tokens
    private String root;
    private String ars;

    private static final String USERNAME = "pff";
    private static final String PASSWORD = "sub";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String NOT_REFERENCE = "noReference";
    private static final String REFERENCE = "=5;7";
    private static final String SPREADSHEET_NAME = "Notas ES";
    private static int FOLHA_ID;
    private static int FOLHA_ID_SEM_PERMISSAO;
    private static final String CELL_ID = "4;2";
    private static String USER_TOKEN;
    private static String USER_TOKEN_PODE_ESCREVER;
    private static String USER_TOKEN_NO_ACCESS;
    private static String USER_TOKEN_NOT_IN_SESSION;
    private static String EMPTY_TOKEN;
    private static int FOLHA_ID_INEXISTENT = 100;
    private static int FOLHA_ID_NEGATIVE = -100;
    
    private static String USERNAME1;
    private static String FOLHA_TESTE;
    private static int FOLHA_TESTE_LINHAS;
    private static int FOLHA_TESTE_COLUNAS;
    private static String FOLHA_TESTE_DATA;
    private static String USER_TOKEN_LEITURA;
    private static String USER_TOKEN_ESCRITA;
    
    
	public void populate4Test() {

		//Limpa a base de dados
    	unPopulate4Test();
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	//Cria users
		Utilizador user1 = createUser("ter", "te#", "Teresa Palhoto");
    	Utilizador user2 = createUser("mig", "mi#", "Miguel Torrado");
    	Utilizador user3 = createUser("ree", "re#", "Isabel Costa");
    	Utilizador user4 = createUser("faa", "fa#", "InÃªs Garcia");
    	Utilizador user5 = createUser("doo", "do#", "FÃ¡bio Pedro");
    	    
    	//Faz o login dos users
    	
    	USER_TOKEN = addUserToSession("ter");
    	USER_TOKEN_NO_ACCESS = addUserToSession("mig");
    	USER_TOKEN_LEITURA = addUserToSession("faa");
    	USER_TOKEN_ESCRITA = addUserToSession("doo");
    	
    	EMPTY_TOKEN = "";
    	USER_TOKEN_NOT_IN_SESSION = addUserToSession("ree");
    	turnTokenInvalid(USER_TOKEN_NOT_IN_SESSION);
    	
    	//cria duas folhas
    	FolhadeCalculo folha1 = createSpreadSheet(user1, "terFolha", 20, 30);
		FolhadeCalculo folha2 = createSpreadSheet(user2, "migFolha", 40, 11);
		FolhadeCalculo folhaTeste = createSpreadSheet(user1, "terFolha", 20, 30);
		
		//Preenche a folha (folha1) do user "ab"
		FOLHA_ID = folha1.getID();
		
		String conteudoLiteral = "4";
		folha1.modificarCelula(3,2,conteudoLiteral);
		
		String conteudoAdd = "=ADD(2,3;2)";
		folha1.modificarCelula(5,7,conteudoAdd);
		
		String conteudoReferencia = "=3;2";
		folha1.modificarCelula(1,1,conteudoReferencia);
		
		
    	//SUCESSDONO
    	USERNAME1 = user1.getUsername();
    	FOLHA_TESTE = folhaTeste.getNomeFolha();
    	FOLHA_TESTE_LINHAS = folhaTeste.getLinhas();
    	FOLHA_TESTE_COLUNAS = folhaTeste.getColunas();
    	FOLHA_TESTE_DATA = folhaTeste.getDataCriacao().toString();
    	
    	
    	//da "ab" da permissoes de escrita a "pi" para preencher a sua folha
    	bd.darPermissoes("escrita", "ter", "doo", FOLHA_ID);
    	bd.darPermissoes("leitura", "ter", "faa", FOLHA_ID);
    	
    }
	
	
	 @Mocked StoreRemoteServices remote;
	 
	//1
	@Test
	public void successDonoExport () {
		
		
		new StrictExpectations() {
	 		   
    		{
    			remote = new StoreRemoteServices();
    			remote.storeDocument("ter", "terFolha",(byte[]) any);
		    }
		};
		
		
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, USER_TOKEN);
		exportDocument.execute();
		
		org.jdom2.Document doc = null;
		
		try {
			doc = byteToJdomDoc(exportDocument);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 
		
		String donoFolha = doc.getRootElement().getAttributeValue("dono");
		assertEquals(USERNAME1, donoFolha);
		
		String nomeFolha = doc.getRootElement().getAttributeValue("nome");
		assertEquals(FOLHA_TESTE, nomeFolha);
		
		int linhas = Integer.parseInt(doc.getRootElement().getAttributeValue("linhas"));
		assertEquals(FOLHA_TESTE_LINHAS, linhas);
		
		int colunas = Integer.parseInt(doc.getRootElement().getAttributeValue("colunas"));
		assertEquals(FOLHA_TESTE_COLUNAS, colunas);
		
		int id = Integer.parseInt(doc.getRootElement().getAttributeValue("id"));
		assertEquals(FOLHA_ID, id);
		
		String data = doc.getRootElement().getAttributeValue("data");
		assertEquals(FOLHA_TESTE_DATA, data);
		
		FolhadeCalculo folha = getSpreadSheet(FOLHA_TESTE);
		FolhadeCalculo folha1 = getSpreadSheet("terFolha");
		
		assertEquals(folha.getCelulaSet().size(), folha1.getCelulaSet().size());
		
		folha.importFromXML(doc.getRootElement());
		
		int cellCount = 0;
		for(Celula cell : folha.getCelulaSet()){
			for(Celula cell2 : folha1.getCelulaSet()){
				if(cell.getLinha() == cell2.getLinha() && cell.getColuna() == cell2.getColuna()){
					assertEquals(cell.getValor(), cell2.getValor());
					assertEquals(cell.getConteudo().getClass(), cell2.getConteudo().getClass());
					cellCount++;					
				}
			}
			
		}
		assertEquals(folha.getCelulaSet().size(), cellCount/2); // Aqui tens a certeza que passaste por todas as celulas
																// porque viste o numero de celulas iguais
																// igual ao numero de celulas na folha
		
	}
	//2
	@Test
	public void successUserReadExport () {
		
		new StrictExpectations() {
    		{
    			remote = new StoreRemoteServices();
    			remote.storeDocument("faa", "terFolha",(byte[]) any);
		    }
		};
		
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, USER_TOKEN_LEITURA);
		exportDocument.execute();
		
		org.jdom2.Document doc = null;
		
		try {
			doc = byteToJdomDoc(exportDocument);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 
		String donoFolha = doc.getRootElement().getAttributeValue("dono");
		assertEquals(USERNAME1, donoFolha);
		
		String nomeFolha = doc.getRootElement().getAttributeValue("nome");
		assertEquals(FOLHA_TESTE, nomeFolha);
		
		int linhas = Integer.parseInt(doc.getRootElement().getAttributeValue("linhas"));
		assertEquals(FOLHA_TESTE_LINHAS, linhas);
		
		int colunas = Integer.parseInt(doc.getRootElement().getAttributeValue("colunas"));
		assertEquals(FOLHA_TESTE_COLUNAS, colunas);
		
		int id = Integer.parseInt(doc.getRootElement().getAttributeValue("id"));
		assertEquals(FOLHA_ID, id);
		
		String data = doc.getRootElement().getAttributeValue("data");
		assertEquals(FOLHA_TESTE_DATA, data);
		
		FolhadeCalculo folha = getSpreadSheet(FOLHA_TESTE);
		FolhadeCalculo folha1 = getSpreadSheet("terFolha");
		
		assertEquals(folha.getCelulaSet().size(), folha1.getCelulaSet().size());
		
		folha.importFromXML(doc.getRootElement());
		
		int cellCount = 0;
		for(Celula cell : folha.getCelulaSet()){
			for(Celula cell2 : folha1.getCelulaSet()){
				if(cell.getLinha() == cell2.getLinha() && cell.getColuna() == cell2.getColuna()){
					assertEquals(cell.getValor(), cell2.getValor());
					assertEquals(cell.getConteudo().getClass(), cell2.getConteudo().getClass());
					cellCount++;					
				}
			}
			
		}
		assertEquals(folha.getCelulaSet().size(), cellCount/2); // Aqui tens a certeza que passaste por todas as celulas
																// porque viste o numero de celulas iguais
																// igual ao numero de celulas na folha
		
	}
	//3
	@Test
	public void successUserWriteExport () {
		new StrictExpectations() {
	 		   
    		{
    			remote = new StoreRemoteServices();
    			remote.storeDocument("doo", "terFolha",(byte[]) any);
		    }
		};
		
		
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, USER_TOKEN_ESCRITA);
		exportDocument.execute();
		
		org.jdom2.Document doc = null;
		
		try {
			doc = byteToJdomDoc(exportDocument);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 
		String donoFolha = doc.getRootElement().getAttributeValue("dono");
		assertEquals(USERNAME1, donoFolha);
		
		String nomeFolha = doc.getRootElement().getAttributeValue("nome");
		assertEquals(FOLHA_TESTE, nomeFolha);
		
		int linhas = Integer.parseInt(doc.getRootElement().getAttributeValue("linhas"));
		assertEquals(FOLHA_TESTE_LINHAS, linhas);
		
		int colunas = Integer.parseInt(doc.getRootElement().getAttributeValue("colunas"));
		assertEquals(FOLHA_TESTE_COLUNAS, colunas);
		
		int id = Integer.parseInt(doc.getRootElement().getAttributeValue("id"));
		assertEquals(FOLHA_ID, id);
		
		String data = doc.getRootElement().getAttributeValue("data");
		assertEquals(FOLHA_TESTE_DATA, data);
		
		FolhadeCalculo folha = getSpreadSheet(FOLHA_TESTE);
		FolhadeCalculo folha1 = getSpreadSheet("terFolha");
		
		assertEquals(folha.getCelulaSet().size(), folha1.getCelulaSet().size());
		
		folha.importFromXML(doc.getRootElement());
		
		int cellCount = 0;
		for(Celula cell : folha.getCelulaSet()){
			for(Celula cell2 : folha1.getCelulaSet()){
				if(cell.getLinha() == cell2.getLinha() && cell.getColuna() == cell2.getColuna()){
					assertEquals(cell.getValor(), cell2.getValor());
					assertEquals(cell.getConteudo().getClass(), cell2.getConteudo().getClass());
					cellCount++;					
				}
			}
			
		}
		assertEquals(folha.getCelulaSet().size(), cellCount/2); // Aqui tens a certeza que passaste por todas as celulas
																// porque viste o numero de celulas iguais
																// igual ao numero de celulas na folha
		
	}
	//4
	@Test(expected = UnauthorizedOperationException.class)
	public void unauthorizedExport() {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, USER_TOKEN_NO_ACCESS);
		exportDocument.execute();
	}
	//8
	@Test(expected = InvalidTokenException.class)
	public void emptyTokenExport () {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, EMPTY_TOKEN);
		exportDocument.execute();
	}
	 //7
	@Test(expected = UserNotInSessionException.class)
	public void invalidSessionExport () {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, USER_TOKEN_NOT_IN_SESSION);
		exportDocument.execute();
	}
	
	//5
	@Test(expected = SpreadSheetDoesNotExistException.class)
	public void idDoesNotExistExport() {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID_INEXISTENT, USER_TOKEN);
		exportDocument.execute();
	}
	//6
	@Test(expected = IdFolhaInvalidoException.class)
	public void invalidIdExport() {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID_NEGATIVE, USER_TOKEN);
		exportDocument.execute();
	}


	//9
	@Test(expected = UnavailableServiceException.class)
	public void remoteCallFail () {
		
//		new MockUp <StoreRemoteServices>() {
//    		@Mock
//    		public void storeDocument (String user, String docName, byte[] document) {
//    			throw new RemoteInvocationException();
//    		}
//    	};
    	
    	new StrictExpectations() {
 		   
    		{
    			remote = new StoreRemoteServices();
    			remote.storeDocument("doo", "terFolha",(byte[]) any);
    			result = new RemoteInvocationException();
		    }
		};
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, USER_TOKEN_ESCRITA);
		exportDocument.execute();
	
	}

	public org.jdom2.Document byteToJdomDoc (ExportDocumentService service) throws UnsupportedEncodingException {  		// byte-> string -> outP -> doc

		
		org.jdom2.Document doc = null;
		SAXBuilder saxBuilder = new SAXBuilder();
		saxBuilder.setIgnoringElementContentWhitespace(true);
		try {
			doc = saxBuilder.build(new ByteArrayInputStream(service.getResult()));
		} catch (JDOMException | IOException e) {
			System.out.println("falhou a conversao de byte[] para jdom2 Doc: " + e);
		}
		
		return doc;
		
	}
	
	
}
