package de.if25b003.util;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkExtractor {
    private final Pattern LINK_PATTERN =
            Pattern.compile("<a\\s+[^>]*?href\\s*=\\s*[\"']([^\"']+)[\"']",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public Set<String> extract(String html, String baseUrl) {
        Set<String> links = new LinkedHashSet<>();
        Matcher m = LINK_PATTERN.matcher(html);
        while (m.find()) {
            String link = DataProcessHelper.resolve(baseUrl, m.group(1));
            if (link != null) links.add(link);
        }
        return links;
    }
}