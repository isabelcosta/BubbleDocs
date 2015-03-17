package pt.tecnico.ulisboa.essd.bubbledocs.domain;

public class Parser {

    public static void parseExpressao(FolhadeCalculo folha, String expressao) throws Exception{
		String[] args = expressao.split("\\|");
		int[] endereco = null;

		endereco = parseEndereco(args[0], folha);
		
		Conteudo cont = null;
	
		if (args.length > 1)
		    cont = parseConteudo(folha, args[1]);
    }

    
    
    public static int[] parseEndereco(String endereco, FolhadeCalculo folha) throws Exception{
		String[] args = endereco.split(";");
		
		int[] vec = {Integer.parseInt(args[0]), Integer.parseInt(args[1])};
		
		if(vec[0] <=0 || vec[0] > folha.getLinhas())
			throw new Exception();
		if(vec[1] <=0 || vec[1] > folha.getColunas())	
			throw new Exception();
		return vec;
    }

    
    
    public static Conteudo parseConteudo(FolhadeCalculo folha, String conteudo) throws Exception {
		
    	if (conteudo.contains("(")) {                 			// é uma função
		    String funcao = conteudo.substring(1);    			// remove =
		    String nomeFuncao = parseNomeFuncao(funcao);
		    String Operando = parseOperandoFuncao(funcao);

		    
		    if (conteudo.contains(","))
		    	return parseFuncaoBinaria(folha, nomeFuncao, Operando);
		    
		    /*else
		    	return parseFuncaoIntervalo(folha, nomeFuncao, Operando); */
		    
		} else if (conteudo.contains("=")) {          			 // é uma referencia 
		    return parseReferencia(folha, conteudo.substring(1));
		
		} else
		    return parseLiteral(conteudo);
    	
		return null;
    }

    
    
    private static String parseNomeFuncao(String funcao) {
    	return funcao.substring(0, funcao.indexOf("("));
    }

    
    
    
    private static String parseOperandoFuncao(String funcao) {
    	return funcao.substring(funcao.indexOf("(") + 1, funcao.indexOf(")"));
    }
    
    

    
    public static FuncaoBinaria parseFuncaoBinaria(FolhadeCalculo folha, String nomeFuncao, String Operando) throws Exception{
		String[] Operandos = Operando.split(",");
	
		Argumento arg1 = parseOperando(folha, Operandos[0]);
		Argumento arg2 = parseOperando(folha, Operandos[1]);
	
		switch(nomeFuncao) {
			case "MUL":
			    return new MUL(arg1,arg2);
			case "DIV":
			    return new DIV(arg1,arg2);
			case "SUB":
			    return new SUB(arg1,arg2);
			case "ADD":
				return new ADD(arg1,arg2);
			}
		
		return null;
    }

    
   /* public static IntervaloFunction parseFuncaoIntervalo(FolhadeCalculo folha, String nomeFuncao, String Operando) throws Exception{
		Intervalo intervalo = parseIntervalo(folha, Operando);
	
		switch(nomeFuncao) {
			case "PRD":
			    return new Prd(intervalo);
			case "AVG":
			    return new Avg(intervalo);
			}
		
		return null;
    } */

    
    public static Argumento parseOperando(FolhadeCalculo folha, String Operando) throws Exception {
		if (Operando.contains(";")) {
		    return parseReferencia(folha, Operando);
		}
		
		return parseLiteral(Operando);
		
    }


    
    
    public static Literal parseLiteral(String literal) {
    	return new Literal(Integer.parseInt(literal));  	 // o Operando que representa o literal 
    }

    
    
    public static Referencia parseReferencia(FolhadeCalculo folha, String referencia) throws Exception{
    		int[] i = parseEndereco(referencia,folha);
    		
    		for (Celula c : folha.getCelulaSet()){
    			if (c.getLinha() == i[0] && c.getColuna() == i[1]){
//    				System.out.println("|| Va " + c.getConteudo().getValor()+ " ||");
//    				System.out.println("|| c " + c.getColuna()+ " ||");
//    				System.out.println("entrou no if");
    					return new Referencia(c);
    				}
    			}
    		return new Referencia(null);
    }
    

}