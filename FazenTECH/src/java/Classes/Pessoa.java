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
public class Pessoa {

    private int _idPessoa;
    private String _nome;
    private String _cpf;
    private Date _dataNasc;
    private String _tel;

    public Pessoa(int _idPessoa, String _nome, String _cpf, Date _dataNasc, String _tel) {
        this._idPessoa = _idPessoa;
        this._nome = _nome;
        this._cpf = _cpf;
        this._dataNasc = _dataNasc;
        this._tel = _tel;
    }
}
