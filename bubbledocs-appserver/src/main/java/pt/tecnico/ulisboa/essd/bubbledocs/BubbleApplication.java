
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

		 FolhadeCalculo fc = FolhadeCalculo.getInstance();
			 populateDomain(fc);

	    	//remover folha1 do utilizador pf
	    	if(folha1.isDono("pf"))
	    		user1.removeFolha("Notas ES");
	    	else
	    		System.out.println("Não é o dono da folha!");
	    	
	    	//Escrever os nomes e ids todas as folhas de calculo do pf
	    	System.out.println("Estas sao as minhas folhas:");
	    	for(FolhadeCalculo folha : user1.getFolhascriadasSet()){
	    		System.out.println("Nome:"+ folha.getNomeFolha()+ "Id:" + folha.getId());
	    			   	
	    	}
			for(Utilizador user : ){}


			//--------------------------------------------------------------------------
			//Escrever a informacao sobre todos os utilizadores registados na aplicacao.
			//--------------------------------------------------------------------------
			
			
			
			
			
			
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
	
		// inicia o estado persistente da aplicacao caso este nao esteja inicializado
		// dados relativos ao cenario de teste
	
		static void populateDomain() {
    
		if (!pb.getPersonSet().isEmpty())
    	    return;

       	Utilizador user1 = new Utilizador(Paul Door, pf, sub);
    	Utilizador user2 = new Utilizador(Step Rabbit, ra, cor);
    	FolhadeCalculo folha = new FolhadeCalculo(pf, Notas ES, 300, 20);

    	//--------------Inserir conteudo na folha---------------
    	
    	//-->Literal 5 na posicao (3, 4)
		Literal conteudoLiteral = new Literal(new Integer.parseInt(5));
		folha.addCelula(new Celula(3, 4, conteudoLiteral));
		
		//-->Referencia para a celula (5, 6) na posicao (1, 1)
//		Referencia conteudoReferencia = new Referencia(5,6);
		Funcao conteudoReferencia = Parser.parseConteudo("=5;6");
		folha.addCelula(new Celula(1,1, conteudoReferencia));
		
		//-->Funcao = ADD(2, 3; 4) na posicao (5, 6)
		Funcao conteudoAdd = Parser.parseConteudo("=ADD(2,3;4)");
		folha.addCelula(new Celula(5,6,conteudoAdd));
		
		//-->Funcao = DIV (1; 1, 3; 4) na posicao (2, 2)
		Funcao conteudoDiv = Parser.parseConteudo("=DIV(1;1,3;4)");
		folha.addCelula(new Celula(2,2,conteudoDiv));
		
		}
    
	}
