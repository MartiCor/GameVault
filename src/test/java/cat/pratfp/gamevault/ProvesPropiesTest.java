package cat.pratfp.gamevault;

import cat.pratfp.gamevault.io.IndexHores;
import cat.pratfp.gamevault.model.Joc;
import cat.pratfp.gamevault.servei.CatalegException;
import cat.pratfp.gamevault.servei.Convertidor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Les teves proves (PR1-14). Aquesta classe SÍ que l'has de modificar.
 * Escriu dues proves noves que no estiguin a les altres classes de prova.
 * Canvia el @DisplayName perquè digui què comproves i treu el fail().
 * Idees: l'índex amb un títol de més de 40 caràcters; una còpia de seguretat d'un fitxer sense extensió;
 * un CSV amb una línia en blanc; carregar un .json que no existeix amb el Convertidor;
 * sumaHores en dos registres seguits...
 */
class ProvesPropiesTest {

    @TempDir
    Path tmp;

    @Test
    @DisplayName("PR1-14 · carregar un fitxer json que no existeix llança CatalegException")
    void carregarFitxerInexistent() {
        Convertidor convertidor = new Convertidor();
        Path noExisteix = tmp.resolve("inventat.json");
        assertThrows(CatalegException.class, () -> convertidor.carrega(noExisteix));
    }

    @Test
    @DisplayName("PR1-14 · suma hores en dos registres consecutius")
    void sumaHoresDosRegistres() throws Exception {
        Path dat = tmp.resolve("hores_test.dat");
        IndexHores index = new IndexHores(dat);
        Joc joc1 = new Joc(1, "Zelda", "Switch", "Nintendo", LocalDate.of(2023, 5, 12), 10.0, 9.5);
        Joc joc2 = new Joc(2, "Mario", "Switch", "Nintendo", LocalDate.of(2023, 10, 20), 5.0, 9.0);

        index.escriu(0, joc1);
        index.escriu(1, joc2);

        index.sumaHores(0, 2.5);
        index.sumaHores(1, 4.0);

        assertEquals(12.5, index.llegeixHores(0));
        assertEquals(9.0, index.llegeixHores(1));
    }
}
