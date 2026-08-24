package DAO;

import java.sql.*;

import app.Config;

public class DAO {
	protected Connection conexao;
	
	public DAO() {
		conexao = null;
	}
	
	public boolean conectar() {
		// Credenciais vêm de variável de ambiente ou do arquivo .env — ver Config.java
		String driverName = "org.postgresql.Driver";
		String serverName = Config.get("NEXUS_DB_HOST", "localhost");
		String mydatabase = Config.get("NEXUS_DB_NAME", "nexus");
		int porta         = Config.getInt("NEXUS_DB_PORT", 5432);
		String url = "jdbc:postgresql://" + serverName + ":" + porta +"/" + mydatabase;
		String username = Config.get("NEXUS_DB_USER", "postgres");
		String password = Config.get("NEXUS_DB_PASSWORD", "");
		boolean status = false;

		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao == null);
			System.out.println("Conexão efetuada com o postgres!");
		} catch (ClassNotFoundException e) { 
			System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
		}

		return status;
	}
	
	public boolean close() {
		boolean status = false;
		
		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}
}