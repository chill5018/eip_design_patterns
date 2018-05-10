package EIPTests;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ReadFromQueue {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        try {

            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    // Get some data from Queue
                    from("activemq:qa").to("file:data/inbox").to("stream:out");
                }
            });

            context.start();
            Thread.sleep(20000);

        } catch (Exception e) {
            System.out.println("Error: " + e);
        } finally {
            context.stop();
        }
    }
}
