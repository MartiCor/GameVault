package cat.pratfp.gamevault.io;

import cat.pratfp.gamevault.model.Joc;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Lectura i escriptura del catàleg en JSON amb Jackson.
 */
public class JsonJocDao {

    private final ObjectMapper mapper = creaMapper();

    /**
     * Retorna una instància d'ObjectMapper configurada per suportar dates de Java 8
     * (LocalDate) com a cadenes ISO-8601 i tolerar propietats desconegudes.
     *
     * @return instància configurada d'ObjectMapper
     */
    private static ObjectMapper creaMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * Escriu la llista de jocs al fitxer en format JSON amb sortida indentada.
     *
     * @param p ruta del fitxer JSON de destí
     * @param jocs llista de jocs a serialitzar
     * @throws IOException si es produeix un error durant l'escriptura
     */
    public void escriu(Path p, List<Joc> jocs) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(p.toFile(), jocs);
    }

    /**
     * Llegeix la llista de jocs des del fitxer JSON fent servir un {@link TypeReference}.
     *
     * @param p ruta del fitxer JSON
     * @return llista d'objectes Joc deserialitzats
     * @throws IOException si es produeix un error de lectura o deserialització
     */
    public List<Joc> llegeix(Path p) throws IOException {
        return mapper.readValue(p.toFile(), new TypeReference<List<Joc>>() {});
    }
}
