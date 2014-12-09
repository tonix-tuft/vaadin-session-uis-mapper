package com.example.application_lifecycle_chapter_4_7;

import javax.servlet.ServletException;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@Theme("application_lifecycle_chapter_4_7")
@Push // setting this UI as pushable
public class ApplicationLifecycleUI extends VaadinAbstractMappableUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@WebServlet(value = {"/AppLife/*", "/VAADIN/*"}, asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = ApplicationLifecycleUI.class)
	public static class Servlet extends VaadinServlet 
								implements SessionInitListener, SessionDestroyListener, 
										   BootstrapListener {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;


		// Handling Session Initialization and Destruction
		@Override 
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();
	        getService().addSessionInitListener(this);
	        getService().addSessionDestroyListener(this);
		}
		
		
	    @Override
	    public void sessionInit(SessionInitEvent event)
	            throws ServiceException {
	        // Do session start stuff here
	    	
	    	// Adding the bootstrap listener to the Session
	    	VaadinSession.getCurrent().addBootstrapListener(this);
	    }

	    @Override
	    public void sessionDestroy(SessionDestroyEvent event) {
	        // Do session end stuff here
	    }


	    // Customizing the Loader Page
		@Override
		public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
			
		}


		@Override
		public void modifyBootstrapPage(BootstrapPageResponse response) {
			
		}

	}

	@SuppressWarnings("serial")
	@Override
	protected void init(VaadinRequest request) {
		
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		
		Button button = new Button("Click Me");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				layout.addComponent(new Label("Thank you for clicking"));
			}
		});
		layout.addComponent(button);
		
		Button logoutButton = new Button("Logout");
		logoutButton.addClickListener(e -> {
			getPage().setLocation("/Application_Lifecycle_Chapter_4.7/VAADIN/logout.html");
			getSession().close();
		});
		// Setting a faster heartbeat
		//setPollInterval(3000);
		layout.addComponent(logoutButton);
	}

}
