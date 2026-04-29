package main;

import vista.ProductosVista;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicación Productos Disponibles (MVC).
 */
public class Main {
    public static void main(String[] args) {
        // Apariencia nativa del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            ProductosVista vista = new ProductosVista();
            vista.setVisible(true);
        });
    }
}