package pt.tecnico.ulisboa.essd.bubbledocs.services.dtos;

public class UserDto  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String email;
	private String name;
	
	 public UserDto(String username, String email, String name){
		 
		 this.username = username;
		 this.email = email;
		 this.name = name;
	    
	    }

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

}
