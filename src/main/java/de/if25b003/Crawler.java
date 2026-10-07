package de.if25b003;

import de.if25b003.model.Node;
import de.if25b003.observer.CrawlEvents;
import de.if25b003.util.CrawlConfig;
import de.if25b003.util.DataProcessHelper;
import de.if25b003.util.LinkExtractor;
import de.if25b003.util.VisitedRegistry;

import java.util.ArrayList;
import java.util.List;

public class Crawler {
    private final CrawlConfig config;
    private final LinkExtractor extractor = new LinkExtractor();
    private final VisitedRegistry registry = new VisitedRegistry();
    private final CrawlEvents events = CrawlEvents.getInstance();

    public Crawler(CrawlConfig config) {
        this.config = config;
    }

    public Node crawl() throws InterruptedException {
        String start = DataProcessHelper.normalize(config.startUrl());
        registry.markVisited(start);
        Node root = new Node(start, 0);
        List<Node> level = List.of(root);

        while (!level.isEmpty() && level.getFirst().getDepth() <= config.maxDepth()) {
            int depth = level.getFirst().getDepth();
            long levelStart = System.currentTimeMillis();
            events.levelStarted(depth, level.size());

            runLevel(level, depth);

            events.levelFinished(depth, level, System.currentTimeMillis() - levelStart);
            level = collectChildren(level);
        }
        return root;
    }

    private void runLevel(List<Node> level, int depth) throws InterruptedException {
        List<Thread> threads = new ArrayList<>(level.size());
        for (Node node : level) {
            Runnable worker = new CrawlWorker(node, config, extractor, registry);
            threads.add(Thread.ofVirtual().name("crawl-d" + depth).unstarted(worker));
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
    }

    private List<Node> collectChildren(List<Node> level) {
        List<Node> next = new ArrayList<>();
        for (Node n : level) next.addAll(n.getChildren());
        return next;
    }
}
