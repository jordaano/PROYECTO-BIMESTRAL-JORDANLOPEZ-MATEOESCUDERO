package main;

import dao.ActivoDAO;
import dao.IActivoDAO;
import servicios.ActivoServicio; 
import controlador.ActivoController;
import vista.FormInventario;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IActivoDAO dao = new ActivoDAO();
            
            ActivoServicio servicio = new ActivoServicio(dao);
            
            ActivoController controlador = new ActivoController(servicio);
            
            FormInventario ventana = new FormInventario(controlador);
            ventana.setVisible(true);
        });
    }
}