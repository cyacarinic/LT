/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yacarini
 */
public class Scanner {
    private String cod; //variable para codigo completo
    private String palabra_clave[]={"_while","_if","_main"};    //palabras clave
    private String buffer="";   //variable para almacenamiento temporal
    private int caso=0;
    private int token[];
    private String Lexema[];
    private String Descripcion[];
    private char aux;
    private boolean error=false;
    private int numError=0;
    private Tokens tablaFull[];
    private Tokens tablaSimbolos[];
    private int topTF=0;
    private int topTS=0;
    
    public Scanner(){
        tablaFull=new Tokens[50];
        tablaSimbolos=new Tokens[50];
    }
    
    public Scanner(String cod){
        cod=cod.trim();
        this.cod=cod+'#';
    }
    
    public void setCadena(String cod){
        cod=cod.trim();
        this.cod=cod+'#';
    }
    
    
    
    public boolean analizadorLexico(){
        int posComm=0;
        String temp="";
        boolean errorLatente=false;
        int indNum=1;
        int indId=1;
        int indDec=1;
        buffer="";
        //System.out.println(cod);
        //for(int i=0;i<cod.length();i++){
        //    System.out.println("caracter ["+i+"]="+cod.charAt(i));
        //}
        
        int col=0;
        for(;;){
            //System.out.println("buffer="+buffer+"  col="+col+"  caso:"+caso);
            aux=cod.charAt(col);
            //si signo terminal aparece
            if(aux=='#'||aux==' '){
                
                switch(caso){
                    case 0: col++;caso=0;
                            break;
                    case 1: agregarToken(100+indNum,buffer,"numero");
                            indNum++;
                            col++;caso=0;buffer="";
                            break;
                    case 2: agregarToken(200+indId,buffer,"ID");
                            indId++;
                            col++;caso=0;buffer="";
                            break;
                    case 3: agregarToken(100+indNum,temp,"numero"); 
                            indNum++;
                            col++;caso=0;buffer="";setError(true);numError=col+1;
                            break;
                    case 4: agregarToken(300+indDec,buffer,"decimal");
                            indDec++;
                            col++;caso=0;buffer="";
                            break;
                    case 6: agregarToken(200+indId,temp,"ID");
                            indId++;
                            setError(true);numError=col;col++; caso=0;
                            break;
                    case 8: if(aux=='#'){
                                col=posComm;caso=0;aux='a';
                                
                            }else{
                                col++;caso=8;
                            }
                            break;
                }              
                if(aux=='#'&&errorLatente==true){
                    error=true; numError=col;
                }
                if(aux=='#'||(aux==' '&&error==true)){     //si es el primer simbolo
                    if(error==true){
                        System.out.println("Error en el programa.Columna:"+numError);
                    }else{
                        System.out.println("No hubieron errores.");
                    }
                    System.out.println("Fin de programa.");
                    break;
                }else{          //sino
                    
                }
            }
            else{
                switch(caso){
                    case 0: if(Character.isDigit(aux)){                                
                                caso=1; buffer=buffer+aux; col++;
                            }else{ 
                                if(Character.isLetter(aux)){
                                    caso=2;buffer=buffer+aux;col++;                                
                                }
                                if(aux=='_'){
                                    caso=5; buffer=buffer+aux;temp="";col++;
                                }
                                if(aux=='('){
                                    caso=7; col++;
                                }
                            } 
                            if((!Character.isLetterOrDigit(aux))&&aux!='_'&&aux!='('){
                                caso=0;error=true;numError=col;col++;
                            }
                            break;
                    //NUMEROS
                    case 1: if(Character.isDigit(aux)){
                                caso=1; col++; buffer=buffer+aux;
                            }else{ 
                                if(Character.isLetter(aux)){
                                    caso=2; agregarToken(100+indNum,buffer,"numero"); 
                                    indNum++;
                                    buffer=""+aux; col++; setError(true); numError=col+1;
                                }
                                else{ 
                                    if(aux=='.'){
                                        temp=buffer;buffer=buffer+'.'; col++; caso=3;
                                    }else{
                                        caso=0; agregarToken(100+indNum,buffer,"numero"); 
                                        indNum++;
                                        buffer=""; col++; setError(true); numError=col+1;
                                    }
                                }
                            }
                            break;
                    //ID
                    case 2: if(Character.isLetterOrDigit(aux)){
                                caso=2; col++;buffer=buffer+aux;
                            }else{
                                setError(true); agregarToken(200+indId,buffer,"ID");
                                indId++;
                                buffer=""; col++; caso=0;numError=col+1;
                            }
                            break;
                    //paso intermedio decimal
                    case 3: if(Character.isDigit(aux)){
                                buffer=buffer+aux; col++;
                                caso=4; 
                            }else{
                                if(Character.isLetter(aux)){
                                    caso=2; agregarToken(100+indNum,temp,"numero"); 
                                    indNum++;
                                    buffer=""+aux; col++; setError(true); numError=col+1;
                                }else{                        
                                    caso=0; agregarToken(100+indNum,temp,"numero"); 
                                    indNum++;
                                    buffer=""; col++; setError(true); numError=col+1;
                                }
                            }
                            break;
                    //decimal
                    case 4: if(Character.isDigit(aux)){
                                buffer=buffer+aux; col++;
                                caso=4;
                            }else{
                                caso=0; agregarToken(300+indDec,buffer,"decimal"); 
                                indDec++;
                                buffer=""+aux; col++; setError(true); numError=col+1;
                            }
                            break;
                    //paso intermedio palabra clave
                    case 5: if(Character.isLetter(aux)){
                                buffer=buffer+aux; col++;
                                temp=temp+aux; caso=6;
                            }else{
                                if(Character.isDigit(aux)){
                                    numError=col;setError(true); buffer=temp+aux;col++;
                                    caso=1;
                                }
                            }
                            break;
                    //palabra clave
                    case 6: if(Character.isLetter(aux)){
                                temp=temp+aux; buffer=buffer+aux;
                                if(verificaClave(buffer)){
                                    caso=0; agregarToken(400,buffer,"palabra clave"); 
                                    buffer=""; col++;
                                }else{
                                    col++; 
                                }
                            }else{
                                if(Character.isDigit(aux)){
                                    caso=2; setError(true);numError=col; 
                                    buffer=temp; col++; 
                                }else if(!Character.isLetterOrDigit(aux)){
                                    setError(true); agregarToken(200,temp,"ID");
                                    buffer=""; col++; caso=0;numError=col;
                                }
                            }
                        break;
                    //comentario cerrado
                    case 7: if(aux=='*'){
                                caso=8; col++; posComm=col;
                                errorLatente=true;
                            }else{
                                caso=0;setError(true);numError=col;
                            }
                            break;
                     //ignorando comentarios
                    case 8: if(aux!='*'){
                                col++;
                            }else{
                                caso=9;col++;
                            }
                            break;
                    //marca fin de comentario cerrado
                    case 9: if(aux==')'){
                                caso=0;buffer="";col++;
                                errorLatente=false;
                            }else{
                                caso=8;col++;
                            }
                            break;
                }
            }
        }
        return error;
    }
    
