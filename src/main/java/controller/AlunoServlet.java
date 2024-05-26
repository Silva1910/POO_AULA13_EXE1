package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Aluno;
import persistence.AlunoDao;
import persistence.GenericDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Aluno")
public class AlunoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AlunoServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Aluno> alunos = listarAlunos();
            request.setAttribute("alunos", alunos);
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("erro", e.getMessage());
        }

        RequestDispatcher rd = request.getRequestDispatcher("Aluno.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        String raAluno = request.getParameter("RA");
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");

        String saida = "";
        String erro = "";
        List<Aluno> alunos = new ArrayList<>();
        Aluno aluno = new Aluno();

        if (cmd.contains("Cadastrar") || cmd.contains("Alterar")) {
            if (raAluno != null && !raAluno.isEmpty()) {
                aluno.setRA(Integer.parseInt(raAluno));
            }
            aluno.setNome(nome);
            aluno.setEmail(email);
        }

        try {
            if (cmd.contains("Cadastrar")) {
                cadastrarAluno(aluno);
                saida = "Aluno cadastrado com sucesso";
            } else if (cmd.contains("Alterar")) {
                alterarAluno(aluno);
                saida = "Aluno alterado com sucesso";
            } else if (cmd.contains("Excluir")) {
                if (raAluno != null && !raAluno.isEmpty()) {
                    aluno.setRA(Integer.parseInt(raAluno));
                    excluirAluno(aluno);
                    saida = "Aluno excluído com sucesso";
                } else {
                    erro = "RA do aluno é necessário para exclusão.";
                }
            } else if (cmd.contains("Listar")) {
                alunos = listarAlunos();
            }
        } catch (SQLException | ClassNotFoundException e) {
            erro = e.getMessage();
        } finally {
            request.setAttribute("saida", saida);
            request.setAttribute("erro", erro);
            request.setAttribute("alunos", alunos);

            RequestDispatcher rd = request.getRequestDispatcher("Aluno.jsp");
            rd.forward(request, response);
        }
    }

    private void cadastrarAluno(Aluno aluno) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        AlunoDao aDao = new AlunoDao(gDao);
        aDao.inserir(aluno);
    }

    private void alterarAluno(Aluno aluno) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        AlunoDao aDao = new AlunoDao(gDao);
        aDao.atualizar(aluno);
    }

    private void excluirAluno(Aluno aluno) throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        AlunoDao aDao = new AlunoDao(gDao);
        aDao.excluir(aluno);
    }

    private List<Aluno> listarAlunos() throws SQLException, ClassNotFoundException {
        GenericDao gDao = new GenericDao();
        AlunoDao aDao = new AlunoDao(gDao);
        return aDao.listar();
    }
}
