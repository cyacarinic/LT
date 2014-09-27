/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Brenda
 */
public class Tokens {
    private int token;
    private String lexema;
    private String descripcion;
    
    public Tokens(int token,String lexema,String descripcion){
        this.token=token;
        this.lexema=lexema;
        this.descripcion=descripcion;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
