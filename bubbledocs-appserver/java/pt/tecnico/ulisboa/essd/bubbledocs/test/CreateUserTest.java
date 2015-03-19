package pt.tecnico.ulisboa.essd.bubbledocs.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.service.dtos.User;
import pt.tecnico.ulisboa.essd.bubbledocs.service.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.service.exception.EmptyUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.service.exception.UnauthorizedOperationException;
import pt.tecnico.ulisboa.essd.bubbledocs.service.exception.UserNotInSessionException;

// add needed import declarations

public class CreateUserTest extends BubbleDocsServiceTest {

    // the tokens
    private String root;
    private String ars;

    private static final String USERNAME = "ars";
    private static final String PASSWORD = "ars";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";

    @Override
    public void populate4Test() {
        createUser(USERNAME, PASSWORD, "António Rito Silva");
        root = addUserToSession("root");
        ars = addUserToSession("ars");
    }

    @Test
    public void success() {
        CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST, "jose",
                "José Ferreira");
        service.execute();

	// User is the domain class that represents a User
        User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

        assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
        assertEquals("jose", user.getPassword());
        assertEquals("José Ferreira", user.getName());
    }

    @Test(expected = DuplicateUsernameException.class)
    public void usernameExists() {
        CreateUser service = new CreateUser(root, USERNAME, "jose",
                "José Ferreira");
        service.execute();
    }

    @Test(expected = EmptyUsernameException.class)
    public void emptyUsername() {
        CreateUser service = new CreateUser(root, "", "jose", "José Ferreira");
        service.execute();
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void unauthorizedUserCreation() {
        CreateUser service = new CreateUser(ars, USERNAME_DOES_NOT_EXIST, "jose",
                "José Ferreira");
        service.execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void accessUsernameNotExist() {
        removeUserFromSession(root);
        CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST, "jose",
                "José Ferreira");
        service.execute();
    }

}
