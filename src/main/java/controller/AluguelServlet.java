package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Aluno;
import model.Exemplar;
import model.Livro;
import model.Revista;
import model.Aluguel;
import persistence.AlunoDao;
import persistence.ExemplarDao;
import persistence.GenericDao;
import persistence.AluguelDao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Aluguel")
public class AluguelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AluguelServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            carregarExemplares(request);
            carregarAlunos(request);
        } catch (ClassNotFoundException | SQLException e) {
            request.setAttribute("erro", e.getMessage());
        }

        RequestDispatcher rd = request.getRequestDispatcher("Aluguel.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            carregarExemplares(request);
            carregarAlunos(request);
        } catch (ClassNotFoundException | SQLException e) {
            request.setAttribute("erro", e.getMessage());
        }

        String cmd = request.getParameter("cmd");
        String codigoExemplar = request.getParameter("ExemplarCodigo");
        String raAluno = request.getParameter("RAAluno");
        String dataRetiradaStr = request.getParameter("dataRetirada");
        String dataDevolucaoStr = request.getParameter("dataDevolucao");

        String saida = "";
        String erro = "";
        List<Aluguel> alugueis = new ArrayList<>();
        Aluguel aluguel = new Aluguel();

        if (cmd != null && (cmd.contains("Cadastrar") || cmd.contains("Alterar"))) {
            Exemplar exemplar = null;

            try {
                int cdg = Integer.parseInt(codigoExemplar);
                exemplar = consultarExemplar(cdg);

                if (exemplar != null) {
                    aluguel.setExemplar(exemplar);

                    Aluno aluno = new Aluno();
                    aluno.setRA(Integer.parseInt(raAluno));
                    aluguel.setAluno(aluno);

                    try {
                        if (!dataRetiradaStr.isEmpty()) {
                            aluguel.setDataRetirada(LocalDate.parse(dataRetiradaStr));
                        }
                        if (!dataDevolucaoStr.isEmpty()) {
                            aluguel.setDataDevolucao(LocalDate.parse(dataDevolucaoStr));
                        }
                    } catch (IllegalArgumentException e) {
                        erro = "Formato de data inválido. Use o formato AAAA-MM-DD.";
                    }
                } else {
                    erro = "Exemplar não foi encontrado.";
                }
            } catch (SQLException | ClassNotFoundException e) {
                erro = e.getMessage();
            }
        }

        try {
            if (cmd != null && cmd.contains("Cadastrar")) {
                cadastrarAluguel(aluguel);
                saida = "Aluguel cadastrado com sucesso";
            } else if (cmd != null && cmd.contains("Alterar")) {
                alterarAluguel(aluguel);
                saida = "Aluguel alterado com sucesso";
            } else if (cmd != null && cmd.contains("Excluir")) {
                excluirAluguel(aluguel);
                saida = "Aluguel excluído com sucesso";
            } else if (cmd != null && cmd.contains("Listar")) {
                alugueis = listarAlugueis();
            }
        } catch (SQLException | ClassNotFoundException | IllegalArgumentException e) {
            erro = e.getMessage();
        } finally {
            request.setAttribute("saida", saida);
            request.setAttribute("erro", erro);
            request.setAttribute("alugueis", alugueis);

            RequestDispatcher rd = request.getRequestDispatcher("Aluguel.jsp");
            rd.forward(request, response);
        }
    }

    private void cadastrarAluguel(Aluguel aluguel) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        AluguelDao aDao = new AluguelDao(gDao);
        aDao.inserir(aluguel);
    }

    private void alterarAluguel(Aluguel aluguel) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        AluguelDao aDao = new AluguelDao(gDao);
        aDao.atualizar(aluguel);
    }

    private void excluirAluguel(Aluguel aluguel) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        AluguelDao aDao = new AluguelDao(gDao);
        aDao.excluir(aluguel);
    }

    private List<Aluguel> listarAlugueis() throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        AluguelDao aDao = new AluguelDao(gDao);
        return aDao.listar();
    }

    private void carregarExemplares(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        ExemplarDao eDao = new ExemplarDao(gDao);
        List<Exemplar> exemplares = eDao.listar();
       
        request.setAttribute("exemplares", exemplares);
    }

    private void carregarAlunos(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        AlunoDao aDao = new AlunoDao(gDao);
        List<Aluno> alunos = aDao.listar();
        request.setAttribute("alunos", alunos);
    }
    
    public Exemplar consultarExemplar(int codigo) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        Connection c = gDao.getConnection();
        Exemplar exemplar = null;

        // Consultar Revista
        String sqlRevista = "SELECT e.codigo, e.nome, e.qtdPaginas, r.ISSN FROM exemplar e JOIN revista r ON e.codigo = r.codigo WHERE e.codigo = ?";
        PreparedStatement psRevista = c.prepareStatement(sqlRevista);
        psRevista.setInt(1, codigo);
        ResultSet rsRevista = psRevista.executeQuery();

        if (rsRevista.next()) {
            Revista revista = new Revista();
            revista.setCodigo(rsRevista.getInt("codigo"));
            revista.setNome(rsRevista.getString("nome"));
            revista.setQtdPaginas(rsRevista.getInt("qtdPaginas"));
            revista.setIssn(rsRevista.getString("ISSN"));
            exemplar = revista;
        }

        rsRevista.close();
        psRevista.close();

        // Consultar Livro
        if (exemplar == null) {
            String sqlLivro = "SELECT e.codigo, e.nome, e.qtd_paginas, l.ISBN, l.edicao FROM exemplar e JOIN livro l ON e.codigo = l.codigo WHERE e.codigo = ?";
            PreparedStatement psLivro = c.prepareStatement(sqlLivro);
            psLivro.setInt(1, codigo);
            ResultSet rsLivro = psLivro.executeQuery();

            if (rsLivro.next()) {
                Livro livro = new Livro();
                livro.setCodigo(rsLivro.getInt("codigo"));
                livro.setNome(rsLivro.getString("nome"));
                livro.setQtdPaginas(rsLivro.getInt("qtd_paginas"));
                livro.setISBN(rsLivro.getString("ISBN"));
                livro.setEdicao(rsLivro.getInt("edicao"));
                exemplar = livro;
            }

            rsLivro.close();
            psLivro.close();
        }

        c.close();
        return exemplar;
    }
}
