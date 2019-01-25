package io.opentracing.contrib;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class App
{
    static OkHttpClient client = new OkHttpClient();

    static ExecutorService service = Executors.newCachedThreadPool();

    public static void main( String[] args ) throws IOException
    {
        String url = "https://raw.github.com/square/okhttp/master/README.md";

        Future<?> [] futures = new Future<?>[5];
        for (int i = 0; i < futures.length; i++) {
            futures[i] = doRequest(url);
        }

        waitForFutures(futures);
    }

    static Future<?> doRequest(final String url) throws IOException {
        return service.submit(new Runnable() {
            @Override
            public void run() {
                Request req = new Request.Builder()
                    .url(url)
                    .build();

                try {
                    try (Response res = client.newCall(req).execute()) {
                        System.out.println(res.code());
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        });
    }

    static void waitForFutures(Future [] futures) {
        for (int i = 0; i < futures.length; i++) {
            try {
                futures[i].get();
            } catch (InterruptedException e) {
                System.out.println(e);
            } catch (ExecutionException e) {
                System.out.println(e);
            }
        }
    }
}
