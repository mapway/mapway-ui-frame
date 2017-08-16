package cn.mapway.ui.client.mvc;

import java.util.Map;

/**
 * 模块调度器
 * 
 * @author zhangjianshe
 *
 */
public interface IModuleDispatcher {

  /**
   * 切换模块.
   * 
   * @param code
   * @param parameter
   * @return
   */
  IModuleDispatcher switchModule(String code, Map<String, Object> parameter, boolean saveToHistory);
}
