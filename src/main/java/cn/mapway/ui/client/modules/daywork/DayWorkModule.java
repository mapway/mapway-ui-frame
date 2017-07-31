package cn.mapway.ui.client.modules.daywork;

import cn.mapway.ui.client.mvc.AbstractModule;
import cn.mapway.ui.client.mvc.ModuleMarker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

@ModuleMarker(value = "MC_DAYWORK", name = "日志模块", summary = "--==--")
public class DayWorkModule extends AbstractModule {

  private static DayWorkModuleUiBinder uiBinder = GWT.create(DayWorkModuleUiBinder.class);

  interface DayWorkModuleUiBinder extends UiBinder<Widget, DayWorkModule> {
  }

  public DayWorkModule() {
    initWidget(uiBinder.createAndBindUi(this));
    registerSubModule("MC_BAIDU");
    registerSubModule("MC_SINGLE_MODULE");

    registerTools(new Button("Helloe"));
    registerTools(new Button("Helloe232"));
  }

  @Override
  public Widget getRootWidget() {
    return this;
  }

}
