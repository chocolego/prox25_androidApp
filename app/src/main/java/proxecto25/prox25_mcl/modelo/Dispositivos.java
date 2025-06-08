package proxecto25.prox25_mcl.modelo;

import java.util.List;

public class Dispositivos {

    private Integer id;
    private String nombreDispositivo;
    private String numeroSerie;
    private String modelo;
    private String fechaCreacion;  // Add this!
    private List<Targets> targets;

    public Dispositivos() {
    }

    public Dispositivos(Integer id, String nombreDispositivo, String numeroSerie, String modelo) {
        this.id = id;
        this.nombreDispositivo = nombreDispositivo;
        this.numeroSerie = numeroSerie;
        this.modelo = modelo;
    }

    public Dispositivos(String modelo, String numeroSerie, String nombreDispositivo) {
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.nombreDispositivo = nombreDispositivo;
    }

    public Dispositivos(String modelo, String numeroSerie, String nombreDispositivo, Integer id, List<Targets> targets) {
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.nombreDispositivo = nombreDispositivo;
        this.id = id;
        this.targets = targets;
    }

    public Dispositivos(Integer id, String nombreDispositivo, String numeroSerie, String modelo, String fechaCreacion, List<Targets> targets) {
        this.id = id;
        this.nombreDispositivo = nombreDispositivo;
        this.numeroSerie = numeroSerie;
        this.modelo = modelo;
        this.fechaCreacion = fechaCreacion;
        this.targets = targets;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreDispositivo() {
        return nombreDispositivo;
    }

    public void setNombreDispositivo(String nombreDispositivo) {
        this.nombreDispositivo = nombreDispositivo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public List<Targets> getTargets() {
        return targets;
    }

    public void setTargets(List<Targets> targets) {
        this.targets = targets;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Disp- " +
                "id: " + id +
                ", " + '\'' + nombreDispositivo + '\''
                +" (" + numeroSerie +") ";
    }
}
