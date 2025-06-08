package proxecto25.prox25_mcl.modelo;

public class Prefs {

    private Integer id;
    private String name;
    private String pass;
    private String ip;
    private String port;
    private String language;
    private String imagenprof;

    public Prefs() {
    }

    public Prefs(Integer id, String mail, String pass, String ip, String port, String language, String imagenprof) {
        this.id = id;
        this.name = mail;
        this.pass = pass;
        this.ip = ip;
        this.port = port;
        this.language = language;
        this.imagenprof = imagenprof;
    }

    public Prefs(String mail, String pass, String ip, String port, String language, String imagenprof) {
        this.name = mail;
        this.pass = pass;
        this.ip = ip;
        this.port = port;
        this.language = language;
        this.imagenprof = imagenprof;
    }

    public Prefs(String pass, String mail) {
        this.pass = pass;
        this.name = mail;
    }

    public Prefs(String ip, String port, String language) {
        this.ip = ip;
        this.port = port;
        this.language = language;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImagenprof() {
        return imagenprof;
    }

    public void setImagenprof(String imagenprof) {
        this.imagenprof = imagenprof;
    }
}
