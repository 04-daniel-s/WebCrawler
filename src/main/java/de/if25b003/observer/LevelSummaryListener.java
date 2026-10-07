package de.if25b003.observer;

import de.if25b003.model.Node;

import java.util.List;

public class LevelSummaryListener implements CrawlListener {

    @Override
    public void onLevelFinished(int depth, List<Node> level, long ms) {
        int links = level.stream().mapToInt(n -> n.getFoundLinks().size()).sum();
        System.out.println("\n===== Ebene " + depth + " abgeschlossen =====");
        System.out.println("Knoten/Threads: " + level.size() + " | neue Links: " + links + " | Dauer: " + ms + " ms");
        level.stream().limit(5).forEach(n ->
                System.out.println("  " + n.getUrl() + " -> " + n.getFoundLinks().size() + " neue Links"));
        if (level.size() > 5) System.out.println("  ... (" + (level.size() - 5) + " weitere)");
        System.out.println();
    }
}
