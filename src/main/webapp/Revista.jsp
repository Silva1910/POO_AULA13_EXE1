<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>REVISTA</title>
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
		<form action="Revista" method="post">
			<p class="title"><b>REVISTA</b></p>
			<table>
				<tr>
					<td><input class="input_data" type="number" id="codigo" name="codigo" step="1" placeholder="Código da Revista"></td>
				</tr>
				<tr>
					<td><input class="input_data" type="text" id="nome" name="nome" placeholder="Nome do Exemplar"></td>
				</tr>
				<tr>
					<td><input class="input_data" type="number" id="qtdPaginas" name="qtdPaginas" step="1" placeholder="Quantidade de Páginas"></td>
				</tr>
				<tr>
					<td><input class="input_data" type="text" id="issn" name="issn" placeholder="ISSN"></td>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="cmd" id="cmd" value="">
						<input type="submit" id="EnviarExcluir" name="botao" value="Excluir" onclick="document.getElementById('cmd').value = 'Excluir';">
						<input type="submit" id="EnviarAtualizar" name="botao" value="Atualizar" onclick="document.getElementById('cmd').value = 'Alterar';">
						<input type="submit" id="EnviarCadastrar" name="botao" value="Cadastrar" onclick="document.getElementById('cmd').value = 'Cadastrar';">
						<input type="submit" id="EnviarListar" name="botao" value="Listar" onclick="document.getElementById('cmd').value = 'Listar';">
					</td>
				</tr>
			</table>
		</form>
	</div>
	<br /><br />
	<div align="center">
		<c:if test="${not empty saida}">
			<p>${saida}</p>
		</c:if>
		<c:if test="${not empty erro}">
			<p style="color: red;">${erro}</p>
		</c:if>
	</div>
	<div class="container2" align="center">
		<c:if test="${not empty revistas}">
			<h2>Lista de Revistas</h2>
			<table border="1">
				<thead>
					<tr>
						<th>Código</th>
						<th>Nome</th>
						<th>Quantidade de Páginas</th>
						<th>ISSN</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="revista" items="${revistas}">
						<tr>
							<td>${revista.codigo}</td>
							<td>${revista.nome}</td>
							<td>${revista.qtdPaginas}</td>
							<td>${revista.issn}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div>
</body>
</html>
