package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Exemplar;
import model.Livro;
import persistence.ExemplarDao;
import persistence.GenericDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Livro")
public class LivroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LivroServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Livro> livros = listarLivros();
            request.setAttribute("livros", livros);
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("erro", e.getMessage());
        }

        RequestDispatcher rd = request.getRequestDispatcher("Livro.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        String codigo = request.getParameter("codigoLivro");
        String nome = request.getParameter("nome");
        String qtdPaginas = request.getParameter("qtdPaginas");
        String isbn = request.getParameter("ISBN");
        String edicao = request.getParameter("edicao");

        String saida = "";
        String erro = "";
        List<Livro> livros = new ArrayList<>();
        Livro livro = new Livro();

        if (cmd.contains("Cadastrar") || cmd.contains("Alterar")) {
            livro.setCodigo(Integer.parseInt(codigo));
            livro.setNome(nome);
            livro.setQtdPaginas(Integer.parseInt(qtdPaginas));
            livro.setISBN(isbn);
            livro.setEdicao(Integer.parseInt(edicao));
        }

        try {
            if (cmd.contains("Cadastrar")) {
                cadastrarLivro(livro);
                saida = "Livro cadastrado com sucesso";
            } else if (cmd.contains("Alterar")) {
                alterarLivro(livro);
                saida = "Livro alterado com sucesso";
            } else if (cmd.contains("Excluir")) {
                livro.setCodigo(Integer.parseInt(codigo));
                excluirLivro(livro);
                saida = "Livro excluído com sucesso";
            } else if (cmd.contains("Listar")) {
                livros = listarLivros();
            }
        } catch (SQLException | ClassNotFoundException e) {
            erro = e.getMessage();
        } finally {
            request.setAttribute("saida", saida);
            request.setAttribute("erro", erro);
            request.setAttribute("livros", livros);

            RequestDispatcher rd = request.getRequestDispatcher("Livro.jsp");
            rd.forward(request, response);
        }
    }

    private void cadastrarLivro(Livro livro) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        ExemplarDao eDao = new ExemplarDao(gDao);
        eDao.inserir(livro);
    }

    private void alterarLivro(Livro livro) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        ExemplarDao eDao = new ExemplarDao(gDao);
        eDao.atualizar(livro);
    }

    private void excluirLivro(Livro livro) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        ExemplarDao eDao = new ExemplarDao(gDao);
        eDao.excluir(livro);
    }

    private List<Livro> listarLivros() throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        ExemplarDao eDao = new ExemplarDao(gDao);
        List<Exemplar> exemplares = eDao.listar();
        List<Livro> livros = new ArrayList<>();
        for (Exemplar exemplar : exemplares) {
            if (exemplar instanceof Livro) {
                livros.add((Livro) exemplar);
            }
        }
        return livros;
    }
}
