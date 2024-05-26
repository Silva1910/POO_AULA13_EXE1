package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Aluno;

public class AlunoDao implements ICrud<Aluno> {
    private GenericDao gDao;

    public AlunoDao(GenericDao gDao) {
        this.gDao = gDao;
    }

    @Override
    public void inserir(Aluno a) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "INSERT INTO aluno (RA, nome, email) VALUES (?, ?, ?)";

        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, a.getRA());
        ps.setString(2, a.getNome());
        ps.setString(3, a.getEmail());
        ps.execute();
        ps.close();
        c.close();
    }

    @Override
    public void atualizar(Aluno a) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "UPDATE aluno SET nome = ?, email = ? WHERE RA = ?";

        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, a.getNome());
        ps.setString(2, a.getEmail());
        ps.setInt(3, a.getRA());
        ps.execute();
        ps.close();
        c.close();
    }

    @Override
    public void excluir(Aluno a) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "DELETE FROM aluno WHERE RA = ?";

        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, a.getRA());
        ps.execute();
        ps.close();
        c.close();
    }

    @Override
    public Aluno consultar(Aluno a) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "SELECT RA, nome, email FROM aluno WHERE RA = ?";

        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, a.getRA());
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            a.setNome(rs.getString("nome"));
            a.setEmail(rs.getString("email"));
        }

        rs.close();
        ps.close();
        c.close();

        return a;
    }

    @Override
    public List<Aluno> listar() throws SQLException, ClassNotFoundException {
        List<Aluno> alunos = new ArrayList<>();
        Connection c = gDao.getConnection();
        String sql = "SELECT RA, nome, email FROM aluno";

        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Aluno a = new Aluno();
            a.setRA(rs.getInt("RA"));
            a.setNome(rs.getString("nome"));
            a.setEmail(rs.getString("email"));
            alunos.add(a);
        }

        rs.close();
        ps.close();
        c.close();

        return alunos;
    }
}

