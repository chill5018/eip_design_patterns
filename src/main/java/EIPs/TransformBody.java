package EIPs;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultProducerTemplate;

public class TransformBody {
    public static void main(String[] args) throws Exception {
        //JndiContext jndiContext = new JndiContext();
        //jndiContext.bind("uppercase", new UpperCase());
        //CamelContext camelContext = new DefaultCamelContext(jndiContext);
        CamelContext camelContext = new DefaultCamelContext();
        try {
            camelContext.addRoutes(new RouteBuilder() {
                public void configure() {
                    // Simple
                    from("direct:start")
                            .transform(simple("<out>${body}</out>"))
                            .to("stream:out");

                    // Method
                    from("direct:start").log("Transform ${body} to upperCase")
                            .transform(simple("${body.toUpperCase()} regex 'NO?'"));
                    //.transform(method("uppercase")).to("stream:out")


                    // Conditional
                    from("direct:start")
                            .log("Article name is ${body}")
                            .choice()
                            .when().simple("${body} contains 'Camel'")
                            .transform(constant("Yes"))
                            .to("stream:out")
                            .otherwise()
                            .transform(constant("No"))
                            .to("stream:out")
                            .end();

                }
            });
            camelContext.start();

            ProducerTemplate template = new DefaultProducerTemplate(camelContext);
            template.start();
            template.sendBody("direct:start", "hello world");
        } finally {
            camelContext.stop();
        }
    }
}
