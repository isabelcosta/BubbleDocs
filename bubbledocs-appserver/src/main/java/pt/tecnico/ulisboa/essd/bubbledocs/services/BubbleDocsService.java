package pt.tecnico.ulisboa.essd.bubbledocs.services;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

// add needed import declarations

public abstract class BubbleDocsService {

    @Atomic
    public final void execute() throws BubbleDocsException {
        dispatch();
    }

    protected abstract void dispatch() throws BubbleDocsException;
}
