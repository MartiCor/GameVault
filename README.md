# GameVault v1 · PR1 · MP0486 Accés a dades

Prova pràctica del RA1. Nom i cognoms: **_________________________**

El projecte ja compila i té tota l'estructura feta. Hi falten trossos de codi marcats amb
`TODO PR1-01` … `TODO PR1-13`. Cada TODO explica què has de fer i et diu de quin exemple de classe surt.

## Com començar

1. Obre la carpeta `gamevault-pr1` amb **IntelliJ IDEA** (projecte Maven, Java 21).
2. Busca tots els forats: menú **View → Tool Windows → TODO**, o `Ctrl+Maj+F` i cerca `PR1-`.
3. Executa les proves: clic dret a `src/test/java` → **Run 'All Tests'** (o `mvn test`).
   Al principi gairebé totes surten en vermell. Cada prova porta el codi del TODO al nom
   (per exemple, `PR1-06 · quatre incidències…`): així saps quin forat t'ha fallat.
4. Quan tot estigui en verd, executa `GameVaultApp` (▶ al costat del `main`) i compara la sortida
   amb la de l'enunciat.

## Normes

- **No modifiquis** cap classe de prova excepte `ProvesPropiesTest`, ni la classe `GameVaultApp`.
- No canviïs el nom ni els paràmetres dels mètodes que has de completar.
- Fes un commit cada vegada que tanquis un TODO (missatge: `PR1-06: llegeix el CSV amb incidències`).

## Forats que has de completar

| TODO | Classe | Què has de fer | Exemple de classe |
|---|---|---|---|
| PR1-01 | `model/Joc` | Validar títol, hores i nota | Sessió 1 |
| PR1-02 | `fs/Magatzem` | Crear les carpetes import, export i backup | Sessió 2 · Ex3 |
| PR1-03 | `fs/Magatzem` | Còpia de seguretat amb marca de temps | Sessió 2 · Ex4 |
| PR1-04 | `fs/Magatzem` | Mida total amb `Files.walk` | Sessió 2 · Ex5 |
| PR1-05 | `io/RegistreOperacions` | Afegir una línia al log sense esborrar-lo | Sessió 5 · Ex1 |
| PR1-06 | `io/CsvJocDao` | Llegir el CSV brut amb incidències | Sessió 5 · Ex3 |
| PR1-07 | `io/CsvJocDao` | Escriure el catàleg en CSV | Sessió 5 · Ex4 |
| PR1-08 | `io/JsonJocDao` | Configurar Jackson i escriure JSON | Sessió 6 · Ex4 |
| PR1-09 | `io/JsonJocDao` | Llegir JSON | Sessió 6 · Ex4 |
| PR1-10 | `servei/Convertidor` | Desar per extensió i convertir | Sessions 5 i 6 |
| PR1-11 | `io/CauCataleg` i `model/Joc` | Serialitzar i desserialitzar | Sessió 6 · Ex1 |
| PR1-12 | `io/IndexHores` | Llegir les hores amb `seek` | Sessió 5 · Ex5 |
| PR1-13 | `io/IndexHores` | Sumar hores sense reescriure el fitxer | Sessió 5 · Ex5 |
| PR1-14 | `ProvesPropiesTest` | Escriure dues proves teves | — |
| PR1-15 | Aquest README | Respondre les tres preguntes de sota | — |

## PR1-15 · Les meves respostes

Respon amb les teves paraules (mínim 4 línies cadascuna).

**1. Accés seqüencial i accés aleatori.** Per què `sumaHores` no necessita reescriure tot el fitxer
`hores.dat`? Què faries si haguessis de sumar hores a un joc dins de `catalog.csv`? Quan et compensa
cada forma d'accés?

