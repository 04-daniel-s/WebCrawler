package de.if25b003.observer;

import de.if25b003.model.Node;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LiveStatsListener implements CrawlListener {

    private final long startTime = System.currentTimeMillis();
    private final AtomicInteger pages = new AtomicInteger();
    private final AtomicInteger errors = new AtomicInteger();
    private final AtomicInteger links = new AtomicInteger();
    private final AtomicInteger duplicates = new AtomicInteger();
    private final AtomicInteger active = new AtomicInteger();
    private final AtomicInteger totalThreads = new AtomicInteger();
    private final AtomicLong bytes = new AtomicLong();
    private final AtomicLong lastPrint = new AtomicLong();

    @Getter
    private volatile int currentDepth = 0;

    public double getElapsedSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000.0;
    }

    @Override
    public void onLevelStarted(int d, int n) {
        currentDepth = d;
        printNow();
    }

    @Override
    public void onThreadStarted(Node n) {
        active.incrementAndGet();
        totalThreads.incrementAndGet();
        throttledPrint();
    }

    @Override
    public void onThreadFinished(Node n) {
        active.decrementAndGet();
        throttledPrint();
    }

    @Override
    public void onPageDownloaded(Node n, int b) {
        pages.incrementAndGet();
        bytes.addAndGet(b);
        throttledPrint();
    }

    @Override
    public void onError(Node n) {
        errors.incrementAndGet();
        throttledPrint();
    }

    @Override
    public void onLinkFound(Node n, String l, boolean dup) {
        links.incrementAndGet();
        if (dup) duplicates.incrementAndGet();
        throttledPrint();
    }

    @Override
    public void onLevelFinished(int depth, List<Node> level, long ms) {
        printNow();
    }

    private void throttledPrint() {
        long now = System.currentTimeMillis();
        long last = lastPrint.get();
        if (now - last >= 500 && lastPrint.compareAndSet(last, now)) printNow();
    }

    public void printNow() {
        System.out.printf("[LIVE %5.1fs] Tiefe=%d | Threads aktiv=%d (gesamt %d) | Seiten=%d | Links=%d | Duplikate=%d | Fehler=%d | %d KB%n",
                getElapsedSeconds(), getCurrentDepth(), getActiveThreads(), getTotalThreads(),
                getPages(), getLinks(), getDuplicates(), getErrors(), getBytes() / 1024);
    }

    public int getPages() {
        return pages.get();
    }

    public int getErrors() {
        return errors.get();
    }

    public int getLinks() {
        return links.get();
    }

    public int getDuplicates() {
        return duplicates.get();
    }

    public int getActiveThreads() {
        return active.get();
    }

    public int getTotalThreads() {
        return totalThreads.get();
    }

    public long getBytes() {
        return bytes.get();
    }
}