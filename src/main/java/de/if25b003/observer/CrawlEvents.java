package de.if25b003.observer;

import de.if25b003.model.Node;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CrawlEvents {
    private final List<CrawlListener> listeners;

    private static CrawlEvents instance;

    public CrawlEvents() {
        listeners = new CopyOnWriteArrayList<>();
    }

    public void register(CrawlListener l) {
        listeners.add(l);
    }

    public void levelStarted(int d, int n) {
        listeners.forEach(l -> l.onLevelStarted(d, n));
    }

    public void threadStarted(Node n) {
        listeners.forEach(l -> l.onThreadStarted(n));
    }

    public void threadFinished(Node n) {
        listeners.forEach(l -> l.onThreadFinished(n));
    }

    public void pageDownloaded(Node n, int bytes) {
        listeners.forEach(l -> l.onPageDownloaded(n, bytes));
    }

    public void error(Node n) {
        listeners.forEach(l -> l.onError(n));
    }

    public void linkFound(Node n, String s, boolean d) {
        listeners.forEach(l -> l.onLinkFound(n, s, d));
    }

    public void levelFinished(int d, List<Node> lv, long ms) {
        listeners.forEach(l -> l.onLevelFinished(d, lv, ms));
    }

    public static CrawlEvents getInstance() {
        if (instance == null) {
            instance = new CrawlEvents();
        }

        return instance;
    }
}
