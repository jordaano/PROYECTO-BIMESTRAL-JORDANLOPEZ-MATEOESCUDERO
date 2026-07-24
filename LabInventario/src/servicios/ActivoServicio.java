package servicios;

import dao.IActivoDAO;
import modelo.Activo;
import java.util.List;
import exception.ActivoDuplicadoException;
import exception.ActivoNoEncontradoException;
import exception.DatoInvalidoException;

public class ActivoServicio implements IActivoServicio {

    private final IActivoDAO activoDAO;

    // Recibe el DAO mediante inyección de dependencias
    public ActivoServicio(IActivoDAO activoDAO) {
        this.activoDAO = activoDAO;
    }

    @Override
    public boolean registrarActivo(Activo activo)
            throws ActivoDuplicadoException,
            DatoInvalidoException {

        if (activo.getId() == null || activo.getId().trim().isEmpty()) {
            throw new DatoInvalidoException("El ID no puede estar vacío.");
        }

        if (activo.getNombre() == null || activo.getNombre().trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre es obligatorio.");
        }

        if (activo.getMarca() == null || activo.getMarca().trim().isEmpty()) {
            throw new DatoInvalidoException("La marca es obligatoria.");
        }

        if (activo.getValorAdquisicion() <= 0) {
            throw new DatoInvalidoException("El valor debe ser mayor que cero.");
        }

        if (activoDAO.obtenerPorId(activo.getId()) != null) {
            throw new ActivoDuplicadoException(
                    "Ya existe un activo con ese ID.");
        }

        return activoDAO.insertar(activo);

    }

    @Override
    public boolean actualizarActivo(Activo activo)
            throws ActivoNoEncontradoException {

        if (activoDAO.obtenerPorId(activo.getId()) == null) {
            throw new ActivoNoEncontradoException(
                    "No existe un activo con ese ID.");
        }

        return activoDAO.actualizar(activo);

    }

    @Override
    public boolean eliminarActivo(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return activoDAO.eliminar(id);
    }

    @Override
    public List<Activo> listarTodos() {
        return activoDAO.obtenerTodos();
    }

    @Override
    public Activo buscarPorId(String id) {
        return activoDAO.obtenerPorId(id);
    }

    @Override
    public double calcularCostoMantenimientoTotal() {
        List<Activo> activos = activoDAO.obtenerTodos();
        double total = 0.0;
        for (Activo activo : activos) {
            total += activo.calcularCostoMantenimiento();
        }
        return total;
    }
}
