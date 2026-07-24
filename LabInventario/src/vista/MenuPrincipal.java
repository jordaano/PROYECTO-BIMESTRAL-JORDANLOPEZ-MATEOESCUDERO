package vista;

import controlador.ActivoController;
import factory.ActivoFactory;
import modelo.Activo;

import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private final ActivoController controlador;
    private final Scanner sc;

    public MenuPrincipal(ActivoController controlador) {
        this.controlador = controlador;
        sc = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion = -1;

        do {
            System.out.println("\n========== LAB INVENTARIO ==========");
            System.out.println("1. Registrar activo");
            System.out.println("2. Buscar activo");
            System.out.println("3. Actualizar activo");
            System.out.println("4. Eliminar activo");
            System.out.println("5. Listar activos");
            System.out.println("6. Calcular mantenimiento");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1:
                        registrarActivo();
                        break;
                    case 2:
                        buscarActivo();
                        break;
                    case 3:
                        actualizarActivo();
                        break;
                    case 4:
                        eliminarActivo();
                        break;
                    case 5:
                        listarActivos();
                        break;
                    case 6:
                        mostrarTotal();
                        break;
                    case 0:
                        System.out.println("Hasta luego.");
                        break;
                    default:
                        System.out.println("Opción incorrecta.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }

        } while (opcion != 0);
    }

    // --- IMPLEMENTACIÓN DE LOS MÉTODOS ---
    private void registrarActivo() {
        System.out.println("\n--- Registrar Activo ---");
        try {
            System.out.print("ID: ");
            String id = sc.nextLine();
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Marca: ");
            String marca = sc.nextLine();
            System.out.print("Valor de Adquisición: ");
            double valor = Double.parseDouble(sc.nextLine());

            System.out.print("Tipo (HARDWARE / LICENCIA / PERIFERICO): ");
            String tipo = sc.nextLine().toUpperCase();

            String datoEspecial = "";
            switch (tipo) {
                case "HARDWARE":
                    System.out.print("Meses de uso: ");
                    datoEspecial = sc.nextLine();
                    break;
                case "LICENCIA":
                    System.out.print("Meses de vigencia: ");
                    datoEspecial = sc.nextLine();
                    break;
                case "PERIFERICO":
                    System.out.print("Es crítico (SI/NO): ");
                    datoEspecial = sc.nextLine();
                    break;
                default:
                    System.out.println("❌ Tipo no reconocido. Abortando registro.");
                    return;
            }

            // Usamos el Factory para instanciar la clase correcta sin usar 'new' directamente aquí
            Activo nuevoActivo = ActivoFactory.crearActivo(tipo, id, nombre, marca, valor, datoEspecial);

            if (controlador.agregarActivo(tipo, id, nombre, marca, valor, datoEspecial)) {
                System.out.println("✅ Activo registrado correctamente.");
            } else {
                System.out.println("❌ Error al registrar el activo.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Formato de número inválido para el valor.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void buscarActivo() {
        System.out.println("\n--- Buscar Activo ---");
        System.out.print("Ingrese el ID del activo: ");
        String id = sc.nextLine();

        Activo activo = controlador.buscarActivoPorId(id);
        if (activo != null) {
            System.out.println("✅ Activo encontrado:");
            System.out.println("ID: " + activo.getId() + " | Nombre: " + activo.getNombre()
                    + " | Marca: " + activo.getMarca() + " | Tipo: " + activo.getTipo()
                    + " | Valor: $" + activo.getValorAdquisicion());
        } else {
            System.out.println("❌ No se encontró ningún activo con ese ID.");
        }
    }

    private void actualizarActivo() {
        System.out.println("\n--- Actualizar Activo ---");
        System.out.print("Ingrese el ID del activo a actualizar: ");
        String id = sc.nextLine();

        Activo activoExistente = controlador.buscarActivoPorId(id);
        if (activoExistente == null) {
            System.out.println("❌ No existe un activo con ese ID.");
            return;
        }

        try {
            System.out.print("Nuevo Nombre (actual: " + activoExistente.getNombre() + "): ");
            String nombre = sc.nextLine();
            System.out.print("Nueva Marca (actual: " + activoExistente.getMarca() + "): ");
            String marca = sc.nextLine();
            System.out.print("Nuevo Valor de Adquisición (actual: " + activoExistente.getValorAdquisicion() + "): ");
            double valor = Double.parseDouble(sc.nextLine());

            String tipo = activoExistente.getTipo();
            String datoEspecial = "";

            // Dependiendo del tipo, pedimos el dato específico correspondiente
            switch (tipo) {
                case "HARDWARE":
                    System.out.print("Nuevos meses de uso (actual: " + activoExistente.getMesesUso() + "): ");
                    datoEspecial = sc.nextLine();
                    break;
                case "LICENCIA":
                    System.out.print("Nuevos meses de vigencia (actual: " + activoExistente.getMesesVigencia() + "): ");
                    datoEspecial = sc.nextLine();
                    break;
                case "PERIFERICO":
                    String criticoActual = activoExistente.getEsCritico() == 1 ? "SI" : "NO";
                    System.out.print("Es crítico (SI/NO, actual: " + criticoActual + "): ");
                    datoEspecial = sc.nextLine();
                    break;
            }

            // Recreamos el activo con los nuevos datos manteniendo el mismo ID y Tipo
            Activo activoActualizado = ActivoFactory.crearActivo(tipo, id, nombre, marca, valor, datoEspecial);

            if (controlador.agregarActivo(tipo, id, nombre, marca, valor, datoEspecial)) {
                System.out.println("✅ Activo registrado correctamente.");
            } else {
                System.out.println("❌ Error al registrar el activo.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Formato de número inválido.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void eliminarActivo() {
        System.out.println("\n--- Eliminar Activo ---");
        System.out.print("Ingrese el ID del activo a eliminar: ");
        String id = sc.nextLine();

        if (controlador.eliminarActivo(id)) {
            System.out.println("✅ Activo eliminado correctamente.");
        } else {
            System.out.println("❌ No se pudo eliminar (verifique si el ID existe en la base).");
        }
    }

    private void listarActivos() {
        System.out.println("\n--- Listado de Activos ---");
        List<Activo> lista = controlador.listarActivos();

        if (lista.isEmpty()) {
            System.out.println("No hay activos registrados en el sistema.");
        } else {
            for (Activo a : lista) {
                System.out.printf("- ID: %s | Nombre: %s | Tipo: %s | Marca: %s | Costo Mantenimiento: $%.2f%n",
                        a.getId(), a.getNombre(), a.getTipo(), a.getMarca(), a.calcularCostoMantenimiento());
            }
        }
    }

    private void mostrarTotal() {
        System.out.println("\n--- Costo de Mantenimiento Total ---");
        double total = controlador.calcularCostoMantenimientoTotal();
        System.out.printf("El costo total de mantenimiento de todos los activos es: $%.2f%n", total);
    }
}
