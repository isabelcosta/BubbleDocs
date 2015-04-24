Documentação do LoginUser
=========================
## Dúvidas
Não hesitem em perguntar alguma questão. _Francisco Caixeiro_ e _Vicente Rocha_

## Introdução ao LoginUser
O `LoginUser` trata da autenticação dos Utilizadores.
 1. Efectua o login de um utilizador na aplicação autenticando-o.
 2. Apenas os utilizadores que tenham uma sessão válida é que podem aceder à aplicação. 
 3. Qualquer acesso feito por um utilizador que não esteja em sessão deve ser recusado pela aplicação.
 4. Qualquer acesso feito pelo utilizador enquanto a sessão é válida reinicia o período de validade da sessão do utilizador por mais duas horas.
 5. Após ficar com a sua sessão inválida, caso o utilizador queira aceder à aplicação novamente terá que realizar o login de novo.
 6. Quando um utilizador efectua um login são removidos da sessão todos os utilizadores que tenham expirado.
 7. O `Token` é composto pela concatenação do username com um número entre 0 e 9 gerado aleatoriamente.
 8. Este `Token` deve ser utilizado nos restantes serviços para identificar o utilizador que quer realizar a operação.

### 1 (_LoginUser.java_)
```java

    for(Utilizador user : Bubbledocs.getInstance().getUtilizadoresSet()){
        if(user.getUsername().equals(_username)){
            if(!user.getPassword().equals(_password)){
                throw new WrongPasswordException("Password incorrecta!");
            } else {
                result = setUserToken(user.getUsername());
                return ;
            }
        } 
    }
    throw new UtilizadorInvalidoException("Utilizador não existe!"); 

```

### 2, 3, 4 e 8 (Em todos os serviços)
```java

    if(!validSession(tokenUserLogged)){
        throw new DontHavePermissionException("Session for user " + tokenUserLogged.substring(0, tokenUserLogged.length()-1) + " is invalid" );
    }else{
        refreshToken(tokenUserLogged);
    }    

```


### 6 (_BubbleApplication.java_)
Deverá ser feito no _BubbleApplication.java_ ou faz-se dentro do serviço _LoginUser.java_ ?
```java

    for(Token token : Bubbledocs.getInstance().getTokensSet()){
        int minutes = Minutes.minutesBetween(token.getTime(), new LocalTime()).getMinutes();
        if(minutes > 120){
            Bubbledocs.getInstance().getTokensSet().remove(token);
        }
    } 

```

### 7 (_LoginUser.java_)
```java

    public final String setUserToken(String user) {
        String userToken = user + generateToken();
        return userToken;
    }
    
    public int generateToken(){
        Random rand = new Random(); 
        int intToken = rand.nextInt(10);
        return intToken;
    }

```

### Iterar sobre Tokens
```java

    Boolean existsToken = false;
    for(Token token : Bubbledocs.getInstance().getTokensSet()){
        if(token.getUsername().equals("<USERNAME>")){
            existsToken = true;
        }
    }
    if(!existsToken){
        //DO SOMETHING
    }

```
