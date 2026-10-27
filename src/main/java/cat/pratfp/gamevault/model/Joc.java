package cat.pratfp.gamevault.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Un joc del catàleg GameVault.
 * <p>
 * És un record: immutable, amb constructor, accessors, equals, hashCode i toString generats.
 * El constructor compacte valida les dades abans de crear l'objecte.
 */
public record Joc(int id, String titol, String plataforma, String estudi,
                  LocalDate dataSortida, double horesJugades, double nota) implements Serializable {

    /**
     * Valida les dades del joc en el constructor compacte.
     *
     * @throws IllegalArgumentException si el títol és buit o nul, les hores són negatives o la nota no està entre 0 i 10
     */
    public Joc {
        if (titol == null || titol.isBlank()) {
            throw new IllegalArgumentException("El títol no pot ser buit o nul.");
        }
        if (horesJugades < 0) {
            throw new IllegalArgumentException("Les hores jugades no poden ser negatives.");
        }
        if (nota < 0 || nota > 10) {
            throw new IllegalArgumentException("La nota ha d'estar entre 0 i 10.");
        }
    }
}
