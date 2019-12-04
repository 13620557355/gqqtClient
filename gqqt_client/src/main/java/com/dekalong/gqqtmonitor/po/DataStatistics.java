package com.dekalong.gqqtmonitor.po;

public class DataStatistics extends BaseModel
{
  private static final long serialVersionUID = 1L;
  private Customer custUuid;
  private Busbar pipUuid;
  private Monitor monUuid;
  private Integer replaceNum;
  private String generationDates;
  private String gasMedium;
  private String containerType;
  private String replaceDirection;
  private String remarks;

  public static long getSerialversionuid()
  {
    return 1L;
  }
  public Customer getCustUuid() {
    return this.custUuid;
  }
  public Busbar getPipUuid() {
    return this.pipUuid;
  }
  public Integer getReplaceNum() {
    return this.replaceNum;
  }
  public String getGenerationDates() {
    return this.generationDates;
  }
  public String getGasMedium() {
    return this.gasMedium;
  }
  public String getContainerType() {
    return this.containerType;
  }
  public String getReplaceDirection() {
    return this.replaceDirection;
  }
  public String getRemarks() {
    return this.remarks;
  }
  public void setCustUuid(Customer custUuid) {
    this.custUuid = custUuid;
  }
  public void setPipUuid(Busbar pipUuid) {
    this.pipUuid = pipUuid;
  }
  public void setReplaceNum(Integer replaceNum) {
    this.replaceNum = replaceNum;
  }
  public void setGenerationDates(String generationDates) {
    this.generationDates = generationDates;
  }
  public void setGasMedium(String gasMedium) {
    this.gasMedium = gasMedium;
  }
  public void setContainerType(String containerType) {
    this.containerType = containerType;
  }
  public void setReplaceDirection(String replaceDirection) {
    this.replaceDirection = replaceDirection;
  }
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
  public Monitor getMonUuid() {
    return this.monUuid;
  }
  public void setMonUuid(Monitor monUuid) {
    this.monUuid = monUuid;
  }

  public String toString() {
    return "DataStatistics [uuid=" + getUuid() + ", custUuid=" + this.custUuid + ", pipUuid=" + this.pipUuid + ", monUuid=" + this.monUuid + ", replaceNum=" + this.replaceNum + ", generationDates=" + this.generationDates + ", gasMedium=" + this.gasMedium + ", containerType=" + this.containerType + ", replaceDirection=" + this.replaceDirection + ", remarks=" + this.remarks + "]";
  }
}