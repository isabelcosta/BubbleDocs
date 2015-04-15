//package pt.tecnico.ulisboa.essd.bubbledocs.services;
//
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.Bubbledocs;
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.Celula;
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.FolhadeCalculo;
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.Parser;
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.Token;
//import pt.tecnico.ulisboa.essd.bubbledocs.domain.Utilizador;
//import pt.tecnico.ulisboa.essd.bubbledocs.exception.BubbleDocsException;
//import pt.tecnico.ulisboa.essd.bubbledocs.exception.NotLiteralException;
//import pt.tecnico.ulisboa.essd.bubbledocs.exception.OutOfBoundsException;
//import pt.tecnico.ulisboa.essd.bubbledocs.exception.RemoteInvocationException;
//import pt.tecnico.ulisboa.essd.bubbledocs.exception.SpreadSheetDoesNotExistException;
//import pt.tecnico.ulisboa.essd.bubbledocs.exception.UnauthorizedOperationException;
//import pt.tecnico.ulisboa.essd.bubbledocs.services.remote.IDRemoteServices;
//
//
//public class RenewPasswordService extends BubbleDocsService {
//    private String result;
//    private String newPassword;
//    private String userToken;
//    
//    
//    public RenewPasswordService(String tokenUser) {
//    	
//    	this.userToken = tokenUser;
//    }
//
//    
//    @Override
//    protected void dispatch() throws BubbleDocsException {
//    	
//    	Bubbledocs bd = Bubbledocs.getInstance();
////    	try {
//    		//Verifica se a pessoa aidna esta logada
//			if(bd.validSession(userToken)){
//				refreshToken(userToken);
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
//					
//					Utilizador user = bd.getUserFromToken(userToken);
//					
//					//O RENEW NAO DEVOLVCE NADA COMO TENHO A NOVA PASS?????
////					user.setPassword(newPassword);
//					
//				} catch (RemoteInvocationException rie) {
//					throw new UnavailableServiceException();
//				}
//				//...
//
//			    }
//				
//			}
////		} catch (UnauthorizedOperationException | NotLiteralException | OutOfBoundsException | SpreadSheetDoesNotExistException e) {
////			System.err.println("Couldn't assign Literal: " + e);
////		}
//    	
////    }
//
//    public String getResult() {
//        return result;
//    }
//
//}