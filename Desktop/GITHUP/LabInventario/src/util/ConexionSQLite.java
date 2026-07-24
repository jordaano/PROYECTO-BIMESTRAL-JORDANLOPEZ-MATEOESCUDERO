package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLite {
    private static Connection con = null;
    private static final String URL = "jdbc:sqlite:inventario.db";

    public static Connection conectar() {
        try {
            Class.forName("org.sqlite.JDBC"); 
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(URL);
                System.out.println("Conexión exitosa a SQLite.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: ¡Falta agregar el driver JAR de SQLite! " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return con;
    }
}
