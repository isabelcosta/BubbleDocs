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
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignLiteralCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.AssignReferenceCellService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.CreateSpreadSheetService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.CreateUserService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.ExportDocumentService;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.LoginUserService;



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
			//3. Escrever o nomes de todas as folhas de calculo dos utilizadores pfa e rad.
			//--------------------------------------------------------------------------
			
			System.out.println("----------------------------------------------------------------------------------------");
			
			System.out.println("3. Escrever o nomes de todas as folhas de calculo dos utilizadores pfa e rad.");
			
			System.out.println("Nomes das folhas de calculo de pfa:");
			
			for(Utilizador userIter : bd.getUtilizadoresSet()){
				if(userIter.getUsername().equals("pfa")){
			    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
			    		if(folhaIter.getDono().equals("pfa"))
			    			System.out.println("Nome: " + folhaIter.getNomeFolha());
			    	}
				}
			}

			System.out.println("Nomes das folhas de calculo de rad:");
			
			for(Utilizador userIter : bd.getUtilizadoresSet()){
				if(userIter.getUsername().equals("rad")){
			    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
			    		if(folhaIter.getDono().equals("rad"))
			    			System.out.println("Nome: " + folhaIter.getNomeFolha());
			    	}
				}
			}
	    	
			
	        
			//-----------------------------------------------------------------------------------------
			//4. Aceder as folhas de calculo do utilizador pf, utilizando a funcionalidade de exportacao. 
			//-----------------------------------------------------------------------------------------
			
			System.out.println("---------------------------------------------------------------------------------");
			
			System.out.println("4.Aceder as folhas de calculo do utilizador pfa. ");
			
			for(Utilizador userIter : bd.getUtilizadoresSet()){
				String userToken2= null;
				if(userIter.getUsername().equals("pfa")){
			    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
		    			for (Token token : bd.getTokensSet()) {
							if (token.getUsername().equals("pfa")) {
								userToken2= token.getToken();
							}
						}
		    			//EXPORTDOCUMENTSERVICE
		    			ExportDocumentService exportService = new ExportDocumentService(folhaIter.getID(),userToken2 );
		    			exportService.execute();
		    			
						System.out.println("Nome da Folha: " + folhaIter.getNomeFolha() + " de " + userIter.getNome() );
						System.out.println("-----------------------------------INIT--------------------------------");
						//doc= exportService.getResult();
						printDomainInXML(doc);
			    	}
				
			    		
			    		
			    		
						System.out.println("-----------------------------------END--------------------------------");
			    	}
			}
			
			//--------------------------------------------------------------------------
			//5. Remover a folha de calculo Notas ES do utilizador pf.
			//--------------------------------------------------------------------------
			
			System.out.println("---------------------------------------------------------------------------------");
			
			System.out.println("5.Remover a folha de calculo Notas ES do utilizador pfa. ");
			
	    	for(Utilizador userTeste : bd.getUtilizadoresSet()){
	    		if(userTeste.getUsername().equals("pfa")){
	    			for(FolhadeCalculo folha : bd.getFolhasSet()){
	    				if(folha.getNomeFolha().equals("Notas ES")){
	    					bd.eliminaFolha(folha.getID());
	    					System.out.println(" A folha foi removida! ");
	    				}
	    			}
	    		}
			}
	    	
			//--------------------------------------------------------------------------
			//6. Escrever os nomes e ids de todas as folhas de calculo do utilizador pfa.
			//--------------------------------------------------------------------------
	    	
	    	System.out.println("--------------------------------------------------------------------------------");
	    	
	    	System.out.println("6.Escrever os nomes e ids de todas as folhas de calculo do utilizador pfa.");

	    	System.out.println("Estas sao as minhas folhas:");
	    	
	    	for(Utilizador user : bd.getUtilizadoresSet()){
	    		if(user.getUsername().equals("pfa")){
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
	 		
			recoverFromBackup(doc, bd);	
			
			//--------------------------------------------------------------------------
			//8. Escrever os nomes e ids de todas as folhas de calculo do utilizador pfa.
			//--------------------------------------------------------------------------
	    	
			System.out.println("---------------------------------");
	    	
	    	System.out.println("8.Escrever os nomes e ids de todas as folhas de calculo do utilizador pfa.");

	    	System.out.println("Estas sao as minhas folhas:");
	    	
	    	for(Utilizador user : bd.getUtilizadoresSet()){
	    		if(user.getUsername().equals("pfa")){
	    			for(FolhadeCalculo folha : bd.getFolhasSet()){
	    					System.out.println(" Nome: " + folha.getNomeFolha()+ " Id: " + folha.getID());
	    			}
	    			
	    		}
	    	}
	    	
	    	//--------------------------------------------------------------------------
			//9. Aceder as folhas de calculo do utilizador pfa, utilizando a funcionalidade de exportacao. 
			//--------------------------------------------------------------------------
			
	    	System.out.println("---------------------------------------------------------------------------------");
			
			System.out.println("9.Aceder as folhas de calculo do utilizador pfa. ");

			for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
				if(folhaIter.getDono().equals("pfa")){
					System.out.println("Nome da Folha: " + folhaIter.getNomeFolha() + " de " + folhaIter.getDono() );
					System.out.println("-----------------------------------INIT--------------------------------");
					String userToken9 = null;
					for (Token token : bd.getTokensSet()) {
						if (token.getUsername().equals("pfa")) {
							userToken9= token.getToken();
						}
					}
					
					ExportDocumentService exportService = new ExportDocumentService(folhaIter.getID(), userToken9);
	    			exportService.execute();
					
					//doc = exportService.getResult();
					
					
					
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
    	
    	//procura o token do pfa
    	String donoFolhaToken = null;
        for(Token token : bd.getTokensSet()){
        	if(token.getUsername().equals(donoFolha)){
        		donoFolhaToken = token.getToken();
        	}	
        }
    	
    	
    	//caso nao tenha encontrado a folha cria uma nova
 		CreateSpreadSheetService serviceFolha = new CreateSpreadSheetService(donoFolhaToken, nomeFolha, linhas, colunas);
 		serviceFolha.execute();
    	
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
        LoginUserService loginRoot = new LoginUserService("root", "root");
        loginRoot.execute();
        Bubbledocs.getInstance().addTokens(new Token("root", loginRoot.getUserToken()));
        
       
        //procura o token da root
        for(Token token : bd.getTokensSet()){
        	if(token.getUsername().equals("root")){
        		rootToken = token.getToken();
        	}	
        }
        
        
        //cria os utilizadores
        CreateUserService serviceUser1 = new CreateUserService(rootToken, "pfa", "sub", "Paul Door");
        serviceUser1.execute();
        		
        CreateUserService serviceUser2 = new CreateUserService(rootToken, "rad", "cor", "Step Rabbit");
        serviceUser2.execute();
	
    	
    	Boolean existsToken = false;

    	for(Token token : Bubbledocs.getInstance().getTokensSet()){
    		if(token.getUsername().equals("pfa")){
    			existsToken = true;
    		}
		}
    	
    	if(!existsToken){
    		LoginUserService login = new LoginUserService("pfa", "sub");
        	login.execute(); //	-> cria o result
        	Bubbledocs.getInstance().addTokens(new Token("pfa", login.getUserToken()));
    	}
    	

    	
 		for(Token token : Bubbledocs.getInstance().getTokensSet()){
			int minutes = Minutes.minutesBetween(token.getTime(), new LocalTime()).getMinutes();
			if(minutes > 120){
				Bubbledocs.getInstance().getTokensSet().remove(token);
			}
		}
		
        
 		//procura o token do pfa
        for(Token token : bd.getTokensSet()){
        	if(token.getUsername().equals("pfa")){
        		pfToken = token.getToken();
        	}	
        }
        
 		
        //cria a folha 
 		CreateSpreadSheetService serviceFolha = new CreateSpreadSheetService(pfToken, "Notas ES", 300, 20);
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
    			String conteudoDiv = "=SUB(1;1,3;4)";
    			folhaIter.modificarCelula(2,2,conteudoDiv);
    			
    			
    		}
    	}
    }
    
    
}
