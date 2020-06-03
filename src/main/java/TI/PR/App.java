package TI.PR;

import okhttp3.*;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

public class App {

    App() {
        SocketAddress address = new InetSocketAddress("31.14.131.70", 8080);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
        OkHttpClient client = new OkHttpClient.Builder().proxy(proxy).build();
        head(client);
    }
    public static void main(String[] args ) {
        new App();
    }
    public void get(OkHttpClient client){
        Request request = new Request.Builder()
                .url("http://api.plos.org/search?q=title:DNA")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            String s = response.body().string();
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void post(OkHttpClient client){
        RequestBody formBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();
        Request request = new Request.Builder()
                .url("https://en.wikipedia.org/w/index.php")
                .post(formBody)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!response.isSuccessful()) try {
            throw new IOException("Unexpected code " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void options(OkHttpClient client){
        RequestBody body = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

            }
        };
        Request request = new Request.Builder()
                .url("http://unite.md/")
                .method("OPTIONS", body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void head(OkHttpClient client){
        Request request = new Request.Builder()
                .url("https://api.github.com/repos/square/okhttp/issues")
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json; q=0.5")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println("Server: " + response.header("Server"));
            System.out.println("Date: " + response.header("Date"));
            System.out.println("Vary: " + response.headers("Vary"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
