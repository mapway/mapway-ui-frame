package cn.mapway.ui.client.history;

import cn.mapway.ui.client.mvc.BaseAbstractModule;
import cn.mapway.ui.client.mvc.IModuleDispatcher;
import cn.mapway.ui.client.mvc.ModuleInfo;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

/**
 * 历史回退管理器
 * 
 * @author zhangjianshe
 *
 */
public class HistoryManager implements ValueChangeHandler<String> {

  public final static String SEPRATOR = ":";
  private IModuleDispatcher mDispatcher;

  public final static void push(String[] moduleCodes) {
    String r = encode(moduleCodes);
    GWT.log("push history:" + r);
    if (r.length() > 0) {
      History.newItem(r, false);
    }
  }

  private static HistoryManager historyManager = null;

  public final static HistoryManager get(IModuleDispatcher dispatcher) {
    if (historyManager == null) {
      historyManager = new HistoryManager(dispatcher);
      History.addValueChangeHandler(historyManager);
    }
    return historyManager;
  }

  protected HistoryManager(IModuleDispatcher dispatcher) {
    mDispatcher = dispatcher;
  }

  public void popup(String token) {
    String[] modules = decode(token);
    IModuleDispatcher d = mDispatcher;
    if (modules.length > 0) {
      int index = 0;
      while (d != null) {
        if (index >= modules.length) {
          break;
        }
        String hash = modules[index++];
        GWT.log("popup hash>" + hash);
        ModuleInfo info = BaseAbstractModule.getModuleFactory().findModuleInfoByHash(hash);
        if (info != null) {
          d = d.switchModule(info.code, null, false);
        }
      }
    }
  }


  private final static String encode(String... moduleCodes) {
    if (moduleCodes == null || moduleCodes.length == 0) {
      return "";
    }
    String r = "";

    for (int i = 0; i < moduleCodes.length; i++) {
      if (i < moduleCodes.length - 1) {
        r += moduleCodes[i] + ":";
      } else {
        r += moduleCodes[i];
      }
    }
    return r;
  }

  private final static String[] decode(String token) {
    String[] r;
    if (token == null || token.length() == 0) {
      r = new String[0];
    } else {
      r = token.split(":");
    }
    return r;
  }

  @Override
  public void onValueChange(ValueChangeEvent<String> event) {
    String token = event.getValue();
    GWT.log("history navi to " + token);
    if (token == null || token.length() == 0) {
      return;
    }
    popup(token);
  }
}
