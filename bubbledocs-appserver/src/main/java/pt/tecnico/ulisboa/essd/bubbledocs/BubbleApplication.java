package pt.tecnico.ulisboa.essd.bubbledocs;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.jdom2.output.XMLOutputter;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.AssignLiteralCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.AssignReferenceCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.CreateSpreadSheet;
import pt.tecnico.ulisboa.essd.bubbledocs.services.CreateUser;
import pt.tecnico.ulisboa.essd.bubbledocs.services.ExportDocumentService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.LoginUser;



public class BubbleApplication {

	
	public static void main(String[] args) {

		System.out.println("Welcome to Bubble application!!");
		org.jdom2.Document doc=null;
		
		TransactionManager tm = FenixFramework.getTransactionManager();
		boolean committed = false;

		try {
			tm.begin();
			
			//-------Create BubbleDocs--------------//
			
			Bubbledocs bd = Bubbledocs.getInstance();
			
			
			//------- Erases users and spread sheets--------
			
			unPopulateBubbleDocs(bd);
			
			//--------------------------------------------------------------------------
			//1. Inserir os dados relativos ao cenario de teste na base de dados caso o estado persistente ´
			//   da aplicacao nao esteja inicializado.
			//--------------------------------------------------------------------------
			
			System.out.println("---------------------------------------------------------------------------------");
			
			System.out.println("1. Inserir dados relativos ao cenario de teste na base de dados.");
			
			populateBubbleDocs(bd);
	    	

			//--------------------------------------------------------------------------
			//2. Escrever a informacao sobre todos os utilizadores registados na aplicacao.
			//--------------------------------------------------------------------------
			
			System.out.println("---------------------------------------------------------------------------------------");
			
			System.out.println("2. Escrever a informacao sobre todos os utilizadores registados na aplicacao.");
			
			for (Utilizador user: bd.getUtilizadoresSet()){
				System.out.println("Nome: " + user.getNome() + " ; Username: " + user.getUsername() + " ; Password: " + user.getPassword());
			}
					
			
			//--------------------------------------------------------------------------
			//3. Escrever o nomes de todas as folhas de calculo dos utilizadores pf e ra.
			//--------------------------------------------------------------------------
			
			System.out.println("----------------------------------------------------------------------------------------");
			
			System.out.println("3. Escrever o nomes de todas as folhas de calculo dos utilizadores pf e ra.");
			
			System.out.println("Nomes das folhas de calculo de pf:");
			
			for(Utilizador userIter : bd.getUtilizadoresSet()){
				if(userIter.getUsername().equals("pf")){
			    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
			    		if(folhaIter.getDono().equals("pf"))
			    			System.out.println("Nome: " + folhaIter.getNomeFolha());
			    	}
				}
			}

			System.out.println("Nomes das folhas de calculo de ra:");
			
			for(Utilizador userIter : bd.getUtilizadoresSet()){
				if(userIter.getUsername().equals("ra")){
			    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
			    		if(folhaIter.getDono().equals("ra"))
			    			System.out.println("Nome: " + folhaIter.getNomeFolha());
			    	}
				}
			}
	    	
			
	        
			//-----------------------------------------------------------------------------------------
			//4. Aceder as folhas de calculo do utilizador pf, utilizando a funcionalidade de exportacao. 
			//-----------------------------------------------------------------------------------------
			
			System.out.println("---------------------------------------------------------------------------------");
			
			System.out.println("4.Aceder as folhas de calculo do utilizador pf. ");
			
