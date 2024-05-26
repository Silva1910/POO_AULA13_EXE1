package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Exemplar;
import model.Revista;
import persistence.ExemplarDao;
import persistence.GenericDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Revista")
public class RevistaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RevistaServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Revista> revistas = listarRevistas();
            request.setAttribute("revistas", revistas);
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("erro", e.getMessage());
        }

        RequestDispatcher rd = request.getRequestDispatcher("Revista.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        String codigo = request.getParameter("codigo");
        String nome = request.getParameter("nome");
        String qtdPaginas = request.getParameter("qtdPaginas");
        String issn = request.getParameter("issn");

        String saida = "";
        String erro = "";
        List<Revista> revistas = new ArrayList<>();
        Revista revista = new Revista();

        try {
            if (cmd.contains("Cadastrar") || cmd.contains("Alterar") || cmd.contains("Excluir")) {
                if (codigo != null && !codigo.isEmpty()) {
                    revista.setCodigo(Integer.parseInt(codigo));
                }
                if (nome != null && !nome.isEmpty()) {
                    revista.setNome(nome);
                }
                if (qtdPaginas != null && !qtdPaginas.isEmpty()) {
                    revista.setQtdPaginas(Integer.parseInt(qtdPaginas));
                }
                if (issn != null && !issn.isEmpty()) {
                    revista.setIssn(issn);
                }
            }

            if (cmd.contains("Cadastrar")) {
                cadastrarRevista(revista);
                saida = "Revista cadastrada com sucesso";
            } else if (cmd.contains("Alterar")) {
                alterarRevista(revista);
                saida = "Revista alterada com sucesso";
            } else if (cmd.contains("Excluir")) {
                excluirRevista(revista);
                saida = "Revista excluída com sucesso";
            } else if (cmd.contains("Listar")) {
                revistas = listarRevistas();
            }
        } catch (SQLException | ClassNotFoundException e) {
            erro = e.getMessage();
        } finally {
            request.setAttribute("saida", saida);
            request.setAttribute("erro", erro);
            request.setAttribute("revistas", revistas);

            RequestDispatcher rd = request.getRequestDispatcher("Revista.jsp");
            rd.forward(request, response);
        }
    }


    private void cadastrarRevista(Revista revista) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        ExemplarDao eDao = new ExemplarDao(gDao);
        eDao.inserir(revista);
    }

    private void alterarRevista(Revista revista) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        ExemplarDao eDao = new ExemplarDao(gDao);
        eDao.atualizar(revista);
    }

    private void excluirRevista(Revista revista) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        ExemplarDao eDao = new ExemplarDao(gDao);
        eDao.excluir(revista);
    }

    private List<Revista> listarRevistas() throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        ExemplarDao eDao = new ExemplarDao(gDao);
        List<Exemplar> exemplares = eDao.listar();
        List<Revista> revistas = new ArrayList<>();
        for (Exemplar exemplar : exemplares) {
            if (exemplar instanceof Revista) {
                revistas.add((Revista) exemplar);
            }
        }
        return revistas;
    }
}
