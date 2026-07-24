package modelo;

public class Licencia extends Activo {
    private int mesesVigencia;

    public Licencia(String id, String nombre, String marca, double valorAdquisicion, int mesesVigencia) {
        super(id, nombre, marca, valorAdquisicion);
        this.mesesVigencia = mesesVigencia;
    }

    public int getMesesVigencia() { return mesesVigencia; }
    public void setMesesVigencia(int mesesVigencia) { this.mesesVigencia = mesesVigencia; }

    @Override
    public double calcularCostoMantenimiento() {
        if (mesesVigencia <= 3) {
            return getValorAdquisicion() * 0.10;
        }
        return getValorAdquisicion() * 0.03;
    }
}