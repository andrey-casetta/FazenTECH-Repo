/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.Date;

/**
 *
 * @author andre
 */
public class Usuario extends Pessoa  {

    public static String getLogin() {
        return _login;
    }

    public String getSenha() {
        return _senha;
    }

    public static boolean isLogado() {
        return _logado;
    }

    public void setLogado(boolean _logado) {
        this._logado = _logado;
    }
    
    public static int getIdPessoa() {
        return _idPessoa;
    }

    public static void setIdPessoa(int _idPessoa) {
        Usuario._idPessoa = _idPessoa;
    }

    private static String _login;

    public static void setLogin(String _login) {
        Usuario._login = _login;
    }
    
    private String _senha;
    private static boolean _logado;
    private static int _idPessoa;


    
    public Usuario(int _idPessoa, String _login, String _senha, String _nome, String _cpf, Date _dataNasc, String _tel) {
        super(_idPessoa, _nome, _cpf, _dataNasc, _tel);
    }
    
    
    public void UsuarioLogado(){
        
    }
 
}
