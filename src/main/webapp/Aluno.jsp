<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>ALUNO</title>
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
		<form action="Aluno" method="post" >
			<p class="title">
				<b>Aluno</b>
			</p>
			<table>
				<tr>
					<td><input class="input_data" type="number" id="RA" name="RA"
						step="1" placeholder="RA DO ALUNO"></td>
				</tr>
				<tr>
					<td><input class="input_data" type="text" id="nome"
						name="nome" placeholder="Nome do Aluno"></td>
				</tr>
				<tr>
					<td><input class="input_data" type="text" id="email"
						name="email" placeholder="e-mail do aluno"></td>
				</tr>
				<tr>
					<td> 
					   <input type="hidden" name="cmd" id="cmd" value="">
					    <input type="submit" id="EnviarExcluir" name="botao" value="Excluir"	onclick="document.getElementById('cmd').value = 'Excluir';">
						<input type="submit" id="EnviarAtualizar" name="botao"	value="Atualizar" onclick="document.getElementById('cmd').value = 'Alterar';">
						<input type="submit" id="EnviarCadastrar" name="botao" value="Cadastrar" onclick="document.getElementById('cmd').value = 'Cadastrar';">
						<input type="submit" id="EnviarListar" name="botao" value="Listar"	onclick="document.getElementById('cmd').value = 'Listar';">
					</td>
				</tr>
			</table>
		</form>
	</div>
	<br />
	<br />
	<div align="center">
	<c:if test="${not empty saida}">
		<p>${saida}</p>
	</c:if>
	<c:if test="${not empty erro}">
		<p style="color: red;">${erro}</p>
	</c:if>
	</div>
	<div class="container2" align="center">
	<c:if test="${not empty alunos}">
		<h2>Lista de Alunos</h2>
		<table border="1">
			<thead>
				<tr>
					<th>RA</th>
					<th>Nome</th>
					<th>Email</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="a" items="${alunos}">
					<tr>
						<td>${a.RA}</td>
						<td>${a.nome}</td>
						<td>${a.email}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	</div>
</body>
</html>
