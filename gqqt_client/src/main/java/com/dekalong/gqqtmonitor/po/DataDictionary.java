package com.dekalong.gqqtmonitor.po;

public class DataDictionary extends BaseModel{
  private static final long serialVersionUID = 1L;
  private int functionCode;
  private String functionCodeName;
  private String dataValue;

  public int getFunctionCode()
  {
    return this.functionCode;
  }
  public String getFunctionCodeName() {
    return this.functionCodeName;
  }
  public String getDataValue() {
    return this.dataValue;
  }
  public void setFunctionCode(int functionCode) {
    this.functionCode = functionCode;
  }
  public void setFunctionCodeName(String functionCodeName) {
    this.functionCodeName = functionCodeName;
  }
  public void setDataValue(String dataValue) {
    this.dataValue = dataValue;
  }

  public String toString() {
    return "DataDictionary [functionCode=" + this.functionCode + ", functionCodeName=" + this.functionCodeName + ", dataValue=" + this.dataValue + "]";
  }
}