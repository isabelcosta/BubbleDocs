package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.IdFolhaInvalidoException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.InvalidTokenException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.CreateSpreadSheet;
import pt.tecnico.ulisboa.essd.bubbledocs.services.ExportDocumentService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.LoginUser;

public class ExportDocumentTest extends BubbleDocsServiceTest{
	
	// the tokens
    private String root;
    private String ars;

    private static final String USERNAME = "pf";
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
		Utilizador user1 = createUser("te", "te#", "Teresa Palhoto");
    	Utilizador user2 = createUser("mi", "mi#", "Miguel Torrado");
    	Utilizador user3 = createUser("re", "re#", "Isabel Costa");
    	Utilizador user4 = createUser("fa", "fa#", "Inês Garcia");
    	Utilizador user5 = createUser("do", "do#", "Fábio Pedro");
    	    
    	//Faz o login dos users
    	
    	USER_TOKEN = addUserToSession("te");
    	USER_TOKEN_NO_ACCESS = addUserToSession("mi");
    	USER_TOKEN_LEITURA = addUserToSession("fa");
    	USER_TOKEN_ESCRITA = addUserToSession("do");
    	
    	EMPTY_TOKEN = "";
    	USER_TOKEN_NOT_IN_SESSION = addUserToSession("re");
    	turnTokenInvalid(USER_TOKEN_NOT_IN_SESSION);
    	
    	//cria duas folhas
    	FolhadeCalculo folha1 = createSpreadSheet(user1, "teFolha", 20, 30);
		FolhadeCalculo folha2 = createSpreadSheet(user2, "miFolha", 40, 11);
		FolhadeCalculo folhaTeste = createSpreadSheet(user1, "teFolha", 20, 30);
		
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
    	bd.darPermissoes("escrita", "te", "do", FOLHA_ID);
    	bd.darPermissoes("leitura", "te", "fa", FOLHA_ID);
    	
    }
	
	@Test
	public void successDonoExport () {
		
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, USER_TOKEN);
		exportDocument.execute();
		
		String donoFolha = exportDocument.getResult().getRootElement().getAttributeValue("dono");
		assertEquals(USERNAME1, donoFolha);
		
		String nomeFolha = exportDocument.getResult().getRootElement().getAttributeValue("nome");
		assertEquals(FOLHA_TESTE, nomeFolha);
		
		int linhas = Integer.parseInt(exportDocument.getResult().getRootElement().getAttributeValue("linhas"));
		assertEquals(FOLHA_TESTE_LINHAS, linhas);
		
		int colunas = Integer.parseInt(exportDocument.getResult().getRootElement().getAttributeValue("colunas"));
		assertEquals(FOLHA_TESTE_COLUNAS, colunas);
		
		int id = Integer.parseInt(exportDocument.getResult().getRootElement().getAttributeValue("id"));
		assertEquals(FOLHA_ID, id);
		
		String data = exportDocument.getResult().getRootElement().getAttributeValue("data");
		assertEquals(FOLHA_TESTE_DATA, data);
		
		FolhadeCalculo folha = getSpreadSheet(FOLHA_TESTE);
		FolhadeCalculo folha1 = getSpreadSheet("teFolha");
		
		assertEquals(folha.getCelulaSet().size(), folha1.getCelulaSet().size());
		
		folha.importFromXML(exportDocument.getResult().getRootElement());
		
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
	
	@Test
	public void successUserReadExport () {
		
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, USER_TOKEN_LEITURA);
		exportDocument.execute();
		
		String donoFolha = exportDocument.getResult().getRootElement().getAttributeValue("dono");
		assertEquals(USERNAME1, donoFolha);
		
		String nomeFolha = exportDocument.getResult().getRootElement().getAttributeValue("nome");
		assertEquals(FOLHA_TESTE, nomeFolha);
		
		int linhas = Integer.parseInt(exportDocument.getResult().getRootElement().getAttributeValue("linhas"));
		assertEquals(FOLHA_TESTE_LINHAS, linhas);
		
		int colunas = Integer.parseInt(exportDocument.getResult().getRootElement().getAttributeValue("colunas"));
		assertEquals(FOLHA_TESTE_COLUNAS, colunas);
		
		int id = Integer.parseInt(exportDocument.getResult().getRootElement().getAttributeValue("id"));
		assertEquals(FOLHA_ID, id);
		
		String data = exportDocument.getResult().getRootElement().getAttributeValue("data");
		assertEquals(FOLHA_TESTE_DATA, data);
		
		FolhadeCalculo folha = getSpreadSheet(FOLHA_TESTE);
		FolhadeCalculo folha1 = getSpreadSheet("teFolha");
		
		assertEquals(folha.getCelulaSet().size(), folha1.getCelulaSet().size());
		
		folha.importFromXML(exportDocument.getResult().getRootElement());
		
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
	
	@Test
	public void successUserWriteExport () {
		
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, USER_TOKEN_ESCRITA);
		exportDocument.execute();
		
		String donoFolha = exportDocument.getResult().getRootElement().getAttributeValue("dono");
		assertEquals(USERNAME1, donoFolha);
		
		String nomeFolha = exportDocument.getResult().getRootElement().getAttributeValue("nome");
		assertEquals(FOLHA_TESTE, nomeFolha);
		
		int linhas = Integer.parseInt(exportDocument.getResult().getRootElement().getAttributeValue("linhas"));
		assertEquals(FOLHA_TESTE_LINHAS, linhas);
		
		int colunas = Integer.parseInt(exportDocument.getResult().getRootElement().getAttributeValue("colunas"));
		assertEquals(FOLHA_TESTE_COLUNAS, colunas);
		
		int id = Integer.parseInt(exportDocument.getResult().getRootElement().getAttributeValue("id"));
		assertEquals(FOLHA_ID, id);
		
		String data = exportDocument.getResult().getRootElement().getAttributeValue("data");
		assertEquals(FOLHA_TESTE_DATA, data);
		
		FolhadeCalculo folha = getSpreadSheet(FOLHA_TESTE);
		FolhadeCalculo folha1 = getSpreadSheet("teFolha");
		
		assertEquals(folha.getCelulaSet().size(), folha1.getCelulaSet().size());
		
		folha.importFromXML(exportDocument.getResult().getRootElement());
		
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
	
	@Test(expected = UnauthorizedOperationException.class)
	public void unauthorizedExport() {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, USER_TOKEN_NO_ACCESS);
		exportDocument.execute();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void emptyTokenExport () {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, EMPTY_TOKEN);
		exportDocument.execute();
	}
	 
	@Test(expected = UserNotInSessionException.class)
	public void invalidSessionExport () {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID, USER_TOKEN_NOT_IN_SESSION);
		exportDocument.execute();
	}
	
	
	@Test(expected = SpreadSheetDoesNotExistException.class)
	public void idDoesNotExistExport() {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID_INEXISTENT, USER_TOKEN);
		exportDocument.execute();
	}
	@Test(expected = IdFolhaInvalidoException.class)
	public void invalidIdExport() {
		ExportDocumentService exportDocument = new ExportDocumentService(FOLHA_ID_NEGATIVE, USER_TOKEN);
		exportDocument.execute();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
