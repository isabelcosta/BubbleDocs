package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import mockit.Mocked;
import mockit.StrictExpectations;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.IdFolhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.ExportDocumentIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.integrator.ImportDocumentIntegrator;
import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.StoreRemoteServices;

public class ImportDocumentIntegratorTest extends BubbleDocsServiceTest{
	
    private static int FOLHA_ID;
    private static String USER_TOKEN;
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
    private static byte[] _folha1Exportada;
    private static String TESTE;
    private static String CONTEUDO_LITERAL = "4";
    private static String CONTEUDO_ADD = "=ADD(2,3;2)";
    private static String CONTEUDO_REFERENCIA = "=3;2";
    
	public void populate4Test() {

		//Limpa a base de dados
    	unPopulate4Test();
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	//Cria users                  userna  pw     name
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
		bd.darPermissoes("escrita", "ter", "doo", FOLHA_ID);

		/*
		 * <QUESTION>
		 * Fazer um export 
		 * 
		 * 
		 * */
		
		FOLHA_ID = folha1.getID();
		_folha1Exportada = exportFolhadeCalculo(FOLHA_ID, USER_TOKEN_ESCRITA);
		
		
		
		
		//Preenche a folha (folha1) do user "ab"
		
		
		
		folha1.modificarCelula(3,2,CONTEUDO_LITERAL);
		
		
		folha1.modificarCelula(5,7,CONTEUDO_ADD);
		
		
		folha1.modificarCelula(1,1,CONTEUDO_REFERENCIA);
		
		
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
	
	public byte[] folhaToByte4Mock(Integer sheetId, String userToken) {
		Bubbledocs bd = Bubbledocs.getInstance();
		FolhadeCalculo folha;
		
		
		folha = bd.getFolhaOfId(sheetId);
		
		String userNameOfToken = bd.getUsernameOfToken(userToken);
    	if(folha.podeLer(userNameOfToken) || folha.podeEscrever(userNameOfToken)){
    		
//CONVERTER A FOLHA EM BYTES
			org.jdom2.Document sheetDoc = bd.exportSheet(folha);
			
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(org.jdom2.output.Format.getPrettyFormat());
			String docString = xmlOutput.outputString(sheetDoc);
			
			try {
				return docString.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				System.out.println("export falhou: " + e);
			}
    	}
		return null;
		
	}
	
	
	@Mocked StoreRemoteServices remote;
	 
	//1
	@Test
	public void successDonoExport () {
		Bubbledocs _bd = Bubbledocs.getInstance();
		byte[] folhaByte;
		folhaByte = folhaToByte4Mock(FOLHA_ID, USER_TOKEN_ESCRITA); //guardar a folha em bytes para usar no mock do import
		//removeSpreadsheet(FOLHA_ID); 						//remover a folha para importar sem estar na bd
		
		new StrictExpectations() {
	 		   
    		{
    			remote = new StoreRemoteServices();
    			remote.loadDocument("doo", "terFolha");
    			result = folhaByte;
		    }
		};
		
		ImportDocumentIntegrator importDocument = new ImportDocumentIntegrator(FOLHA_ID, USER_TOKEN_ESCRITA);
		importDocument.execute();
		
		Integer newSheetId = importDocument.getResult();
		FolhadeCalculo folhaImportada = _bd.getFolhaOfId(newSheetId);
		
		assertEquals(folhaImportada.getDono(), "doo");
		assertEquals(folhaImportada.getNomeFolha(), "terFolha");
		assertEquals(folhaImportada.getLinhas().toString(), "20");
		assertEquals(folhaImportada.getColunas().toString(), "30");
		assertEquals(folhaImportada.getDataCriacao().toString(), FOLHA_TESTE_DATA);
		assertEquals(folhaImportada.getCelulaEspecifica(3, 2).getConteudo().toString(), CONTEUDO_LITERAL);
		assertEquals(folhaImportada.getCelulaEspecifica(1, 1).getConteudo().toString(), CONTEUDO_REFERENCIA);
		assertEquals(folhaImportada.getCelulaEspecifica(5, 7).getConteudo().toString(), CONTEUDO_ADD);
		assertEquals(folhaImportada.getCelulaSet().size(), 3);
		
		
		
		
			
	}
	/*//2
	@Test
	public void successUserReadExport () {
		
		new StrictExpectations() {
    		{
    			remote = new StoreRemoteServices();
    			remote.storeDocument("faa", "terFolha",(byte[]) any);
		    }
		};
		
		ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(FOLHA_ID, USER_TOKEN_LEITURA);
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
		
		
		ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(FOLHA_ID, USER_TOKEN_ESCRITA);
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
		ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(FOLHA_ID, USER_TOKEN_NO_ACCESS);
		exportDocument.execute();
	}
	//8
	@Test(expected = InvalidTokenException.class)
	public void emptyTokenExport () {
		ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(FOLHA_ID, EMPTY_TOKEN);
		exportDocument.execute();
	}
	 //7
	@Test(expected = UserNotInSessionException.class)
	public void invalidSessionExport () {
		ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(FOLHA_ID, USER_TOKEN_NOT_IN_SESSION);
		exportDocument.execute();
	}
	
	//5
	@Test(expected = SpreadSheetDoesNotExistException.class)
	public void idDoesNotExistExport() {
		ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(FOLHA_ID_INEXISTENT, USER_TOKEN);
		exportDocument.execute();
	}
	//6
	@Test(expected = IdFolhaInvalidoException.class)
	public void invalidIdExport() {
		ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(FOLHA_ID_NEGATIVE, USER_TOKEN);
		exportDocument.execute();
	}


	//9
	@Test(expected = UnavailableServiceException.class)
	public void remoteCallFail () {
    	
    	new StrictExpectations() {
 		   
    		{
    			remote = new StoreRemoteServices();
    			remote.storeDocument("doo", "terFolha",(byte[]) any);
    			result = new RemoteInvocationException();
		    }
		};
		ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(FOLHA_ID, USER_TOKEN_ESCRITA);
		exportDocument.execute();
	
	}
	
	//10
		@Test(expected = CannotStoreDocumentException.class)
		public void cannotStoreDocTest () {
	    	
	    	new StrictExpectations() {
	 		   
	    		{
	    			remote = new StoreRemoteServices();
	    			remote.storeDocument("doo", "terFolha",(byte[]) any);
	    			result = new CannotStoreDocumentException("terFolha");
			    }
			};
			ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(FOLHA_ID, USER_TOKEN_ESCRITA);
			exportDocument.execute();
		
		}
	
	
	//11
	@Test(expected = CannotStoreDocumentException.class)
	public void cannotStoreTest () {
    	
    	new StrictExpectations() {
 		   
    		{
    			remote = new StoreRemoteServices();
    			remote.storeDocument("doo", "terFolha",(byte[]) any);
    			result = new CannotStoreDocumentException("terFolha");
		    }
		};
		ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(FOLHA_ID, USER_TOKEN_ESCRITA);
		exportDocument.execute();
	
	}

	public org.jdom2.Document byteToJdomDoc (ExportDocumentIntegrator service) throws UnsupportedEncodingException {  		// byte-> string -> outP -> doc

		
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
	
	
*/
}