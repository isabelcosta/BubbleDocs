package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.*;

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
import pt.tecnico.ulisboa.essd.bubbledocs.exception.CannotLoadDocumentException;
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
    private static int FOLHA2_ID;
    private static String USER_TOKEN;
    private static String USER_TOKEN_MIG;
    private static String USER_TOKEN_NOT_IN_SESSION;
    private static int FOLHA_ID_NONEXISTENT = 100;
    private static int FOLHA_ID_NEGATIVE = -100;
    
    private static String FOLHA_TESTE_DATA;
    private static String USER_TOKEN_ESCRITA;
    private static String CONTEUDO_LITERAL = "4";
    private static String CONTEUDO_ADD = "=ADD(2,3;2)";
    private static String CONTEUDO_REFERENCIA = "=3;2";
    
	/*
	 * --------------------------------------------------------
	 *  Populate
	 * --------------------------------------------------------
	 */
    
    
	public void populate4Test() {

		//Limpa a base de dados
    	unPopulate4Test();
    	
    	Bubbledocs bd = Bubbledocs.getInstance();
    	
    	//Cria users                  userna  pw     name
		Utilizador user1 = createUser("ter", "te#", "Teresa Palhoto");
    	Utilizador user2 = createUser("mig", "mi#", "Miguel Torrado");
    	createUser("ree", "re#", "Isabel Costa");
    	createUser("faa", "fa#", "InÃªs Garcia");
    	createUser("doo", "do#", "FÃ¡bio Pedro");
    	    
    	//Faz o login dos users
    	
    	USER_TOKEN = addUserToSession("ter");
    	USER_TOKEN_MIG = addUserToSession("mig");
    	USER_TOKEN_ESCRITA = addUserToSession("doo");
    	
    	USER_TOKEN_NOT_IN_SESSION = addUserToSession("ree");
    	turnTokenInvalid(USER_TOKEN_NOT_IN_SESSION);
    	
    	//cria duas folhas
    	FolhadeCalculo folha1 = createSpreadSheet(user1, "terFolha", 20, 30);
		FolhadeCalculo folha2 = createSpreadSheet(user2, "migFolha", 40, 11);
		
		FOLHA_ID = folha1.getID();
		FOLHA2_ID = folha2.getID();

		bd.darPermissoes("escrita", "ter", "doo", FOLHA_ID);
		exportFolhadeCalculo(FOLHA_ID, USER_TOKEN_ESCRITA);
		
		
//		bd.darPermissoes("escrita", "mig", "doo", FOLHA2_ID);
		
		exportFolhadeCalculo(FOLHA2_ID, USER_TOKEN_MIG);
		
		
		
		
		//Preenche a folha (folha1) do user "ter"
		folha1.modificarCelula(3,2,CONTEUDO_LITERAL);
		folha1.modificarCelula(5,7,CONTEUDO_ADD);
		folha1.modificarCelula(1,1,CONTEUDO_REFERENCIA);
		
		
    	//SUCESSDONO
    	FOLHA_TESTE_DATA = folha1.getDataCriacao().toString();
    	
    	
    	//da "ab" da permissoes de escrita a "pi" para preencher a sua folha
    	bd.darPermissoes("escrita", "ter", "doo", FOLHA_ID);
    	bd.darPermissoes("leitura", "ter", "faa", FOLHA_ID);
    	
    }
	
	
	/*
	 * --------------------------------------------------------
	 *  Funcoes auxiliares
	 * --------------------------------------------------------
	 */

