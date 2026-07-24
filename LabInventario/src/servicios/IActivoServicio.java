package servicios;

import modelo.Activo;
import java.util.List;
import exception.ActivoDuplicadoException;
import exception.ActivoNoEncontradoException;
import exception.DatoInvalidoException;

public interface IActivoServicio {
    boolean registrarActivo(Activo activo) throws ActivoDuplicadoException, DatoInvalidoException;
    boolean actualizarActivo(Activo activo) throws ActivoNoEncontradoException;
    boolean eliminarActivo(String id);
    List<Activo> listarTodos();
    Activo buscarPorId(String id);
    double calcularCostoMantenimientoTotal();
}