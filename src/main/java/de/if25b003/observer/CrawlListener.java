package de.if25b003.observer;

import de.if25b003.model.Node;

import java.util.List;

interface CrawlListener {
    default void onLevelStarted(int depth, int nodeCount) {}
    default void onThreadStarted(Node node) {}
    default void onThreadFinished(Node node) {}
    default void onPageDownloaded(Node node, int bytes) {}
    default void onError(Node node) {}
    default void onLinkFound(Node node, String link, boolean duplicate) {}
    default void onLevelFinished(int depth, List<Node> level, long millis) {}
}