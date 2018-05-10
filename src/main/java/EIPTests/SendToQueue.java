package EIPTests;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class SendToQueue {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    // Read file, split it in lines
                    // send each to a queue as a separate message

                    from("file:data/inbox")
                        .to("activemq:qa")
                        .to("stream:out");

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
