package com.PhysicalED.gui;

import com.PhysicalED.repo.*;
import com.PhysicalED.service.GradingService;
import com.PhysicalED.service.ScoreService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carica FXML principale
        Scene scene = new Scene(
                FXMLLoader.load(getClass().getResource("/com/PhysicalED/gui/main.fxml"))
        );
        primaryStage.setTitle("Physical Education App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Inizializza EntityManager
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

        // Passa le istanze ai controller o a un contesto condiviso se necessario
        ClassSectionViewController classSectCtrl = new ClassSectionViewController(classSectionRepo, schoolYearRepo);
        SchoolYearViewController schoolYearCtrl = new SchoolYearViewController(schoolYearRepo);
        ScoreViewController scoreCtrl = new ScoreViewController(scoreRepo, studentRepo, physicalTestRepo, scoreService);
        SportCategoryViewController sportCatCtrl = new SportCategoryViewController(sportCategoryRepo);
        StudentViewController studentCtrl = new StudentViewController(studentRepo, classSectionRepo);
        PhysicalTestViewController physicalTestCtrl = new PhysicalTestViewController(physicalTestRepo, sportCategoryRepo);

        launch(args);
    }
}