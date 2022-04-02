package edu.wpi.cs3733.d22.teamW.wDB;

import lombok.Getter;

@Getter
public class MedEquipRequest extends Entity {
  Integer
      requestID; // Maybe switch to Hexatrigesimal uses 0-9 and then A to Z, so it allows a large
  // number of req
  // With 6 char, it will allow 36^6 = 2.18 billion
  Integer emergency;
  String itemID; // Medical Equipment item
  String nodeID; // Location
  Integer status; // 0 not started; 1 in progress; 2 done; 3 cancelled
  String employeeName; // Will be changed to employee ID starting in sprint 1

  public MedEquipRequest(
      Integer requestID, Integer emergency, String itemID, String nodeID, String employeeName) {
    this.requestID = requestID;
    this.emergency = emergency;
    this.itemID = itemID;
    this.nodeID = nodeID;
    this.employeeName = employeeName;
    this.status = 0;
  }

  public MedEquipRequest(String[] medReqData) {
    try {
      this.requestID = Integer.parseInt(medReqData[0]);
    } catch (NumberFormatException e) {
      this.requestID = null;
    }

    this.itemID = medReqData[1];
    this.nodeID = medReqData[2];
    this.employeeName = medReqData[3];

    try {
      this.emergency = Integer.parseInt(medReqData[4]);
    } catch (NumberFormatException e) {
      this.emergency = 0;
    }

    try {
      this.status = Integer.parseInt(medReqData[5]);
    } catch (NumberFormatException e) {
      this.status = null;
    }
  }

  public void start() {
    if (this.status == 0) {
      this.status = 1;
    } else {
      // Tells the user that it is in progress or completed
      // Could be a pop-up to the user when they click the start button or something
    }
  }

  public void complete() {
    if (this.status == 1) {
      this.status = 2;
    } else {
      // The complete button should only appear if it is in progress
    }
  }

  public void cancel() {
    this.status = 3;
  }

  public String toCSVString() {
    return String.format(
        "%d,%d,%s,%s,%d,%s",
        this.requestID, isEmergency(), this.itemID, this.nodeID, this.status, this.employeeName);
  }

  @Override
  public String toValuesString() {

    return String.format(
        "%d, '%s', '%s', '%s', %d, %d",
        this.requestID, this.itemID, this.nodeID, this.employeeName, this.emergency, this.status);
  }

  public Integer getRequestID() {
    return requestID;
  }

  public int isEmergency() {
    return emergency;
  }

  public String getItemID() {
    return itemID;
  }

  public String getNodeID() {
    return nodeID;
  }

  public int getStatus() {
    return status;
  }

  public String getEmployeeName() {
    return employeeName;
  }

  public void setRequestID(Integer requestID) {
    this.requestID = requestID;
  }

  public void setEmergency(Integer emergency) {
    this.emergency = emergency;
  }

  public void setItemID(String itemID) {
    this.itemID = itemID;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }
}