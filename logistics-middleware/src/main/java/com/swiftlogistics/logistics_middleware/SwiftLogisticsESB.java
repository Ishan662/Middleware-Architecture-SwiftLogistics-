package com.swiftlogistics.logistics_middleware;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SwiftLogisticsESB extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        // Error handling configuration: send failed messages to a dedicated Kafka topic.
        onException(Exception.class)
                .handled(true) // Mark the exception as handled
                .log("Error processing message. Sending to Dead Letter Topic: ${exception.message}")
                .to("kafka:orders-dlq?brokers=localhost:9092");

        // --- ROUTE 1: Front-end (REST) to Kafka Topic ---
        restConfiguration()
                .component("servlet")
                .port(8080)
                .bindingMode(org.apache.camel.model.rest.RestBindingMode.json)
                .enableCORS(true);

        from("rest:post:/orders/new")
                .log("New order request received from front-end. Publishing to Kafka.")
                .to("kafka:orders-in?brokers=localhost:9092");

        // --- ROUTE 2: Kafka Topic to CMS (SOAP) ---
        from("kafka:orders-in?brokers=localhost:9092")
                .log("New order message received from Kafka. Processing for CMS.")
                // Use the dedicated JSON to SOAP processor
                .process(new JsonToSoapProcessor())
                .to("cxf://http://cms-service:8080/services/OrderService?wsdlURL=classpath:wsdl/cms.wsdl&serviceName={urn:cms-service:v1}OrderService")
                .log("CMS response received: ${body}");

        // --- ROUTE 3: WMS (TCP) to Kafka Topic ---
        from("netty:tcp://wms-service:9090?textline=true&sync=false")
                .log("Shipment ready message received from WMS. Publishing to Kafka.")
                .to("kafka:shipments-ready?brokers=localhost:9092");

        // --- ROUTE 4: Kafka Topic to ROS (REST) ---
        from("kafka:shipments-ready?brokers=localhost:9092")
                .log("Shipment ready message received from Kafka. Sending to ROS.")
                // Use the dedicated TCP to JSON processor
                .process(new TcpToJsonProcessor())
                .to("http://ros-service:8080/shipments/ready?throwExceptionOnFailure=true")
                .log("ROS response received: ${body}");
    }
}
