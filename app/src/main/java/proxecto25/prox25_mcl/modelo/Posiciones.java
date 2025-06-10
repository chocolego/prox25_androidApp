package proxecto25.prox25_mcl.modelo;

import java.time.LocalDateTime;
import java.util.List;

public class Posiciones {

        private String id; // MongoDB ID
        private int idDispositivo;
        private String timestamp; // Changed to String for Android compatibility
        private List<Double> location; // GeoJSON coordinates: [longitude, latitude]

        public Posiciones() {}

        public Posiciones(int idDispositivo, String timestamp, List<Double> location) {
            this.idDispositivo = idDispositivo;
            this.timestamp = timestamp;
            this.location = location;
        }

        // Getters & Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public int getIdDispositivo() { return idDispositivo; }
        public void setIdDispositivo(int idDispositivo) { this.idDispositivo = idDispositivo; }

        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

        public List<Double> getLocation() { return location; }
        public void setLocation(List<Double> location) { this.location = location; }

        public double getLongitude() { return location.get(0); }
        public double getLatitude() { return location.get(1); }

        @Override
        public String toString() {
            return "Posiciones{" +
                    "id='" + id + '\'' +
                    ", idDispositivo=" + idDispositivo +
                    ", timestamp='" + timestamp + '\'' +
                    ", location=" + location +
                    '}';
        }


}
