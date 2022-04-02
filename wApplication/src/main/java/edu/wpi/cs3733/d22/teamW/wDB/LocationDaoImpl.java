package edu.wpi.cs3733.d22.teamW.wDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

public class LocationDaoImpl implements LocationDao {

  ArrayList<Location> locationList;
  DBController dbController;

  public LocationDaoImpl(DBController dbController) {
    this.dbController = dbController;
    this.locationList = dbController.getLocationTable();
  }

  @Override
  public ArrayList<Location> getAllLocations() {
    return this.locationList;
  }

  @Override
  public void addLocation(String inputID) {
    Location param = new Location();
    int index = getIndexOf(inputID);
    if (index != -1) {
      System.out.println("The database already contains a location with the ID: " + inputID);
    } else {
      Location newLocation = new Location(inputID, null, null, null, null, null, null, null);
      locationList.add(newLocation);
      dbController.addEntity(param, inputID); // addition in database
    }
  }

  /**
   * This delete function handles the ArrayList. It looks for the ID entered and if it does not
   * already exist in the ArrayList, then it prints to the console saying that it could not be
   * found. If that location is found, it is removed from the ArrayList. At the end of the function,
   * it calls a function in dbController, which is where the location is deleted from the database
   *
   * <p>Author: Edison
   *
   * @param nodeID is the ID of the Location object that the user would like to delete
   * @throws SQLException
   */
  @Override
  public void deleteLocation(String nodeID) {
    int index = getIndexOf(nodeID);
    if (index == -1) {
      System.out.println("The database does not contain a location with the ID: " + nodeID);
    } else {
      locationList.remove(locationList.get(index));
      try {
        dbController.deleteLocation("LOCATIONS", nodeID);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void changeLocation(String nodeID, String newFloor, String newType) {
    // Check for valid data
    if (!isValidFloor(newFloor)) {
      System.out.println("Invalid floor entered");
      return;
    }
    if (!isValidNodeType(newType)) {
      System.out.println("Invalid node type entered");
      return;
    }

    int nodeIndex = getIndexOf(nodeID);
    if (nodeIndex == -1) {
      System.out.println("Location to modify does not exist.");
      return;
    } else {
      // Disabled automatic id updating per Matthew
      /*
      int newIDNumber = dbController.countFloorTypeFromTable(newFloor, newType) + 1;
      String newIDNumberString;
      if (0 <= newIDNumber && newIDNumber <= 9) {
          newIDNumberString = "00" + Integer.toString(newIDNumber);
      } else if (10 <= newIDNumber && newIDNumber <= 99) {
          newIDNumberString = "0" + Integer.toString(newIDNumber);
      } else if (100 <= newIDNumber && newIDNumber <= 999) {
          newIDNumberString = Integer.toString(newIDNumber);
      } else {
          newIDNumberString = "999";
      }
      String newID = "W" + newType + newIDNumberString + newFloor;
       */
      locationList.get(nodeIndex).floor = newFloor;
      locationList.get(nodeIndex).nodeType = newType;
      // Disabled automatic id updating per Matthew
      // locationList.get(modifyIndex).nodeID = newID;
      dbController.updateNodeFromLocationTable(nodeID, newFloor, newType);
      // Disabled automatic id updating per Matthew
      // dbController.updateNodeIdFromLocationTable(modifyID, newID);
    }
  }

  @Override
  public void exportLocationCSV(String fileName) {
    File csvOutputFile = new File(fileName);
    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
      // print Table headers
      pw.print("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName");

      // print all locations
      for (Location l : locationList) {
        pw.println();
        pw.print(l.toCSVString());
      }

    } catch (FileNotFoundException e) {

      System.out.println(String.format("Error Exporting to File %s", fileName));
      e.printStackTrace();
    }
  }

  private boolean isValidNodeType(String nodeType) {
    return nodeType.length() == 4 && nodeType.equals(nodeType.toUpperCase(Locale.ROOT));
  }

  private boolean isValidFloor(String floor) {
    return floor.equals("01")
        || floor.equals("02")
        || floor.equals("03")
        || floor.equals("L1")
        || floor.equals("L2");
  }

  /**
   * Loops through the ArrayList, checking for matching nodeID. If found, returns that index.
   * Otherwise, returns -1.
   *
   * @param inputID is the nodeID of the object that we are looking for
   * @return index of location or -1 if it does not exist
   */
  private int getIndexOf(String inputID) {
    int size = locationList.size();
    boolean found = false;
    for (int i = 0; i < size; i++) {
      if (locationList.get(i).nodeID.equals(inputID)) {
        return i;
      }
    }
    return -1;
  }
}