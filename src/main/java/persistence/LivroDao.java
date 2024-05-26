package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Livro;

public class LivroDao implements ICrud<Livro> {
    private GenericDao gDao;

    public LivroDao(GenericDao gDao) {
        this.gDao = gDao;
    }

    @Override
    public void inserir(Livro l) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "INSERT INTO exemplar (codigo, nome, qtd_paginas) VALUES (?, ?, ?)";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, l.getCodigo());
        ps.setString(2, l.getNome());
        ps.setInt(3, l.getQtdPaginas());
        ps.execute();

        sql = "INSERT INTO livro (codigo, ISBN, edicao) VALUES (?, ?, ?)";
        ps = c.prepareStatement(sql);
        ps.setInt(1, l.getCodigo());
        ps.setString(2, l.getISBN());
        ps.setInt(3, l.getEdicao());
        ps.execute();

        ps.close();
        c.close();
    }

    @Override
    public void atualizar(Livro l) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "UPDATE exemplar SET nome = ?, qtd_paginas = ? WHERE codigo = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, l.getNome());
        ps.setInt(2, l.getQtdPaginas());
        ps.setInt(3, l.getCodigo());
        ps.execute();

        sql = "UPDATE livro SET ISBN = ?, edicao = ? WHERE codigo = ?";
        ps = c.prepareStatement(sql);
        ps.setString(1, l.getISBN());
        ps.setInt(2, l.getEdicao());
        ps.setInt(3, l.getCodigo());
        ps.execute();

        ps.close();
        c.close();
    }
    @Override
    public void excluir(Livro l) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "DELETE FROM livro WHERE codigo = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, l.getCodigo());
        ps.executeUpdate();

        sql = "DELETE FROM exemplar WHERE codigo = ?";
        ps = c.prepareStatement(sql);
        ps.setInt(1, l.getCodigo());
        ps.executeUpdate();

        ps.close();
        c.close();
    }
    @Override
    public Livro consultar(Livro l) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "SELECT e.codigo, e.nome, e.qtd_paginas, l.ISBN, l.edicao FROM exemplar e JOIN livro l ON e.codigo = l.codigo WHERE e.codigo = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, l.getCodigo());
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            l.setNome(rs.getString("nome"));
            l.setQtdPaginas(rs.getInt("qtd_paginas"));
            l.setISBN(rs.getString("ISBN"));
            l.setEdicao(rs.getInt("edicao"));
        }

        rs.close();
        ps.close();
        c.close();

        return l;
    }

    @Override
    public List<Livro> listar() throws SQLException, ClassNotFoundException {
        List<Livro> livros = new ArrayList<>();
        Connection c = gDao.getConnection();
        String sql = "SELECT e.codigo, e.nome, e.qtd_paginas, l.ISBN, l.edicao FROM exemplar e JOIN livro l ON e.codigo = l.codigo";

        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Livro l = new Livro();
            l.setCodigo(rs.getInt("codigo"));
            l.setNome(rs.getString("nome"));
            l.setQtdPaginas(rs.getInt("qtd_paginas"));
            l.setISBN(rs.getString("ISBN"));
            l.setEdicao(rs.getInt("edicao"));
            livros.add(l);
        }

        rs.close();
        ps.close();
        c.close();

        return livros;
    }

}
