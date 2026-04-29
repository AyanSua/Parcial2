package modelado;

/**
 *
 * @author SALA-404
 */
public class Producto {
 
    private int    idProducto;
    private String producto;
    private int    cantidad;
    private double precioUnidad;
    private String descripcion;
 
    // ─── Constructores ───────────────────────────────────────────────────────
 
    public Producto() {}
 
    public Producto(int idProducto, String producto, int cantidad, double precioUnidad, String descripcion) {
        this.idProducto   = idProducto;
        this.producto     = producto;
        this.cantidad     = cantidad;
        this.precioUnidad = precioUnidad;
        this.descripcion  = descripcion;
    }
 
    // ─── Getters y Setters ───────────────────────────────────────────────────
 
    public int getIdProducto()              { return idProducto; }
    public void setIdProducto(int id)       { this.idProducto = id; }
 
    public String getProducto()             { return producto; }
    public void setProducto(String p)       { this.producto = p; }
 
    public int getCantidad()                { return cantidad; }
    public void setCantidad(int c)          { this.cantidad = c; }
 
    public double getPrecioUnidad()         { return precioUnidad; }
    public void setPrecioUnidad(double p)   { this.precioUnidad = p; }
 
    public String getDescripcion()          { return descripcion; }
    public void setDescripcion(String d)    { this.descripcion = d; }
 
    @Override
    public String toString() {
        return "Producto{id=" + idProducto + ", nombre='" + producto +
               "', cantidad=" + cantidad + ", precio=" + precioUnidad + "}";
    }
}

