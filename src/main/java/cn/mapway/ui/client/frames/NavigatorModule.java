package cn.mapway.ui.client.frames;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mapway.ui.client.modules.common.UnAuthorityModule;
import cn.mapway.ui.client.mvc.AbstractModule;
import cn.mapway.ui.client.mvc.IModule;
import cn.mapway.ui.client.mvc.IModuleDispatcher;
import cn.mapway.ui.client.mvc.ModuleInfo;
import cn.mapway.ui.client.widget.common.DataHolder;
import cn.mapway.ui.client.widget.common.ItemList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ksyzt.gwt.client.event.MessageEvent;
import com.ksyzt.gwt.client.event.MessageHandler;

/**
 * 简单的模块调度器.
 * 
 * @author zhangjianshe
 *
 */
public abstract class NavigatorModule extends AbstractModule implements IModuleDispatcher {

  class UiFieldHolder {
    @UiField
    DockLayoutPanel root;
    @UiField
    ItemList subList;
    @UiField
    ScrollPanel subModules;


    @UiField
    HorizontalPanel tblNavi;


    @UiField
    HorizontalPanel tools;

    @UiField
    Image icon;
  }

  private UiFieldHolder holder;
  /**
   * 子模块
   */
  private List<ModuleInfo> mSubModules;

  /**
   * 注册子模块代码.
   * 
   * @param moduleCode
   */
  final public void registerSubModule(String moduleCode, boolean single) {
    ModuleInfo item = getModuleFactory().findModuleInfo(moduleCode);
    if (item != null) {
      boolean find = false;
      for (ModuleInfo i : mSubModules) {
        if (i.code.equals(item.code)) {
          GWT.log("重复注册子模块" + item.code);
          find = true;
          break;
        }
      }
      if (find == false) {
        mSubModules.add(item.copy().setSingle(single));
      }
    } else {
      GWT.log("没有子模块" + moduleCode + "信息");
    }
  }

  private static NavigatorModuleUiBinder uiBinder = GWT.create(NavigatorModuleUiBinder.class);

  interface NavigatorModuleUiBinder extends UiBinder<Widget, NavigatorModule.UiFieldHolder> {
  }

  private MessageHandler subModuleClicked = new MessageHandler() {

    @Override
    public void onMessage(Object sender, Integer message, Object value) {
      if (message == MessageEvent.ITEMCLICK) {
        DataHolder item = (DataHolder) value;
        ModuleInfo mi = (ModuleInfo) item.getData();
        switchModule(mi.code, null);
      }
    }
  };
  private ClickHandler homeClicked = new ClickHandler() {

    @Override
    public void onClick(ClickEvent event) {
      String code = getModuleInfo().code;
      switchModule(code, null);
    }
  };

  public NavigatorModule() {
    holder = new UiFieldHolder();
    initWidget(uiBinder.createAndBindUi(holder));
    mSubModules = new ArrayList<ModuleInfo>();
    holder.subList.addMessageHandler(subModuleClicked);
  }

  IModule subCurrent;
  Widget currentWidget = null;


  IModule current;


  @Override
  public void initialize(IModule parentModule, Map<String, Object> parameters) {
    super.initialize(parentModule, parameters);

    if (mSubModules == null || mSubModules.size() == 0) {
      // 单模块没有子模块
      // 本模块
      // 处理导航
      ModuleInfo thisModule = getModuleInfo();
      holder.tblNavi.clear();

      Label home = new Label(thisModule.name);
      holder.tblNavi.add(home);
      holder.icon.setUrl(thisModule.icon);

      // 处理模块内容
      removeCurrent();
      currentWidget = getRootWidget();
      holder.root.add(currentWidget);
      holder.root.setWidgetSize(holder.subModules, 0);

    }
  }

  @Override
  public void switchModule(String code, Map<String, Object> parameter) {

    IModule parent = getParentModule();
    if (parameter == null) {
      parameter = new HashMap<String, Object>();
    }

    // 两种情况 本模块 或者 子模块

    boolean isThisModule = false;
    ModuleInfo thisModule = getModuleInfo();
    if (thisModule.code.equals(code)) {
      isThisModule = true;
    }


    if (isThisModule) {
      // 本模块
      // 处理导航
      holder.tblNavi.clear();
      Label home = new Label(thisModule.name);
      holder.tblNavi.add(home);
      holder.icon.setUrl(thisModule.icon);

      // 处理模块内容
      removeCurrent();
      currentWidget = getRootWidget();
      holder.root.add(currentWidget);
      initialize(getParentModule(), parameter);

      // 处理子模块
      if (mSubModules == null || mSubModules.size() == 0) {
        holder.root.setWidgetSize(holder.subModules, 0);
      } else {
        holder.subList.clear();
        for (ModuleInfo item : mSubModules) {
          holder.subList.addItem(item.name, item.summary, item.icon, item);
        }
      }
    } else {

      ModuleInfo mi = null;
      for (ModuleInfo m : mSubModules) {
        if (m.code.equals(code)) {
          mi = m;
          break;
        }
      }

      IModule module = null;
      if (mi == null) {
        // 没有找到子模块
        mi = getModuleFactory().findModuleInfo(UnAuthorityModule.SYS_UNAUTHORITY_MODULE);
        parameter.put(UnAuthorityModule.PARA_MODULE_NAME, code);
        module = getModuleFactory().createModule(UnAuthorityModule.SYS_UNAUTHORITY_MODULE, true);
      } else {
        module = getModuleFactory().createModule(mi.code, true);
      }

      // 处理导航
      holder.tblNavi.clear();
      Anchor home = new Anchor(thisModule.name);
      holder.tblNavi.add(home);
      home.addClickHandler(homeClicked);

      holder.tblNavi.add(new HTML("&gt;"));
      holder.tblNavi.add(new Label(mi.name));
      holder.icon.setUrl(mi.icon);

      // 处理模块内容
      removeCurrent();
      currentWidget = module.getRootWidget();
      holder.root.add(currentWidget);
      module.initialize(this, parameter);

      // 处理子模块
      if (mSubModules == null || mSubModules.size() == 0) {
        holder.root.setWidgetSize(holder.subModules, 0);
      } else {
        // 有子模块
        if (thisModule.code.equals(mi.code)
            || holder.subList.getWidgetCount() != mSubModules.size()) {
          holder.root.setWidgetSize(holder.subModules, 300);
          holder.subList.clear();
          for (ModuleInfo item : mSubModules) {
            holder.subList.addItem(item.name, item.summary, item.icon, item);
          }
        }
      }

    }
  }


  private void removeCurrent() {
    if (currentWidget != null) {
      updateTools();
      currentWidget.removeFromParent();
      currentWidget = null;
    }
  }


  /**
   * 更新按钮情况
   */
  @Override
  public void updateTools(Widget... toolsWidget) {
    super.updateTools(toolsWidget);
    holder.tools.clear();
    for (Widget w : toolsWidget) {
      holder.tools.add(w);
    }
  }


  Widget content = null;

  /**
   * 初始化模块的界面
   * 
   * @param widget
   */
  public void initModuleContent(Widget widget) {

    content = widget;

  }

  @Override
  public Widget getRootWidget() {
    assert content != null : "模块初始化必须调用 iniModuleContent 方法";
    return content;
  }


}
