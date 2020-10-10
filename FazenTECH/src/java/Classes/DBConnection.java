package Classes;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {

    private static final String CONNECTION_DATABASE_STRING = "jdbc:sqlserver://localhost:1433;" + "databaseName=fazenda-bd";
    private Connection con;
    private boolean isConnected;

    public static void main(String[] args) {

    }

    public void Connect() {
        String connectionUrl = CONNECTION_DATABASE_STRING;

        if (!isConnected) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
                con = DriverManager.getConnection(connectionUrl, "sa", "admin");
                System.out.println("Conexão com banco realizada com sucesso!!!");
                isConnected = true;
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState:" + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
                isConnected = false;
            } catch (Exception e) {
                System.out.println("Não foi possível conectar ao banco!!!" + e);
                isConnected = false;
            }
        }
    }

    public boolean ConnectAndLogin(String login, String senha) {
        Connect();
        try {
            String sql = "SELECT * FROM USUARIO WHERE login_usuario ='" + login + "' AND senha_usuario = '" + senha + "'";
            if (!con.isClosed()) {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int idPessoa = rs.getInt("id_pessoa");
                    sql = "SELECT * FROM PESSOA WHERE ID_PESSOA = " + idPessoa;
                    PreparedStatement ps2 = con.prepareStatement(sql);
                    ResultSet rs2 = ps2.executeQuery();

                    String nome = "";
                    String cpf = "";
                    String tel = "";
                    Date dataNasc = null;

                    if (rs2.next()) {
                        nome = rs2.getString("nome_pessoa");
                        cpf = rs2.getString("cpf_pessoa");
                        tel = rs2.getString("tel_pessoa");
                        dataNasc = rs2.getDate("datanasc_pessoa");
                    }

                    Usuario usu = new Usuario(idPessoa, login, senha, nome, cpf, dataNasc, tel);
                    usu.setLogado(true);
                    Usuario.setLogin(login);
                    Usuario.setIdPessoa(idPessoa);

                } else {
                    con.close();
                    return false;
                }
                con.close();
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
        return false;
    }

    public boolean ConsultarCPF(String _cpf) {
        Connect();
        if (isConnected) {

            try {
                String sql = "SELECT * FROM PESSOA WHERE CPF_PESSOA ='" + _cpf + "'";
                if (!con.isClosed()) {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        Usuario.setIdPessoa(rs.getInt("id_pessoa"));
                    } else {
                        return false;
                    }
                    con.close();
                    return true;
                }
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
            }
        }
        return false;
    }

    public int GetUsuario() {
        Connect();
        if (isConnected && Usuario.isLogado()) {
            return Usuario.getIdPessoa();
        } else {
            return 0;
        }
    }

    public boolean CadastrarVaca(int _idVaca, double _qtdLeite, java.util.Date _data) {
        Connect();
        boolean done = false;

        if (isConnected) {
            try {

                String sql = "insert into vaca_leiteira values (" + _idVaca + "," + _qtdLeite + ",'" + _data + "','" + Usuario.getLogin() + "')";
                if (!con.isClosed()) {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.executeUpdate();
                    ps.close();
                    done = true;
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                done = false;
            }
        }
        return done;
    }

    public List<List<String>> ConsultarVaca(int _idVaca, String _login) {
        Connect();
        List<List<String>> mainList = new ArrayList();

        if (isConnected) {
            try {
                String sql = "";

                if (_idVaca != 0 && !_login.equals("")) {
                    sql = "select * from vaca_leiteira where NUMERO_IDENT = " + _idVaca + " AND login_usuario = '" + _login + "'";
                } else if (_idVaca != 0 && _login.equals("")) {
                    sql = "select * from vaca_leiteira where NUMERO_IDENT = " + _idVaca;
                } else if (_idVaca == 0 && !_login.equals("")) {
                    sql = "select * from vaca_leiteira where login_usuario = '" + _login + "'";
                } else {
                    sql = "select * from vaca_leiteira";
                }

                if (!con.isClosed()) {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        List<String> list = new ArrayList();
                        String sLoginUsuario = rs.getString("LOGIN_USUARIO");
                        int idVaca = rs.getInt("IDENTIFICACAO");
                        float qtdLeite = (float) rs.getDouble("QTD_LEITE");

                        Timestamp data = rs.getTimestamp("HORARIO");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        String sQtdLeite = Float.toString(qtdLeite);
                        String sData = formatter.format(data);
                        String sIdVaca = Integer.toString(idVaca);

                        list.add(sIdVaca);
                        list.add(sQtdLeite);
                        list.add(sData);
                        list.add(sLoginUsuario);

                        mainList.add(list);
                    }
                    rs.close();
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
            }
        }
        return mainList;
    }

    public boolean CadastrarUsuario(String login, String senha) {
        Connect();
        boolean done = false;

        if (isConnected) {
            try {
                String sql = "insert into usuario values ('" + login + "','" + senha + "'," + Usuario.getIdPessoa() + ")";
                if (!con.isClosed()) {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.executeUpdate();
                    ps.close();
                    done = true;
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                done = false;
            }
        }
        return done;
    }

    public boolean CadastrarAnimal(int _idAnimal, String _tipoTratamento, int _diasTratamento) {
        Connect();
        boolean done = false;

        if (isConnected) {
            try {
                String sql = "insert into animais_doentes values (" + _idAnimal + ",'" + _tipoTratamento + "'," + _diasTratamento + ")";
                if (!con.isClosed()) {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.executeUpdate();
                    ps.close();
                    done = true;
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                done = false;
            }
        }
        return done;
    }

    public List<List<String>> ConsultarAnimal(int _idAnimal) {
        Connect();
        List<List<String>> mainList = new ArrayList();

        if (isConnected) {
            try {
                String sql = "";
                if (_idAnimal != 0) {
                    sql = "select ID_ANIMAL, TRATAMENTO, QTD_DIAS from animais_doentes where ID_ANIMAL = " + _idAnimal;
                } else {
                    sql = "select ID_ANIMAL, TRATAMENTO, QTD_DIAS from animais_doentes";
                }
                if (!con.isClosed()) {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        List<String> list = new ArrayList();
                        int idAnimal = rs.getInt("ID_ANIMAL");
                        int iQtdDias = rs.getInt("QTD_DIAS");
                        String sTratamento = rs.getString("TRATAMENTO");

                        String sIdAnimal = Integer.toString(idAnimal);
                        String sQtdDias = Integer.toString(iQtdDias);
                        list.add(sIdAnimal);
                        list.add(sTratamento);
                        list.add(sQtdDias);

                        mainList.add(list);
                    }
                    rs.close();
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
            }
        }
        return mainList;
    }

}
