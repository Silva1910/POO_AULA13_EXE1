<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>ALUGUEL</title>
<link rel="stylesheet" href="./css/estilo.css">
</head>
<body>
	<nav id="menu">
		<ul>
			<li><a href="Aluguel.jsp">Aluguel</a></li>
			<li><a href="${pageContext.request.contextPath}/Aluno">Aluno</a></li>
			<li><a href="${pageContext.request.contextPath}/Livro">Livro</a></li>
			<li><a href="${pageContext.request.contextPath}/Revista">Revista</a></li>
		</ul>
	</nav>

	<div class="container" align="center">
		<form action="Aluguel" method="post" onsubmit="return validateForm()">
			<p class="title">
				<b>Aluguel</b>
			</p>
			<table>
				<tr>
					<td><select class="input_data" id="ExemplarCodigo"
						name="ExemplarCodigo">
							<option value="">Escolha um Exemplar</option>
							<c:forEach var="exemplar" items="${exemplares}">
								<option value="${exemplar.codigo}"
									<c:if test="${exemplar.codigo eq param.codigo}">selected</c:if>>
									<c:out value="${exemplar.nome}" />
								</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td><select class="input_data" id="RAAluno" name="RAAluno">
							<option value="">Escolha um Aluno</option>
							<c:forEach var="aluno" items="${alunos}">
								<option value="${aluno.RA}"
									<c:if test="${aluno.RA eq param.RA}">selected</c:if>>
									<c:out value="${aluno.nome}" />
								</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td><input class="input_data" type="date" id="dataRetirada"
						name="dataRetirada" placeholder="Insira a Data de Retirada"></td>
				</tr>
				<tr>
					<td><input class="input_data" type="date" id="dataDevolucao"
						name="dataDevolucao" placeholder="Insira a Data de Devolução"></td>
				</tr>
				<tr>
					<td><input type="hidden" name="cmd" id="cmd" value="">
						<input type="submit" id="EnviarExcluir" name="botao"
						value="Excluir"
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
		
		<br />

	</div>
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
		<c:if test="${not empty alugueis}">
			<h2>Lista de Aluguel</h2>
			<table border="1">
				<thead>
					<tr>
						<th>Exemplar</th>
						<th>Aluno</th>
						<th>Data de Retirada</th>
						<th>Data de Devolução</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="aluguel" items="${alugueis}">
						<tr>
							<td>${aluguel.exemplar.codigo}</td>
							<td>${aluguel.aluno.RA}</td>
							<td>${aluguel.dataRetirada}</td>
							<td>${aluguel.dataDevolucao}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div>

</body>
</html>