// transforma uma folha em bytes para ser usado no mock de uma folha importada
	public byte[] folhaToByte4Mock(Integer sheetId, String userToken) {
		
		Bubbledocs bd = Bubbledocs.getInstance();
		
		// Folha de calculo associada ao ID
		FolhadeCalculo folha;
		folha = bd.getFolhaOfId(sheetId);
		
		// userName associado ao Token
		String userNameOfToken = bd.getUsernameOfToken(userToken);
    	
		// caso possa escrever ou ler passa á exportacao do doc
		if(folha.podeLer(userNameOfToken) || folha.podeEscrever(userNameOfToken)){
    		
    		// transformar a folha no jdomDoc
			org.jdom2.Document sheetDoc = bd.exportSheet(folha);
			
			// criar com a ajuda do XMLOutputter uma string apartir do jdomDoc
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(org.jdom2.output.Format.getPrettyFormat());
			String docString = xmlOutput.outputString(sheetDoc);
			
			// por fim transforma-la em bytes
			try {
				return docString.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				System.out.println("export falhou: " + e);
			}
    	}
		return null;
		
	}
	
	/*
	 * --------------------------------------------------------
	 *  TESTES
	 * --------------------------------------------------------
	 */

	@Mocked StoreRemoteServices remote;
	 
	//1
	@Test
	public void successImportWithContentTest () {
		Bubbledocs _bd = Bubbledocs.getInstance();
		byte[] folhaByte;
		folhaByte = folhaToByte4Mock(FOLHA_ID, USER_TOKEN_ESCRITA); //guardar a folha em bytes para usar no mock do import
		removeSpreadsheet(FOLHA_ID); 						//remover a folha para importar sem estar na bd
		
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
		
		assertEquals("doo", folhaImportada.getDono());
		assertEquals("terFolha", folhaImportada.getNomeFolha());
		assertEquals("20", folhaImportada.getLinhas().toString());
		assertEquals("30", folhaImportada.getColunas().toString());
		assertEquals(FOLHA_TESTE_DATA, folhaImportada.getDataCriacao().toString());
		assertEquals(CONTEUDO_LITERAL, folhaImportada.getCelulaEspecifica(3, 2).getConteudo().toString());
		assertEquals(CONTEUDO_REFERENCIA, folhaImportada.getCelulaEspecifica(1, 1).getConteudo().toString());
		assertEquals(CONTEUDO_ADD, folhaImportada.getCelulaEspecifica(5, 7).getConteudo().toString());
		assertEquals(3, folhaImportada.getCelulaSet().size());
		
	}
	
	//2
	@Test
	public void successImportEmptyTest () {
		Bubbledocs _bd = Bubbledocs.getInstance();
		byte[] folhaByte;
		folhaByte = folhaToByte4Mock(FOLHA2_ID, USER_TOKEN_MIG);	//guardar a folha em bytes para usar no mock do import
		removeSpreadsheet(FOLHA2_ID); 									//remover a folha para importar sem estar na bd
		
		new StrictExpectations() {
	 		   
    		{
    			remote = new StoreRemoteServices();
    			remote.loadDocument("mig", "migFolha");
    			result = folhaByte;
		    }
		};
		ImportDocumentIntegrator importDocument = new ImportDocumentIntegrator(FOLHA2_ID, USER_TOKEN_MIG);
		importDocument.execute();
		
		Integer newSheetId = importDocument.getResult();
		FolhadeCalculo folhaImportada = _bd.getFolhaOfId(newSheetId);
		
		
		assertEquals("mig", folhaImportada.getDono());
		assertEquals("migFolha", folhaImportada.getNomeFolha());
		assertEquals("40", folhaImportada.getLinhas().toString());
		assertEquals("11", folhaImportada.getColunas().toString());
		assertEquals(FOLHA_TESTE_DATA, folhaImportada.getDataCriacao().toString());
		assertEquals(0, folhaImportada.getCelulaSet().size());
		
	}
		
		
	//3
	@Test
	public void failUserDidntExportedTest () {
		
		ImportDocumentIntegrator importDocument = new ImportDocumentIntegrator(FOLHA2_ID, USER_TOKEN);
		
		try {
			importDocument.execute();
			fail();
		} catch (CannotLoadDocumentException e) {
			assertNull("Verificar que a folha não foi importada ", importDocument.getResult());
		}
	}
	
	//4
	@Test
	public void failInvalidSheetIDTest () {
		
		ImportDocumentIntegrator importDocument = new ImportDocumentIntegrator(FOLHA_ID_NEGATIVE, USER_TOKEN);
		
		try {
			importDocument.execute();
			fail();
		} catch (IdFolhaInvalidoException e) {
			assertNull("Verificar que a folha não foi importada ", importDocument.getResult());
		}
		
	}
	
	/*
	 *  Com a implementacao que temos o user sabe que folhas exportou, logo o caso de folha invalida, inexistente ou existente mas nao exportou sao semelhantes
	 *  
	 */
	
	//5
	@Test
	public void failNonExistentSheetTest () {
		
		ImportDocumentIntegrator importDocument = new ImportDocumentIntegrator(FOLHA_ID_NONEXISTENT, USER_TOKEN);
		
		try {
			importDocument.execute();
			fail();
		} catch (CannotLoadDocumentException e) {
			assertNull("Verificar que a folha não foi importada ", importDocument.getResult());
		}
		
	}
	
	//6
	@Test
	public void failServiceUnavailableTest() {
		
		new StrictExpectations() {
	 		   
    		{
    			remote = new StoreRemoteServices();
    			remote.loadDocument("doo", "terFolha");
    			result = new RemoteInvocationException();
		    }
		};
		
		
		ImportDocumentIntegrator importDocument = new ImportDocumentIntegrator(FOLHA_ID, USER_TOKEN_ESCRITA);
		
		try {
			importDocument.execute();
			fail();
		} catch (UnavailableServiceException e) {
			assertNull("Verificar que a folha não foi importada ", importDocument.getResult());
		}
		
	}
				
		
		
}