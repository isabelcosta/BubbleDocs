package pt.tecnico.ulisboa.essd.bubbledocs.services.integrator;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public abstract class BubbleDocsIntegrator {
	
	public BubbleDocsIntegrator() {
	}
	
	
    public final void execute() throws BubbleDocsException {
        dispatch();
    }
    
    public static Bubbledocs getBubbleDocs() {
        return FenixFramework.getDomainRoot().getBubbledocs();
    }

    protected abstract void dispatch() throws BubbleDocsException;

}
