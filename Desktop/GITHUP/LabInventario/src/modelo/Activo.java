package modelo;

public abstract class Activo {
    private String id;
    private String nombre;
    private String marca;
    private double valorAdquisicion;

    public Activo(String id, String nombre, String marca, double valorAdquisicion) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.valorAdquisicion = valorAdquisicion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public double getValorAdquisicion() { return valorAdquisicion; }
    public void setValorAdquisicion(double valorAdquisicion) { this.valorAdquisicion = valorAdquisicion; }

    public abstract double calcularCostoMantenimiento();
}
