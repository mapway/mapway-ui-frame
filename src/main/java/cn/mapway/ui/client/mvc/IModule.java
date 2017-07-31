package cn.mapway.ui.client.mvc;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;

/**
 * 系统中模块的定义接口，根据设计原则，可以将代码划分为 系统/模块/子模块/操作 四个界别<br/>
 * 这个接口定义了如果要满足 模块和子模块的功能必须要实现的接口
 * 
 * @author zhangjianshe
 *
 */
public interface IModule {
  /**
   * 初始化模块.
   *
   * @param parameters the parameters
   * @param context the context
   */
  void initialize(Map<String, Object> parameters);

  /**
   * 卸载模块.
   */
  void unInitialize();

  /**
   * 返回模块的Widget.
   *
   * @return the root widget
   */
  Widget getRootWidget();

  /**
   * 子模块代码，返回null 或者 count=0 表示没有子模块
   * 
   * @return
   */
  List<ModuleItem> getSubModuleCodes();

  /**
   * 获取模块的<b>操作</b>界面
   * 
   * @return
   */
  List<Widget> getTools();


}
