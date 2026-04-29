package vista;

import modelado.Producto;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo modal para Agregar o Editar un Producto.
 * Se reutiliza para ambas acciones: si se pasa un Producto existente → modo edición.
 */
public class ProductoDialogo extends JDialog {

    private JTextField txtProducto;
    private JTextField txtCantidad;
    private JTextField txtPrecio;
    private JTextArea  txtDescripcion;

    private boolean confirmado = false;
    private Producto productoEditado;
    private final Producto productoOriginal; // null si es nuevo

    public ProductoDialogo(JFrame parent, Producto productoExistente) {
        super(parent, productoExistente == null ? "Agregar Producto" : "Editar Producto", true);
        this.productoOriginal = productoExistente;
        initUI();
        if (productoExistente != null) rellenarCampos(productoExistente);
    }

    private void initUI() {
        setSize(380, 310);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // ── Campos ────────────────────────────────────────────────────────────
        txtProducto    = new JTextField(20);
        txtCantidad    = new JTextField(20);
        txtPrecio      = new JTextField(20);
        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        agregarFila(panel, gbc, 0, "Producto:",       txtProducto);
        agregarFila(panel, gbc, 1, "Cantidad:",       txtCantidad);
        agregarFila(panel, gbc, 2, "Precio Unidad:",  txtPrecio);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(new JScrollPane(txtDescripcion), gbc);

        // ── Botones ───────────────────────────────────────────────────────────
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.setPreferredSize(new Dimension(90, 28));
        btnCancelar.setPreferredSize(new Dimension(90, 28));

        btnGuardar.addActionListener(e -> accionGuardar());
        btnCancelar.addActionListener(e -> dispose());

        panelBtn.add(btnCancelar);
        panelBtn.add(btnGuardar);

        setLayout(new BorderLayout());
        add(panel,    BorderLayout.CENTER);
        add(panelBtn, BorderLayout.SOUTH);
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila, String label, JTextField campo) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    private void rellenarCampos(Producto p) {
        txtProducto.setText(p.getProducto());
        txtCantidad.setText(String.valueOf(p.getCantidad()));
        txtPrecio.setText(String.valueOf(p.getPrecioUnidad()));
        txtDescripcion.setText(p.getDescripcion());
    }

    private void accionGuardar() {
        String nombre = txtProducto.getText().trim();
        String cantStr = txtCantidad.getText().trim();
        String precioStr = txtPrecio.getText().trim();

        // ── Validaciones ──────────────────────────────────────────────────────
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del producto es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtProducto.requestFocus();
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantStr);
            if (cantidad < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero mayor o igual a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtCantidad.requestFocus();
            return;
        }
        double precio;
        try {
            precio = Double.parseDouble(precioStr.replace(",", "."));
            if (precio < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido mayor o igual a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtPrecio.requestFocus();
            return;
        }

        // ── Construir producto ────────────────────────────────────────────────
        productoEditado = new Producto(
            productoOriginal != null ? productoOriginal.getIdProducto() : 0,
            nombre,
            cantidad,
            precio,
            txtDescripcion.getText().trim()
        );

        confirmado = true;
        dispose();
    }

    // ─── Getters para la vista principal ─────────────────────────────────────
    public boolean isConfirmado() { return confirmado; }
    public Producto getProducto() { return productoEditado; }
}