package camp.nextstep;

import com.interface21.context.annotation.ComponentScan;
import java.io.IOException;
import java.util.stream.Stream;

@ComponentScan
public class Application {

    private static final int DEFAULT_PORT = 8080;

    public static void main(final String[] args) throws Exception {
        final int port = defaultPortIfNull(args);
        final var tomcat = new TomcatStarter(Application.class, port);
        tomcat.start();
        await();
        tomcat.stop();
    }

    private static int defaultPortIfNull(final String[] args) {
        return Stream.of(args)
                .findFirst()
                .map(Integer::parseInt)
                .orElse(DEFAULT_PORT);
    }

    private static void await() throws IOException {
        // make the application wait until we press any key.
        System.in.read();
    }
}
