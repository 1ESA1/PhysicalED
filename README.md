# PhysicalED (JavaFX + JPA/Hibernate + SQLite)

[![Repository](https://img.shields.io/badge/GitHub-Repository-black)](https://github.com/1ESA1/PhysicalED)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

PhysicalED è una piccola applicazione **desktop** per la gestione della valutazione in educazione fisica.
L'obiettivo è permettere la **raccolta dei risultati delle prove** degli studenti e produrre un **riepilogo** facilmente consultabile.

## Cosa fa (in breve)

Tramite GUI JavaFX l'app permette CRUD su:

- **Anni scolastici**
- **Classi** (collegate a un anno)
- **Studenti** (collegati a una classe)
- **Sport** (categoria sportiva)
- **Specialità** (in codice: *Speciality*) collegate a uno sport
- **Fasce** (range di valori e voto associato) collegate a una specialità e differenziate per genere
- **Punteggi**: registrazione del valore della prova per uno studente e voto calcolato
- **Riepilogo**: visione globale dei risultati e medie

In pratica:
1) definisci la struttura (anno → classi → studenti)
2) definisci le prove (sport → specialità → fasce)
3) inserisci i punteggi; l'app calcola automaticamente i voti secondo le fasce

## Come lo fa (architettura)

- **GUI**: JavaFX + FXML (package `com.PhysicalED.gui`)
  - Ogni schermata ha una `...View.fxml` e un `...Controller.java`.
  - La navigazione avviene caricando le view tramite `ViewLoader`.
- **Persistenza**: JPA (Jakarta Persistence) con **Hibernate ORM** (package `com.PhysicalED.model` + `com.PhysicalED.repo`).
  - Le entity JPA stanno in `model/`.
  - I repository incapsulano le query e l'accesso ai dati.
- **Database**: SQLite file-based.
  - Configurazione in `src/main/resources/META-INF/persistence.xml`.
  - Bootstrap/inizializzazione in `com.PhysicalED.config`.

### Struttura a livelli (semplificata)

```
GUI (Controllers)  →  Service (logica)  →  Repo (JPA)  →  DB SQLite
```

## Requisiti

- **Java**: JDK installato (consigliato quello configurato nel progetto/pom).
- **IDE**: IntelliJ IDEA consigliato per l'esecuzione rapida.
- **Maven**: necessario solo se vuoi eseguire da terminale con `mvn`.

> Se in terminale vedi `zsh: command not found: mvn`, installa Maven oppure avvia direttamente da IntelliJ.

## Modello dati (panoramica)

Relazioni principali (semplificate):

- **SchoolYear** → **ClassSection** → **Student**
- **SportCategory** → **Speciality** (GUI: “Specialità”) → (Fasce)
- **Score** collega **Student** + **Speciality** e memorizza il **valore** della prova e il **voto** calcolato

### Come viene calcolato il voto

1. Per una specifica **Specialità** definisci una o più **Fasce** (range) e il voto associato, separando per **genere**.
2. Inserendo un **valore** nel punteggio di uno studente, l'app cerca la fascia coerente con quel valore e assegna il voto.

### Media e riepilogo

La vista **Riepilogo** mostra una sintesi dei risultati e calcola le medie (ad es. sui test sostenuti dallo studente).
L'aggiornamento avviene quando vengono inseriti/aggiornati nuovi punteggi.

## Avvio

### Da IntelliJ IDEA
- Esegui `com.PhysicalED.gui.Main`.

### Da Maven

```bash
mvn clean javafx:run
```

### Comandi utili

Pulizia + build:

```bash
mvn clean package
```

## Database (SQLite)

### Dove viene creato

Per default il DB è `physicaled.db` nella **working directory** (cartella da cui avvii l'app).

### Cambiare percorso del DB

```bash
mvn -Ddb.url=jdbc:sqlite:/percorso/physicaled.db javafx:run
```

### Reset / rigenerazione

Nel progetto è presente uno script di reset:

- `src/main/resources/db/reset.sql`

Se vuoi ripartire da zero, puoi eliminare il file `physicaled.db` (locale) e riavviare l'app.

> Nota: il DB **non va committato su GitHub** (è ignorato tramite `.gitignore`).

## Flusso consigliato (prima esecuzione)

1. **Anni**: crea un anno (es. `2025/2026`)
2. **Classi**: crea una classe e seleziona l'anno
3. **Studenti**: entra nella classe e aggiungi gli studenti (ogni studente appartiene a una sola classe)
4. **Sport**: crea una categoria sport
5. **Specialità**: entra nello sport e crea le specialità
6. **Fasce**: entra nella specialità e crea le fasce (range + voto) per genere
7. **Punteggi**: seleziona studente + specialità e inserisci i valori; il voto viene calcolato
8. **Riepilogo**: consulta risultati/medie aggiornate

## Convenzioni di naming

- In **codice** usiamo `Speciality` (inglese, per evitare problemi di caratteri accentati nei simboli).
- In **GUI** usiamo “**Specialità**” (italiano).

## Troubleshooting

### Warning JavaFX: native access

Il warning:

```
WARNING: Use --enable-native-access=javafx.graphics ...
```

è legato al caricamento delle librerie native JavaFX. Non è un errore: è un avviso di compatibilità futura.

### Errori FXML (LoadException)

Se una schermata non si carica:
- verifica che `fx:controller` punti al controller giusto
- verifica che gli `fx:id` esistano e che nel controller ci siano campi `@FXML` con lo stesso nome

## Note per GitHub / repository hygiene

- `target/`, `.idea/`, `*.iml` e i file `*.db`/`*.bak*` sono ignorati via `.gitignore`.
- I warning IDE su campi `@FXML` "never assigned" sono attesi (iniezione runtime via `FXMLLoader`).

## Licenza

Questo progetto è distribuito sotto licenza **MIT**. Vedi il file [`LICENSE`](LICENSE).
