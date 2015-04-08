package pt.tecnico.ulisboa.essd.bubbledocs.exception;

import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;


public class ReferenciaInvalidaException extends BubbleDocsException {

	private static final long serialVersionUID = 1L;
	
	private FolhadeCalculo folha;
	private String conteudoInvalido;
	
	public ReferenciaInvalidaException(FolhadeCalculo folha, String conteudo) {
		
		this.folha = folha;
		this.conteudoInvalido=conteudo;
		
	}
	
	public String getReferenciaInvalida() {
		return this.folha + this.conteudoInvalido;
	}
	
}