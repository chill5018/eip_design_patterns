package EIPs;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class EIPMulticast {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("activemq:nextoperation")
                            .multicast().parallelProcessing()
                    .to("activemq:archive", "file:data/inbox");
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
