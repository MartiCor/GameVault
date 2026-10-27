package cat.pratfp.gamevault.io;

import cat.pratfp.gamevault.model.Joc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Cau binària del catàleg: desa la llista sencera amb serialització Java per recuperar-la ràpidament.
 * Només la pot llegir Java: no és un format d'intercanvi.
 */
public class CauCataleg {

    /**
     * Serialitza la llista sencera de jocs al fitxer binari indicat.
     * S'embolica la llista en un {@link ArrayList} per assegurar que sempre se serialitza
     * un tipus de llista concret i homogeni, garantint compatibilitat fins i tot si es rep
     * una implementació immutable com la de List.of().
     *
     * @param p ruta del fitxer binari de destí (.ser)
     * @param jocs llista de jocs a serialitzar
     * @throws IOException si es produeix un error d'escriptura
     */
    public void desa(Path p, List<Joc> jocs) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(p))) {
            oos.writeObject(new ArrayList<>(jocs));
        }
    }

    /**
     * Recupera el catàleg desat amb {@link #desa(Path, List)}.
     *
     * @param p fitxer .ser
     * @return la llista de jocs recuperada
     * @throws IOException si el fitxer no existeix, està corromput o la classe no es troba
     */
    @SuppressWarnings("unchecked")
    public List<Joc> carrega(Path p) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(p))) {
            return (List<Joc>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("No s'ha trobat la classe al deserialitzar: " + e.getMessage(), e);
        }
    }
}
