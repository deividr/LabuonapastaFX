package labuonapastafx.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsavel pela conexao com o banco de dados.
 * Quando necessario mudanca de banco de dados basta alterar essa classe.
 * 
 * @author   Deivid Assumpcao Rodrigues
 * @version  %I%, %G%
 * @since    1.0
 *
 */
public class Conexao {
	
	/**
	 * Retorna um objeto do tipo conexao com o banco de dados.
	 * 
	 * @return Objeto Connection da conexao com o banco de dados.
	 */
	public static Connection getConexao() {
		
		String url = "jdbc:mysql://localhost/labuonapasta";
		
		Connection con = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,"root","lu1312enzo");
		} catch (SQLException | ClassNotFoundException e) {
			throw new RuntimeException("Erro ao conectar o banco de dados: " + e.getMessage());
		}
		
		return con;
		
	}

}
