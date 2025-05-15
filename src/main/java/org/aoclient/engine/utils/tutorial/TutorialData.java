package org.aoclient.engine.utils.tutorial;

import java.util.List;

public class TutorialData {
    private final List<TutorialPage> pages;
    private int currentPage = 0;

    public TutorialData(List<TutorialPage> pages) {
        this.pages = pages;
    }
    public int getNumPages() { return pages.size(); }
    public int getCurrentPageIndex() { return currentPage; }
    public TutorialPage getCurrentPage() { return pages.get(currentPage); }
    public void nextPage() { if (currentPage < pages.size() - 1) currentPage++; }
    public void prevPage() { if (currentPage > 0) currentPage--; }
    public boolean isFirstPage() { return currentPage == 0; }
    public boolean isLastPage() { return currentPage == pages.size() - 1; }
    public void setPage(int idx) { if (idx >= 0 && idx < pages.size()) currentPage = idx; }
}
