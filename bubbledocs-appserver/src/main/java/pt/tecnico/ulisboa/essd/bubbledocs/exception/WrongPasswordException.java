package pt.tecnico.ulisboa.essd.bubbledocs.exception;

public class WrongPasswordException extends BubbleDocsException {
	
	 private static final long serialVersionUID = 1L;
	
	 private String username;
	
	 
	 
	 public WrongPasswordException() {
	 
	 }
	 
	 
	 public WrongPasswordException(String username) {
	        
		 this.username = username;
	 
	 }
	 
	 
	 public String getName() {
	        return this.username;
	 }
	 
	 
	 public String getMessage(){
		 return "Introduziu a password errada para o utilizador " + username + ".";
	 
	 }
	 
}