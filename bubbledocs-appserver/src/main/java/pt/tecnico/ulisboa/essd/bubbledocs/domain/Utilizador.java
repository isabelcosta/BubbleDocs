package pt.tecnico.ulisboa.essd.bubbledocs.domain;

public class Utilizador extends Utilizador_Base {
    
    public Utilizador(String nomeUtilizador, String userName, String password) {
        super();
        setNome(nomeUtilizador);
		setUsername(userName);
		setPassword(password);
    }
    
}
