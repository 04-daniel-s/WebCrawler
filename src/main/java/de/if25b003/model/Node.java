package de.if25b003.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class Node {
    @Getter
    private final String url;

    @Getter
    private final int depth;

    private final List<String> foundLinks = new ArrayList<>();
    private final List<Node> children = new ArrayList<>();

    public List<String> getFoundLinks() {
        return Collections.unmodifiableList(foundLinks);
    }

    public List<Node> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void addChild(Node child) {
        foundLinks.add(child.getUrl());
        children.add(child);
    }
}