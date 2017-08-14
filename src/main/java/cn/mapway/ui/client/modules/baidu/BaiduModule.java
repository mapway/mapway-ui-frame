package cn.mapway.ui.client.modules.baidu;

import java.util.Map;

import cn.mapway.ui.client.mvc.AbstractModule;
import cn.mapway.ui.client.mvc.IModule;
import cn.mapway.ui.client.mvc.ModuleInfo;
import cn.mapway.ui.client.mvc.ModuleMarker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

@ModuleMarker(value = BaiduModule.MODULE_CODE, name = "百度模块", summary = "XXXXXX", icon = "help.png")
public class BaiduModule extends AbstractModule {

  public static final String MODULE_CODE = "MC_BAIDU";

  @Override
  public ModuleInfo getModuleInfo() {
    return getModuleFactory().findModuleInfo(MODULE_CODE);
  }

  private static BaiduModuleUiBinder uiBinder = GWT.create(BaiduModuleUiBinder.class);

  interface BaiduModuleUiBinder extends UiBinder<Widget, BaiduModule> {
  }

  public BaiduModule() {
    initWidget(uiBinder.createAndBindUi(this));

    Button btn = new Button("tets");
    btn.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        if (b) {
          tr.getStyle().setHeight(1, Unit.PX);
          b = false;
        } else {
          tr.getStyle().setHeight(20, Unit.PX);
          b = true;
        }
      }
    });
    root.add(btn);

    txt.getElement().setAttribute("placeholder", "DEBUG_ID_PREFIX");

  }

  @Override
  public void initialize(IModule parentModule, Map<String, Object> parameters) {
    super.initialize(parentModule, parameters);
    if (parentModule != null) {
      parentModule.updateTools(new Button("Back"));
    }
  }

  @Override
  public Widget getRootWidget() {
    return this;
  }

  @UiField
  Element tr;

  boolean b = true;


  @UiField
  HTMLPanel root;

  @UiField
  TextBox txt;


}
