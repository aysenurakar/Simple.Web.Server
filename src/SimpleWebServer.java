import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SimpleWebServer {

    private static final String AD_SOYAD = "Ayşenur Akar";
    private static final String OGRENCI_NO ="1240505038";
    private static final int PORT = 1989;

    public static void main(String[] args) {
        System.out.println("Basit Web Sunucu başlatılıyor... Port: " + PORT);
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Hazır! Tarayıcıdan http://localhost:" + PORT + "/ adresine gidin.");

            while (true) {
                Socket client = server.accept();
                new Thread(() -> handleClient(client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket client) {
        try (client;
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
             OutputStream rawOut = client.getOutputStream();
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(rawOut, StandardCharsets.UTF_8))) {

            String line;
            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;
            while ((line = in.readLine()) != null && !line.isEmpty()) { }

            String html = buildHtml();
            byte[] body = html.getBytes(StandardCharsets.UTF_8);

            String statusLine = "HTTP/1.1 200 OK\r\n";
            String headers =
                    "Date: " + httpDate() + "\r\n" +
                            "Server: SimpleWebServer/1.0\r\n" +
                            "Content-Type: text/html; charset=UTF-8\r\n" +
                            "Content-Length: " + body.length + "\r\n" +
                            "Connection: close\r\n\r\n";

            out.write(statusLine);
            out.write(headers);
            out.flush();
            rawOut.write(body);
            rawOut.flush();

        } catch (IOException ignored) {}
    }

    private static String buildHtml() {
        return "<!DOCTYPE html>\n" +
                "<html lang='tr'>\n" +
                "<head>\n" +
                "  <meta charset='UTF-8'>\n" +
                "  <meta name='viewport' content='width=device-width, initial-scale=1'>\n" +
                "  <title>Java Web Sunucusu</title>\n" +
                "  <style>\n" +
                "    :root{--bg1:#0f172a;--bg2:#111827;--card:#1f2937;--fg:#e5e7eb;--muted:#9ca3af;--accent:#60a5fa;--accent2:#34d399}\n" +
                "    *{box-sizing:border-box}\n" +
                "    body{margin:0;min-height:100vh;display:grid;place-items:center;background:linear-gradient(135deg,var(--bg1),var(--bg2));color:var(--fg);font-family:Segoe UI, Roboto, Arial, sans-serif}\n" +
                "    .wrap{width:min(880px,92%);padding:24px}\n" +
                "    .card{background:var(--card);border-radius:18px;box-shadow:0 10px 30px rgba(0,0,0,.25);padding:36px 28px}\n" +
                "    header{display:flex;flex-direction:column;gap:10px;align-items:center;margin-bottom:18px}\n" +
                "    h1{margin:0;font-size:clamp(28px,4.2vw,40px);letter-spacing:.3px;color:var(--accent)}\n" +
                "    h2{margin:0;font-size:clamp(18px,2.2vw,22px);font-weight:600;color:var(--accent2)}\n" +
                "    .meta{margin-top:6px;font-size:14px;color:var(--muted)}\n" +
                "    .divider{height:1px;background:linear-gradient(90deg,transparent,rgba(255,255,255,.15),transparent);margin:18px 0}\n" +
                "    .bio{line-height:1.7;font-size:17px;color:var(--fg);background:rgba(255,255,255,.03);border-left:4px solid var(--accent);padding:14px 16px;border-radius:10px}\n" +
                "    .bio strong{color:#fff}\n" +
                "    .tags{display:flex;flex-wrap:wrap;gap:8px;margin-top:14px}\n" +
                "    .tag{font-size:12px;padding:6px 10px;border-radius:999px;background:rgba(96,165,250,.12);color:#dbeafe;border:1px solid rgba(96,165,250,.35)}\n" +
                "    footer{margin-top:18px;text-align:center;font-size:12px;color:var(--muted)}\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <main class='wrap'>\n" +
                "    <section class='card'>\n" +
                "      <header>\n" +
                "        <h1>" + AD_SOYAD + "</h1>\n" +
                "        <h2>Öğrenci No: " + OGRENCI_NO + "</h2>\n" +
                "        <div class='meta'>Basit Java Socket tabanlı HTTP sunucu örneği</div>\n" +
                "      </header>\n" +
                "      <div class='divider'></div>\n" +
                "      <section class='bio'>\n" +
                "        <p><strong>Merhaba!</strong> Ben " + AD_SOYAD + ". Web teknolojileri ve yazılım geliştirme alanlarıyla ilgileniyorum. \n" +
                "        Şu anda <strong>Kırklareli Üniversitesi</strong> <em>Yazılım Mühendisliği</em> <strong>2. sınıf</strong> öğrencisiyim. \n" +
                "        Bu sayfa, Java'nın <em>ServerSocket</em> ve <em>Socket</em> API'leri kullanılarak yazılan küçük bir HTTP sunucusu ile tarayıcıya dinamik HTML içerik gönderme mantığını göstermektedir.</p>\n" +
                "        <div class='tags'>\n" +
                "          <span class='tag'>Java</span>\n" +
                "          <span class='tag'>HTTP</span>\n" +
                "          <span class='tag'>Socket</span>\n" +
                "          <span class='tag'>HTML &amp; CSS</span>\n" +
                "        </div>\n" +
                "      </section>\n" +
                "      <footer>Server: SimpleWebServer/1.0 • " + httpDate() + "</footer>\n" +
                "    </section>\n" +
                "  </main>\n" +
                "</body>\n" +
                "</html>";
    }





    private static String httpDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        return fmt.format(new Date());
    }
}