package cl.tbk.msk.consumer;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import com.amazonaws.services.schemaregistry.deserializers.GlueSchemaRegistryKafkaDeserializer;
import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants;
import com.amazonaws.services.schemaregistry.utils.AvroRecordType;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;


@Configuration
@PropertySource("classpath:kafka.properties")
public class KafkaConsumerConfig {
	
    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServersConfig;
    
    @Value("${sasl.username}")
    private String saslUsername;

    @Value("${sasl.password}")
    private String saslPassword;
    
    @Value("${confluent.registry.name}")
    private String confluentRegistryName;
    
    @Value("${confluent.schema.name}")
    private String confluentSchemaName;
    
    @Value("${aws.region}")
    private String awsRegion;
    
    @Bean
    public ConsumerFactory<String, GenericRecord> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServersConfig);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "java-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GlueSchemaRegistryKafkaDeserializer.class);
        
        props.put(AWSSchemaRegistryConstants.SECONDARY_DESERIALIZER, KafkaAvroDeserializer.class.getName());
        props.put("schema.registry.url", this.confluentRegistryName);
        //props.put(AWSSchemaRegistryConstants.REGISTRY_NAME, this.confluentRegistryName);
        //props.put(AWSSchemaRegistryConstants.SCHEMA_NAME, this.confluentSchemaName);

        
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "SCRAM-SHA-512");
        props.put("sasl.jaas.config", String.format("org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";", this.saslUsername, this.saslPassword));
        URL truststoreUrl = getClass().getClassLoader().getResource("kafka.client.truststore.jks");
        if (truststoreUrl != null) {
            File truststoreFile = new File(truststoreUrl.getFile());
            props.put("ssl.truststore.location", truststoreFile.getAbsolutePath());
        }
        
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        
        props.put(AWSSchemaRegistryConstants.AWS_REGION, this.awsRegion);
        props.put(AWSSchemaRegistryConstants.AVRO_RECORD_TYPE, AvroRecordType.GENERIC_RECORD.getName());
        
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GenericRecord> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GenericRecord> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        return factory;
    }
}
