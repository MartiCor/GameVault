package cat.pratfp.gamevault.fs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

/**
 * El magatzem de fitxers de GameVault: una carpeta base amb tres subcarpetes,
 * import, export i backup.
 */
public class Magatzem {

    private static final DateTimeFormatter MARCA = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final Path base;

    /**
     * Crea un magatzem sobre una carpeta base. No toca el disc.
     *
     * @param base carpeta arrel del magatzem (per exemple, Path.of("dades"))
     */
    public Magatzem(Path base) {
        this.base = base;
    }

    /** @return la carpeta d'importació (base/import) */
    public Path importacio() {
        return base.resolve("import");
    }

    /** @return la carpeta d'exportació (base/export) */
    public Path exportacio() {
        return base.resolve("export");
    }

    /** @return la carpeta de còpies de seguretat (base/backup) */
    public Path copies() {
        return base.resolve("backup");
    }

    /*
     * TODO PR1-02 · Crear l'estructura (CA 1.1)
     * Crea les tres carpetes: importacio(), exportacio() i copies().
     * Si ja existeixen, el mètode NO pot fallar: es pot cridar tantes vegades com calgui.
     * Pista: sessió 2, Ex3CreaCarpetes. Quin dels dos mètodes de Files no peta si la carpeta ja hi és?
     */
    public void preparaEstructura() throws IOException {
        // Escriu aquí el teu codi (PR1-02)
        throw new UnsupportedOperationException("PR1-02: encara no està fet");
    }

    /*
     * TODO PR1-03 · Còpia de seguretat amb marca de temps (CA 1.1)
     * Copia el fitxer dins de copies() amb el nom  <nom>_<marca>.<extensió>
     *   exemple: catalog.csv  ->  backup/catalog_20261013-151002.csv
     * - La marca te la dona marcaDeTemps().
     * - Separa el nom de l'extensió amb lastIndexOf('.').
     * - Si el destí ja existeix, l'ha de substituir.
     * - L'original NO es pot moure ni esborrar.
     * Retorna la ruta de la còpia.
     */
    public Path copiaSeguretat(Path fitxer) throws IOException {
        // Escriu aquí el teu codi (PR1-03)
        throw new UnsupportedOperationException("PR1-03: encara no està fet");
    }

    /*
     * TODO PR1-04 · Mida total del magatzem (CA 1.1)
     * Retorna la suma de les mides (en bytes) de TOTS els fitxers regulars que hi ha dins de la
     * carpeta base, també els de les subcarpetes. Les carpetes no compten.
     * Pista: sessió 2, Ex5Recorre. Files.walk obre recursos del sistema: va dins d'un try-with-resources.
     */
    public long midaTotal() throws IOException {
        // Escriu aquí el teu codi (PR1-04)
        throw new UnsupportedOperationException("PR1-04: encara no està fet");
    }

    /**
     * Llista els fitxers regulars del magatzem amb la seva mida, ordenats per ruta.
     *
     * @return una línia per fitxer, amb el format "ruta relativa (N bytes)"
     * @throws IOException si no es pot recórrer la carpeta
     */
    public List<String> inventari() throws IOException {
        try (Stream<Path> camins = Files.walk(base)) {
            return camins.filter(Files::isRegularFile)
                    .sorted()
                    .map(p -> base.relativize(p) + " (" + mida(p) + " bytes)")
                    .toList();
        }
    }

    /** Mida d'un fitxer sense excepció comprovada, per poder-la fer servir dins d'un map. */
    private static long mida(Path p) {
        try {
            return Files.size(p);
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * Marca de temps per als noms de les còpies.
     *
     * @return la data i hora actuals amb el format yyyyMMdd-HHmmss
     */
    public static String marcaDeTemps() {
        return LocalDateTime.now().format(MARCA);
    }
}
