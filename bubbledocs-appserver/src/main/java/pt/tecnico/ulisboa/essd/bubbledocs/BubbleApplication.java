
package pt.tecnico.ulisboa.essd.bubbledocs;

import java.text.Format;
import java.util.Set;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.ojb.jdo.jdoql.ThisExpression;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.*;


public class BubbleApplication {

	private int counter = 0;
	
	public static void main(String[] args) {

		System.out.println("Welcome to Bubble application!!");
		
		
		
		TransactionManager tm = FenixFramework.getTransactionManager();
		boolean committed = false;

		try {
			tm.begin();

			//--------------------------------------------------------------------------
			//1. Inserir os dados relativos ao cenario de teste na base de dados caso o estado persistente ´
			//   da aplicacao nao esteja inicializado.
			//--------------------------------------------------------------------------
			System.out.println("---------------------------------------------------------------------------------");
			
			System.out.println("1. Inserir dados relativos ao cenario de teste na base de dados.");
			
			Utilizador user1 = new Utilizador("Paul Door", "pf", "sub");
	    	FenixFramework.getDomainRoot().addUtilizadores(user1);
	    	
	    	Utilizador user2 = new Utilizador("Step Rabbit", "ra", "cor");
	    	FenixFramework.getDomainRoot().addUtilizadores(user2);
	    	
	    	FolhadeCalculo folha1 = new FolhadeCalculo();
	    	folha1.setDono("pf");
	    	folha1.setNomeFolha("Notas ES");
	    	folha1.setLinhas(300);
	    	folha1.setColunas(20);
	    	
	    	for(FolhadeCalculo folhaIter : user1.getFolhascriadasSet()){
	    		if(folhaIter.getNomeFolha().equals("Notas ES")){
	    			
	    			//-->Literal 5 na posicao (3, 4)
	    			String conteudoLiteral = "5";
	    			folhaIter.modificarCelula(3, 4, conteudoLiteral);
	    			
	    			//-->Referencia para a celula (5, 6) na posicao (1, 1)
	    			String conteudoReferencia = "=5;6";
	    			folhaIter.modificarCelula(1,1, conteudoReferencia);
	    			
	    			//-->Funcao = ADD(2, 3; 4) na posicao (5, 6)
	    			String conteudoAdd = "=ADD(2,3;4)";
	    			folhaIter.modificarCelula(5,6,conteudoAdd);
	    			
	    			//-->Funcao = DIV (1; 1, 3; 4) na posicao (2, 2)
	    			String conteudoDiv = "=DIV(1;1,3;4)";
	    			folhaIter.modificarCelula(2,2,conteudoDiv);
	    			
	    			break;
	    		}
	    	}	
			
			//--------------------------------------------------------------------------
			//2. Escrever a informacao sobre todos os utilizadores registados na aplicacao.
			//--------------------------------------------------------------------------
			
			System.out.println("---------------------------------------------------------------------------------------");
			
			System.out.println("2. Escrever a informacao sobre todos os utilizadores registados na aplicacao.");
			
			for (Utilizador user: FenixFramework.getDomainRoot().getUtilizadoresSet()){
				System.out.println("Nome: " + user.getNome() + " ; Username: " + user.getUsername() + " ; Password: " + user.getPassword());
			}
			
			//--------------------------------------------------------------------------
			//3. Escrever o nomes de todas as folhas de calculo dos utilizadores pf e ra.
			//--------------------------------------------------------------------------
			System.out.println("----------------------------------------------------------------------------------------");
			
			System.out.println("3. Escrever o nomes de todas as folhas de calculo dos utilizadores pf e ra.");
			
			System.out.println("Nomes das folhas de calculo de pf:");
			
			for(Utilizador userIter : FenixFramework.getDomainRoot().getUtilizadoresSet()){
				if(userIter.getNome().equals("pf")){
			    	for(FolhadeCalculo folhaIter : userIter.getFolhascriadasSet()){
			    		System.out.println("Nome: " + folhaIter.getNomeFolha());
			    	}
			    	break;
				}
			}

			System.out.println("Nomes das folhas de calculo de ra:");
			
			for(Utilizador userIter : FenixFramework.getDomainRoot().getUtilizadoresSet()){
				if(userIter.getNome().equals("ra")){
			    	for(FolhadeCalculo folhaIter : userIter.getFolhascriadasSet()){
			    		System.out.println("Nome: " + folhaIter.getNomeFolha());
			    	}
			    	break;
				}
			}
	    	
			//--------------------------------------------------------------------------
			//4. Aceder as folhas de calculo do utilizador pf, utilizando a funcionalidade de exportacao. 
			//--------------------------------------------------------------------------
			
			System.out.println("---------------------------------");
			
			System.out.println("4.Aceder as folhas de calculo do utilizador pf. ");
		
			
			//--------------------------------------------------------------------------
			//5. Remover a folha de calculo Notas ES do utilizador pf.
			//--------------------------------------------------------------------------
			
			System.out.println("---------------------------------");
			
			System.out.println("5.Remover a folha de calculo Notas ES do utilizador pf. ");
	/*
	    	//if(folha1.isDono("pf"))
	    		//FolhadeCalculo folha = new FolhadeCalculo();
	    		

	    		if(folha1.getDono().equals("pf")){

	    			for (FolhadeCalculo folha : FenixFramework.getDomainRoot().getFolhasdecalculoSet()){
	    				if(folha.getNomeFolha().equals("Notas ES")){
	    					folha.apagarFolha();
	    					FenixFramework.getDomainRoot().removeFolhasdecalculo(folha);

	    				}
	    			}	
	    		}
	    	
	    	else
	    		System.out.println("Não é o dono da folha!");
*/
			//--------------------------------------------------------------------------
			//6. Escrever os nomes e ids de todas as folhas de calculo do utilizador pf.
			//--------------------------------------------------------------------------
	    	
	    	System.out.println("6.Escrever os nomes e ids de todas as folhas de calculo do utilizador pf.");

	    	System.out.println("Estas sao as minhas folhas:");
	    	/*for(FolhadeCalculo folha : FenixFramework.getDomainRoot().getFolhasdecalculoSet()){
	    		if(folha.getDono().equals(user1))
	    			System.out.println("Nome:"+ folha.getNomeFolha()+ "Id:" + folha.getID());
	    	}*/
	    	
			//--------------------------------------------------------------------------------------------------------------
			//7. Utilizar a funcionalidade de importacao para criar uma folha de calculo semelhante a exportada anteriormente
	    	//	 e removida agora.
			//--------------------------------------------------------------------------------------------------------------

			System.out.println("7.Utilizar a funcionalidade de importacao para criar uma folha de calculo.");
			
			org.jdom2.Document doc = convertToXML(folha1);

			printDomainInXML(doc);
			
			
			
			//--------------------------------------------------------------------------
			//8. Escrever os nomes e ids de todas as folhas de calculo do utilizador pf.
			//--------------------------------------------------------------------------
	    	
	    	System.out.println("8.Escrever os nomes e ids de todas as folhas de calculo do utilizador pf.");
			
	    	
	    	//--------------------------------------------------------------------------
			//9. Aceder as folhas de calculo do utilizador pf, utilizando a funcionalidade de exportacao. 
			//--------------------------------------------------------------------------
			
			System.out.println("9.Aceder as folhas de calculo do utilizador pf. ");
			
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
    
    /*
    
	//Metodo que cria a  folha de calculo
	public void criaFolha(String criador, String nome, int linhas, int colunas){
		
		for(Utilizador u : FenixFramework.getDomainRoot().getUtilizadoresSet())
			if (u.getNome().equals(criador)){
				u.criaFolha(nome, linhas, colunas , counter);
				counter++;
			}
		
		
		
		}
	
	
	//Metodo que remove uma folha 
	public void removeFolha(String criador, String nome){

		for(Utilizador u : FenixFramework.getDomainRoot().getUtilizadoresSet())
			if (u.getNome().equals(criador))
				u.removeFolha(nome);
				
		
	}
	
	//Metodo que cria um cliente 
	public void criaUtilizador(String nomeUtilizador,String userName, String password){
		
		Utilizador utilizador = new Utilizador(nomeUtilizador, userName, password);
		
		FenixFramework.getDomainRoot().addUtilizadores(utilizador);
		}

	
	//Metodo que remove um Utilizador e todas as folhas que ele contêm
	
	public void removeUtilizador(String userName){

		if(userName.equals("root")){
			
			for (Utilizador utilizador : FenixFramework.getDomainRoot().getUtilizadoresSet()){
				if(utilizador.getNome().equals(userName)){
					utilizador.apagaFolhas();
					FenixFramework.getDomainRoot().removeUtilizadores(utilizador);
				
				}
			}
			
		}
	} */
}
