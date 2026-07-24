package controlador;

import modelo.Activo;
import servicios.ActivoServicio;
import java.util.List;

public class ActivoController {
    private final ActivoServicio servicio;

    public ActivoController(ActivoServicio servicio) {
        this.servicio = servicio;
    }

    public boolean agregarActivo(Activo activo) {
        return servicio.registrarActivo(activo);
    }

    public boolean modificarActivo(Activo activo) {
        return servicio.actualizarActivo(activo);
    }

    public boolean eliminarActivo(String id) {
        return servicio.eliminarActivo(id);
    }

    public List<Activo> listarActivos() {
        return servicio.listarTodos();
    }

    public Activo buscarActivoPorId(String id) {
        return servicio.buscarPorId(id);
    }

    public double calcularCostoMantenimientoTotal() {
        return servicio.calcularCostoMantenimientoTotal();
    }
}
