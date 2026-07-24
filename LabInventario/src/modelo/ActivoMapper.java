package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import factory.ActivoFactory;
import modelo.Activo;

public class ActivoMapper {

    private ActivoMapper() {}

    public static void llenarPreparedStatement(PreparedStatement ps, Activo activo) throws SQLException {
        ps.setString(1, activo.getId());
        ps.setString(2, activo.getNombre());
        ps.setString(3, activo.getMarca());
        ps.setDouble(4, activo.getValorAdquisicion());
        ps.setString(5, activo.getTipo());

        // Manejo nulo unificado sin utilizar instanceof
        asignarEnteroONulo(ps, 6, activo.getMesesUso());
        asignarEnteroONulo(ps, 7, activo.getMesesVigencia());
        asignarEnteroONulo(ps, 8, activo.getEsCritico());
    }

    private static void asignarEnteroONulo(PreparedStatement ps, int indice, Integer valor) throws SQLException {
        if (valor != null) {
            ps.setInt(indice, valor);
        } else {
            ps.setNull(indice, Types.INTEGER);
        }
    }

    public static Activo crearActivo(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String nombre = rs.getString("nombre");
        String marca = rs.getString("marca");
        double valor = rs.getDouble("valor_adquisicion");
        String tipo = rs.getString("tipo");

        String datoEspecial;
        switch (tipo) {
            case "HARDWARE":
                datoEspecial = String.valueOf(rs.getInt("meses_uso"));
                break;
            case "LICENCIA":
                datoEspecial = String.valueOf(rs.getInt("meses_vigencia"));
                break;
            case "PERIFERICO":
                datoEspecial = rs.getInt("es_critico") == 1 ? "SI" : "NO";
                break;
            default:
                return null;
        }

        return ActivoFactory.crearActivo(tipo, id, nombre, marca, valor, datoEspecial);
    }
}