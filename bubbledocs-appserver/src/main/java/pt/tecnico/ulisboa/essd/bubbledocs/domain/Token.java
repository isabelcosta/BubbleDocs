package pt.tecnico.ulisboa.essd.bubbledocs.domain;
import org.joda.time.LocalTime;

public class Token extends Token_Base {
    
    public Token(String user, String token) {
    	super();   	
    	setTime(new LocalTime());
    	setUsername(user);
    	setToken(token);

    }
    
}
