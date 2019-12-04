package com.dekalong.gqqtmonitor.po;

import java.math.BigDecimal;

public class DeviceModel extends BaseModel{
  private static final long serialVersionUID = 1L;
  protected Customer custUuid;
  protected String custQueryUuid;
  protected double iotParamLeft = 0.0D;
  protected double iotParamRight = 0.0D;
  protected double iotParamComm = 0.0D;
  protected int onLineStatus = 0;
  protected long lastTime = 0L;

  protected int leftSMSFullNotifly = 0;
  protected int rightSMSFullNotifly = 0;
  protected int leftSMSEmptyNotifly = 1;
  protected int rightSMSEmptyNotifly = 1;
  protected boolean leftBackflowSwicth = false;
  protected boolean rightBackflowSwicth = false;
  protected boolean commBackflowSwicth = false;
  protected String transmitterType;
  protected int comparValue = 0;

  public double getIotParamLeft()
  {
    BigDecimal b = new BigDecimal(this.iotParamLeft);
    double b1 = b.setScale(1, 2).doubleValue();
    return b1;
  }
  public double getIotParamRight() {
    BigDecimal b = new BigDecimal(this.iotParamRight);
    double b1 = b.setScale(1, 2).doubleValue();
    return b1;
  }
  public double getIotParamComm() {
    BigDecimal b = new BigDecimal(this.iotParamComm);
    double b1 = b.setScale(1, 2).doubleValue();
    return b1;
  }

  public void setIotParamLeft(double iotParamLeft) {
    BigDecimal b = new BigDecimal(iotParamLeft);
    double b1 = b.setScale(1, 2).doubleValue();
    this.iotParamLeft = b1;
  }
  public void setIotParamRight(double iotParamRight) {
    BigDecimal b = new BigDecimal(iotParamRight);
    double b1 = b.setScale(1, 2).doubleValue();
    this.iotParamRight = b1;
  }

  public void setIotParamComm(double iotParamComm) {
    BigDecimal b = new BigDecimal(iotParamComm);
    double b1 = b.setScale(1, 2).doubleValue();
    this.iotParamComm = b1;
  }

  public int getOnLineStatus() {
    return this.onLineStatus;
  }
  public void setOnLineStatus(int onLineStatus) {
    this.onLineStatus = onLineStatus;
  }
  public long getLastTime() {
    return this.lastTime;
  }
  public void setLastTime(long lastTime) {
    this.lastTime = lastTime;
  }

  public int getLeftSMSFullNotifly()
  {
    return this.leftSMSFullNotifly;
  }
  public int getRightSMSFullNotifly() {
    return this.rightSMSFullNotifly;
  }
  public int getLeftSMSEmptyNotifly() {
    return this.leftSMSEmptyNotifly;
  }
  public int getRightSMSEmptyNotifly() {
    return this.rightSMSEmptyNotifly;
  }
  public void setLeftSMSFullNotifly(int leftSMSFullNotifly) {
    this.leftSMSFullNotifly = leftSMSFullNotifly;
  }
  public void setRightSMSFullNotifly(int rightSMSFullNotifly) {
    this.rightSMSFullNotifly = rightSMSFullNotifly;
  }
  public void setLeftSMSEmptyNotifly(int leftSMSEmptyNotifly) {
    this.leftSMSEmptyNotifly = leftSMSEmptyNotifly;
  }
  public void setRightSMSEmptyNotifly(int rightSMSEmptyNotifly) {
    this.rightSMSEmptyNotifly = rightSMSEmptyNotifly;
  }

  public String getCustQueryUuid() {
    return this.custQueryUuid;
  }
  public void setCustQueryUuid(String custQueryUuid) {
    this.custQueryUuid = custQueryUuid;
  }
  public Customer getCustUuid() {
    return this.custUuid;
  }
  public void setCustUuid(Customer custUuid) {
    this.custUuid = custUuid;
  }

  public void setComparValue(int value) {
    this.comparValue = value;
  }
  public int getComparValue() {
    return this.comparValue;
  }
  public boolean isLeftBackflowSwicth() {
    return this.leftBackflowSwicth;
  }
  public boolean isRightBackflowSwicth() {
    return this.rightBackflowSwicth;
  }
  public boolean isCommBackflowSwicth() {
    return this.commBackflowSwicth;
  }
  public void setLeftBackflowSwicth(boolean leftBackflowSwicth) {
    this.leftBackflowSwicth = leftBackflowSwicth;
  }
  public void setRightBackflowSwicth(boolean rightBackflowSwicth) {
    this.rightBackflowSwicth = rightBackflowSwicth;
  }
  public void setCommBackflowSwicth(boolean commBackflowSwicth) {
    this.commBackflowSwicth = commBackflowSwicth;
  }
  public String getTransmitterType() {
    return this.transmitterType;
  }
  public void setTransmitterType(String transmitterType) {
    this.transmitterType = transmitterType;
  }

  public String toString() {
    return "DeviceModel [" + super.toString() + ", custUuid=" + this.custUuid + ", iotParamLeft=" + this.iotParamLeft + ", iotParamRight=" + this.iotParamRight + ", iotParamComm=" + this.iotParamComm + ", onLineStatus=" + this.onLineStatus + ", lastTime=" + this.lastTime + ", leftSMSFullNotifly=" + this.leftSMSFullNotifly + ", rightSMSFullNotifly=" + this.rightSMSFullNotifly + ", leftSMSEmptyNotifly=" + this.leftSMSEmptyNotifly + ", rightSMSEmptyNotifly=" + this.rightSMSEmptyNotifly + ", transmitterType=" + this.transmitterType + "]";
  }
}