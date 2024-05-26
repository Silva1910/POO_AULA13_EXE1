<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>LIVRO</title>
<link rel="stylesheet" href="./css/estilo.css">
</head>
<body>
	<nav id="menu">
		<ul>
			<li><a href="Aluguel">Aluguel</a></li>
			<li><a href="Aluno.jsp">Aluno</a></li>
			<li><a href="Livro.jsp">Livro</a></li>
			<li><a href="Revista.jsp">Revista</a></li>
		
		</ul>
	</nav>
	<div class="container" align="center">
		<form action="Livro" method="post" onsubmit="preencherTime()">
			<p class="title">
				<b>Livro</b>
			</p>
			<table>
				<tr>
				
				

				<tr>
					<td><input class="input_data" type="number" id="codigoLivro" name="codigoLivro"
						step="1" placeholder="Codigo do Livro"></td>
				</tr>
				<tr>
					<td><input class="input_data" type="text" id="nome"
						name="nome" placeholder="Nome do exemplar"></td>
				</tr>
				<tr>
					<td><input class="input_data" type="number" id="qtdPaginas"
						name="qtdPaginas" step="0.01" placeholder="Quantidade de paginas"></td>
				</tr>
				<tr>
					<td><input class="input_data" type="text" id="ISBN"
						name="ISBN" placeholder="ISBN"></td>
				</tr>
				<tr>
					<td><input class="input_data" type="number" id="edicao"
						name="edicao" step="0.01" placeholder="Edicao do livro"></td>
				</tr>
				<tr>
					<td><input type="hidden"
						name="cmd" id="cmd" value=""> <input type="submit"
						id="EnviarExcluir" name="botao" value="Excluir"
						onclick="document.getElementById('cmd').value = 'Excluir';">
						<input type="submit" id="EnviarAtualizar" name="botao"
						value="Atualizar"
						onclick="document.getElementById('cmd').value = 'Alterar';">
						<input type="submit" id="EnviarCadastrar" name="botao"
						value="Cadastrar"
						onclick="document.getElementById('cmd').value = 'Cadastrar';">
						<input type="submit" id="EnviarListar" name="botao" value="Listar"
						onclick="document.getElementById('cmd').value = 'Listar';">
					</td>
				</tr>
			</table>
		</form>
	</div>
	<br />
	<br />
	<div  align = "center">
	<c:if test="${not empty saida}">
		<p>${saida}</p>
	</c:if>
	<c:if test="${not empty erro}">
		<p style="color: red;">${erro}</p>
	</c:if>
	</div>
<div class="container2" align="center">
    <c:if test="${not empty livros}">
        <h2>Lista de Livros</h2>
        <table border="1">
            <thead>
                <tr>
                    <th>codigo</th>
                    <th>nome</th>
                    <th>Quantidade de Pg</th>
                    <th>ISBN</th>
                    <th>Edicao</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="livro" items="${livros}">
                    <tr>
                        <td>${livro.codigo}</td>
                        <td>${livro.nome}</td>
                        <td>${livro.qtdPaginas}</td>
                        <td>${livro.ISBN}</td>
                        <td>${livro.edicao}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>


</body>
</html>
