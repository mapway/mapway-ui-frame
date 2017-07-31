package cn.mapway.ui.client.test;

import cn.mapway.ui.client.frames.SimpleModuleDispatcher;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class TestEntry implements EntryPoint {

  @Override
  public void onModuleLoad() {


    SimpleModuleDispatcher dispatcher = new SimpleModuleDispatcher();
    RootLayoutPanel.get().add(dispatcher);

    // dispatcher.switchModule("MC_SINGLE_MODULE", null);
    dispatcher.switchModule("MC_DAYWORK", null);


  }
}
