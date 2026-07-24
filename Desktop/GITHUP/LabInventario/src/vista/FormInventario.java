package vista;

import controlador.ActivoController;
import modelo.Activo;
import modelo.Hardware;
import modelo.Licencia;
import modelo.Periferico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormInventario extends JFrame {
    private final ActivoController controlador;

    private JTextField txtId, txtNombre, txtMarca, txtValor, txtVariable;
    private JComboBox<String> cbTipo;
    private JLabel lblVariable;
    private JTable tablaActivos;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalMantenimiento;

    public FormInventario(ActivoController controlador) {
        this.controlador = controlador;
        setTitle("Lab-Inventario - UTPL");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        cargarDatosTabla();
    }

    private void initComponents() {
        JPanel panelForm = new JPanel(new GridLayout(7, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Activo"));

        panelForm.add(new JLabel("ID:"));
        txtId = new JTextField();
        panelForm.add(txtId);

        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        panelForm.add(txtMarca);

        panelForm.add(new JLabel("Valor de Adquisición ($):"));
        txtValor = new JTextField();
        panelForm.add(txtValor);

        panelForm.add(new JLabel("Tipo de Activo:"));
        cbTipo = new JComboBox<>(new String[]{"HARDWARE", "LICENCIA", "PERIFERICO"});
        panelForm.add(cbTipo);

        lblVariable = new JLabel("Meses de Uso:");
        panelForm.add(lblVariable);
        txtVariable = new JTextField();
        panelForm.add(txtVariable);

        JButton btnGuardar = new JButton("Registrar Activo");
        panelForm.add(btnGuardar);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Inventario de Activos"));

        String[] columnas = {"ID", "Nombre", "Marca", "Valor", "Tipo", "Detalle Tipo", "Mantenimiento ($)"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaActivos = new JTable(modeloTabla);
        panelTabla.add(new JScrollPane(tablaActivos), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        lblTotalMantenimiento = new JLabel("Total Mantenimiento: $0.00");
        lblTotalMantenimiento.setFont(new Font("Arial", Font.BOLD, 14));

        panelInferior.add(btnEliminar);
        panelInferior.add(lblTotalMantenimiento);

        setLayout(new BorderLayout(10, 10));
        add(panelForm, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // --- MANEJO DE EVENTOS (LISTENERS) ---

        cbTipo.addActionListener(e -> {
            String seleccionado = (String) cbTipo.getSelectedItem();
            if ("HARDWARE".equals(seleccionado)) {
                lblVariable.setText("Meses de Uso:");
                txtVariable.setEnabled(true);
            } else if ("LICENCIA".equals(seleccionado)) {
                lblVariable.setText("Meses Vigencia:");
                txtVariable.setEnabled(true);
            } else if ("PERIFERICO".equals(seleccionado)) {
                lblVariable.setText("¿Es Crítico? (si/no):");
                txtVariable.setEnabled(true);
            }
        });

        btnGuardar.addActionListener(e -> registrarActivo());

        btnEliminar.addActionListener(e -> eliminarActivoSeleccionado());
    }

    private void registrarActivo() {
        try {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String marca = txtMarca.getText().trim();
            double valor = Double.parseDouble(txtValor.getText().trim());
            String tipo = (String) cbTipo.getSelectedItem();
            String variable = txtVariable.getText().trim();

            Activo nuevoActivo = null;

            if ("HARDWARE".equals(tipo)) {
                int mesesUso = Integer.parseInt(variable);
                nuevoActivo = new Hardware(id, nombre, marca, valor, mesesUso);
            } else if ("LICENCIA".equals(tipo)) {
                int mesesVigencia = Integer.parseInt(variable);
                nuevoActivo = new Licencia(id, nombre, marca, valor, mesesVigencia);
            } else if ("PERIFERICO".equals(tipo)) {
                boolean esCritico = variable.equalsIgnoreCase("si") || variable.equalsIgnoreCase("sí");
                nuevoActivo = new Periferico(id, nombre, marca, valor, esCritico);
            }

            if (nuevoActivo != null && controlador.agregarActivo(nuevoActivo)) {
                JOptionPane.showMessageDialog(this, "Activo registrado correctamente.");
                limpiarCampos();
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el activo. Asegúrate de que el ID no esté repetido.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa datos válidos en los campos numéricos.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarActivoSeleccionado() {
        int filaSeleccionada = tablaActivos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un activo de la tabla para eliminarlo.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar el activo " + id + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (controlador.eliminarActivo(id)) {
                JOptionPane.showMessageDialog(this, "Activo eliminado.");
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al intentar eliminar el activo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0); 
        List<Activo> activos = controlador.listarActivos();

        for (Activo a : activos) {
            String detalleTipo = "";
            if (a instanceof Hardware) {
                detalleTipo = "Meses de uso: " + ((Hardware) a).getMesesUso();
            } else if (a instanceof Licencia) {
                detalleTipo = "Meses vigencia: " + ((Licencia) a).getMesesVigencia();
            } else if (a instanceof Periferico) {
                detalleTipo = ((Periferico) a).isEsCritico() ? "Es Crítico" : "No Crítico";
            }

            modeloTabla.addRow(new Object[]{
                    a.getId(),
                    a.getNombre(),
                    a.getMarca(),
                    String.format("$%.2f", a.getValorAdquisicion()),
                    a.getClass().getSimpleName().toUpperCase(),
                    detalleTipo,
                    String.format("$%.2f", a.calcularCostoMantenimiento())
            });
        }

        double totalMantenimiento = controlador.calcularCostoMantenimientoTotal();
        lblTotalMantenimiento.setText(String.format("Total Mantenimiento Proyectado: $%.2f", totalMantenimiento));
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtMarca.setText("");
        txtValor.setText("");
        txtVariable.setText("");
        txtId.requestFocus();
    }
}
