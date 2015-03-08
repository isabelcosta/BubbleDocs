/*
package pt.tecnico.ulisboa.essd.bubbledocs;

import java.util.Set;

import javax.transaction.SystemException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.*;


public class BubbleApplication {

	public static void main(String[] args) {

		System.out.println("Welcome to Bubble application");

		TransactionManager tm = FenixFramework.getTransactionManager();
		boolean committed = false;


		try {
			tm.begin();

			Utilizador utilizador = Utilizador

			for(Utilizador user : ){}


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
		static void populateDomain() {
    	if (!pb.getPersonSet().isEmpty())
    	    return;

    	// setup the initial state if phonebook is empty

    	Utilizador user1 = new Utilizador(Paul Door, pf, sub);
    	Utilizador user2 = new Utilizador(Step Rabbit, ra, cor);
    	FolhadeCalculo folha = new FolhadeCalculo(pf, Notas ES, 300, 20);

    }
	} */