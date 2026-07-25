package modelo;

import modelo.Activo;
import modelo.Hardware;
import modelo.Licencia;
import modelo.Periferico;

public class ActivoFactory {

    private ActivoFactory() {
    }

    public static Activo crearActivo(
            String tipo,
            String id,
            String nombre,
            String marca,
            double valor,
            String datoEspecial) {

        switch (tipo.toUpperCase()) {

            case "HARDWARE":
                return new Hardware(
                        id,
                        nombre,
                        marca,
                        valor,
                        Integer.parseInt(datoEspecial));

            case "LICENCIA":
                return new Licencia(
                        id,
                        nombre,
                        marca,
                        valor,
                        Integer.parseInt(datoEspecial));

            case "PERIFERICO":
                return new Periferico(
                        id,
                        nombre,
                        marca,
                        valor,
                        datoEspecial.equalsIgnoreCase("SI")
                                || datoEspecial.equalsIgnoreCase("SÍ")
                                || datoEspecial.equalsIgnoreCase("TRUE"));

            default:
                throw new IllegalArgumentException("Tipo de activo no válido.");
        }
    }
}