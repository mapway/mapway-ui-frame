package cn.mapway.ui.client.test;

import cn.mapway.ui.client.widget.common.TimeRangeBox;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class TestEntry implements EntryPoint {

  @Override
  public void onModuleLoad() {

    TimeRangeBox box = new TimeRangeBox();
    RootPanel.get().add(box);

  }
}
