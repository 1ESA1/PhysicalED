package com.PhysicalED;
import com.PhysicalED.model.*;
import com.PhysicalED.repo.*;
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
        SportingDisciplineRepository sportingDisciplineRepo = new SportingDisciplineRepository(em);
        StudentRepository studentRepo = new StudentRepository(em);
        TestDisciplineRepository testDisciplineRepo = new TestDisciplineRepository(em);

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
                               testDisciplineRepo,
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
                                           sportingDisciplineRepo,
                                           testDisciplineRepo);
                    break;
                case 5:
                    menuTestFisici(scanner,
                                   testDisciplineRepo,
                                   sportingDisciplineRepo,
                                   classSectionRepo,
                                   scoreRepo);
                    break;
                case 6:
                    menuPunteggiDiscipline(scanner,
                                           scoreRepo,
                                           studentRepo,
                                           testDisciplineRepo);
                    break;
                case 7:
                    menuStatistichePunteggi(scanner,
                                            scoreRepo,
                                            studentRepo,
                                            testDisciplineRepo);
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
                                   TestDisciplineRepository testDisciplineRepo,
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
                    // Controllo TestDiscipline collegati
                    List<TestDiscipline> testCollegati = testDisciplineRepo.findByClassSectionId(idToDel);
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
                    Student stud = new Student(name, surname);
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
                                               SportingDisciplineRepository repo,
                                               TestDisciplineRepository testDisciplineRepo) {
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
                    SportingDiscipline disciplina = new SportingDiscipline();
                    disciplina.setDescription(nomeDisc);
                    repo.save(disciplina);
                    System.out.println("Disciplina aggiunta con successo.");
                    break;
                case 2:
                    for (SportingDiscipline d : repo.findAll()) {
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
                    // Controllo TestDiscipline collegati
                    List<TestDiscipline> testCollegati = testDisciplineRepo.findBySportingDisciplineId(idToDel);
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
                                       TestDisciplineRepository testDisciplineRepo,
                                       SportingDisciplineRepository sportingDisciplineRepo,
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
                    for (SportingDiscipline d : sportingDisciplineRepo.findAll()) {
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
                    SportingDiscipline disciplina = sportingDisciplineRepo.findById(discId);
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
                    TestDiscipline test = new TestDiscipline();
                    test.setDescription(descr);
                    test.setSportingDiscipline(disciplina);
                    test.setClassSection(sezione);
                    test.setTestDate(testDate);
                    testDisciplineRepo.save(test);
                    System.out.println("Test fisico aggiunto con successo.");
                    break;
                case 2:
                    for (TestDiscipline t : testDisciplineRepo.findAll()) {
                        System.out.println(
                                t.getId() + ": " +
                                        t.getDescription() + // aggiungi campo description/testName se manca!
                                        " | Disciplina: " + t.getSportingDiscipline().getDescription() +
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
                    List<Score> punteggiCollegati = scoreRepo.findByTestDisciplineId(idToDel);
                    if (!punteggiCollegati.isEmpty()) {
                        System.out.println("Non puoi eliminare questo test fisico: esistono punteggi collegati!");
                        break;
                    }
                    try{
                        testDisciplineRepo.delete(idToDel);
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
                                               TestDisciplineRepository testDisciplineRepo) {
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
                    for (TestDiscipline t : testDisciplineRepo.findAll()) {
                        System.out.println(t.getId() + ": " + t.getDescription());
                    }
                    System.out.print("ID Test Fisico: ");
                    Long testId = Long.parseLong(scanner.nextLine());
                    TestDiscipline test = testDisciplineRepo.findById(testId);
                    if (test == null) {
                        System.out.println("Test fisico non trovato!");
                        break;
                    }
                    // 3. INSERISCI PUNTEGGIO
                    System.out.print("Punteggio: ");
                    Double value = Double.parseDouble(scanner.nextLine());
                    Score score = new Score();
                    score.setStudent(studente);
                    score.setTestDiscipline(test);
                    score.setValue(value);
                    scoreRepo.save(score);
                    System.out.println("Voto del test aggiunto con successo.");
                    break;
                case 2:
                    System.out.println("Tutti i voti dei test:");
                    for (Score s : scoreRepo.findAll()) {
                        System.out.println(
                                "ID: " + s.getId() +
                                        " | Studente: " + s.getStudent().getFirstName() + " " + s.getStudent().getLastName() +
                                        " | Test: " + s.getTestDiscipline().getDescription() +
                                        " | Punteggio: " + s.getValue()
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
                                                TestDisciplineRepository testDisciplineRepo) {
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
                                        " | Test: " + s.getTestDiscipline().getDescription() +
                                        " | Disciplina: " + s.getTestDiscipline().getSportingDiscipline().getDescription() +
                                        " | Punteggio: " + s.getValue()
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
                    for (TestDiscipline test : testDisciplineRepo.findAll()) {
                        List<Score> voti = scoreRepo.findByTestDisciplineId(test.getId()); // DA AGGIUNGERE IN REPO!
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
                                "Test: " + s.getTestDiscipline().getDescription() +
                                        " | Punteggio: " + s.getValue()
                        );
                    }
                    break;
                case 5:
                    System.out.println("Scegli ID test per vedere tutti i voti:");
                    for (TestDiscipline test : testDisciplineRepo.findAll())
                        System.out.println(test.getId() + ": " + test.getDescription());
                    Long testId = Long.parseLong(scanner.nextLine());
                    List<Score> votiTest = scoreRepo.findByTestDisciplineId(testId); // DA AGGIUNGERE IN REPO!
                    for (Score s : votiTest) {
                        System.out.println(
                                "Studente: " + s.getStudent().getFirstName() + " " + s.getStudent().getLastName() +
                                        " | Punteggio: " + s.getValue()
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
}
