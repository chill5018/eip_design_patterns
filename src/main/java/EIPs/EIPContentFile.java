package EIPs;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class EIPContentFile {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {

                    // File is sent from "Inbox" to Active MQ
                    from("file:data/inbox").to("activemq:incoming");

                    // In coming messages are checked by their extension
                    // If file is not ".xml" or ".txt" then the order is
                    // not accepted
                    from("activemq:incoming")
                            .choice()
                                .when(header("CamelFileName").endsWith(".xml"))
                                    .to("activemq:xmlorders")
                                .when(header("CamelFileName").endsWith(".txt"))
                                    .to("activemq:txtorders")
                                .otherwise()
                                    .to("activemq:badorders").stop()
                            .end()
                            .to("activemq:nextoperation");
                }
            });

            context.start();
            Thread.sleep(20000);
        } catch (Exception e) {
            System.out.println("Error: "+ e);
        } finally {
            context.stop();
        }
    }
}
