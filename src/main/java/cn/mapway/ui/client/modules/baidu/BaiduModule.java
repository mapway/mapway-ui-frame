package cn.mapway.ui.client.modules.baidu;

import cn.mapway.ui.client.mvc.AbstractModule;
import cn.mapway.ui.client.mvc.ModuleMarker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

@ModuleMarker(value = "MC_BAIDU", name = "百度模块", summary = "XXXXXX", icon = "help.png")
public class BaiduModule extends AbstractModule {

  private static BaiduModuleUiBinder uiBinder = GWT.create(BaiduModuleUiBinder.class);

  interface BaiduModuleUiBinder extends UiBinder<Widget, BaiduModule> {
  }

  public BaiduModule() {
    initWidget(uiBinder.createAndBindUi(this));
    registerTools(new Button("Fuck Baidu"));

  }

  @Override
  public Widget getRootWidget() {
    return this;
  }
}
