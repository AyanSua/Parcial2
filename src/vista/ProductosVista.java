package vista;

import controlador.ProductoControlador;
import modelado.Producto;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista: ProductosView
 * Interfaz gráfica (Swing) que replica la pantalla "Productos Disponibles".
 * Columnas: Id Producto | Producto | Cantidad | Precio Unidad
 * Botones  : Buscar | Eliminar | Editar | Agregar
 */
public class ProductosVista extends JFrame {

    // ─── Componentes ─────────────────────────────────────────────────────────
    private JTable             tabla;
    private DefaultTableModel  modeloTabla;
    private JTextArea          areaDescripcion;
    private JTextField         campoBusqueda;
    private JButton            btnBuscar, btnEliminar, btnEditar, btnAgregar;

    // ─── Controlador ─────────────────────────────────────────────────────────
    private final ProductoControlador controller = new ProductoControlador();

    // ─── Colores (rosa pastel igual a la imagen) ──────────────────────────────
    private static final Color COLOR_FONDO  = new Color(240, 180, 180); // rosa
    private static final Color COLOR_BLANCO = Color.WHITE;
    private static final Color COLOR_HEADER = new Color(220, 220, 220); // gris claro

    // ─── Constructor ─────────────────────────────────────────────────────────
    public ProductosVista() {
        initUI();
        cargarTabla(controller.listarTodos());
    }

    // ─── Inicialización de la interfaz ────────────────────────────────────────
    private void initUI() {
        setTitle("Productos Disponibles");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 420);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con fondo rosa
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Título ────────────────────────────────────────────────────────────
        JLabel lblTitulo = new JLabel("Productos Disponibles", SwingConstants.RIGHT);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // ── Tabla ─────────────────────────────────────────────────────────────
        String[] columnas = {"Id Producto", "Producto", "Cantidad", "Precio Unidad"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(22);
        tabla.setGridColor(COLOR_HEADER);
        tabla.getTableHeader().setBackground(COLOR_BLANCO);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.setBackground(COLOR_BLANCO);

        // Escuchar selección para mostrar descripción
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarDescripcion();
        });

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(new LineBorder(Color.GRAY));
        scrollTabla.setPreferredSize(new Dimension(0, 160));

        // ── Área de descripción ───────────────────────────────────────────────
        areaDescripcion = new JTextArea(4, 0);
        areaDescripcion.setEditable(false);
        areaDescripcion.setBackground(new Color(245, 245, 245));
        areaDescripcion.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        areaDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JScrollPane scrollDesc = new JScrollPane(areaDescripcion);
        scrollDesc.setBorder(new LineBorder(Color.GRAY));

        // ── Panel central (tabla + descripción) ───────────────────────────────
        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        panelCentro.setBackground(COLOR_FONDO);
        panelCentro.add(scrollTabla, BorderLayout.NORTH);
        panelCentro.add(scrollDesc,  BorderLayout.CENTER);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // ── Panel inferior (campo búsqueda + botones) ─────────────────────────
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelBotones.setBackground(COLOR_FONDO);

        campoBusqueda = new JTextField(15);
        campoBusqueda.setPreferredSize(new Dimension(150, 28));

        btnBuscar   = crearBoton("Buscar");
        btnEliminar = crearBoton("Eliminar");
        btnEditar   = crearBoton("Editar");
        btnAgregar  = crearBoton("Agregar");

        panelBotones.add(campoBusqueda);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnAgregar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // ── Eventos de botones ────────────────────────────────────────────────
        btnBuscar  .addActionListener(e -> accionBuscar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnEditar  .addActionListener(e -> accionEditar());
        btnAgregar .addActionListener(e -> accionAgregar());

        setContentPane(panelPrincipal);
    }

    // ─── Utilidad: crear botón estilizado ─────────────────────────────────────
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(90, 28));
        btn.setBackground(COLOR_BLANCO);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return btn;
    }

    // ─── Carga la tabla con la lista de productos ─────────────────────────────
    private void cargarTabla(List<Producto> lista) {
        modeloTabla.setRowCount(0);
        for (Producto p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getIdProducto(),
                p.getProducto(),
                p.getCantidad(),
                String.format("$ %.2f", p.getPrecioUnidad())
            });
        }
        areaDescripcion.setText("");
    }

    // ─── Muestra la descripción del producto seleccionado ────────────────────
    private void mostrarDescripcion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Producto p = controller.obtenerPorId(id);
        if (p != null) {
            areaDescripcion.setText("Descripción: " + p.getDescripcion());
        }
    }

    // ─── Acción: Buscar ───────────────────────────────────────────────────────
    private void accionBuscar() {
        String texto = campoBusqueda.getText().trim();
        if (texto.isEmpty()) {
            cargarTabla(controller.listarTodos());
        } else {
            List<Producto> resultado = controller.buscar(texto);
            if (resultado.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron productos.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }
            cargarTabla(resultado);
        }
    }

    // ─── Acción: Eliminar ─────────────────────────────────────────────────────
    private void accionEliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el producto \"" + nombre + "\"?", "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
                cargarTabla(controller.listarTodos());
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ─── Acción: Editar ───────────────────────────────────────────────────────
    private void accionEditar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Producto p = controller.obtenerPorId(id);
        if (p == null) return;

        // Reutilizamos el mismo diálogo de formulario
        ProductoDialogo dialogo = new ProductoDialogo(this, p);
        dialogo.setVisible(true);
        if (dialogo.isConfirmado()) {
            Producto editado = dialogo.getProducto();
            if (controller.editar(editado)) {
                JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
                cargarTabla(controller.listarTodos());
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ─── Acción: Agregar ──────────────────────────────────────────────────────
    private void accionAgregar() {
        ProductoDialogo dialogo = new ProductoDialogo(this, null);
        dialogo.setVisible(true);
        if (dialogo.isConfirmado()) {
            Producto nuevo = dialogo.getProducto();
            if (controller.agregar(nuevo)) {
                JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
                cargarTabla(controller.listarTodos());
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
