package dao;

import java.util.List;
import modelo.Activo;

public interface IActivoDAO {
    boolean insertar(Activo activo);
    boolean actualizar(Activo activo);
    boolean eliminar(String id);
    Activo obtenerPorId(String id);
    List<Activo> obtenerTodos();
}