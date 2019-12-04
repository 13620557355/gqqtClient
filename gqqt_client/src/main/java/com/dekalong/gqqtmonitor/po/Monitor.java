package com.dekalong.gqqtmonitor.po;

public class Monitor extends DeviceModel
{
  private static final long serialVersionUID = 1L;
  private String monName;
  private String monType;
  private int monIotAddr;
  private int monIdLeft;
  private int monIdRight;
  private int monIdComm;
  private String monContainerType;
  private double monAlarm;
  private double monRange;
  private int monLeftCylindersNum;
  private int monRightCylindersNum;
  private int monStockNumber;
  private String monRemarks;
  private double monGasPressure;
  private ExtendInterface extendInterface;

  public String getMonName()
  {
    return this.monName;
  }
  public String getMonType() {
    return this.monType;
  }
  public int getMonIotAddr() {
    return this.monIotAddr;
  }
  public int getMonIdLeft() {
    return this.monIdLeft;
  }
  public int getMonIdRight() {
    return this.monIdRight;
  }
  public int getMonIdComm() {
    return this.monIdComm;
  }
  public String getMonContainerType() {
    return this.monContainerType;
  }
  public double getMonAlarm() {
    return this.monAlarm;
  }
  public double getMonRange() {
    return this.monRange;
  }
  public int getMonLeftCylindersNum() {
    return this.monLeftCylindersNum;
  }
  public int getMonRightCylindersNum() {
    return this.monRightCylindersNum;
  }
  public int getMonStockNumber() {
    return this.monStockNumber;
  }
  public String getMonRemarks() {
    return this.monRemarks;
  }
  public double getMonGasPressure() {
    return this.monGasPressure;
  }
  public ExtendInterface getExtendInterface() {
    return this.extendInterface;
  }

  public void setMonName(String monName) {
    this.monName = monName;
  }
  public void setMonType(String monType) {
    this.monType = monType;
  }
  public void setMonIotAddr(int monIotAddr) {
    this.monIotAddr = monIotAddr;
  }
  public void setMonIdLeft(int monIdLeft) {
    this.monIdLeft = monIdLeft;
  }
  public void setMonIdRight(int monIdRight) {
    this.monIdRight = monIdRight;
  }
  public void setMonIdComm(int monIdComm) {
    this.monIdComm = monIdComm;
  }
  public void setMonContainerType(String monContainerType) {
    this.monContainerType = monContainerType;
  }
  public void setMonAlarm(double monAlarm) {
    this.monAlarm = monAlarm;
  }
  public void setMonRange(double monRange) {
    this.monRange = monRange;
  }
  public void setMonLeftCylindersNum(int monLeftCylindersNum) {
    this.monLeftCylindersNum = monLeftCylindersNum;
  }
  public void setMonRightCylindersNum(int monRightCylindersNum) {
    this.monRightCylindersNum = monRightCylindersNum;
  }
  public void setMonStockNumber(int monStockNumber) {
    this.monStockNumber = monStockNumber;
  }
  public void setMonRemarks(String monRemarks) {
    this.monRemarks = monRemarks;
  }
  public void setMonGasPressure(double monGasPressure) {
    this.monGasPressure = monGasPressure;
  }
  public void setExtendInterface(ExtendInterface extendInterface) {
    this.extendInterface = extendInterface;
  }

  public String toString() {
    return "Monitor [uuid=" + getUuid() + ", monName=" + this.monName + ", monType=" + this.monType + ", monIotAddr=" + this.monIotAddr + ", monIdLeft=" + this.monIdLeft + ", monIdRight=" + this.monIdRight + ", monIdComm=" + this.monIdComm + ", monContainerType=" + this.monContainerType + ", monAlarm=" + this.monAlarm + ", monRange=" + this.monRange + ", monLeftCylindersNum=" + this.monLeftCylindersNum + ", monRightCylindersNum=" + this.monRightCylindersNum + ", stockNumber=" + this.monStockNumber + ", monRemarks=" + this.monRemarks + ", monGasPressure=" + this.monGasPressure + ", extendInterface=" + this.extendInterface + "]";
  }
}