package com.example.application_lifecycle_chapter_4_7;

import com.vaadin.annotations.Push;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

/**
 * A base abstract class which provides the basic mapping for the current UI
 * using th {@link VaadinSessionUIsMapper} singleton class.
 * 
 * @author Anton Bagdatyev
 * @see VaadinMappableUI
 * @see VaadinSessionUIsMapper
 */
@Push
public abstract class VaadinAbstractMappableUI extends UI {

    private static final long serialVersionUID = 1L;

    /**
     * A boolean which tells whether the class was overridden in a bad way (i.e. the
     * constructor didn't call the super implementation). The default value is false
     * (we suppose the user didn't call super).
     * 
     * @var overriddenBad boolean
     */
    private boolean overriddenBad = true;

    /**
     * Constructs a new mappable UI
     */
    public VaadinAbstractMappableUI() {
        super();
        initMapping(this);
    }

    /**
     * Constructs a new mappable UI
     * 
     * @param content The content of the UI
     */
    public VaadinAbstractMappableUI(Component content) {
        super(content);
        initMapping(this);
    }

    /**
     * Initializes the mapping for this mappableUI
     * 
     * @param ui The mapped UI
     */
    protected final void initMapping(VaadinAbstractMappableUI ui) {
        overriddenBad = false;
        String sessionId = VaadinSession.getCurrent().getSession().getId();
        VaadinSessionUIsMapper.getInstance().putUI(sessionId, this);
    }

    /**
     * Gets whether the child class has overridden this class in a bad way (without
     * calling the super implementation)
     * 
     * @return boolean True if the class has called the super constructor, false
     *         otherwise.
     */
    public boolean isOverriddenBad() {
        return overriddenBad;
    }

    @Override
    public void detach() {
        // DO NOT DO THIS!!! The session here is already null
        /*
         * String sessId = VaadinSession.getCurrent().getSession().getId();
         * 
         * VaadinSessionUIsMapper.getInstance().removeUI(sessId, this);
         */
    }
}
