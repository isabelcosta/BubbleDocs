package pt.tecnico.ulisboa.essd.bubbledocs.services.local;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom2.Document;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.ulisboa.essd.bubbledocs.exception.CannotLoadDocumentException;

public class ImportDocumentService extends BubbleDocsService {

	private org.jdom2.Document _jdomDoc;
	private Bubbledocs _bd = Bubbledocs.getInstance();
	private byte[] _folha;
	private String _userToken;
	private Integer _result;
	
	public ImportDocumentService(byte[] folha, String userToken) {
		_folha = folha;
		_userToken = userToken;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
//		if (_folha == null) {
//			throw new CannotLoadDocumentException("folha");
//		}
			
	// verifica se user exportou a folha
		
		/*
		 *  converter de byte[] para jdomDoc
		 */
		String folhaStr = null;
		
	// 1. converter os bytes numa string com codificacao UFT-8
		try { 
			folhaStr = new String(_folha, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("import falhou: " + e);
		}
	
	// 2. converter a string num w3c Document	
		org.w3c.dom.Document folhaXMLDoc = loadXML(folhaStr);
	
	// 3. criar um DOMBuilder para converter o w3c Document num jdom2
		DOMBuilder domBuilder = new DOMBuilder();
	
	// 4. chamar o metodo build para converter o doc
		_jdomDoc = 	domBuilder.build(folhaXMLDoc);


		/*
		 * chamar a funcao que importa o jdomDoc para a base de dados do programa
		 */
		_result = _bd.recoverFromBackup(_jdomDoc, _userToken);
	}
	
	public org.w3c.dom.Document loadXML(String xml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		org.w3c.dom.Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			StringReader stream = new StringReader(xml);
			doc = builder.parse(new InputSource(stream));
//			stream.reset();
		} catch (Exception e){
			e.printStackTrace();
		}
		return doc;
	}
	
	public Integer getResult() {
		return _result;
	}
}
