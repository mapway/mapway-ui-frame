package cn.mapway.ui.client.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.ksyzt.gwt.client.common.MessageComposite;

/**
 * 模块基类.
 * 
 * @author zhangjianshe
 *
 */
public abstract class BaseAbstractModule extends MessageComposite implements IModule {


  private IModule mParentModule;

  public BaseAbstractModule() {}


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
  public void initialize(IModule parentModule, Map<String, Object> parameters) {
    mParentModule = parentModule;
  }

  @Override
  public void unInitialize() {

  }



  @Override
  public void updateTools(Widget... tools) {
    if (mParentModule != null) {
      mParentModule.updateTools(tools);
    }
  }

  @Override
  public IModule getParentModule() {
    return mParentModule;
  }

  @Override
  public ModuleInfo getModuleInfo() {
    return getModuleFactory().findModuleInfo(getModuleCode());
  }

  /**
   * 获取模块代码
   * 
   * @return
   */
  public abstract String getModuleCode();

  public void initModuleWidget(Widget w) {
    initWidget(w);

  }

  @Override
  public Widget getRootWidget() {
    return this;
  }

  private List<IModule> getModuleStack(IModule module) {
    List<IModule> modules = new ArrayList<IModule>();

    IModule p = module;
    while (p != null) {
      modules.add(p);
      p = p.getParentModule();
    }
    return modules;
  }

  public String[] getModulePath(IModule module) {
    List<IModule> modules = getModuleStack(module);
    String[] ms = new String[modules.size()];
    int index = modules.size() - 1;
    for (IModule m : modules) {
      ms[index--] = m.getModuleInfo().hash;
    }
    return ms;
  }


}
