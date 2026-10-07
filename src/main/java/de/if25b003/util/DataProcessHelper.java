package de.if25b003.util;

import de.if25b003.model.Node;
import de.if25b003.observer.CrawlEvents;
import lombok.experimental.UtilityClass;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Semaphore;

@UtilityClass
public class DataProcessHelper {

    private static final int MAX_PARALLEL_REQUESTS = 8;

    private static final Semaphore requestLimiter = new Semaphore(MAX_PARALLEL_REQUESTS);

    private static final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static String resolve(String base, String href) {
        try {
            href = href.trim();
            if (href.startsWith("#") || href.startsWith("javascript:") || href.startsWith("mailto:"))
                return null;
            URI uri = URI.create(base).resolve(href);
            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equals("http") || scheme.equals("https"))) return null;

            return normalize(uri.toString());
        } catch (Exception ignored) {
            return null;
        }
    }

    public static String normalize(String url) {
        int hash = url.indexOf('#');
        if (hash >= 0) url = url.substring(0, hash);
        if (url.endsWith("/")) url = url.substring(0, url.length() - 1);
        return url.replaceFirst("^http://", "https://");
    }

    public static String download(Node node) {
        CrawlEvents events = CrawlEvents.getInstance();

        try {
            requestLimiter.acquire();
            try {
                HttpRequest req = HttpRequest.newBuilder(URI.create(node.getUrl()))
                        .timeout(Duration.ofSeconds(15))
                        .header("User-Agent", "StudentCrawler/1.0 (university assignment)")
                        .GET().build();
                HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                if (resp.statusCode() != 200) {
                    events.error(node);
                    return null;
                }
                events.pageDownloaded(node, resp.body().length());
                return resp.body();
            } finally {
                requestLimiter.release();
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            return null;
        } catch (Exception ignored) {
            events.error(node);
            return null;
        }
    }
}