			for(Utilizador userIter : bd.getUtilizadoresSet()){
				String userToken2= null;
				if(userIter.getUsername().equals("pf")){
			    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
		    			for (Token token : bd.getTokensSet()) {
							if (token.getUsername().equals("pf")) {
								userToken2= token.getToken();
							}
						}
		    			//EXPORTDOCUMENTSERVICE
		    			ExportDocumentService exportService = new ExportDocumentService(folhaIter.getID(),userToken2 );
		    			exportService.execute();
		    			
						System.out.println("Nome da Folha: " + folhaIter.getNomeFolha() + " de " + userIter.getNome() );
						System.out.println("-----------------------------------INIT--------------------------------");
						doc= exportService.getResult();
						printDomainInXML(doc);
			    	}
				
			    		
			    		
			    		
						System.out.println("-----------------------------------END--------------------------------");
			    	}
			}
			
			//--------------------------------------------------------------------------
			//5. Remover a folha de calculo Notas ES do utilizador pf.
			//--------------------------------------------------------------------------
			
			System.out.println("---------------------------------------------------------------------------------");
			
			System.out.println("5.Remover a folha de calculo Notas ES do utilizador pf. ");
			
	    	for(Utilizador userTeste : bd.getUtilizadoresSet()){
	    		if(userTeste.getUsername().equals("pf")){
	    			for(FolhadeCalculo folha : bd.getFolhasSet()){
	    				if(folha.getNomeFolha().equals("Notas ES")){
	    					bd.eliminaFolha("Notas ES");
	    					System.out.println(" A folha foi removida! ");
	    				}
	    			}
	    		}
			}
	    	
			//--------------------------------------------------------------------------
			//6. Escrever os nomes e ids de todas as folhas de calculo do utilizador pf.
			//--------------------------------------------------------------------------
	    	
	    	System.out.println("--------------------------------------------------------------------------------");
	    	
	    	System.out.println("6.Escrever os nomes e ids de todas as folhas de calculo do utilizador pf.");

	    	System.out.println("Estas sao as minhas folhas:");
	    	
	    	for(Utilizador user : bd.getUtilizadoresSet()){
	    		if(user.getUsername().equals("pf")){
	    			for(FolhadeCalculo folha : bd.getFolhasSet()){
	    					System.out.println(" Nome: " + folha.getNomeFolha()+ " Id: " + folha.getID());
	    			}
	    			
	    		}
	    	}
	    	
	    	System.out.println("--------------------------------------------------------------------------------");
	    	
			//--------------------------------------------------------------------------------------------------------------
			//7. Utilizar a funcionalidade de importacao para criar uma folha de calculo semelhante a exportada anteriormente
	    	//	 e removida agora.
			//--------------------------------------------------------------------------------------------------------------

			System.out.println("7.Utilizar a funcionalidade de importacao para criar uma folha de calculo.");
			/*		
	 		
			--------Inicio do import a partir do ficheiro

			
			String aux = "Notas ES.xml";
			
			SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File(aux);
		 
			Document document;
			try {
				document = (Document) builder.build(xmlFile);
				printDomainInXML(document);
				//recoverFromBackup(document);
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			--------Fim do import a partir do ficheiro
					
			System.out.println(doc + " doc");
			printDomainInXML(doc);
			System.out.println(doc + " doc");
			 */
			recoverFromBackup(doc, bd);	
			
			//--------------------------------------------------------------------------
			//8. Escrever os nomes e ids de todas as folhas de calculo do utilizador pf.
			//--------------------------------------------------------------------------
	    	
			System.out.println("---------------------------------");
	    	
	    	System.out.println("8.Escrever os nomes e ids de todas as folhas de calculo do utilizador pf.");

	    	System.out.println("Estas sao as minhas folhas:");
	    	
	    	for(Utilizador user : bd.getUtilizadoresSet()){
	    		if(user.getUsername().equals("pf")){
	    			for(FolhadeCalculo folha : bd.getFolhasSet()){
	    					System.out.println(" Nome: " + folha.getNomeFolha()+ " Id: " + folha.getID());
	    			}
	    			
	    		}
	    	}
	    	
	    	//--------------------------------------------------------------------------
			//9. Aceder as folhas de calculo do utilizador pf, utilizando a funcionalidade de exportacao. 
			//--------------------------------------------------------------------------
			
	    	System.out.println("---------------------------------------------------------------------------------");
			
			System.out.println("9.Aceder as folhas de calculo do utilizador pf. ");

			for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
				if(folhaIter.getDono().equals("pf")){
					System.out.println("Nome da Folha: " + folhaIter.getNomeFolha() + " de " + folhaIter.getDono() );
					System.out.println("-----------------------------------INIT--------------------------------");
					String userToken9 = null;
					for (Token token : bd.getTokensSet()) {
						if (token.getUsername().equals("pf")) {
							userToken9= token.getToken();
						}
					}
					
					ExportDocumentService exportService = new ExportDocumentService(folhaIter.getID(), userToken9);
	    			exportService.execute();
					
					doc = exportService.getResult();
					
					
					
					printDomainInXML(doc);
					System.out.println("-----------------------------------END--------------------------------");
				}
			}
			
			tm.commit();
			committed = true;
		} catch (SystemException| NotSupportedException | RollbackException| HeuristicMixedException | HeuristicRollbackException ex) {
			System.err.println("Error in execution of transaction: " + ex);
		} catch (UserNotInSessionException dhpe) {
			System.err.println("Error with permissions: " + dhpe);
		}finally {
			if (!committed) 
				try {
					tm.rollback();
				} catch (SystemException ex) {
					System.err.println("Error in roll back of transaction: " + ex);
				}
			}
		}
	
	
	@Atomic
    public static org.jdom2.Document convertToXML(FolhadeCalculo folha) { 		//ALTERAR
		
		// FolhadeCalculo pb = FolhadeCalculo.getInstance();
		
		org.jdom2.Document jdomDoc = new org.jdom2.Document();

		jdomDoc.setRootElement(folha.exportToXML());

		return jdomDoc;
    }

    @Atomic
    public static void printDomainInXML(org.jdom2.Document jdomDoc) {
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(org.jdom2.output.Format.getPrettyFormat());
		System.out.println(xml.outputString(jdomDoc));
    }
    
    @Atomic
    private static void recoverFromBackup(org.jdom2.Document jdomDoc, Bubbledocs bd) {
    	String donoFolha = jdomDoc.getRootElement().getAttributeValue("dono");
    	String nomeFolha = jdomDoc.getRootElement().getAttributeValue("nome");
    	int linhas = Integer.parseInt(jdomDoc.getRootElement().getAttributeValue("linhas"));
    	int colunas = Integer.parseInt(jdomDoc.getRootElement().getAttributeValue("colunas"));
    	int id = Integer.parseInt(jdomDoc.getRootElement().getAttributeValue("id"));
    	String data = jdomDoc.getRootElement().getAttributeValue("data");
    	
    	
    	for(FolhadeCalculo folha : bd.getFolhasSet())
	    	if(folha.getNomeFolha().equals(nomeFolha)) {
	    		folha.setDataCriacao(new LocalDate(data));
    			folha.setID(id);
	    		folha.importFromXML(jdomDoc.getRootElement());
	    		return;
	    	}
    	
    	//caso nao tenha encontrado a folha cria uma nova
    	bd.criaFolha(nomeFolha, donoFolha, linhas, colunas);
    	
    	for (FolhadeCalculo folha : bd.getFolhasSet())
    		if(folha.getNomeFolha().equals(nomeFolha)){
    			
    			folha.setDataCriacao(new LocalDate(data));
    			folha.setID(id);
	    		folha.importFromXML(jdomDoc.getRootElement());
    		}
    	
    }
    
    @Atomic
    private static boolean isInicialized(Bubbledocs bd) {
        return !(bd.getUtilizadoresSet().isEmpty() && bd.getFolhasSet().isEmpty());
    }
    
    //Populates BubbleDocs with the initial test cenario
    static void unPopulateBubbleDocs(Bubbledocs bd) {
        if (!isInicialized(bd))
            return;
        
		for (FolhadeCalculo folha : bd.getFolhasSet()){
			bd.removeFolhas(folha);
		}			
		for (Utilizador user : bd.getUtilizadoresSet()){
			bd.removeUtilizadores(user);
		}
		for(Token token : bd.getTokensSet()){
    		bd.removeTokens(token);
		}
    }    
    //Populates BubbleDocs with the initial test cenario
    static void populateBubbleDocs(Bubbledocs bd) {
        if (isInicialized(bd))
            return;

        String rootToken = null;
        String pfToken = null;
        int folhaID = 0;
        
        // setup the initial state if bubbledocs is empty
        
        //faz login da root
        LoginUser loginRoot = new LoginUser("root", "root");
        loginRoot.execute();
        Bubbledocs.getInstance().addTokens(new Token("root", loginRoot.getUserToken()));
        
       
        //procura o token da root
        for(Token token : bd.getTokensSet()){
        	if(token.getUsername().equals("root")){
        		rootToken = token.getToken();
        	}	
        }
        
        
        //cria os utilizadores
        CreateUser serviceUser1 = new CreateUser(rootToken, "pf", "sub", "Paul Door");
        serviceUser1.execute();
        		
        CreateUser serviceUser2 = new CreateUser(rootToken, "ra", "cor", "Step Rabbit");
        serviceUser2.execute();
	
    	
    	Boolean existsToken = false;

    	for(Token token : Bubbledocs.getInstance().getTokensSet()){
    		if(token.getUsername().equals("pf")){
    			existsToken = true;
    		}
		}
    	
    	if(!existsToken){
    		LoginUser login = new LoginUser("pf", "sub");
        	login.execute(); //	-> cria o result
        	Bubbledocs.getInstance().addTokens(new Token("pf", login.getUserToken()));
    	}
    	

    	
 		for(Token token : Bubbledocs.getInstance().getTokensSet()){
			int minutes = Minutes.minutesBetween(token.getTime(), new LocalTime()).getMinutes();
			if(minutes > 120){
				Bubbledocs.getInstance().getTokensSet().remove(token);
			}
		}
		
        
 		//procura o token do pf
        for(Token token : bd.getTokensSet()){
        	if(token.getUsername().equals("pf")){
        		pfToken = token.getToken();
        	}	
        }
        
 		
        //cria a folha 
 		CreateSpreadSheet serviceFolha = new CreateSpreadSheet(pfToken, "Notas ES", 300, 20);
 		serviceFolha.execute();
    
 		
    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
    		if(folhaIter.getNomeFolha().equals("Notas ES")){
    			
    			folhaID = folhaIter.getID();
    		
    			
    			//-->Literal 5 na posicao (3, 4)
    			AssignLiteralCellService serviceLiteral = new AssignLiteralCellService(pfToken, folhaID, "3;4", "5" );
    			serviceLiteral.execute();
    			
    			
    			//-->Funcao = ADD(2, 3; 4) na posicao (5, 6)
    			String conteudoAdd = "=ADD(2,3;4)";
    			folhaIter.modificarCelula(5,6,conteudoAdd);
    			
    			
    			//-->Referencia para a celula (5, 6) na posicao (1, 1)
    			AssignReferenceCellService serviceReferencia = new AssignReferenceCellService(pfToken, folhaID, "1;1", "=5;6" );
    			serviceReferencia.execute();

    			
    			//-->Funcao = DIV (1; 1, 3; 4) na posicao (2, 2)
    			String conteudoDiv = "=DIV(1;1,3;4)";
    			folhaIter.modificarCelula(2,2,conteudoDiv);
    			
    			
    		}
    	}
    }
    
    
}
