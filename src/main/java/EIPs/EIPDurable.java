package EIPs;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class EIPDurable {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:start").to("activemq:topic:foo");

                    from("activemq:topic:foo?clientId=1&durableSubscriptionName=bar1")
                            .to("mock:result1");
                    from("activemq:topic:foo?clientId=2&durableSubscriptionName=bar2")
                            .to("mock:result2");
                }
            });

            context.start();
            Thread.sleep(20000);
        } catch (Exception e ) {
            System.out.println("Error: " + e);
        } finally {
            context.stop();
        }
    }
}
