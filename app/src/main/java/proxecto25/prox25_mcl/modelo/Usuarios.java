package proxecto25.prox25_mcl.modelo;

import java.util.List;

public class Usuarios {

    private Integer id;
    private String nombreUsuario;
    private String nombreDisplay;
    private String email;
    private String contrasena;
    private String telefono;
    List<Dispositivos> dispostivosAsignados;

    public Usuarios() {
    }

    public Usuarios(Integer id, String nombreUsuario, String nombreDisplay, String email, String contrasena, String telefono, List<Dispositivos> dispostivosAsignados) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.nombreDisplay = nombreDisplay;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.dispostivosAsignados = dispostivosAsignados;
    }

    public Usuarios(String nombreUsuario, String nombreDisplay, String email, String contrasena, String telefono, List<Dispositivos> dispostivosAsignados) {
        this.nombreUsuario = nombreUsuario;
        this.nombreDisplay = nombreDisplay;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.dispostivosAsignados = dispostivosAsignados;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreDisplay() {
        return nombreDisplay;
    }

    public void setNombreDisplay(String nombreDisplay) {
        this.nombreDisplay = nombreDisplay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Dispositivos> getDispostivosAsignados() {
        return dispostivosAsignados;
    }

    public void setDispostivosAsignados(List<Dispositivos> dispostivosAsignados) {
        this.dispostivosAsignados = dispostivosAsignados;
    }

    @Override
    public String toString() {
        return "Target - " +
                "id: " + id
                + ", " + '\'' + nombreUsuario + '\''
                + " (" + email +")";
    }
}
