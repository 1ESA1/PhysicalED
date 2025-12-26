package com.PhysicalED;
import com.PhysicalED.model.*;
import com.PhysicalED.repo.*;
import com.PhysicalED.service.*;
import jakarta.persistence.*;
import java.util.Scanner;
import java.util.List;
/**
 * Programma di gestione dei voti e media scolastica in ed.fisica,
 * in base a punteggi assegnati nei vari test fisici.
 */
public class App {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhysicalEDPU");
        EntityManager em = emf.createEntityManager();

        // Inizializza i repository
        ClassSectionRepository classSectionRepo = new ClassSectionRepository(em);
        SchoolYearRepository schoolYearRepo = new SchoolYearRepository(em);
        ScoreRepository scoreRepo = new ScoreRepository(em);
        SportCategoryRepository sportCategoryRepo = new SportCategoryRepository(em);
        StudentRepository studentRepo = new StudentRepository(em);
        PhysicalTestRepository physicalTestRepo = new PhysicalTestRepository(em);
        GradingScaleRepository gradingScaleRepo = new GradingScaleRepository();
        GradingService gradingService = new GradingService(gradingScaleRepo);
        ScoreService scoreService = new ScoreService(scoreRepo, gradingService);

        // Menu Principale per interazione con l'utente
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("----- Benvenuto in PhysicalED -----");
            System.out.println("1. Anno Scolastico");
            System.out.println("2. Sezioni di Classe");
            System.out.println("3. Studenti");
            System.out.println("4. Discipline Sportive");
            System.out.println("5. Test Fisici");
            System.out.println("6. Punteggi Discipline Sportive");
            System.out.println("7. Statistiche e Visualizzazioni voti");
            System.out.println("8. Gestione Fasce di Valutazione");
            System.out.println("0. Esci");
            System.out.print("Seleziona un'opzione: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Switch per gestione menù principale
            switch (choice) {
                case 1:
                    menuAnniScolastici(scanner,
                                       schoolYearRepo,
                                       classSectionRepo);
                    break;
                case 2:
                    menuClassi(scanner,
                               classSectionRepo,
                               studentRepo,
                               physicalTestRepo,
                               schoolYearRepo);
                    break;
                case 3:
                    menuStudenti(scanner,
                                 studentRepo,
                                 scoreRepo,
                                 classSectionRepo);
                    break;
                case 4:
                    menuDisciplineSportive(scanner,
                                           sportCategoryRepo,
                                           physicalTestRepo);
                    break;
                case 5:
                    menuTestFisici(scanner,
                                   physicalTestRepo,
                                   sportCategoryRepo,
                                   classSectionRepo,
                                   scoreRepo);
                    break;
                case 6:
                    menuPunteggiDiscipline(scanner,
                                           scoreRepo,
                                           studentRepo,
                                           physicalTestRepo,
                                           scoreService, em);
                    break;
                case 7:
                    menuStatistichePunteggi(scanner,
                                            scoreRepo,
                                            studentRepo,
                                            physicalTestRepo);
                    break;
                case 8:
                    menuFasceValutazione(scanner, gradingScaleRepo, physicalTestRepo, em);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opzione non valida. Riprova.");
            }
        }
        scanner.close();
        System.out.println("Uscita dal programma. Arrivederci!");
        em.close(); // Chiudi l'EntityManager
        emf.close(); // Chiudi l'EntityManagerFactory
    }

    // Sotto-menù per Anni Scolastici
    private static void menuAnniScolastici(Scanner scanner,
                                           SchoolYearRepository schoolYearRepo,
                                           ClassSectionRepository classSectionRepo) {
        boolean running = true;
        while (running) {
            System.out.println("----- Gestione Anni Scolastici -----");
            System.out.println("1. Aggiungi Anno Scolastico");
            System.out.println("2. Visualizza Anni Scolastici");
            System.out.println("3. Elimina Anno Scolastico");
            System.out.println("0. Torna al Menu Principale");
            System.out.print("Seleziona un'opzione: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Switch per gestione menù Anni Scolastici
            switch (choice) {
                case 1:
                    System.out.println("Descrizione dell'anno scolastico: ");
                    String description = scanner.nextLine();
                    SchoolYear newYear = new SchoolYear(description);
                    schoolYearRepo.save(newYear);
                    System.out.println("Anno scolastico aggiunto con successo.");
                    break;
                case 2:
                    for (SchoolYear y : schoolYearRepo.findAll()) {
                        System.out.println(y.getId() + ": " + y.getDescription());
                    }
                    break;
                case 3:
                    System.out.println("ID dell'anno scolastico da eliminare: ");
                    Long idToDel;
                    try {
                        idToDel = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ID dell'anno scolastico non valido.");
                        break;
                    }
                    // Controllo ClassSection collegate
                    List<ClassSection> classiCollegate = classSectionRepo.findBySchoolYearId(idToDel);
                    if (!classiCollegate.isEmpty()) {
                        System.out.println("Non puoi eliminare questo anno scolastico: esistono classi collegate!");
                        break;
                    }
                    try{
                        schoolYearRepo.delete(idToDel);
                        System.out.println("Anno scolastico eliminato con successo.");
                    } catch (Exception e) {
                        System.out.println("Errore durante l'eliminazione dell'anno scolastico: " + e.getMessage());
                        break;
                    }
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opzione non valida. Riprova.");
            }
        }
    }

    // Sotto-menù per Classi
    private static void menuClassi(Scanner scanner,
                                   ClassSectionRepository classSectionRepo,
                                   StudentRepository studentRepo,
                                   PhysicalTestRepository physicalTestRepo,
                                   SchoolYearRepository schoolYearRepo) {
        boolean running = true;
        while (running) {
            System.out.println("----- Gestione Classi -----");
            System.out.println("1. Aggiungi Classe");
            System.out.println("2. Visualizza Classi");
            System.out.println("3. Elimina Classe");
            System.out.println("0. Torna al Menu Principale");
            System.out.print("Seleziona un'opzione: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Switch per gestione menù Classi
            switch (choice) {
                case 1:
                    System.out.println("Nome della classe: ");
                    String sec = scanner.nextLine();
                    // Mostra lista degli anni scolastici disponibili
                    System.out.println("Anni scolastici disponibili:");
                    for (SchoolYear y : schoolYearRepo.findAll()) {
                        System.out.println(y.getId() + ": " + y.getDescription());
                    }
                    System.out.println("ID anno scolastico: ");
                    long schoolYearId;
                    try {
                        schoolYearId = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ID anno scolastico non valido.");
                        break;
                    }
                    // Recupera l'anno scolastico e collega la classSection
                    SchoolYear anno = schoolYearRepo.findById(schoolYearId);
                    if (anno == null) {
                        System.out.println("Anno scolastico non trovato.");
                        break;
                    }
                    // Crea e salva la nuova classe
                    ClassSection s = new ClassSection();
                    s.setName(sec);
                    s.setSchoolYear(anno);
                    classSectionRepo.save(s);
                    System.out.println("Classe aggiunta con successo.");
                    break;
                case 2:
                    for (ClassSection c : classSectionRepo.findAll()) {
                        System.out.println(c.getId() + ": " + c.getName());
                    }
                    break;
                case 3:
                    System.out.println("ID della classe da eliminare: ");
                    Long idToDel;
                    try {
                        idToDel = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ID della classe non valido.");
                        break;
                    }
                    // Controllo Studenti collegati
                    List<Student> studentiCollegati = studentRepo.findByClassSectionId(idToDel);
                    if (!studentiCollegati.isEmpty()) {
                        System.out.println("Non puoi eliminare questa classe: esistono studenti collegati!");
                        break;
                    }
                    // Controllo PhysicalTest collegati
                    List<PhysicalTest> testCollegati = physicalTestRepo.findByClassSectionId(idToDel);
                    if (!testCollegati.isEmpty()) {
                        System.out.println("Non puoi eliminare questa classe: esistono test fisici collegati!");
                        break;
                    }
                    try{
                        classSectionRepo.delete(idToDel);
                        System.out.println("Classe eliminata con successo.");
                    } catch (Exception e) {
                        System.out.println("Errore durante l'eliminazione della classe: " + e.getMessage());
                        break;
                    }
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opzione non valida. Riprova.");
            }
        }
    }

    // Sotto-menù  per Studenti
    private static void menuStudenti(Scanner scanner,
                                     StudentRepository studentRepo,
                                     ScoreRepository scoreRepo,
                                     ClassSectionRepository classSectionRepo) {
        boolean running = true;
        while (running) {
            System.out.println("----- Gestione Studenti -----");
            System.out.println("1. Aggiungi Studente");
            System.out.println("2. Visualizza Studenti");
            System.out.println("3. Elimina Studente");
            System.out.println("0. Torna al Menu Principale");
            System.out.print("Seleziona un'opzione: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Switch per gestione menù Studenti
            switch (choice) {
                case 1:
                    System.out.println("Nome dello studente: ");
                    String name = scanner.nextLine();
                    System.out.println("Cognome dello studente: ");
                    String surname = scanner.nextLine();
                    System.out.println("Genere dello studente (M/F): ");
                    String gen = scanner.nextLine().trim().toUpperCase();
                    Gender gender;
                    try {
                        gender = Gender.valueOf(gen.equals("F") ? "F" : "M");
                    } catch (Exception e) {
                        System.out.println("Genere non valido.");
                        break;
                    }
                    // Mostra lista delle classi disponibili
                    System.out.println("Classi disponibili:");
                    for (ClassSection cs : classSectionRepo.findAll()) {
                        System.out.println(cs.getId() + ": " + cs.getName());
                    }
                    System.out.println("ID classe dove iscrivere lo studente: ");
                    long classSectionId;
                    try {
                        classSectionId = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ID classe non valido.");
                        break;
                    }
                    // Recupera la classSection e collega lo studente
                    ClassSection classe = classSectionRepo.findById(classSectionId);
                    if (classe == null) {
                        System.out.println("Classe non trovata.");
                        break;
                    }
                    // Crea e salva il nuovo studente
                    Student stud = new Student(name, surname, gender);
                    stud.setClassSection(classe);
                    studentRepo.save(stud);
                    System.out.println("Studente aggiunto con successo.");
                    break;
                case 2:
                    for (Student s : studentRepo.findAll()) {
                        System.out.println(s.getId() + ": " + s.getFirstName() + " " + s.getLastName());
                    }
                    break;
                case 3:
                    System.out.println("ID dello studente da eliminare: ");
                    Long idToDel;
                    try{
                        idToDel = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ID dello studente non valido.");
                        break;
                    }
                    // Controllo Punteggi collegati
                    List<Score> punteggiCollegati = scoreRepo.findByStudentId(idToDel);
                    if (!punteggiCollegati.isEmpty()) {
                        System.out.println("Non puoi eliminare questo studente: esistono punteggi collegati!");
                        break;
                    }
                    try {
                        studentRepo.delete(idToDel);
                        System.out.println("Studente eliminato con successo.");
                    } catch (Exception e) {
                        System.out.println("Errore durante l'eliminazione dello studente: " + e.getMessage());
                        break;
                    }
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opzione non valida. Riprova.");
            }
        }
    }

    // Sotto-menù per Discipline Sportive
    private static void menuDisciplineSportive(Scanner scanner,
                                               SportCategoryRepository repo,
                                               PhysicalTestRepository physicalTestRepo) {
        boolean running = true;
        while (running) {
            System.out.println("----- Gestione Discipline Sportive -----");
            System.out.println("1. Aggiungi Disciplina");
            System.out.println("2. Visualizza Discipline");
            System.out.println("3. Elimina Disciplina");
            System.out.println("0. Torna al menu principale");
            System.out.print("Seleziona un'opzione: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Nome disciplina sportiva: ");
                    String nomeDisc = scanner.nextLine();
                    SportCategory disciplina = new SportCategory();
                    disciplina.setDescription(nomeDisc);
                    repo.save(disciplina);
                    System.out.println("Disciplina aggiunta con successo.");
                    break;
                case 2:
                    for (SportCategory d : repo.findAll()) {
                        System.out.println(d.getId() + ": " + d.getDescription());
                    }
                    break;
                case 3:
                    System.out.print("ID della disciplina da eliminare: ");
                    Long idToDel;
                    try{
                        idToDel = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ID della disciplina non valido.");
                        break;
                    }
                    // Controllo PhysicalTest collegati
                    List<PhysicalTest> testCollegati = physicalTestRepo.findBySportCategoryId(idToDel);
                    if (!testCollegati.isEmpty()) {
                        System.out.println("Non puoi eliminare questa disciplina: esistono test fisici collegati!");
                        break;
                    }
                    try{
                        repo.delete(idToDel);
                    } catch (Exception e) {
                        System.out.println("Errore durante l'eliminazione della disciplina: " + e.getMessage());
                        break;
                    }
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Scelta non valida.");
            }
            System.out.println();
        }
    }

    // Sotto-menù per TestFisici
    private static void menuTestFisici(Scanner scanner,
                                       PhysicalTestRepository physicalTestRepo,
                                       SportCategoryRepository sportCategoryRepo,
                                       ClassSectionRepository classSectionRepo,
                                       ScoreRepository scoreRepo) {
        boolean running = true;
        while (running) {
            System.out.println("----- Gestione Test Fisici -----");
            System.out.println("1. Aggiungi Test Fisico");
            System.out.println("2. Visualizza Test Fisici");
            System.out.println("3. Elimina Test Fisico");
            System.out.println("0. Torna al Menu Principale");
            System.out.print("Seleziona un'opzione: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Switch per gestione menù Test Fisici
            switch (choice) {
                case 1:
                    // 1. Mostra discipline disponibili
                    System.out.println("Discipline disponibili:");
                    for (SportCategory d : sportCategoryRepo.findAll()) {
                        System.out.println(d.getId() + ": " + d.getDescription());
                    }
                    System.out.print("ID disciplina sportiva da collegare: ");
                    long discId;
                    try{
                        discId = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ID disciplina non valido");
                        break;
                    }
                    // Recupera disciplina
                    SportCategory disciplina = sportCategoryRepo.findById(discId);
                    if (disciplina == null) {
                        System.out.println("Disciplina non trovata!");
                        break;
                    }
                    // 2. Mostra classi disponibili
                    System.out.println("Classi disponibili:");
                    for (ClassSection cs : classSectionRepo.findAll()) {
                        System.out.println(cs.getId() + ": " + cs.getName());
                    }
                    System.out.print("ID classe/sezione: ");
                    long sectionId;
                    try{
                        sectionId = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ID classe non valido");
                        break;
                    }
                    // Recupera classe
                    ClassSection sezione = classSectionRepo.findById(sectionId);
                    if (sezione == null) {
                        System.out.println("Classe non trovata!");
                        break;
                    }
                    // 3. Descrizione test
                    System.out.print("Descrizione della prova/test fisico: ");
                    String descr = scanner.nextLine();
                    // 4. Data test
                    System.out.print("Data del test (YYYY-MM-DD): ");
                    String dateStr = scanner.nextLine();
                    java.sql.Date testDate = java.sql.Date.valueOf(dateStr);
                    // 5. Costruisci l'entità
                    PhysicalTest test = new PhysicalTest();
                    test.setDescription(descr);
                    test.setSportCategory(disciplina);
                    test.setClassSection(sezione);
                    test.setTestDate(testDate);
                    physicalTestRepo.save(test);
                    System.out.println("Test fisico aggiunto con successo.");
                    break;
                case 2:
                    for (PhysicalTest t : physicalTestRepo.findAll()) {
                        System.out.println(
                                t.getId() + ": " +
                                        t.getDescription() + // aggiungi campo description/testName se manca!
                                        " | Disciplina: " + t.getSportCategory().getDescription() +
                                        " | Classe: " + t.getClassSection().getName() +
                                        " | Data: " + t.getTestDate()
                        );
                    }
                    break;
                case 3:
                    System.out.println("ID del test fisico da eliminare: ");
                    Long idToDel;
                    try{
                        idToDel = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("ID del test fisico non valido.");
                        break;
                    }
                    // Controllo Punteggi collegati
                    List<Score> punteggiCollegati = scoreRepo.findByPhysicalTestId(idToDel);
                    if (!punteggiCollegati.isEmpty()) {
                        System.out.println("Non puoi eliminare questo test fisico: esistono punteggi collegati!");
                        break;
                    }
                    try{
                        physicalTestRepo.delete(idToDel);
                        System.out.println("Test fisico eliminato con successo.");
                    } catch (Exception e) {
                        System.out.println("Errore durante l'eliminazione del test fisico: " + e.getMessage());
                        break;
                    }
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opzione non valida. Riprova.");
            }
        }
    }

    // Sotto-menù per Punteggi Discipline Sportive
    private static void menuPunteggiDiscipline(Scanner scanner,
                                               ScoreRepository scoreRepo,
                                               StudentRepository studentRepo,
                                               PhysicalTestRepository physicalTestRepo,
                                               ScoreService scoreService,
                                               EntityManager em) {
        boolean running = true;
        while (running) {
            System.out.println("----- Gestione Voti dei Test -----");
            System.out.println("1. Aggiungi Voto del Test");
            System.out.println("2. Visualizza Voti dei Test");
            System.out.println("0. Torna al Menu Principale");
            System.out.print("Seleziona un'opzione: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Switch per gestione menù Voti dei Test
            switch (choice) {
                case 1:
                    // 1. SCEGLI STUDENTE
                    System.out.println("Studenti disponibili:");
                    for (Student s : studentRepo.findAll()) {
                        System.out.println(s.getId() + ": " + s.getFirstName() + " " + s.getLastName());
                    }
                    System.out.print("ID Studente: ");
                    Long studentId = Long.parseLong(scanner.nextLine());
                    Student studente = studentRepo.findById(studentId);
                    if (studente == null) {
                        System.out.println("Studente non trovato!");
                        break;
                    }
                    // 2. SCEGLI TEST FISICO
                    System.out.println("Test Fisici disponibili:");
                    for (PhysicalTest t : physicalTestRepo.findAll()) {
                        System.out.println(t.getId() + ": " + t.getDescription());
                    }
                    System.out.print("ID Test Fisico: ");
                    Long testId = Long.parseLong(scanner.nextLine());
                    PhysicalTest test = physicalTestRepo.findById(testId);
                    if (test == null) {
                        System.out.println("Test fisico non trovato!");
                        break;
                    }
                    // 3. INSERISCI PUNTEGGIO
                    System.out.print("Risultato grezzo (es. metri, ripetizioni, secondi...): ");
                    double valore = Double.parseDouble(scanner.nextLine());
                    try {
                        Score score = scoreService.aggiungiScore(studente, test, valore, em);
                        System.out.println("Risultato salvato. Il voto calcolato è: " + score.getVoto());
                    } catch (IllegalStateException ex) {
                        System.out.println("Errore: " + ex.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Tutti i voti dei test:");
                    for (Score s : scoreRepo.findAll()) {
                        System.out.println(
                                "ID: " + s.getId() +
                                        " | Studente: " + s.getStudent().getFirstName() + " " + s.getStudent().getLastName() +
                                        " | Test: " + s.getPhysicalTest().getDescription() +
                                        " | Punteggio: " + s.getValue() +
                                        " | Voto: " + s.getVoto()
                        );
                    }
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opzione non valida. Riprova.");
            }
        }
    }

    // Sotto-menù per Statistiche e Visualizzazioni voti
    private static void menuStatistichePunteggi(Scanner scanner,
                                                ScoreRepository scoreRepo,
                                                StudentRepository studentRepo,
                                                PhysicalTestRepository physicalTestRepo) {
        boolean running = true;
        while (running) {
            System.out.println("----- Statistiche e Visualizzazione Voti -----");
            System.out.println("1. Visualizza tutti i punteggi");
            System.out.println("2. Media voti per studente");
            System.out.println("3. Media voti per test/discipline");
            System.out.println("4. Voti di uno studente");
            System.out.println("5. Voti di una disciplina/test");
            System.out.println("0. Torna al Menu Principale");
            System.out.print("Seleziona un'opzione: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Switch per gestione menù Statistiche e Visualizzazioni voti
            switch (choice) {
                case 1:

                    System.out.println("Tutti i punteggi:");
                    for (Score s : scoreRepo.findAll()) {
                        System.out.println(
                                "ID: " + s.getId() +
                                        " | Studente: " + s.getStudent().getFirstName() + " " + s.getStudent().getLastName() +
                                        " | Test: " + s.getPhysicalTest().getDescription() +
                                        " | Disciplina: " + s.getPhysicalTest().getSportCategory().getDescription() +
                                        " | Punteggio: " + s.getValue() +
                                        " | Voto: " + s.getVoto()
                        );
                    }
                    break;
                case 2:
                    System.out.println("Medie voti per studente:");
                    for (Student stud : studentRepo.findAll()) {
                        List<Score> voti = scoreRepo.findByStudentId(stud.getId()); // DA AGGIUNGERE IN REPO!
                        double media = voti.isEmpty() ? 0 :
                                voti.stream().mapToDouble(Score::getValue).average().orElse(0);
                        System.out.printf("%s %s (ID %d): %.2f\n", stud.getFirstName(), stud.getLastName(), stud.getId(), media);
                    }
                    break;
                case 3:
                    System.out.println("Medie voti per test:");
                    for (PhysicalTest test : physicalTestRepo.findAll()) {
                        List<Score> voti = scoreRepo.findByPhysicalTestId(test.getId()); // DA AGGIUNGERE IN REPO!
                        double media = voti.isEmpty() ? 0 :
                                voti.stream().mapToDouble(Score::getValue).average().orElse(0);
                        System.out.printf("Test %s (ID %d): %.2f\n", test.getDescription(), test.getId(), media);
                    }
                    break;
                case 4:
                    System.out.println("Scegli ID studente per vedere tutti i voti:");
                    for (Student stud : studentRepo.findAll())
                        System.out.println(stud.getId() + ": " + stud.getFirstName() + " " + stud.getLastName());
                    Long studentId = Long.parseLong(scanner.nextLine());
                    List<Score> votiStudente = scoreRepo.findByStudentId(studentId); // DA AGGIUNGERE IN REPO!
                    for (Score s : votiStudente) {
                        System.out.println(
                                "Test: " + s.getPhysicalTest().getDescription() +
                                        " | Punteggio: " + s.getValue() +
                                        " | Voto: " + s.getVoto()
                        );
                    }
                    break;
                case 5:
                    System.out.println("Scegli ID test per vedere tutti i voti:");
                    for (PhysicalTest test : physicalTestRepo.findAll())
                        System.out.println(test.getId() + ": " + test.getDescription());
                    Long testId = Long.parseLong(scanner.nextLine());
                    List<Score> votiTest = scoreRepo.findByPhysicalTestId(testId); // DA AGGIUNGERE IN REPO!
                    for (Score s : votiTest) {
                        System.out.println(
                                "Studente: " + s.getStudent().getFirstName() + " " + s.getStudent().getLastName() +
                                        " | Punteggio: " + s.getValue() +
                                        " | Voto: " + s.getVoto()
                        );
                    }
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opzione non valida.");
            }
        }
    }
    // Menu per Gestione delle Fasce di Valutazione
    private static void menuFasceValutazione(Scanner scanner,
                                             GradingScaleRepository gradingScaleRepo,
                                             PhysicalTestRepository physicalTestRepo,
                                             EntityManager em) {
        boolean running = true;
        while (running) {
            System.out.println("----- Gestione Fasce di Valutazione -----");
            System.out.println("1. Aggiungi Fascia");
            System.out.println("2. Visualizza Fasce per Test");
            System.out.println("3. Cancella Fascia");
            System.out.println("0. Torna al Menu Principale");
            System.out.print("Seleziona un'opzione: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // Scegli test fisico
                    System.out.println("Test fisici disponibili:");
                    for (PhysicalTest t : physicalTestRepo.findAll()) {
                        System.out.println(t.getId() + ": " + t.getDescription());
                    }
                    System.out.print("ID del test fisico: ");
                    long testId = Long.parseLong(scanner.nextLine());
                    PhysicalTest physicalTest = physicalTestRepo.findById(testId);
                    if (physicalTest == null) {
                        System.out.println("Test non trovato!");
                        break;
                    }
                    // Scegli genere
                    System.out.print("Genere (M/F): ");
                    String gen = scanner.nextLine().trim().toUpperCase();
                    Gender gender;
                    try {
                        gender = Gender.valueOf(gen.equals("F") ? "F" : "M");
                    } catch (Exception e) {
                        System.out.println("Genere non valido.");
                        break;
                    }
                    // Range minimo, massimo, voto
                    System.out.print("Valore MIN: ");
                    double min = Double.parseDouble(scanner.nextLine());
                    System.out.print("Valore MAX: ");
                    double max = Double.parseDouble(scanner.nextLine());
                    System.out.print("Voto associato: ");
                    int voto = Integer.parseInt(scanner.nextLine());
                    GradingScale gs = new GradingScale(physicalTest, gender, min, max, voto);
                    gradingScaleRepo.save(gs, em);
                    System.out.println("Fascia aggiunta con successo!");
                    break;
                case 2:
                    System.out.println("ID test fisico per cui vedere le fasce:");
                    for (PhysicalTest t : physicalTestRepo.findAll())
                        System.out.println(t.getId() + ": " + t.getDescription());
                    long tId = Long.parseLong(scanner.nextLine());
                    PhysicalTest test = physicalTestRepo.findById(tId);
                    if (test == null) {
                        System.out.println("Test non valido.");
                        break;
                    }
                    for (Gender g : Gender.values()) {
                        System.out.println("Fasce per " + g + ":");
                        for (GradingScale fascia : gradingScaleRepo.findByPhysicalTestAndGender(test, g, em)) {
                            System.out.printf("Min: %.2f  Max: %.2f  Voto: %d\n",
                                    fascia.getMinValue(), fascia.getMaxValue(), fascia.getVoto());
                        }
                    }
                    break;
                case 3:
                    // Cancellazione fascia (non implementata)
                    System.out.println("Cancellazione diretta di una fascia non implementata qui.");
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opzione non valida. Riprova.");
            }
        }
    }
}
