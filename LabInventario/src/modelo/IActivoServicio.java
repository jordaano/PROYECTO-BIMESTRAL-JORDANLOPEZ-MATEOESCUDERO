package modelo;

import modelo.Activo;
import java.util.List;
import modelo.excepciones.ActivoDuplicadoException;
import modelo.excepciones.ActivoNoEncontradoException;
import modelo.excepciones.DatoInvalidoException;

public interface IActivoServicio {
    boolean registrarActivo(Activo activo) throws ActivoDuplicadoException, DatoInvalidoException;
    boolean actualizarActivo(Activo activo) throws ActivoNoEncontradoException;
    boolean eliminarActivo(String id);
    List<Activo> listarTodos();
    Activo buscarPorId(String id);
    double calcularCostoMantenimientoTotal();
}