package cl.tbk.msk.consumer;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import com.amazonaws.services.schemaregistry.common.AWSDeserializerInput;
import com.amazonaws.services.schemaregistry.deserializers.GlueSchemaRegistryDeserializerDataParser;
import com.amazonaws.services.schemaregistry.deserializers.GlueSchemaRegistryKafkaDeserializer;

public class CustomGlueSchemaRegistryDeserializer extends GlueSchemaRegistryKafkaDeserializer {
	
    public CustomGlueSchemaRegistryDeserializer() {
        super();
    }

    @Override
    public Object deserialize(String topic, Headers headers, byte[] data) {
    	
    	System.out.println("DESERIALIZANDO...: " + topic);
    	
        GlueSchemaRegistryDeserializerDataParser dataParser = GlueSchemaRegistryDeserializerDataParser.getInstance();
        
        Byte b = dataParser.getHeaderVersionByte(ByteBuffer.wrap(data));
    	
    	System.out.println("BYTE: " + b);
    	
    	AWSDeserializerInput awsInput = prepareInput(data, topic);
    	
    	UUID schemaVersionId = dataParser.getSchemaVersionId(awsInput.getBuffer());
    	
    	
    	System.out.println("UUID: " + schemaVersionId.toString());
    	

        Object deserializedObject = super.deserialize(topic, headers, data);

        if (deserializedObject instanceof GenericRecord) {
            GenericRecord record = (GenericRecord) deserializedObject;

            String schemaId = extractSchemaIdFromHeaders(headers);

            System.out.println("Schema ID: " + schemaId);
        }

        return deserializedObject;
    }
    
    private AWSDeserializerInput prepareInput(byte[] data,
            String topic) {
		return AWSDeserializerInput.builder()
		.buffer(ByteBuffer.wrap(data))
		.transportName(topic)
		.build();
	}

    private String extractSchemaIdFromHeaders(Headers headers) {
    	for (Header header : headers) {
                System.out.println("[HEADER] KEY: " + header.key() + "VALUE: " + header.value());
        }
        return null;
    }

}
