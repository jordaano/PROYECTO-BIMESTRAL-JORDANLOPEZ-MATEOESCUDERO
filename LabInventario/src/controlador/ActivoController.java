package controlador;

import modelo.Activo;
import servicios.IActivoServicio;
import factory.ActivoFactory;
import java.util.List;

public class ActivoController {

    // DIP: Dependemos de la abstracción, no de la implementación (ActivoServicio)
    private final IActivoServicio servicio;

    public ActivoController(IActivoServicio servicio) {
        this.servicio = servicio;
    }

    // SRP: El controlador orquesta la creación usando el Factory y luego llama al servicio
    public boolean agregarActivo(String tipo, String id, String nombre, String marca, double valor, String datoEspecial) throws Exception {
        Activo activo = ActivoFactory.crearActivo(tipo, id, nombre, marca, valor, datoEspecial);
        return servicio.registrarActivo(activo);
    }

    public boolean modificarActivo(String tipo, String id, String nombre, String marca, double valor, String datoEspecial) throws Exception {
        Activo activo = ActivoFactory.crearActivo(tipo, id, nombre, marca, valor, datoEspecial);
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