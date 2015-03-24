package pt.tecnico.ulisboa.essd.bubbledocs.services;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

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
    
    
    public final Boolean validSession(String token) {
    	for(Token token2 : Bubbledocs.getInstance().getTokensSet()){
			if(token2.getToken().equals(token)){
				int minutes = Minutes.minutesBetween(token2.getTime(), new LocalTime()).getMinutes();
				if(minutes > 120){
					Bubbledocs.getInstance().getTokensSet().remove(token2);
					return false;
				}else{
					return true;
				}
			}
		}
    	return false;
    }
    
    public final void refreshToken(String token) {
    	for(Token tokenObject : Bubbledocs.getInstance().getTokensSet()){
    		if(tokenObject.getToken().equals(token)){
    			tokenObject.setTime(new LocalTime());
    		}
    	}
    }
    
    public final Boolean isRoot(String token){		
    	return token.substring(0, token.length()-1).equals("root");
    	
    }
    
}
