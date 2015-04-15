//package pt.tecnico.ulisboa.essd.bubbledocs.services;
//
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
//import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
//import pt.tecnico.ulisboa.essd.bubbledocs.exception.NotLiteralException;
//import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
//import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
//import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
//
//
//public class RenewPasswordService extends BubbleDocsService {
//    private String result;
//    private String newPassword;
//    private Token userToken;
//    
//    
//    public RenewPasswordService(Token tokenUser) {
//    	
//    	this.userToken = tokenUser;
//    }
//
//    
//    @Override
//    protected void dispatch() throws BubbleDocsException {
//    	
//    	Bubbledocs bd = Bubbledocs.getInstance();
//    	try {
//    		//Verifica se a pessoa aidna esta logada
//			if(bd.validSession(userToken.getToken())){
//				refreshToken(userToken.getToken());
//				
//				/*
//				 * Nota: Os serviccos a desenvolver ou a actualizar tem que verificar sempre se o token indicado ˆ
//				 * representa um utilizador com uma sessao activa, devendo ser actualizado a data do ultimo 
//				 * acesso associada ao token
//				 * */
//				
//				IDRemoteServices remote;
//				//...
//
//				try {
//					// invoke some method on remote
//					// remote.renewPassword(......);
//				} catch (RemoteInvocationException rie) {
//					throw new UnavailableServiceException();
//				}
//				//...
//
//				
////				FolhadeCalculo folha = bd.getFolhaOfId(folhaId);
////				
////				if(folha.podeEscrever(bd.getUsernameOfToken(tokenUserLogged))){
////			    	int[] linhaColuna = null;
////			
////					linhaColuna = Parser.parseEndereco(cellToFill, folha);
////					
////					//Verifica se o literal e um inteiro
////					try{
////						Integer.parseInt(literalToAssign);
////					}catch(Exception e){
////						throw new NotLiteralException(literalToAssign);
////					}
////					
////					
////			    	folha.modificarCelula( linhaColuna[0], linhaColuna[1], literalToAssign);
////					
////			    	for(Celula cell: folha.getCelulaSet()){
////			    		if(cell.getLinha() == linhaColuna[0] && cell.getColuna() == linhaColuna[1]){
////			    			result = cell.getConteudo().getValor().toString();
////			    		} 
////			    	}
//			    }
//				
//			}
//		} catch (UnauthorizedOperationException | NotLiteralException | OutOfBoundsException | SpreadSheetDoesNotExistException e) {
//			System.err.println("Couldn't assign Literal: " + e);
//		}
//    	
//    }
//
//    public String getResult() {
//        return result;
//    }
//
//}