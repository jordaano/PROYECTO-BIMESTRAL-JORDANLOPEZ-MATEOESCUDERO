package servicios;

import dao.IActivoDAO;
import modelo.Activo;
import java.util.List;

public class ActivoServicio {
    private final IActivoDAO activoDAO;

    public ActivoServicio(IActivoDAO activoDAO) {
        this.activoDAO = activoDAO;
    }

    public boolean registrarActivo(Activo activo) {
        if (activo.getId() == null || activo.getId().trim().isEmpty()) {
            return false;
        }
        if (activo.getValorAdquisicion() < 0) {
            return false;
        }
        if (activoDAO.obtenerPorId(activo.getId()) != null) {
            return false; 
        }
        return activoDAO.insertar(activo);
    }

    public boolean actualizarActivo(Activo activo) {
        if (activoDAO.obtenerPorId(activo.getId()) == null) {
            return false;
        }
        return activoDAO.actualizar(activo);
    }

    public boolean eliminarActivo(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return activoDAO.eliminar(id);
    }

    public List<Activo> listarTodos() {
        return activoDAO.obtenerTodos();
    }

    public Activo buscarPorId(String id) {
        return activoDAO.obtenerPorId(id);
    }

    public double calcularCostoMantenimientoTotal() {
        List<Activo> activos = activoDAO.obtenerTodos();
        double total = 0.0;
        for (Activo activo : activos) {
            total += activo.calcularCostoMantenimiento();
        }
        return total;
    }
}