package cl.tbk.msk.consumer;

import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:kafka.properties")
public class KafkaConsumer {
	
    private final MessageRepository repository;

    public KafkaConsumer(MessageRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(
    		topics = "#{'${kafka.topic}'}", 
    		groupId = "#{'${consumer.group}'}")
    public void consume(GenericRecord record) {
        String message = record.get("message").toString();
        System.out.println("Consumed message: " + message);
        repository.save(new Message(message));
    }
}
