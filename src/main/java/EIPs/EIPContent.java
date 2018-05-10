package EIPs;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class EIPContent {
	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();

		try {
		    context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    // Read a file, split it in lines, then
                    // send empty lines to the console
                    // lines containing mvn to one queue
                    // the rest of the lines in a new queue
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
