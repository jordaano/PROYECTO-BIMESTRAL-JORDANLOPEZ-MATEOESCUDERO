// Hardware.java
package modelo;

public class Hardware extends Activo {
    private int mesesUso;

    public Hardware(String id, String nombre, String marca, double valorAdquisicion, int mesesUso) {
        super(id, nombre, marca, valorAdquisicion);
        this.mesesUso = mesesUso;
    }

    public void setMesesUso(int mesesUso) { this.mesesUso = mesesUso; }

    @Override
    public double calcularCostoMantenimiento() {
        return (getValorAdquisicion() * 0.05) + (getValorAdquisicion() * (0.015 * mesesUso));
    }

    @Override public String getTipo() { return "HARDWARE"; }
    @Override public Integer getMesesUso() { return mesesUso; }
    @Override public Integer getMesesVigencia() { return null; }
    @Override public Integer getEsCritico() { return null; }
}