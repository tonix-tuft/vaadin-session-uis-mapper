package com.example.application_lifecycle_chapter_4_7;

import java.util.Collection;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Push
@Theme("application_lifecycle_chapter_4_7")
public class MyPushyUI extends VaadinAbstractMappableUI {

    @WebServlet(value = { "/MyPushyUI/*" }, asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyPushyUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        setContent(new Button("Logout", event -> {// Java 8
            String sessId = VaadinSession.getCurrent().getSession().getId();
            Collection<UI> mappedUIs = VaadinSessionUIsMapper.getInstance().getUIs(sessId);

            for (UI ui : mappedUIs) {
                ui.access(() -> { // Runnable implementation with a lambda expression
                    // Redirect from the page
                    System.out.println("UI class = " + ui.getClass().toString());
                    ui.getPage().setLocation("https://vaadin.com/forum");
                    ui.push();
                    VaadinSessionUIsMapper.getInstance().removeUI(sessId, (VaadinAbstractMappableUI) ui);
                });
            }
            try {
                // Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getSession().close();
        }));
    }
}
