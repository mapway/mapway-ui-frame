package cn.mapway.ui.client.frames;

import java.util.List;
import java.util.Map;

import cn.mapway.ui.client.mvc.AbstractModule;
import cn.mapway.ui.client.mvc.IModule;
import cn.mapway.ui.client.mvc.IModuleDispatcher;
import cn.mapway.ui.client.mvc.ModuleItem;
import cn.mapway.ui.client.widget.common.DataHolder;
import cn.mapway.ui.client.widget.common.ItemList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ksyzt.gwt.client.common.MessageComposite;
import com.ksyzt.gwt.client.event.MessageEvent;
import com.ksyzt.gwt.client.event.MessageHandler;

/**
 * 简单的模块调度器.
 * 
 * @author zhangjianshe
 *
 */
public class SimpleModuleDispatcher extends MessageComposite implements IModuleDispatcher {

  private static SimpleModuleDispatcherUiBinder uiBinder = GWT
      .create(SimpleModuleDispatcherUiBinder.class);

  interface SimpleModuleDispatcherUiBinder extends UiBinder<Widget, SimpleModuleDispatcher> {
  }

  private MessageHandler subModuleClicked = new MessageHandler() {

    @Override
    public void onMessage(Object sender, Integer message, Object value) {
      if (message == MessageEvent.ITEMCLICK) {
        DataHolder item = (DataHolder) value;
        switchSubModule((ModuleItem) item.getData(), null);
      }
    }
  };
  private ClickHandler homeClicked = new ClickHandler() {

    @Override
    public void onClick(ClickEvent event) {
      // 处理Tools
      List<Widget> mtools = current.getTools();
      tools.clear();
      for (Widget w : mtools) {
        tools.add(w);
      }

      // 将主模块加载到Center中
      removeCurrent();
      currentWidget = current.getRootWidget();
      root.add(currentWidget);
    }
  };

  public SimpleModuleDispatcher() {
    initWidget(uiBinder.createAndBindUi(this));
    subList.addMessageHandler(subModuleClicked);
    lbTitle.addClickHandler(homeClicked);
  }

  IModule subCurrent;
  Widget currentWidget = null;

  protected void switchSubModule(ModuleItem item, Map<String, Object> parameter) {
    IModule module = AbstractModule.getModuleFactory().createModule(item.code, item.single);
    if (subCurrent != null) {
      if (subCurrent.equals(module)) {
        module.initialize(parameter);
        return;
      }
    }

    subCurrent = module;
    removeCurrent();
    currentWidget = subCurrent.getRootWidget();
    root.add(currentWidget);
    module.initialize(parameter);

    // 处理Tools
    List<Widget> mtools = subCurrent.getTools();
    tools.clear();
    for (Widget w : mtools) {
      tools.add(w);
    }
  }

  IModule current;

  @Override
  public void switchModule(String code, Map<String, Object> parameter) {
    ModuleItem mi = AbstractModule.getModuleFactory().findModuleInfo(code);
    IModule module = AbstractModule.getModuleFactory().createModule(code, true);
    if (current != null) {
      if (current.equals(module)) {
        module.initialize(parameter);
        return;
      }
    }

    lbTitle.setText(mi.name);
    lbSummary.setText(mi.summary);
    icon.setUrl(mi.icon);

    current = module;
    // 处理Tools
    List<Widget> mtools = current.getTools();
    tools.clear();
    for (Widget w : mtools) {
      tools.add(w);
    }

    // 将主模块加载到Center中
    removeCurrent();
    currentWidget = current.getRootWidget();
    root.add(currentWidget);

    // 处理子模块
    List<ModuleItem> subs = current.getSubModuleCodes();
    if (subs == null || subs.size() == 0) {
      root.setWidgetSize(subModules, 0);


    } else {
      // 有子模块
      root.setWidgetSize(subModules, 300);
      subList.clear();

      for (ModuleItem item : subs) {
        subList.addItem(item.name, item.summary, item.icon, item);
      }
    }
  }

  private void removeCurrent() {
    if (currentWidget != null) {
      currentWidget.removeFromParent();
      currentWidget = null;
    }
  }

  @UiField
  DockLayoutPanel root;
  @UiField
  ItemList subList;
  @UiField
  ScrollPanel subModules;

  @UiField
  Label lbTitle;

  @UiField
  Label lbSummary;

  @UiField
  HorizontalPanel tools;

  @UiField
  Image icon;
}
