package pt.tecnico.ulisboa.essd.bubbledocs.domain;

<<<<<<< Upstream, based on origin/master
import java.util.Random;

public class Token extends Token_Base {
    
	String username;
	
    public Token(String nameUser) { //username ou utilizador??
        super();
        nameUser = username;
    }
    
    public String getUserToken (String username) {
    	
    	String userToken = username + generateToken();

    	return userToken;
    }
    
    public int generateToken(){
    	
        Random rand = new Random(); 
        //This will give value from 0 to 9.
        int intToken = rand.nextInt(10);
        
        return intToken;
=======
import org.joda.time.LocalTime;

public class Token extends Token_Base {
    
    public Token(String user, String token) {
    	super();   	
    	setTime(new LocalTime());
    	setUsername(user);
    	setToken(token);
>>>>>>> 3d15570 LoginUser
    }
    
}
