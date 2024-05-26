package persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Aluguel;
import model.Aluno;
import model.Exemplar;
import model.Livro;
import model.Revista;

public class AluguelDao implements ICrud<Aluguel> {
    private GenericDao gDao;

    public AluguelDao(GenericDao gDao) {
        this.gDao = gDao;
    }

    public void inserir(Aluguel aluguel) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "INSERT INTO aluguel (codigo_exemplar, RA_aluno, data_retirada, data_devolucao) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = c.prepareStatement(sql);

        Exemplar exemplar = aluguel.getExemplar();
        if (exemplar != null) {
            ps.setInt(1, exemplar.getCodigo());
        } else {
            throw new SQLException("Exemplar não pode ser nulo.");
        }

        ps.setInt(2, aluguel.getAluno().getRA());
        ps.setDate(3, java.sql.Date.valueOf(aluguel.getDataRetirada()));
        ps.setDate(4, java.sql.Date.valueOf(aluguel.getDataDevolucao()));

        ps.executeUpdate();
        ps.close();
        c.close();
    }




    @Override
    public void atualizar(Aluguel a) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "UPDATE aluguel SET dataRetirada = ?, dataDevolucao = ? WHERE alunoRA = ? AND exemplarCodigo = ?";

        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, a.getDataRetirada().toString());
        ps.setString(2, a.getDataDevolucao() != null ? a.getDataDevolucao().toString() : null);
        ps.setInt(3, a.getAluno().getRA());
        ps.setInt(4, a.getExemplar().getCodigo());
        ps.execute();
        ps.close();
        c.close();
    }

    @Override
    public void excluir(Aluguel a) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "DELETE FROM aluguel WHERE alunoRA = ? AND exemplarCodigo = ?";
        
        PreparedStatement ps = c.prepareStatement(sql);
        try {
            if (a.getAluno() != null && a.getExemplar() != null) {
                ps.setInt(1, a.getAluno().getRA());
                ps.setInt(2, a.getExemplar().getCodigo());
                ps.execute();
            } else {
                // Handle the case where Aluno or Exemplar is null
                System.out.println("Aluno or Exemplar is null. Cannot proceed with deletion.");
            }
        } finally {
            ps.close();
            c.close();
        }
    }


    @Override
    public Aluguel consultar(Aluguel a) throws SQLException, ClassNotFoundException {
        Connection c = gDao.getConnection();
        String sql = "SELECT a.dataRetirada, a.dataDevolucao, al.RA, al.nome, al.email, e.codigo, e.nome, e.qtd_paginas, " +
                "l.ISBN, l.edicao, r.ISSN " +
                "FROM aluguel a " +
                "JOIN aluno al ON a.alunoRA = al.RA " +
                "JOIN exemplar e ON a.exemplarCodigo = e.codigo " +
                "LEFT JOIN livro l ON e.codigo = l.codigo " +
                "LEFT JOIN revista r ON e.codigo = r.codigo " +
                "WHERE a.alunoRA = ? AND a.exemplarCodigo = ?";


        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, a.getAluno().getRA());
        ps.setInt(2, a.getExemplar().getCodigo());

        ResultSet rs = ps.executeQuery();

        Aluguel aluguel = null;
        if (rs.next()) {
            Aluno aluno = new Aluno();
            aluno.setRA(rs.getInt("RA"));
            aluno.setNome(rs.getString("nome"));
            aluno.setEmail(rs.getString("email"));

            Exemplar exemplar;
            if (rs.getString("ISBN") != null) {
                Livro livro = new Livro();
                livro.setCodigo(rs.getInt("codigo"));
                livro.setNome(rs.getString("nome"));
                livro.setQtdPaginas(rs.getInt("qtdPaginas"));
                livro.setISBN(rs.getString("ISBN"));
                livro.setEdicao(rs.getInt("edicao"));
                exemplar = livro;
            } else {
                Revista revista = new Revista();
                revista.setCodigo(rs.getInt("codigo"));
                revista.setNome(rs.getString("nome"));
                revista.setQtdPaginas(rs.getInt("qtdPaginas"));
                revista.setIssn(rs.getString("ISSN"));
                exemplar = revista;
            }

            aluguel = new Aluguel();
            aluguel.setAluno(aluno);
            aluguel.setExemplar(exemplar);
            aluguel.setDataRetirada(LocalDate.parse(rs.getString("dataRetirada")));
            aluguel.setDataDevolucao(rs.getString("dataDevolucao") != null ? LocalDate.parse(rs.getString("dataDevolucao")) : null);
        }

        rs.close();
        ps.close();
        c.close();

        return aluguel;
    }

    @Override
    public List<Aluguel> listar() throws SQLException, ClassNotFoundException {
        List<Aluguel> alugueis = new ArrayList<>();
        Connection c = gDao.getConnection();
        String sql = "SELECT a.dataRetirada, a.dataDevolucao, al.RA, al.nome, al.email, e.codigo, e.nome, e.qtd_paginas, " +
                     "l.ISBN, l.edicao, r.ISSN " +
                     "FROM aluguel a " +
                     "JOIN aluno al ON a.alunoRA = al.RA " +
                     "JOIN exemplar e ON a.exemplarCodigo = e.codigo " +
                     "LEFT JOIN livro l ON e.codigo = l.codigo " +
                     "LEFT JOIN revista r ON e.codigo = r.codigo";

        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Aluno aluno = new Aluno();
            aluno.setRA(rs.getInt("RA"));
            aluno.setNome(rs.getString("nome"));
            aluno.setEmail(rs.getString("email"));

            Exemplar exemplar;
            if (rs.getString("ISBN") != null) {
                Livro livro = new Livro();
                livro.setCodigo(rs.getInt("codigo"));
                livro.setNome(rs.getString("nome"));
                livro.setQtdPaginas(rs.getInt("qtd_paginas"));
                livro.setISBN(rs.getString("ISBN"));
                livro.setEdicao(rs.getInt("edicao"));
                exemplar = livro;
            } else {
                Revista revista = new Revista();
                revista.setCodigo(rs.getInt("codigo"));
                revista.setNome(rs.getString("nome"));
                revista.setQtdPaginas(rs.getInt("qtd_paginas"));
                revista.setIssn(rs.getString("ISSN"));
                exemplar = revista;
            }

            Aluguel aluguel = new Aluguel();
            aluguel.setAluno(aluno);
            aluguel.setExemplar(exemplar);
            aluguel.setDataRetirada(LocalDate.parse(rs.getString("dataRetirada")));
            aluguel.setDataDevolucao(rs.getString("dataDevolucao") != null ? LocalDate.parse(rs.getString("dataDevolucao")) : null);

            alugueis.add(aluguel);
        }

        rs.close();
        ps.close();
        c.close();

        return alugueis;
    }
    
}
