package cn.mapway.ui.client.mvc;

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
public abstract class AbstractModule extends MessageComposite implements IModule {


  private IModule mParentModule;

  public AbstractModule() {}


  private final static ModuleFactory FACTORY = GWT.create(ModuleFactory.class);

  /**
   * 模块工厂
   * 
   * @return
   */
  public ModuleFactory getModuleFactory() {
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

  }

  @Override
  public IModule getParentModule() {
    return mParentModule;
  }
}
