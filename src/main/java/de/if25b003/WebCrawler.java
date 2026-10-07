package de.if25b003;

import de.if25b003.model.Node;
import de.if25b003.observer.CrawlEvents;
import de.if25b003.observer.LevelSummaryListener;
import de.if25b003.observer.LiveStatsListener;
import de.if25b003.util.CrawlConfig;


public class WebCrawler {

    static void main() throws Exception {
        CrawlEvents events = CrawlEvents.getInstance();
        LiveStatsListener stats = new LiveStatsListener();
        events.register(stats);
        events.register(new LevelSummaryListener());

        CrawlConfig config = new CrawlConfig("https://de.wikipedia.org/wiki/Tasse", 3);
        Node root = new Crawler(config).crawl();

        stats.printNow();
        System.out.println("\n=== Crawl beendet ===");
        System.out.println("Baumstruktur:");
        printTree(root, 0, config.maxDepth());
    }

    private static void printTree(Node n, int indent, int maxIndent) {
        System.out.println("  ".repeat(indent) + "- " + n.getUrl());
        if (indent >= maxIndent) return;
        for (Node c : n.getChildren()) printTree(c, indent + 1, maxIndent);
    }
}