package com.dekalong.gqqtmonitor.po;

public class DataFunction extends BaseModel
{
  private static final long serialVersionUID = 1L;
  private DataDictionary dicUuid;
  private Manager manUuid;
  private Customer custUuid;
  private int funID;

  public DataDictionary getDicUuid()
  {
    return this.dicUuid;
  }
  public Manager getManUuid() {
    return this.manUuid;
  }
  public Customer getCustUuid() {
    return this.custUuid;
  }
  public int getFunID() {
    return this.funID;
  }
  public void setDicUuid(DataDictionary dicUuid) {
    this.dicUuid = dicUuid;
  }
  public void setManUuid(Manager manUuid) {
    this.manUuid = manUuid;
  }
  public void setCustUuid(Customer custUuid) {
    this.custUuid = custUuid;
  }
  public void setFunID(int funID) {
    this.funID = funID;
  }

  public String toString() {
    return "DataFunction [dicUuid=" + this.dicUuid + ", manUuid=" + this.manUuid + ", custUuid=" + this.custUuid + ", funID=" + this.funID + "]";
  }
}