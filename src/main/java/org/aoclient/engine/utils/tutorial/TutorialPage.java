package org.aoclient.engine.utils.tutorial;

public class TutorialPage {
    private final String title;
    private final String text;

    public TutorialPage(String title, String text) {
        this.title = title;
        this.text = text;
    }
    public String getTitle() { return title; }
    public String getText() { return text; }
}
