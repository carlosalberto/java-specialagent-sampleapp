package io.opentracing.contrib;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class App
{
  final static int DEFAULT_TOTAL_REQUESTS = 30;

  final static String [] URLS = new String[] {
    "https://raw.github.com/square/okhttp/master/README.md",
    "https://raw.github.com/open-telemetry/opentelemetry-specification/main/README.md",
    "https://raw.github.com/open-telemetry/opentelemetry-java/main/README.md",
    "https://raw.github.com/open-telemetry/opentelemetry-python/main/README.md",
    "https://raw.github.com/open-telemetry/opentelemetry-go/main/README.md",
    "https://raw.github.com/open-telemetry/opentelemetry-js/main/README.md",

    // Incorrect urls
    "https://raw.github.com/open-telemetry/specification/main/README.md",
    "https://raw.github.com/open-telemetry/contrib/main/README.md"
  };

  static OkHttpClient client = new OkHttpClient.Builder().build();
  static ExecutorService service = Executors.newCachedThreadPool();
  static Random rand = new Random();

  public static void main( String[] args ) throws IOException
  {
    Future<?> [] futures = new Future<?>[DEFAULT_TOTAL_REQUESTS];
    for (int i = 0; i < futures.length; i++) {
      futures[i] = doRequest(URLS[rand.nextInt(URLS.length)]);
    }

    System.out.println("Running requests, press any key to stop...");
    System.in.read();
    System.out.println("Shutting down");

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
