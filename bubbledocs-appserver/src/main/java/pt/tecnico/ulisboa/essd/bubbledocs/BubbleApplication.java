
package pt.tecnico.ulisboa.essd.bubbledocs;

import java.util.Set;

import javax.transaction.SystemException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.*;


public class BubbleApplication {

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
	       	
			Utilizador user1 = new Utilizador("Paul Door", "pf", "sub");
	    	FenixFramework.getDomainRoot().addUtilizadores(user1);
	    	
	    	Utilizador user2 = new Utilizador("Step Rabbit", "ra", "cor");
	    	FenixFramework.getDomainRoot().addUtilizadores(user2);
	    	
	    	FolhadeCalculo folha1 = new FolhadeCalculo("pf", "Notas ES", 300, 20);
	    	FenixFramework.getDomainRoot().addFolhasdecalculo(folha1);
	    	
	    	//-->Literal 5 na posicao (3, 4)
			Literal conteudoLiteral = new Literal(new Integer.parseInt(5));
			folha.addCelula(new Celula(3, 4, conteudoLiteral));
			
			//-->Referencia para a celula (5, 6) na posicao (1, 1)
//			Referencia conteudoReferencia = new Referencia(5,6);
			Funcao conteudoReferencia = Parser.parseConteudo("=5;6");
			folha.modificarCelula(new Celula(1,1, conteudoReferencia));
			
			//-->Funcao = ADD(2, 3; 4) na posicao (5, 6)
			Funcao conteudoAdd = Parser.parseConteudo("=ADD(2,3;4)");
			folha.modificarCelula(new Celula(5,6,conteudoAdd));
			
			//-->Funcao = DIV (1; 1, 3; 4) na posicao (2, 2)
			Funcao conteudoDiv = Parser.parseConteudo("=DIV(1;1,3;4)");
			folha.modificarCelula(new Celula(2,2,conteudoDiv));
			
			//--------------------------------------------------------------------------
			//2. Escrever a informacao sobre todos os utilizadores registados na aplicacao.
			//	 Mostro as passwords tambem???? <----  <-----  <-----  <------ <----- <----- <----- <---- <----- <-----
			//--------------------------------------------------------------------------
			
			System.out.println("2. Escrever a informacao sobre todos os utilizadores registados na aplicacao.");
			
			for (Utilizador user: FenixFramework.getDomainRoot().getUtilizadoresSet()){
				System.out.println("Nome: " + user.getNome() + " ; Username: " + user.getUsername() + " ; Password: " + user.getPassword());
			}
			
			//--------------------------------------------------------------------------
			//3. Escrever o nomes de todas as folhas de calculo dos utilizadores pf e ra.
			//--------------------------------------------------------------------------
			
			System.out.println("3. Escrever o nomes de todas as folhas de calculo dos utilizadores pf e ra.");
			
			//--------------------------------------------------------------------------
			//4.Aceder as folhas de calculo do utilizador pf, utilizando a funcionalidade de exportacao. 
			//--------------------------------------------------------------------------
			
			System.out.println("4.Aceder as folhas de calculo do utilizador pf. ");
		
			
			//--------------------------------------------------------------------------
			//5.Remover a folha de calculo Notas ES do utilizador pf.
			//--------------------------------------------------------------------------
	    	
			System.out.println("5.Remover a folha de calculo Notas ES do utilizador pf. ");
	    	
	    	if(folha1.isDono("pf"))
	    		user1.removeFolha("Notas ES");
	    	else
	    		System.out.println("Não é o dono da folha!");

			//--------------------------------------------------------------------------
			//6.Escrever os nomes e ids de todas as folhas de calculo do utilizador pf.
			//--------------------------------------------------------------------------
	    	
	    	System.out.println("6.Escrever os nomes e ids de todas as folhas de calculo do utilizador pf.");

	    	System.out.println("Estas sao as minhas folhas:");
	    	for(FolhadeCalculo folha : FenixFramework.getDomainRoot().getFolhasdecalculoSet()){
	    		if(folha.getDono().equals(user1))
	    			System.out.println("Nome:"+ folha.getNomeFolha()+ "Id:" + folha.getID());
	    	}
	    	
			//--------------------------------------------------------------------------------------------------------------
			//7.Utilizar a funcionalidade de importacao para criar uma folha de calculo semelhante a exportada anteriormente
	    	//	e removida agora.
			//--------------------------------------------------------------------------------------------------------------

			System.out.println("7.Utilizar a funcionalidade de importacao para criar uma folha de calculo.");
			
			
			//--------------------------------------------------------------------------
			//8.Escrever os nomes e ids de todas as folhas de calculo do utilizador pf.
			//--------------------------------------------------------------------------
	    	
	    	System.out.println("8.Escrever os nomes e ids de todas as folhas de calculo do utilizador pf.");
			
	    	
	    	//--------------------------------------------------------------------------
			//9.Aceder as folhas de calculo do utilizador pf, utilizando a funcionalidade de exportacao. 
			//--------------------------------------------------------------------------
			
			System.out.println("9.Aceder as folhas de calculo do utilizador pf. ");
			
			tm.commit();
			committed = true;
		}catchSystemException| NotSupportedException | RollbackException| HeuristicMixedException | HeuristicRollbackException ex) {
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
	

    
	}
