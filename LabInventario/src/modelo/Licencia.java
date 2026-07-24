// Licencia.java
package modelo;

public class Licencia extends Activo {
    private int mesesVigencia;

    public Licencia(String id, String nombre, String marca, double valorAdquisicion, int mesesVigencia) {
        super(id, nombre, marca, valorAdquisicion);
        this.mesesVigencia = mesesVigencia;
    }

    public void setMesesVigencia(int mesesVigencia) { this.mesesVigencia = mesesVigencia; }

    @Override
    public double calcularCostoMantenimiento() {
        return mesesVigencia <= 3 ? getValorAdquisicion() * 0.10 : getValorAdquisicion() * 0.03;
    }

    @Override public String getTipo() { return "LICENCIA"; }
    @Override public Integer getMesesUso() { return null; }
    @Override public Integer getMesesVigencia() { return mesesVigencia; }
    @Override public Integer getEsCritico() { return null; }
}