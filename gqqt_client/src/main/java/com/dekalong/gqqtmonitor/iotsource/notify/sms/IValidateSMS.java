package com.dekalong.gqqtmonitor.iotsource.notify.sms;

import com.dekalong.gqqtmonitor.po.BaseModel;

public abstract interface IValidateSMS
{
  public abstract void validateCustomerSendSMS(BaseModel paramBaseModel);
}