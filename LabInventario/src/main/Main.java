package main;

import controlador.ActivoController;
import modelo.ActivoDAO;
import modelo.IActivoDAO;
import modelo.ActivoServicio;
import modelo.IActivoServicio;
import vista.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        // DIP: Se inyectan abstracciones (interfaces) a lo largo de toda la aplicación.
        IActivoDAO dao = new ActivoDAO();
        IActivoServicio servicio = (IActivoServicio) new ActivoServicio(dao);
        ActivoController controlador = new ActivoController(servicio);
        MenuPrincipal menu = new MenuPrincipal(controlador);
        
        menu.iniciar();
    }
}