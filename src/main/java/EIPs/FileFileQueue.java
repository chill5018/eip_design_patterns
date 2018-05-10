package EIPs;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class FileFileQueue {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        try {
            context.addRoutes(new RouteBuilder(){
                @Override
                public void configure() throws Exception {
                    from("file:data/inbox").to("file:data/outbox").to("activemq:qa");
                }
            });
            context.start();
            Thread.sleep(2000);
        } finally {
            context.stop();
        }
    }
}
