// Periferico.java
package modelo;

public class Periferico extends Activo {
    private boolean esCritico;

    public Periferico(String id, String nombre, String marca, double valorAdquisicion, boolean esCritico) {
        super(id, nombre, marca, valorAdquisicion);
        this.esCritico = esCritico;
    }

    public boolean isEsCritico() { return esCritico; }
    public void setEsCritico(boolean esCritico) { this.esCritico = esCritico; }

    @Override
    public double calcularCostoMantenimiento() {
        return getValorAdquisicion() * (esCritico ? 0.08 : 0.04);
    }

    @Override public String getTipo() { return "PERIFERICO"; }
    @Override public Integer getMesesUso() { return null; }
    @Override public Integer getMesesVigencia() { return null; }
    @Override public Integer getEsCritico() { return esCritico ? 1 : 0; }
}