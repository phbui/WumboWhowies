package edu.wpi.cs3733.d22.teamW.wMid;

import edu.wpi.cs3733.d22.teamW.wApp.controllers.LoadableController;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SceneManager {
  private class Page {
    public Pane pane;
    public LoadableController controller;

    public Page(Pane pane, LoadableController controller) {
      this.pane = pane;
      this.controller = controller;
    }
  }

  public enum Scenes {
    Lab,
    LanguageInterpreter,
    MealDelivery,
    MedicalEquipment,
    MedicineDelivery,
    Security,
    Default,
    MainMenu,
    MapEditor,
    RequestList,
    RequestHub
  }

  private static class Instance {
    public static final SceneManager instance = new SceneManager();
  }

  private Stage primaryStage;
  private final Hashtable<Scenes, Page> pages = new Hashtable<>();
  private Scenes current;

  private Hashtable<Stage, Dictionary<String, Object>> information = new Hashtable<>();

  private SceneManager() {}

  public static SceneManager getInstance() {
    return Instance.instance;
  }

  public void exitApplication() {
    if (pages.get(current).controller != null) {
      pages.get(current).controller.onUnload();
    }
    System.exit(0);
  }

  public void setPrimaryStage(Stage primaryStage) {
    getInstance().primaryStage = primaryStage;

    information.put(primaryStage, new Hashtable<>());
  }

  public void putPane(Scenes scene, Pane pane) {
    pane.setDisable(true);
    pane.setVisible(false);
    if (pages.get(scene) != null) {
      pages.get(scene).pane = pane;
    } else {
      pages.put(scene, new Page(pane, null));
    }
  }

  public void putController(Scenes scene, LoadableController controller) {
    if (pages.get(scene) != null) {
      pages.get(scene).controller = controller;
    } else {
      pages.put(scene, new Page(null, controller));
    }
  }

  public void setPaneVisible(Scenes scene) {
    if (current != null) {
      pages.get(current).pane.setVisible(false);
      pages.get(current).pane.setDisable(true);
      if (pages.get(current).controller != null) {
        pages.get(current).controller.onUnload();
      }
    }
    current = scene;
    pages.get(current).pane.setVisible(true);
    pages.get(current).pane.setDisable(false);
    if (pages.get(current).controller != null) {
      pages.get(current).controller.onLoad();
    }
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  public void setScene(String fileName) throws IOException {
    Parent root =
        FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/d22/teamW/wApp/views/" + fileName));
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public Scene getScene() {
    return primaryStage.getScene();
  }

  public void putInformation(Stage stage, String infoName, Object info) {
    information.get(stage).put(infoName, info);
  }
  public Object getInformation(Stage stage, String infoName) {
    return information.get(stage).get(infoName);
  }

  public Stage openWindow(String fileName) throws IOException {
    Stage stage = new Stage();
    stage.initOwner(primaryStage);
    stage.initModality(Modality.APPLICATION_MODAL);

    Parent root =
        FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/d22/teamW/wApp/views/" + fileName));
    stage.setScene(new Scene(root));
    stage.show();
    stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);

    information.put(stage, new Hashtable<>());
    return stage;
  }

  private void closeWindowEvent(WindowEvent e) {
    information.remove(e.getSource());
  }
}
