package cn.mapway.ui.client.test;

import cn.mapway.ui.client.history.HistoryManager;
import cn.mapway.ui.client.modules.test.FuckTest;
import cn.mapway.ui.client.modules.test.TestSubModule;
import cn.mapway.ui.client.mvc.IModuleDispatcher;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class TestEntry implements EntryPoint {


  IModuleDispatcher d;

  @Override
  public void onModuleLoad() {



    final TestSubModule test = new TestSubModule();
    final FuckTest dingle = new FuckTest();

    final HorizontalPanel p = new HorizontalPanel();
    final HTMLPanel con = new HTMLPanel("");

    d = test;
    HistoryManager his = HistoryManager.get(d);

    RootPanel.get().add(p);
    RootPanel.get().add(con);
    p.setSpacing(5);

    Button btnMul = new Button("多模块");
    p.add(btnMul);
    btnMul.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        con.clear();
        con.add(test);
        test.setSize("800px", "600px");
        test.initialize(null, null);
        test.switchModule("MC_DAYWO", null, true);
      }
    });

    Button btnMul1 = new Button("单模块");
    p.add(btnMul1);
    btnMul1.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        con.clear();
        con.add(dingle);
        dingle.setSize("800px", "600px");
        dingle.initialize(null, null);
      }
    });
  }
}