    public void agregarToken(int token,String lexema,String descrip){
        //System.out.println("TOKEN\tLEXEMA\tDESCRIPCION");        
        //System.out.println(token+"\t"+lexema+"\t"+descrip);
        Tokens Aux=new Tokens(token,lexema,descrip);
        tablaFull[getTopTF()]=Aux;
        if(Aux.getToken()!=400){
            tablaSimbolos[getTopTS()]=Aux;
            topTS++;
        }
        topTF++;
        
    }
    
    public boolean verificaClave(String cadena){
        boolean clave=false;
        for(int i=0;i<palabra_clave.length;i++){
            if(cadena.equals(palabra_clave[i])){
                clave=true;
            }
        }
        return clave;
    }

    public int getTopTF() {
        return topTF;
    }

    public int getTopTS() {
        return topTS;
    }

    public Tokens[] getTablaFull() {
        return tablaFull;
    }

    public Tokens[] getTablaSimbolos() {
        return tablaSimbolos;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setTablaFull(Tokens[] tablaFull) {
        this.tablaFull = tablaFull;
    }

    public void setTablaSimbolos(Tokens[] tablaSimbolos) {
        this.tablaSimbolos = tablaSimbolos;
    }

    public void setTopTF(int topTF) {
        this.topTF = topTF;
    }

    public void setTopTS(int topTS) {
        this.topTS = topTS;
    }
}
