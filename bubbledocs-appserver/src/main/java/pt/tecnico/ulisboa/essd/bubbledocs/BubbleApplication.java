package pt.tecnico.ulisboa.essd.bubbledocs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FuncaoBinaria;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Referencia;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;


public class BubbleApplication {

	
	public static void main(String[] args) {

		System.out.println("Welcome to Bubble application!!");
		
		
		
		TransactionManager tm = FenixFramework.getTransactionManager();
		boolean committed = false;

		try {
			tm.begin();
			
			//-------Create BubbleDocs--------------//
			
			Bubbledocs bd = Bubbledocs.getInstance();
			
			
//			//-------TRYING TO DELETE DATABASE--------
//			
//			for (Utilizador user : bd.getUtilizadoresSet()){
//				user.apagaFolhas();
//				bd.removeUtilizadores(user);
//			}
//			System.out.println("SUPOSTAMENTE APAGUEI ME");
//			
//			//---------------------------------------------------
//			

			//--------------------------------------------------------------------------
			//1. Inserir os dados relativos ao cenario de teste na base de dados caso o estado persistente ´
			//   da aplicacao nao esteja inicializado.
			//--------------------------------------------------------------------------
			System.out.println("---------------------------------------------------------------------------------");
			
			System.out.println("1. Inserir dados relativos ao cenario de teste na base de dados.");
			
			Utilizador user1 = null;
			Utilizador user2 = null;
			
			if(bd.getUtilizadoresSet().isEmpty()){
				
				user1 = new Utilizador("Paul Door", "pf", "sub");
		    	bd.addUtilizadores(user1);
		    	
		    	user2 = new Utilizador("Step Rabbit", "ra", "cor");
		    	bd.addUtilizadores(user2);
		    	
			} else {
				
				for (Utilizador user: bd.getUtilizadoresSet()){
					if(user.getUsername().equals("pf")){
						user1 = user;
					} else if (user.getUsername().equals("ra")){
						user2 = user;
					}
				}				
			}
			
			boolean found = false;
	    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
	    		if(folhaIter.getNomeFolha().equals("Notas ES") && folhaIter.getDono().equals("pf")){
	    			found = true;
	    		}
	    	}
	    	
	    	if(found == false){
	    		bd.criaFolha("Notas ES","pf",300, 20);
	    	}
	    	
	    	
	    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
	    		if(folhaIter.getNomeFolha().equals("Notas ES")){
	    			
	    			//-->Literal 5 na posicao (3, 4)
	    			String conteudoLiteral = "5";
	    			folhaIter.modificarCelula(3, 4, conteudoLiteral);
	    			
	    			//-->Funcao = ADD(2, 3; 4) na posicao (5, 6)
	    			String conteudoAdd = "=ADD(2,3;4)";
	    			folhaIter.modificarCelula(5,6,conteudoAdd);
	    			
	    			//-->Referencia para a celula (5, 6) na posicao (1, 1)
	    			String conteudoReferencia = "2"; //"=5;6";
	    			folhaIter.modificarCelula(7,7, conteudoReferencia);
	    			
	    			//-->Funcao = DIV (1; 1, 3; 4) na posicao (2, 2)
	    			String conteudoDiv = "=DIV(3;4,7;7)";
	    			folhaIter.modificarCelula(2,2,conteudoDiv);
	    			
	    			
	    		}
	    	}	
			/*
	    	for (FolhadeCalculo folhaIter : user1.getFolhascriadasSet()) {
	    		if(folhaIter.getNomeFolha().equals("Notas ES")){
	    			for (Celula cel : folhaIter.getCelulaSet())
	    			{
	    				System.out.println("Coluna: " + cel.getColuna() + " conteudo " + cel.getConteudo());
	    				if (cel.getColuna().equals(2)) {
	    					FuncaoBinaria fb = (FuncaoBinaria) cel.getConteudo();
	    					Referencia arg1 = (Referencia) fb.getArgumento1();
	    					System.out.println("arg1 conteudo " + arg1.getCelularef().getLinha());
						}
	    			}
	    				
	    		}
			}*/
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
	    	
			//--------------------------------------------------------------------------
			//4. Aceder as folhas de calculo do utilizador pf, utilizando a funcionalidade de exportacao. 
			//--------------------------------------------------------------------------
			
			System.out.println("---------------------------------------------------------------------------------");
			
			System.out.println("4.Aceder as folhas de calculo do utilizador pf. ");


			for(Utilizador userIter : bd.getUtilizadoresSet()){
				if(userIter.getNome().equals("pf")){
			    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
			    		
							System.out.println("Nome da Folha: " + folhaIter.getNomeFolha() + " de " + userIter.getNome() );
							System.out.println("-----------------------------------INIT--------------------------------");
				    		org.jdom2.Document doc = convertToXML(folhaIter);
				    		printDomainInXML(doc);
				    		
				    		// new XMLOutputter().output(doc, System.out);
				    		XMLOutputter xmlOutput = new XMLOutputter();
				     
				    		// display nice nice
				    		xmlOutput.setFormat(org.jdom2.output.Format.getPrettyFormat());
				    		try {
								xmlOutput.output(doc, new FileWriter(folhaIter.getNomeFolha()));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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
			
			String aux = "Notas ES";
			
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


			for(Utilizador userIter : bd.getUtilizadoresSet()){
				
			    	for(FolhadeCalculo folhaIter : bd.getFolhasSet()){
			    		
						System.out.println("Nome da Folha: " + folhaIter.getNomeFolha() + " de " + userIter.getNome() );
						System.out.println("-----------------------------------INIT--------------------------------");
			    		org.jdom2.Document doc = convertToXML(folhaIter);
			    		

			    		
						printDomainInXML(doc);
						System.out.println("-----------------------------------END--------------------------------");
			    	}
			}
			
			tm.commit();
			committed = true;
		}catch (SystemException| NotSupportedException | RollbackException| HeuristicMixedException | HeuristicRollbackException ex) {
			System.err.println("Error in execution of transaction: " + ex);
		} finally {
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
    private static void recoverFromBackup(org.jdom2.Document jdomDoc) {
    FolhadeCalculo folha = new FolhadeCalculo();

	//folha.importFromXML(jdomDoc.getRootElement());
    }
    
}