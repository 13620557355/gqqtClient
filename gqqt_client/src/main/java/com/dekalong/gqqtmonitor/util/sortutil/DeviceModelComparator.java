package com.dekalong.gqqtmonitor.util.sortutil;

import com.dekalong.gqqtmonitor.po.DeviceModel;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class DeviceModelComparator
{
  public static Comparator<DeviceModel> getComparatorASC()
  { 
	return new Comparator<DeviceModel>() {
		@Override
		public int compare(DeviceModel dm1, DeviceModel dm2) {
			int a = dm1.getOnLineStatus();
	        int b = dm2.getOnLineStatus();
	        if ((a == 1) && (b == 1)) {
	          double dmv1 = dm1.getComparValue();
	          double dmv2 = dm2.getComparValue();
	          if (dmv1 > dmv2)
	            return 1;
	          if (dmv1 < dmv2) {
	            return -1;
	          }
	          return 0;
	        }

	        return a - b;
	      }
	};
  }

  public static void main(String[] args)
  {
	    Set set = new TreeSet(getComparatorASC());
	    DeviceModel ddModel = null;
	    DeviceModel dm;
	    for (int i = 1; i <= 10; i++) {
	      dm = new DeviceModel();
	      dm.setUuid(Integer.valueOf(i));
	
	      dm.setOnLineStatus(1);
	      dm.setComparValue(i);
	      set.add(dm);
	      if (i == 10) {
	        ddModel = dm;
	      }
	    }
	    ddModel.setOnLineStatus(0);
	    System.out.println("ddModelUuid=" + ddModel.getUuid());
	    Set<DeviceModel> set2 = new TreeSet(getComparatorASC());
	    set2.addAll(set);
	    for (DeviceModel deviceModel : set2) {
	    	System.out.println("uuid=" + deviceModel.getUuid() + "value=" + deviceModel.getOnLineStatus());
    }
      
    }
}