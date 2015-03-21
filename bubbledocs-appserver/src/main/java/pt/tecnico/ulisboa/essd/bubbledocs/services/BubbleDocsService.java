package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

// add needed import declarations

public abstract class BubbleDocsService {

    @Atomic
    public final void execute() throws BubbleDocsException {
        dispatch();
    }
    
    public static Bubbledocs getBubbleDocs() {
        return FenixFramework.getDomainRoot().getBubbledocs();
    }

    protected abstract void dispatch() throws BubbleDocsException;
}
