package cl.tbk.msk.consumer;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import com.amazonaws.services.schemaregistry.deserializers.GlueSchemaRegistryKafkaDeserializer;

public class CustomGlueSchemaRegistryDeserializer extends GlueSchemaRegistryKafkaDeserializer {
	
    public CustomGlueSchemaRegistryDeserializer() {
        super();
    }

    @Override
    public Object deserialize(String topic, Headers headers, byte[] data) {
    	
    	System.out.println("DESERIALIZANDO...: " + topic);
    	
    	for (Header header : headers) {
            System.out.println("[HEADER] KEY: " + header.key() + "VALUE: " + header.value());
    	}

        Object deserializedObject = super.deserialize(topic, headers, data);

        if (deserializedObject instanceof GenericRecord) {
            GenericRecord record = (GenericRecord) deserializedObject;

            String schemaId = extractSchemaIdFromHeaders(headers);

            System.out.println("Schema ID: " + schemaId);
        }

        return deserializedObject;
    }

    private String extractSchemaIdFromHeaders(Headers headers) {
    	for (Header header : headers) {
                System.out.println("[HEADER] KEY: " + header.key() + "VALUE: " + header.value());
        }
        return null;
    }

}
