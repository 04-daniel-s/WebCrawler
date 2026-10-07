package de.if25b003;

import de.if25b003.model.Node;
import de.if25b003.observer.CrawlEvents;
import de.if25b003.util.CrawlConfig;
import de.if25b003.util.DataProcessHelper;
import de.if25b003.util.LinkExtractor;
import de.if25b003.util.VisitedRegistry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CrawlWorker implements Runnable {
    private final Node node;
    private final CrawlConfig config;
    private final LinkExtractor extractor;
    private final VisitedRegistry registry;
    private final CrawlEvents events = CrawlEvents.getInstance();

    @Override
    public void run() {
        events.threadStarted(node);
        try {
            String html = DataProcessHelper.download(node);
            if (html == null || node.getDepth() >= config.maxDepth()) return;

            for (String link : extractor.extract(html, node.getUrl())) {
                boolean isNew = registry.markVisited(link);
                events.linkFound(node, link, !isNew);
                if (isNew) node.addChild(new Node(link, node.getDepth() + 1));
            }
        } finally {
            events.threadFinished(node);
        }
    }
}
