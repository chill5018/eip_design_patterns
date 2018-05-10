package EIPs;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class QueueConsole {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();// Prepared connection with the broker
        //ConnectionFactory connection = new ActiveMQConnectionFactory("tcp://localhost:61616");
        // ProducerTemplate interface allows sending message exchanges to endpoints
        ProducerTemplate template = context.createProducerTemplate();
        try {
            context.addComponent("activemq", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"));
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("activemq:queue:qa").to("stream:out");
                }
            });
            context.start();
            template.sendBody("activemq:qa", "Hi");
            Thread.sleep(2000);
        }
        finally {
            context.stop();
        }
    }
}
