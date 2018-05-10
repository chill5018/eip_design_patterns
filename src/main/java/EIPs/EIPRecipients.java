package EIPs;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class EIPRecipients {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    // These message contain their destination in a header
                    from("file:data/inbox")
                        .split().tokenize("\n")
                    .to("direct:test");

                    from("direct:test")
                        .process(new Processor() {
                            public void process(Exchange exchange) throws Exception {
                                String recipient = exchange.getIn().getBody().toString();
                                System.out.println(recipient);
                                String recipientQueue = " activemq:queue:"+recipient;
                                exchange.getIn().setHeader("queue", recipientQueue);
                            }
                        }).recipientList(header("queue"));
                }
            });
            context.start();
            Thread.sleep(20000);
        } catch (Exception e) {
            System.out.println("Error " +  e);
        } finally {
            context.stop();
        }
    }
}
