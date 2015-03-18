package pt.tecnico.ulisboa.essd.bubbledocs.service.dtos;

public class UserDto  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String newUsername;
	private String password;
	private String name;
	
	 public UserDto(String newUsername, String password, String name){
		 
		 this.newUsername = newUsername;
		 this.password = password;
		 this.name = name;
	    
	    }

	public String getNewUsername() {
		return newUsername;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

}
