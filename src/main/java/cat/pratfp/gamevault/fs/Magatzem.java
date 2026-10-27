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

    /**
     * Crea les tres carpetes de l'estructura del magatzem: import, export i backup.
     * Si ja existeixen no llença cap excepció.
     *
     * @throws IOException si es produeix un error en crear els directoris
     */
    public void preparaEstructura() throws IOException {
        Files.createDirectories(importacio());
        Files.createDirectories(exportacio());
        Files.createDirectories(copies());
    }

    /**
     * Copia el fitxer a la carpeta de backup afegint la marca de temps al nom.
     * Si el destí ja existeix el sobreescriu, mantenint el fitxer original intacte.
     *
     * @param fitxer ruta del fitxer original
     * @return ruta del fitxer copiat a la carpeta de backup
     * @throws IOException si es produeix un error durant la còpia
     */
    public Path copiaSeguretat(Path fitxer) throws IOException {
        String nomOriginal = fitxer.getFileName().toString();
        int punt = nomOriginal.lastIndexOf('.');
        String nom = punt < 0 ? nomOriginal : nomOriginal.substring(0, punt);
        String extensio = punt < 0 ? "" : nomOriginal.substring(punt);
        String nomCopia = nom + "_" + marcaDeTemps() + extensio;
        Path desti = copies().resolve(nomCopia);
        Files.copy(fitxer, desti, StandardCopyOption.REPLACE_EXISTING);
        return desti;
    }

    /**
     * Retorna la suma de les mides en bytes de tots els fitxers regulars del magatzem.
     *
     * @return mida total en bytes
     * @throws IOException si es produeix un error en recórrer el directori
     */
    public long midaTotal() throws IOException {
        try (Stream<Path> camins = Files.walk(base)) {
            return camins.filter(Files::isRegularFile)
                    .mapToLong(Magatzem::mida)
                    .sum();
        }
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
