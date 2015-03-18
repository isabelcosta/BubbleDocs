package pt.tecnico.ulisboa.essd.bubbledocs.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
import pt.tecnico.ulisboa.essd.bubbledocs.service.ExportDocument;
import pt.tecnico.ulisboa.essd.bubbledocs.service.dtos.SpreadSheet;
import pt.tecnico.ulisboa.essd.bubbledocs.service.exception.DuplicateUsernameException;
import pt.tecnico.ulisboa.essd.bubbledocs.service.exception.InvalidSpreadSheetException;

// add needed import declarations

public class ExportDocumentTest extends BubbleDocsServiceTest {

    // the tokens
    private String root;
    private String frc;

    private static final String OWNER = "frc";
    private static final String PASSWORD = "frc";
    private static final String ROOT_USERNAME = "root";
    private static final String SPREADSHEET_NAME = "folha1";
    private static final String SPREADSHEET_DONT_EXIST = "nao-existe";
    
    @Override
    public void populate4Test() {
        createUser(OWNER, PASSWORD, "Fabio Carvalho ");
        root = addUserToSession("root");
        frc = addUserToSession("frc");
        Utilizador dono = new Utilizador("Fabio Carvalho",OWNER,PASSWORD);
        SpreadSheet folha = createSpreadSheet(dono,OWNER,10,10);
    }
    
    
    @Test(expected = InvalidSpreadSheetException.class)
    public void success_() {
        ExportDocument service = new ExportDocument(frc,-1);
        service.execute();
    }
}