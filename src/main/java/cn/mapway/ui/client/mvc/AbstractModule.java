package cn.mapway.ui.client.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.ksyzt.gwt.client.common.MessageComposite;

/**
 * 模块基类.
 * 
 * @author zhangjianshe
 *
 */
public abstract class AbstractModule extends MessageComposite implements IModule {

  /**
   * 子模块
   */
  private List<ModuleItem> mSubModules;
  private List<Widget> mTools;

  public AbstractModule() {
    mSubModules = new ArrayList<ModuleItem>();
    mTools = new ArrayList<Widget>();
  }

  private final static ModuleFactory FACTORY = GWT.create(ModuleFactory.class);

  /**
   * 模块工厂
   * 
   * @return
   */
  public static ModuleFactory getModuleFactory() {
    return FACTORY;
  }

  @Override
  public void initialize(Map<String, Object> parameters) {

  }

  @Override
  public void unInitialize() {

  }



  @Override
  public List<ModuleItem> getSubModuleCodes() {
    return mSubModules;
  }

  /**
   * 注册子模块代码.
   * 
   * @param moduleCode
   */
  final public void registerSubModule(String moduleCode) {
    registerSubModule(moduleCode, false);
  }

  /**
   * 注册子模块代码.
   * 
   * @param moduleCode
   */
  final public void registerSubModule(String moduleCode, boolean single) {
    ModuleItem item = FACTORY.findModuleInfo(moduleCode);
    if (item != null) {
      boolean find = false;
      for (ModuleItem i : mSubModules) {
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

  @Override
  public List<Widget> getTools() {
    return mTools;
  }

  /**
   * 注册操作按钮.
   * 
   * @param tools
   */
  final public void registerTools(Widget tools) {
    mTools.add(tools);
  }

}
