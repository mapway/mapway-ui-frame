package cn.mapway.ui.client.modules.test;

import cn.mapway.ui.client.mvc.AbstractModule;
import cn.mapway.ui.client.mvc.ModuleMarker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

@ModuleMarker(value = "MC_SINGLE_MODULE", name = "单页面模块")
public class SingleModule extends AbstractModule {

  private static SingleModuleUiBinder uiBinder = GWT.create(SingleModuleUiBinder.class);

  interface SingleModuleUiBinder extends UiBinder<Widget, SingleModule> {
  }

  public SingleModule() {
    initWidget(uiBinder.createAndBindUi(this));
    registerTools(tools);

  }

  @UiField
  HorizontalPanel tools;

  @Override
  public Widget getRootWidget() {
    return this;
  }

}
