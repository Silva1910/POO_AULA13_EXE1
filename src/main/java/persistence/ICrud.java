package persistence;

import java.sql.*;

import java.util.*;
public interface ICrud <T> {

	
	public void inserir (T t) throws SQLException, ClassNotFoundException;
	public void atualizar (T t) throws SQLException, ClassNotFoundException;
	public void excluir (T t) throws SQLException, ClassNotFoundException;
	public  T consultar (T t) throws SQLException, ClassNotFoundException;
	public List<T> listar() throws SQLException, ClassNotFoundException;
}