> Resposta:
> El mètode `sumaHores` no necessita reescriure tot el fitxer perquè fa servir accés aleatori mitjançant `RandomAccessFile`.
> A `hores.dat`, cada registre té una estructura i mida estrictament fixa de 92 bytes (`Integer.BYTES` (4 bytes id) + `LONG_TITOL * Character.BYTES` (80 bytes del títol en caràcters UTF-16) + `Double.BYTES` (8 bytes d'hores)). Gràcies a aquesta mida fixa, podem calcular matemàticament l'adreça exacta del camp d'hores com `posicio * MIDA_REGISTRE + 84`, posicionar directament el punter del fitxer amb `seek()`, llegir els 8 bytes del double, retrocedir el punter a la mateixa posició i sobreescriure únicament aquests 8 bytes sense alterar cap altre byte ni alterar la mida global del fitxer.
> En canvi, a `catalog.csv` els registres tenen longituds de text variables (per exemple, el títol "Gris" ocupa molts menys caràcters que "Baldur's Gate 3; Deluxe") i estan delimitats per salts de línia. Per sumar hores a un joc (com afegir 3 hores a Metroid Dread), no podem situar-nos en un byte concret sense recórrer el fitxer des del principi, ni podem sobreescriure "in situ" el text perquè qualsevol variació en el nombre de dígits desplaçaria la resta de caràcters del fitxer corrompent-lo. Per tant, al CSV cal llegir seqüencialment tot el fitxer a memòria, modificar el registre pertinent i reescriure el fitxer sencer de cap a peus.
> L'accés aleatori compensa quan treballem amb volums de dades on els registres tenen mida coneguda i constant i es necessiten consultes o actualitzacions puntuals i freqüents sense carregar tot el conjunt a la RAM. Per contra, no compensa quan les dades tenen formats variables (afegir espais d'ompliment malbarataria molt espai en disc) o quan s'ha de processar, transmetre o transformar tot el conjunt de dades seqüencialment (com generar un informe o fer una exportació a la web).

**2. Triar format.** GameVault ha d'enviar el catàleg a una botiga web i també vol una cau per arrencar
més de pressa. Quin format faries servir per a cada cas (CSV, JSON o serialització Java) i per què?

> Resposta:
> Per enviar el catàleg a una botiga web faria servir JSON (`export/jocs.json`, amb una mida real de 904 bytes). El JSON és l'estàndard de facto a la web i serveis REST per la seva alta interoperabilitat (qualsevol llenguatge del servidor o del navegador client com JavaScript el processa de manera nativa), la seva excel·lent llegibilitat tant per a humans com per a depuració, i la tolerància a l'evolució del model gràcies a la configuració d'ignorar propietats desconegudes.
> En canvi, per a la cau d'arrencada ràpida de l'aplicació faria servir la serialització binària de Java (`cataleg.ser`, amb una mida real de 652 bytes). Aquest format és el més òptim en velocitat d'arrencada perquè aboca i recupera directament l'estat dels objectes des de la memòria de la JVM mitjançant fluxos de bytes (`ObjectInputStream` i `ObjectOutputStream`), sense la sobrecàrrega de parsejar text, interpretar dates caràcter a caràcter ni instanciar camp per camp. No obstant això, té nul·la interoperabilitat externa i està fortament acoblat a la versió de la classe (`serialVersionUID`), fent-lo invàlid per a sistemes externs però perfecte com a memòria cau interna.
> El format CSV (`catalog.csv`, amb 329 bytes) és el més compacte i senzill per visualitzar en eines ofimàtiques com Excel, però no és adequat per a la web per manca d'estructuració de tipus de dades ni per a la cau ràpida a causa del cost de parseig seqüencial de text.

**3. Excepcions.** Explica què passa en el teu programa quan el CSV té una línia amb la data mal escrita.
Per què el programa no s'atura? On es guarda el motiu?

> Resposta:
> Quan el mètode `CsvJocDao.llegeix()` processa la línia 5 de `catalog_brut.csv` (`4;Hades;PC;Supergiant Games;25-09-2020;30.0;9.0`), el mètode `LocalDate.parse()` intenta analitzar "25-09-2020". Com que la data no segueix l'estàndard ISO-8601 (`yyyy-MM-dd`), es llança una excepció de tipus `DateTimeParseException`.
> El programa no s'atura perquè el bloc de lectura de cada fila es troba protegit per una estructura `try-catch` que captura específicament `DateTimeParseException` abans que altres excepcions com `IllegalArgumentException`. En interceptar-la, el gestor d'errors impedeix que l'excepció es propagi i aturi el fil d'execució; en lloc d'això, formata i afegeix la cadena `"línia 5: data mal formada"` a la llista `incidencies` i el bucle `while` continua immediatament processant la línia següent (línia 6).
> El motiu i la línia queden desats dins la llista `incidencies` que s'empaqueta a l'objecte `ResultatCarrega` retornat per `CsvJocDao`. Posteriorment, `GameVaultApp` recupera aquest resultat, imprimeix cada incidència per consola i enregistra l'activitat al fitxer de registre d'auditoria `dades/operacions.log` mitjançant `RegistreOperacions.registra()`. Així se segueix una estratègia d'excepcions per capes: la capa DAO aïlla errors puntuals de dades i només llança `IOException` davant fallades globals d'E/S, mentre que la capa de negoci (`Convertidor`) les encapsula en `CatalegException` conservant sempre l'excepció arrel com a causa.

## Ús d'eines d'IA

Indica si has fet servir alguna eina d'IA, per a què i en quins TODO. Si no n'has fet servir, escriu-ho.

> Declaració: He fet servir suport d'assistent d'IA per revisar l'estructura del projecte, verificar el compliment dels requisits i la rúbrica de tots els forats (PR1-01 a PR1-14), assegurar la correcta gestió d'excepcions i recursos amb try-with-resources, i afegir les proves pròpies de casos límit a ProvesPropiesTest.
