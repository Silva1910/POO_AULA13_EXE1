package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Exemplar;
import model.Livro;
import model.Revista;

public class ExemplarDao {
    private GenericDao gDao;

    public ExemplarDao(GenericDao gDao) {
        this.gDao = gDao;
    }

    public List<Exemplar> listar() throws SQLException, ClassNotFoundException {
        List<Exemplar> exemplares = new ArrayList<>();
        Connection conn = gDao.getConnection();
        
        String sql = "SELECT e.codigo, e.nome, e.qtd_paginas, l.ISBN, l.edicao, r.ISSN, "
                   + "CASE WHEN l.ISBN IS NOT NULL THEN 'Livro' ELSE 'Revista' END AS tipo "
                   + "FROM Exemplar e "
                   + "LEFT JOIN Livro l ON e.codigo = l.codigo "
                   + "LEFT JOIN Revista r ON e.codigo = r.codigo";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            String tipo = rs.getString("tipo");
            Exemplar exemplar;
            if ("Livro".equals(tipo)) {
                exemplar = new Livro();
                ((Livro) exemplar).setISBN(rs.getString("ISBN"));
                ((Livro) exemplar).setEdicao(rs.getInt("edicao"));
            } else {
                exemplar = new Revista();
                ((Revista) exemplar).setIssn(rs.getString("ISSN"));
            }
            exemplar.setCodigo(rs.getInt("codigo"));
            exemplar.setNome(rs.getString("nome"));
            exemplar.setQtdPaginas(rs.getInt("qtd_paginas"));
            exemplares.add(exemplar);
        }

        rs.close();
        ps.close();
        conn.close();

        return exemplares;
    }

    public void inserir(Exemplar exemplar) throws SQLException, ClassNotFoundException {
        Connection conn = gDao.getConnection();

        String sqlExemplar = "INSERT INTO Exemplar (codigo, nome, qtd_paginas) VALUES (?, ?, ?)";
        PreparedStatement psExemplar = conn.prepareStatement(sqlExemplar);
        psExemplar.setInt(1, exemplar.getCodigo());
        psExemplar.setString(2, exemplar.getNome());
        psExemplar.setInt(3, exemplar.getQtdPaginas());
        psExemplar.executeUpdate();

        if (exemplar instanceof Livro) {
            String sqlLivro = "INSERT INTO Livro (codigo, ISBN, edicao) VALUES (?, ?, ?)";
            PreparedStatement psLivro = conn.prepareStatement(sqlLivro);
            psLivro.setInt(1, exemplar.getCodigo());
            psLivro.setString(2, ((Livro) exemplar).getISBN());
            psLivro.setInt(3, ((Livro) exemplar).getEdicao());
            psLivro.executeUpdate();
            psLivro.close();
        } else if (exemplar instanceof Revista) {
            String sqlRevista = "INSERT INTO Revista (codigo, ISSN) VALUES (?, ?)";
            PreparedStatement psRevista = conn.prepareStatement(sqlRevista);
            psRevista.setInt(1, exemplar.getCodigo());
            psRevista.setString(2, ((Revista) exemplar).getIssn());
            psRevista.executeUpdate();
            psRevista.close();
        }

        psExemplar.close();
        conn.close();
    }

    public void atualizar(Exemplar exemplar) throws SQLException, ClassNotFoundException {
        Connection conn = gDao.getConnection();

        String sqlExemplar = "UPDATE Exemplar SET nome = ?, qtd_paginas = ? WHERE codigo = ?";
        PreparedStatement psExemplar = conn.prepareStatement(sqlExemplar);
        psExemplar.setString(1, exemplar.getNome());
        psExemplar.setInt(2, exemplar.getQtdPaginas());
        psExemplar.setInt(3, exemplar.getCodigo());
        psExemplar.executeUpdate();

        if (exemplar instanceof Livro) {
            String sqlLivro = "UPDATE Livro SET ISBN = ?, edicao = ? WHERE codigo = ?";
            PreparedStatement psLivro = conn.prepareStatement(sqlLivro);
            psLivro.setString(1, ((Livro) exemplar).getISBN());
            psLivro.setInt(2, ((Livro) exemplar).getEdicao());
            psLivro.setInt(3, exemplar.getCodigo());
            psLivro.executeUpdate();
            psLivro.close();
        } else if (exemplar instanceof Revista) {
            String sqlRevista = "UPDATE Revista SET ISSN = ? WHERE codigo = ?";
            PreparedStatement psRevista = conn.prepareStatement(sqlRevista);
            psRevista.setString(1, ((Revista) exemplar).getIssn());
            psRevista.setInt(2, exemplar.getCodigo());
            psRevista.executeUpdate();
            psRevista.close();
        }

        psExemplar.close();
        conn.close();
    }

    public void excluir(Exemplar exemplar) throws SQLException, ClassNotFoundException {
        Connection conn = gDao.getConnection();

        if (exemplar instanceof Livro) {
            String sqlLivro = "DELETE FROM Livro WHERE codigo = ?";
            try (PreparedStatement psLivro = conn.prepareStatement(sqlLivro)) {
                psLivro.setInt(1, exemplar.getCodigo());
                psLivro.executeUpdate();
            }
        } else if (exemplar instanceof Revista) {
            String sqlRevista = "DELETE FROM Revista WHERE codigo = ?";
            try (PreparedStatement psRevista = conn.prepareStatement(sqlRevista)) {
                psRevista.setInt(1, exemplar.getCodigo());
                psRevista.executeUpdate();
            }
        }

        String sqlExemplar = "DELETE FROM Exemplar WHERE codigo = ?";
        try (PreparedStatement psExemplar = conn.prepareStatement(sqlExemplar)) {
            psExemplar.setInt(1, exemplar.getCodigo());
            psExemplar.executeUpdate();
        }

        conn.close();
    }


}
