package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Activo;
import util.ConexionSQLite;
import mapper.ActivoMapper;

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

        try (Connection con = ConexionSQLite.conectar()) {
            if (con != null) {
                try (Statement stmt = con.createStatement()) {
                    stmt.execute(sql);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    @Override
    public boolean insertar(Activo activo) {
        String sql = "INSERT INTO activos (id, nombre, marca, valor_adquisicion, tipo, meses_uso, meses_vigencia, es_critico) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionSQLite.conectar(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) return false;

            ActivoMapper.llenarPreparedStatement(ps, activo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar activo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Activo activo) {
        String sql = "UPDATE activos SET nombre = ?, marca = ?, valor_adquisicion = ?, "
                + "tipo = ?, meses_uso = ?, meses_vigencia = ?, es_critico = ? WHERE id = ?";

        try (Connection con = ConexionSQLite.conectar(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) return false;

            ps.setString(1, activo.getNombre());
            ps.setString(2, activo.getMarca());
            ps.setDouble(3, activo.getValorAdquisicion());
            ps.setString(4, activo.getTipo());
            
            if (activo.getMesesUso() != null) ps.setInt(5, activo.getMesesUso()); else ps.setNull(5, Types.INTEGER);
            if (activo.getMesesVigencia() != null) ps.setInt(6, activo.getMesesVigencia()); else ps.setNull(6, Types.INTEGER);
            if (activo.getEsCritico() != null) ps.setInt(7, activo.getEsCritico()); else ps.setNull(7, Types.INTEGER);

            ps.setString(8, activo.getId()); // Se corrigió el índice y duplicación
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
                    return ActivoMapper.crearActivo(rs);
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
                lista.add(ActivoMapper.crearActivo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar activos: " + e.getMessage());
        }
        return lista;
    }
}