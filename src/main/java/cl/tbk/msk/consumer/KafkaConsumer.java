package cl.tbk.msk.consumer;

import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final MessageRepository repository;

    @Autowired
    public KafkaConsumer(MessageRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(
    		topics = "my-topic", 
    		groupId = "java-group2")
    public void consume(GenericRecord record) {
        String message = record.get("message").toString();
        System.out.println("Consumed message: " + message);
        repository.save(new Message(message));
    }
}
