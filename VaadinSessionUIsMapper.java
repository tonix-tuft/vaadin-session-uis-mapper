package com.example.application_lifecycle_chapter_4_7;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

/**
 * A global singleton mapper class which maps a Vaadin session (the session id)
 * to its attached UIs. UIs which want to be mappable must extend the
 * VaadinAbstractMappableUI.
 * 
 * @author Anton Bagdatyev
 * @see VaadinSession
 * @see UI
 */
public class VaadinSessionUIsMapper {

    /**
     * @var Map<String, Collection<UI>> The map that maps Vaadin session ids to the
     *      corresponding UIs
     */
    private Map<String, Collection<UI>> sessionUIMap;

    /**
     * @var VaadinSessionUIsMapper The singleton instance
     */
    private static VaadinSessionUIsMapper instance;

    /**
     * Constructs a new VaadinSessionUIsMapper object
     * 
     * @throws NullPointerException If the VaadinSession is not yet initialized
     */
    private VaadinSessionUIsMapper() {
        String session = VaadinSession.getCurrent().getSession().getId();
        Collection<UI> uis = new ArrayList<UI>();
        this.sessionUIMap = new HashMap<String, Collection<UI>>();
        this.sessionUIMap.put(session, uis);
    }

    /**
     * Gets a singleton instance of this mapper
     * 
     * @return VaadinSessionUIsMapper The mapper
     */
    public static VaadinSessionUIsMapper getInstance() {
        if (instance == null) {
            instance = new VaadinSessionUIsMapper();
        }
        return instance;
    }

    /**
     * Associate a given Vaadin session to the given UI. If the same UI already
     * existed inside the map, the old UI is replaced with the new.
     * 
     * @param sessId {@link String} The session id
     * @param ui     {@link UI} the ui to associate
     * @throws IllegalArgumentException If the given ui doesn't extend the
     *                                  VaadinAbstractMappableUI or if super is not
     *                                  called during UI's construction
     * @throws ClassCastException       If the given ui parameter is not an
     *                                  AbstractMappableUI Vaadin UI
     */
    public synchronized void putUI(String sessId, VaadinAbstractMappableUI ui) {
        if (!(ui instanceof VaadinAbstractMappableUI)) {
            throw new IllegalArgumentException(String.format(
                    "The given UI instance of class %s is not "
                            + "a VaadinAbstractMappableUI (it doesn't extend the VaadinAbstractMappableUI class)",
                    ui.getClass().getName()));
        }

        VaadinAbstractMappableUI mappableUI = (VaadinAbstractMappableUI) ui;
        if (mappableUI.isOverriddenBad()) {
            throw new IllegalArgumentException(String.format(
                    "The given UI instance of class %s has not called super " + " in its constructor overridings",
                    ui.getClass().getName()));
        }

        Collection<UI> cuis = this.getUIs(sessId);

        if (cuis.contains(ui)) {
            for (UI cui : cuis) {
                if (cui.getClass().getName().equals(ui.getClass().getName())) {
                    System.out.println(
                            "Removing UI with class: " + cui.getClass().getName() + " for session ID = " + sessId);
                    cuis.remove(cui);
                    cuis.add((UI) ui);
                    System.out.println(
                            "Adding UI to Session with ID: " + sessId + ", UI class = " + ui.getClass().getName());
                }
            }
        } else {
            System.out
                    .println("New UI added to Session with ID: " + sessId + ", UI class = " + ui.getClass().getName());
            cuis.add((UI) ui);
        }
        System.out.println("Current UIs for session: " + cuis.toString());
    }

    /**
     * Associates the given Vaadin session (the session id) to the given UI
     * collection. If the mapping contained a previous collection for the given
     * Vaadin key, the old collection is replaced.
     * 
     * @param sessId {@link String} The session id
     * @param uis    {@link Collection}<UI> The collection of UI to associate to the
     *               session id
     */
    public synchronized void putUIs(String sessId, Collection<UI> uis) {
        this.sessionUIMap.put(sessId, uis);
    }

    /**
     * Gets a collection of Vaadin UIs belonging to the given Vaadin session (the
     * session id)
     * 
     * @param sessId {@link String} The session id
     * @return Collection<UI> The UI collection
     */
    public synchronized Collection<UI> getUIs(String sessId) {
        if (!this.sessionUIMap.containsKey(sessId))
            this.sessionUIMap.put(sessId, new ArrayList<UI>());
        return this.sessionUIMap.get(sessId);
    }

    /**
     * Removes the mapping of the UIs based on the given session id
     * 
     * @param sessId {@link String} The session id
     */
    public void remove(String sessId) {
        this.sessionUIMap.remove(sessId);
    }

    /**
     * Removes the given UI from the given session id mapping
     * 
     * @param sessId {@link String} The session id
     * @param ui     {@link VaadinAbstractMappableUI} The UI to remove from the
     *               mapping
     */
    public void removeUI(String sessId, VaadinAbstractMappableUI ui) {
        this.sessionUIMap.get(sessId).remove(ui);
    }
}
