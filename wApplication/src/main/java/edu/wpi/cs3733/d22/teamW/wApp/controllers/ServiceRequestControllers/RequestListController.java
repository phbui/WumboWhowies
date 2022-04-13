package edu.wpi.cs3733.d22.teamW.wApp.controllers.ServiceRequestControllers;

import edu.wpi.cs3733.d22.teamW.wApp.controllers.LoadableController;
import edu.wpi.cs3733.d22.teamW.wApp.controllers.customControls.RequestTable;
import edu.wpi.cs3733.d22.teamW.wApp.serviceRequests.*;
import edu.wpi.cs3733.d22.teamW.wDB.RequestFacade;
import edu.wpi.cs3733.d22.teamW.wDB.enums.RequestType;
import edu.wpi.cs3733.d22.teamW.wMid.SceneManager;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class RequestListController extends LoadableController {
  @FXML public RequestTable rt;
  @FXML public TextArea moreInfo;
  @FXML public ComboBox equipmentSelection;
  @FXML public HBox selectionButtons;

  @Override
  protected SceneManager.Scenes GetSceneType() {
    return SceneManager.Scenes.RequestList;
  }

  @Override
  public void initialize(URL location, ResourceBundle rb) {
    super.initialize(location, rb);

    rt.getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSelection, newSelection) -> {
              if (newSelection == null) {
                moreInfo.setText("Select a request to view details.");
              }
              SR request = rt.getSelection();
              try {
                moreInfo.setText(request.getFormattedInfo());
              } catch (SQLException e) {
                e.printStackTrace();
                moreInfo.setText("Error loading request details.");
              }
              selectionButtons.setVisible(newSelection != null);
            });

    equipmentSelection
        .getSelectionModel()
        .selectedIndexProperty()
        .addListener((e, o, n) -> setItemsWithFilter(n.intValue()));
  }

  public void onLoad() {
    rt.setColumnWidth("Req. ID", 60);
    rt.setColumnWidth("Request Type", 130);
    rt.setColumnWidth("Employee Name", 140);
    rt.setColumnWidth("Status", 80);
    rt.setEditable(false);
    moreInfo.setText("Select a request to view details.");
    resetItems();
  }

  public void resetItems() {
    setItemsWithFilter(equipmentSelection.getSelectionModel().getSelectedIndex());
  }

  @Override
  public void onUnload() {}

  private void setItemsWithFilter(int index) {
    switch (index) {
      case -1:
      case 0:
        try {
          rt.setItems(RequestFacade.getRequestFacade().getAllRequests());
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
        break;
      case 1:
        try {
          rt.setItems(
              RequestFacade.getRequestFacade().getAllRequests(RequestType.LabServiceRequest));
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
        break;
      case 2:
        try {
          rt.setItems(
              RequestFacade.getRequestFacade().getAllRequests(RequestType.LanguageInterpreter));
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
        break;
      case 3:
        try {
          rt.setItems(RequestFacade.getRequestFacade().getAllRequests(RequestType.MealDelivery));
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
        break;
      case 4:
        try {
          rt.setItems(
              RequestFacade.getRequestFacade().getAllRequests(RequestType.MedicalEquipmentRequest));
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
        break;
      case 5:
        try {
          rt.setItems(RequestFacade.getRequestFacade().getAllRequests(RequestType.SecurityService));
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
        break;
      case 6:
        try {
          rt.setItems(RequestFacade.getRequestFacade().getAllRequests(RequestType.CleaningRequest));
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
        break;
    }
    clearSelection();
  }

  public void cancel(ActionEvent actionEvent) throws SQLException {
    RequestFacade.getRequestFacade()
        .cancelRequest(rt.getSelection().getRequestID(), rt.getSelection().getRequestType());
    resetItems();
  }

  public void confirm(ActionEvent event) throws SQLException {
    RequestFacade.getRequestFacade()
        .completeRequest(
            rt.getSelection().getRequestID(),
            rt.getSelection().getRequestType(),
            rt.getSelection().getNodeID());
    resetItems();
  }

  public void clearSelection() {
    rt.getSelectionModel().clearSelection();
    selectionButtons.setVisible(false);
  }

  public void start() throws SQLException {
    try {
      RequestFacade.getRequestFacade()
          .startRequest(rt.getSelection().getRequestID(), rt.getSelection().getRequestType());
    } catch (SQLException e) {
      Alert alert = new Alert(Alert.AlertType.WARNING, "Something went wrong.", ButtonType.OK);
      alert.showAndWait();
      throw e;
    } catch (Exception e) {
      Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
      alert.showAndWait();
    }
    resetItems();
  }
}
