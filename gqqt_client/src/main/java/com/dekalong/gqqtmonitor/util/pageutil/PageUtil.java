package com.dekalong.gqqtmonitor.util.pageutil;

import com.dekalong.gqqtmonitor.initmodel.InitAppModel;
import com.dekalong.gqqtmonitor.po.DeviceModel;
import com.dekalong.gqqtmonitor.util.sortutil.DeviceModelComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageUtil
{
  public static <M> List<M> conditionPageList(List<M> list, int newp, int rows)
  {
    if ((list == null) || (list.size() == 0) || (list.size() < newp)) return new ArrayList<>();
    if (newp == 0) newp = 1;
    int start = 0;
    int end = 0;
    if (newp == 1) {
      start = 0;
      end = rows;
    } else if (newp > 1) {
      start = (newp - 1) * rows;
      end = rows + (newp - 1) * rows;
    }

    List<M> newlist = new ArrayList<>();
    if (list.size() <= end) {
      end = list.size();
    }
    for (int i = start; i < end; i++) {
      if (list.size() > i) {
        newlist.add(list.get(i));
      }
    }
    return newlist;
  }

  public static List<DeviceModel> conditionPageSortList(List<DeviceModel> list, int newp, int rows) {
    if ((list == null) || (list.size() == 0) || (list.size() < newp)) return new ArrayList<>();
    if (newp == 0) newp = 1;
    int start = 0;
    int end = 0;
    if (newp == 1) {
      start = 0;
      end = rows;
    } else if (newp > 1) {
      start = (newp - 1) * rows;
      end = rows + (newp - 1) * rows;
    }
    Collections.sort(list, DeviceModelComparator.getComparatorASC());
    List<DeviceModel> newlist = new ArrayList<>();
    if (list.size() <= end) {
      end = list.size();
    }
    for (int i = start; i < end; i++) {
      if (list.size() > i) {
        newlist.add(list.get(i));
      }
    }

    return newlist;
  }

  public static List<DeviceModel> conditionPageAllData(int newp, int rows) {
    if (newp == 0) newp = 1;
    int start = 0;
    int end = 0;
    if (newp == 1) {
      start = 0;
      end = rows;
    } else if (newp > 1) {
      start = (newp - 1) * rows;
      end = rows + (newp - 1) * rows;
    }
    List<DeviceModel> list = InitAppModel.getBusbarAndMonList();
    Collections.sort(list, DeviceModelComparator.getComparatorASC());
    List<DeviceModel> newlist = new ArrayList<>();
    if (list.size() <= end) {
      end = list.size();
    }
    for (int i = start; i < end; i++) {
      if (list.size() > i) {
        newlist.add(list.get(i));
      }
    }
    return newlist;
  }

  public static void main(String[] args)
  {
    List<Integer> list = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      list.add(Integer.valueOf(i));
    }
    System.out.println(conditionPageList(list, 0, 10));
  }
}