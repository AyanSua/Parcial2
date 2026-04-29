package modelado;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionDB {

    private static ConexionDB instancia;
    private Connection connection = null;

    //  CORRECCIÓN: dos puntos (:) entre puerto y nombre del servicio
    private final String url      = "jdbc:oracle:thin:@192.168.254.215:1521:orcl";
    private final String user     = "productosBS";
    private final String password = "productosBS";

    // Constructor privado para Singleton
    private ConexionDB() {
        conectar();
    }

    // Método para conectar a la base de datos
    private void conectar() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println(" Conexión establecida: " + meta.getDriverName());
            }
        } catch (SQLException ex) {
            System.out.println(" Error de conexión: " + ex.getMessage());
            System.out.println(" Verifica: IP, puerto, nombre del servicio, usuario y contraseña.");
        }
    }

    // Obtener instancia única (patrón Singleton)
    public static ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    // Obtener conexión (reconecta si está cerrada)
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("️ Conexión cerrada. Intentando reconectar...");
                conectar();
            }
        } catch (SQLException e) {
            System.out.println(" Error al verificar conexión: " + e.getMessage());
        }
        return connection;
    }

    // Cerrar la conexión
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println(" Conexión cerrada correctamente.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Probar conexión rápida (útil para debug)
    public static void probarConexion() {
        System.out.println("Probando conexión...");
        ConexionDB bd = ConexionDB.getInstancia();
        if (bd.getConnection() != null) {
            System.out.println(" Conexión exitosa.");
        } else {
            System.out.println(" No se pudo conectar.");
        }
    }
}