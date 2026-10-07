package de.if25b003.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class VisitedRegistry {
    private final Set<String> visited = ConcurrentHashMap.newKeySet();

    public boolean markVisited(String url) {
        return visited.add(url);
    }

    public int size() {
        return visited.size();
    }
}