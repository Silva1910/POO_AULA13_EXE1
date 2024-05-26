package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Revista;

public class RevistaDao implements ICrud<Revista> {
    private GenericDao gDao;

    public RevistaDao(GenericDao gDao) {
        this.gDao = gDao;
    }

    @Override
    public void inserir(Revista r) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "INSERT INTO exemplar (codigo, nome, qtdPaginas) VALUES (?, ?, ?)";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, r.getCodigo());
        ps.setString(2, r.getNome());
        ps.setInt(3, r.getQtdPaginas());
        ps.execute();

        sql = "INSERT INTO revista (codigo, ISSN) VALUES (?, ?)";
        ps = c.prepareStatement(sql);
        ps.setInt(1, r.getCodigo());
        ps.setString(2, r.getIssn());
        ps.execute();

        ps.close();
        c.close();
    }

    @Override
    public void atualizar(Revista r) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "UPDATE exemplar SET nome = ?, qtdPaginas = ? WHERE codigo = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, r.getNome());
        ps.setInt(2, r.getQtdPaginas());
        ps.setInt(3, r.getCodigo());
        ps.execute();

        sql = "UPDATE revista SET ISSN = ? WHERE codigo = ?";
        ps = c.prepareStatement(sql);
        ps.setString(1, r.getIssn());
        ps.setInt(2, r.getCodigo());
        ps.execute();

        ps.close();
        c.close();
    }
 
    @Override
    public void excluir(Revista revista) throws SQLException, ClassNotFoundException {
        Connection c = null;
        try {
            GenericDao gDao = new GenericDao();
            c = gDao.getConnection();
            c.setAutoCommit(false); // Desativa o modo de autocommit

            String sql = "DELETE FROM revista WHERE codigo = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, revista.getCodigo());
            ps.executeUpdate();

            sql = "DELETE FROM exemplar WHERE codigo = ?";
            ps = c.prepareStatement(sql);
            ps.setInt(1, revista.getCodigo());
            ps.executeUpdate();

            c.commit(); // Confirma as alterações no banco de dados
        } catch (SQLException e) {
            if (c != null) {
                c.rollback(); // Desfaz as alterações em caso de exceção
            }
            throw e; // Lança a exceção novamente para ser tratada no nível superior
        } finally {
            if (c != null) {
                c.setAutoCommit(true); // Restaura o modo de autocommit padrão
                c.close(); // Certifique-se de fechar a conexão
            }
        }
    }



    @Override
    public Revista consultar(Revista r) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "SELECT e.codigo, e.nome, e.qtdPaginas, r.ISSN FROM exemplar e JOIN revista r ON e.codigo = r.codigo WHERE e.codigo = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, r.getCodigo());
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            r.setNome(rs.getString("nome"));
            r.setQtdPaginas(rs.getInt("qtdPaginas"));
            r.setIssn(rs.getString("ISSN"));
        }

        rs.close();
        ps.close();
        c.close();

        return r;
    }

    @Override
    public List<Revista> listar() throws SQLException, ClassNotFoundException {
        List<Revista> revistas = new ArrayList<>();
        Connection c = gDao.getConnection();
        String sql = "SELECT e.codigo, e.nome, e.qtdPaginas, r.ISSN FROM exemplar e JOIN revista r ON e.codigo = r.codigo";

        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Revista r = new Revista();
            r.setCodigo(rs.getInt("codigo"));
            r.setNome(rs.getString("nome"));
            r.setQtdPaginas(rs.getInt("qtdPaginas"));
            r.setIssn(rs.getString("ISSN"));
            revistas.add(r);
        }

        rs.close();
        ps.close();
        c.close();

        return revistas;
    }
}
