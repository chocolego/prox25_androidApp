package proxecto25.prox25_mcl.modelo;

public class Targets {

    private Integer id;
    private String tipoTarget;
    private String nombre;
    private String descripcion;

    public Targets() {
    }

    public Targets(Integer id, String tipoTarget, String nombre, String descripcion) {
        this.id = id;
        this.tipoTarget = tipoTarget;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Targets(String tipoTarget, String nombre, String descripcion) {
        this.tipoTarget = tipoTarget;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoTarget() {
        return tipoTarget;
    }

    public void setTipoTarget(String tipoTarget) {
        this.tipoTarget = tipoTarget;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Target - " +
                    "id: " + id
                    + ", " + '\'' + nombre + '\''
                    + " (" + tipoTarget +")";
    }
}

