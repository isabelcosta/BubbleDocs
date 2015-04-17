package pt.tecnico.ulisboa.essd.bubbledocs.services;

import org.joda.time.LocalTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;

public abstract class BubbleDocsService {

    @Atomic
    public final void execute() throws BubbleDocsException {
        dispatch();
    }
    
    public static Bubbledocs getBubbleDocs() {
        return FenixFramework.getDomainRoot().getBubbledocs();
    }

    protected abstract void dispatch() throws BubbleDocsException;
    
    
    public final void refreshToken(String token) {
    	for(Token tokenObject : Bubbledocs.getInstance().getTokensSet()){
    		if(tokenObject.getToken().equals(token)){
    			tokenObject.setTime(new LocalTime());
    		}
    	}
    }
    
}
