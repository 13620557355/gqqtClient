package com.dekalong.gqqtmonitor.po.querymodel;

import com.dekalong.gqqtmonitor.po.DataStatistics;

public class DataStatisticsQuery extends DataStatistics
{
  private static final long serialVersionUID = 1L;
  private String startTimer;
  private String endTimer;
  private String gasSpeciesName;
  private Integer manUuid;

  public String getStartTimer()
  {
    return this.startTimer;
  }
  public String getEndTimer() {
    return this.endTimer;
  }
  public void setStartTimer(String startTimer) {
    this.startTimer = startTimer;
  }
  public void setEndTimer(String endTimer) {
    this.endTimer = endTimer;
  }

  public String getGasSpeciesName() {
    return this.gasSpeciesName;
  }
  public void setGasSpeciesName(String gasSpeciesName) {
    this.gasSpeciesName = gasSpeciesName;
  }

  public String toString() {
    return "DataStatisticsQuery [" + super.toString() + " ,startTimer=" + this.startTimer + ", endTimer=" + this.endTimer + ", gasSpeciesName=" + this.gasSpeciesName + ", manUuid=" + this.manUuid + "]";
  }
  public Integer getManUuid() {
    return this.manUuid;
  }
  public void setManUuid(Integer manUuid) {
    this.manUuid = manUuid;
  }
}