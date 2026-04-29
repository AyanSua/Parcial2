package controlador;

import modelado.ConexionDB;
import modelado.Producto;
import java.sql.Types;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoControlador {

    // ─── CREATE ─────────────────────────────────────────

    public boolean agregar(Producto p) {
        String sql = "{ CALL sp_agregar_producto(?, ?, ?, ?, ?) }";
        try {
            Connection con = ConexionDB.getInstancia().getConnection();
            CallableStatement cs = con.prepareCall(sql);

            cs.setString(1, p.getProducto());
            cs.setInt(2, p.getCantidad());
            cs.setDouble(3, p.getPrecioUnidad());
            cs.setString(4, p.getDescripcion());
            cs.registerOutParameter(5, Types.NUMERIC);

            cs.execute();
            return cs.getInt(5) == 1;

        } catch (SQLException e) {
            System.err.println("Error al agregar: " + e.getMessage());
            return false;
        }
    }

    // ─── READ: listar todos ─────────────────────────

    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "{ CALL sp_listar_productos(?) }";

        try {
            Connection con = ConexionDB.getInstancia().getConnection();
            CallableStatement cs = con.prepareCall(sql);

            cs.registerOutParameter(1, Types.REF_CURSOR);            
            cs.execute();

            ResultSet rs = (ResultSet) cs.getObject(1);

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
        }

        return lista;
    }

    // ─── READ: buscar ─────────────────────────

    public List<Producto> buscar(String texto) {
        List<Producto> lista = new ArrayList<>();
        String sql = "{ CALL sp_buscar_productos(?, ?) }";

        try {
            Connection con = ConexionDB.getInstancia().getConnection();
            CallableStatement cs = con.prepareCall(sql);

            cs.setString(1, texto);
            cs.registerOutParameter(2, Types.REF_CURSOR);
            cs.execute();

            ResultSet rs = (ResultSet) cs.getObject(2);

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar: " + e.getMessage());
        }

        return lista;
    }

    // ─── READ: por ID ─────────────────────────

    public Producto obtenerPorId(int id) {
        String sql = "{ CALL sp_obtener_producto(?, ?) }";

        try {
            Connection con = ConexionDB.getInstancia().getConnection();
            CallableStatement cs = con.prepareCall(sql);

            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.REF_CURSOR);
            cs.execute();

            ResultSet rs = (ResultSet) cs.getObject(2);

            if (rs.next()) {
                return mapear(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener por ID: " + e.getMessage());
        }

        return null;
    }

    // ─── UPDATE ─────────────────────────

    public boolean editar(Producto p) {
        String sql = "{ CALL sp_editar_producto(?, ?, ?, ?, ?, ?) }";

        try {
            Connection con = ConexionDB.getInstancia().getConnection();
            CallableStatement cs = con.prepareCall(sql);

            cs.setInt(1, p.getIdProducto());
            cs.setString(2, p.getProducto());
            cs.setInt(3, p.getCantidad());
            cs.setDouble(4, p.getPrecioUnidad());
            cs.setString(5, p.getDescripcion());
            cs.registerOutParameter(6, Types.NUMERIC);

            cs.execute();
            return cs.getInt(6) == 1;

        } catch (SQLException e) {
            System.err.println("Error al editar: " + e.getMessage());
            return false;
        }
    }

    // ─── DELETE ─────────────────────────

    public boolean eliminar(int idProducto) {
        String sql = "{ CALL sp_eliminar_producto(?, ?) }";

        try {
            Connection con = ConexionDB.getInstancia().getConnection();
            CallableStatement cs = con.prepareCall(sql);

            cs.setInt(1, idProducto);
            cs.registerOutParameter(2, Types.NUMERIC);

            cs.execute();
            return cs.getInt(2) == 1;

        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    // ─── MAPEO ─────────────────────────

    private Producto mapear(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getInt("id_producto"),
            rs.getString("producto"),
            rs.getInt("cantidad"),
            rs.getDouble("precio_unidad"),
            rs.getString("descripcion")
        );
    }
}