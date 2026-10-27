package cat.pratfp.gamevault.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Registre de les operacions de GameVault en un fitxer de text
 * (operacions.log).
 */
public class RegistreOperacions {

    /** Format de la data i hora de cada línia: 2026-10-13 15:10:02 */
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path fitxer;

    /**
     * @param fitxer fitxer de registre; si no existeix, es crea en la primera
     *               escriptura
     */
    public RegistreOperacions(Path fitxer) {
        this.fitxer = fitxer;
    }

    /**
     * Afegeix una nova línia al final del fitxer de registre amb marca de temps en codificació UTF-8.
     * Si el fitxer no existeix el crea; si ja existeix, afegeix la línia sense esborrar el contingut previ.
     *
     * @param text descripció de l'operació que s'afegeix al registre
     * @throws IOException si es produeix un error en escriure al fitxer
     */
    public void registra(String text) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(fitxer, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(LocalDateTime.now().format(FORMAT) + " | " + text);
            bw.newLine();
        }
    }
}
