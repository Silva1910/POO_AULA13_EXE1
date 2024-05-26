package persistence;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class GenericDao {

	public GenericDao() {
		super();
	}

	private Connection c;

	public Connection getConnection() throws ClassNotFoundException, SQLException {
	    String hostName = "localhost";
	    String dbname = "escola";
	    String user = "root";
	    String senha = "123456";

	    Class.forName("com.mysql.cj.jdbc.Driver");

	   
	    c = DriverManager.getConnection(String.format("jdbc:mysql://%s:3306/%s?serverTimezone=UTC", hostName, dbname), user, senha);

	    return c;
	}


}
