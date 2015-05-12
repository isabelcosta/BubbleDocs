package pt.tecnico.ulisboa.essd.bubbledocs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.ulisboa.essd.bubbledocs.services.dtos.UserDto;
import pt.tecnico.ulisboa.essd.bubbledocs.services.local.GetUserInfoService;


public class GetUserInfoTest extends BubbleDocsServiceTest {
	
	private static String USERNAME_DOES_NOT_EXISTS = "frc";
	
	private Utilizador user;

	public void populate4Test() {

	    unPopulate4Test();
	   	
		//Cria utilizador
	    user = createUser("mas", "maria@email.com", "Maria Santos");   	
	   		
	 }
	
	 @Test
	    public void success() {
	    
		    GetUserInfoService userInfo = new GetUserInfoService("mas");
		    userInfo.execute();
		    
		    UserDto userDTO = userInfo.getResult();
		    
		    assertEquals(userDTO.getUsername() , user.getUsername());
	        assertEquals(userDTO.getEmail(), user.getEmail());
	        assertEquals(userDTO.getName(), user.getNome());   	
	    }
	 
	 
	 @Test(expected = LoginBubbleDocsException.class)
	    public void userDoesNotExist() {
		 
		 GetUserInfoService userInfo = new GetUserInfoService(USERNAME_DOES_NOT_EXISTS);
		 userInfo.execute();
		 
	 }
	 
}
