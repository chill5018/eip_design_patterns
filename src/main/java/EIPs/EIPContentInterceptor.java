package EIPs;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class EIPContentInterceptor {
    public static void main(String[] args) throws Exception {
        final CamelContext context = new DefaultCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    intercept().process(new Processor() {
                        int count = 0;
                        public void process(Exchange exchange) throws Exception {
                            count++;
                            System.out.println("interceptor: " + count + " from " + exchange.getIn().getBody());
                        }
                    });
                    //Read a file, split it in lines, then
                    // send empty lines to the console
                    // lines containing "mvn" to one queue
                    // the rest to a new queue
                    from("file:data/inbox")
                            .split().tokenize("\n")
                            .choice()
                                .when(simple("${body} == ''"))
                                    .to("stream:out")
                                .when(body().contains("mvn"))
                                    .to("activemq:qc")
                                .otherwise()
                                    .to("activemq:otherwise");
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
