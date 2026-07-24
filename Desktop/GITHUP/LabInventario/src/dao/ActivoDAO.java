package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Activo;
import modelo.Hardware;
import modelo.Licencia;
import modelo.Periferico;
import util.ConexionSQLite;

public class ActivoDAO implements IActivoDAO {

    public ActivoDAO() {
        crearTabla();
    }

    private void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS activos ("
                + "id TEXT PRIMARY KEY, "
                + "nombre TEXT NOT NULL, "
                + "marca TEXT, "
                + "valor_adquisicion REAL, "
                + "tipo TEXT, "             
                + "meses_uso INTEGER, "     
                + "meses_vigencia INTEGER, " 
                + "es_critico INTEGER"      
                + ");";

        Connection con = ConexionSQLite.conectar();
        if (con != null) {
            try (Statement stmt = con.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabla 'activos' verificada/creada.");
            } catch (SQLException e) {
                System.err.println("Error al crear la tabla: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean insertar(Activo activo) {
        String sql = "INSERT INTO activos (id, nombre, marca, valor_adquisicion, tipo, meses_uso, meses_vigencia, es_critico) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            if (con == null) return false;

            ps.setString(1, activo.getId());
            ps.setString(2, activo.getNombre());
            ps.setString(3, activo.getMarca());
            ps.setDouble(4, activo.getValorAdquisicion());

            if (activo instanceof Hardware) {
                ps.setString(5, "HARDWARE");
                ps.setInt(6, ((Hardware) activo).getMesesUso());
                ps.setNull(7, Types.INTEGER);
                ps.setNull(8, Types.INTEGER);
            } else if (activo instanceof Licencia) {
                ps.setString(5, "LICENCIA");
                ps.setNull(6, Types.INTEGER);
                ps.setInt(7, ((Licencia) activo).getMesesVigencia());
                ps.setNull(8, Types.INTEGER);
            } else if (activo instanceof Periferico) {
                ps.setString(5, "PERIFERICO");
                ps.setNull(6, Types.INTEGER);
                ps.setNull(7, Types.INTEGER);
                ps.setInt(8, ((Periferico) activo).isEsCritico() ? 1 : 0);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar activo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Activo activo) {
        String sql = "UPDATE activos SET nombre = ?, marca = ?, valor_adquisicion = ?, "
                + "meses_uso = ?, meses_vigencia = ?, es_critico = ? WHERE id = ?";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            if (con == null) return false;

            ps.setString(1, activo.getNombre());
            ps.setString(2, activo.getMarca());
            ps.setDouble(3, activo.getValorAdquisicion());

            if (activo instanceof Hardware) {
                ps.setInt(4, ((Hardware) activo).getMesesUso());
                ps.setNull(5, Types.INTEGER);
                ps.setNull(6, Types.INTEGER);
            } else if (activo instanceof Licencia) {
                ps.setNull(4, Types.INTEGER);
                ps.setInt(5, ((Licencia) activo).getMesesVigencia());
                ps.setNull(6, Types.INTEGER);
            } else if (activo instanceof Periferico) {
                ps.setNull(4, Types.INTEGER);
                ps.setNull(5, Types.INTEGER);
                ps.setInt(6, ((Periferico) activo).isEsCritico() ? 1 : 0);
            }

            ps.setString(7, activo.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar activo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(String id) {
        String sql = "DELETE FROM activos WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            if (con == null) return false;
            
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar activo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Activo obtenerPorId(String id) {
        String sql = "SELECT * FROM activos WHERE id = ?";
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            if (con == null) return null;
            ps.setString(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearActivo(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar activo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Activo> obtenerTodos() {
        List<Activo> lista = new ArrayList<>();
        String sql = "SELECT * FROM activos";
        try (Connection con = ConexionSQLite.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (con == null) return lista;

            while (rs.next()) {
                lista.add(mapearActivo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar activos: " + e.getMessage());
        }
        return lista;
    }

    private Activo mapearActivo(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String nombre = rs.getString("nombre");
        String marca = rs.getString("marca");
        double valor = rs.getDouble("valor_adquisicion");
        String tipo = rs.getString("tipo");

        switch (tipo) {
            case "HARDWARE":
                int mesesUso = rs.getInt("meses_uso");
                return new Hardware(id, nombre, marca, valor, mesesUso);
            case "LICENCIA":
                int mesesVigencia = rs.getInt("meses_vigencia");
                return new Licencia(id, nombre, marca, valor, mesesVigencia);
            case "PERIFERICO":
                boolean esCritico = rs.getInt("es_critico") == 1;
                return new Periferico(id, nombre, marca, valor, esCritico);
            default:
                return null;
        }
    }
}
