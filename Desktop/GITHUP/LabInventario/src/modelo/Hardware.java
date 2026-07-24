package modelo;

public class Hardware extends Activo {
    private int mesesUso;

    public Hardware(String id, String nombre, String marca, double valorAdquisicion, int mesesUso) {
        super(id, nombre, marca, valorAdquisicion);
        this.mesesUso = mesesUso;
    }

    public int getMesesUso() { return mesesUso; }
    public void setMesesUso(int mesesUso) { this.mesesUso = mesesUso; }

    @Override
    public double calcularCostoMantenimiento() {
        double costoBase = getValorAdquisicion() * 0.05;
        double recargoDesgaste = getValorAdquisicion() * (0.015 * mesesUso);
        return costoBase + recargoDesgaste;
    }
}
