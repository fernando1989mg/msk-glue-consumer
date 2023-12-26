package cl.tbk.msk.consumer;

import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.apache.avro.Schema;


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
        String message = record.toString();
        System.out.println("Consumed message: " + message);
        //repository.save(new Message(message));
        
        Schema schema = record.getSchema();

        // Imprimir el ID del esquema si está disponible
        // Nota: Esta parte depende de cómo se maneja el registro de esquemas en tu sistema
        String schemaId = schema.getProp("schemaId"); // Asumiendo que el ID del esquema se almacena en esta propiedad
        if (schemaId != null) {
            System.out.println("Schema ID: " + schemaId);
        } else {
            System.out.println("Schema ID not found");
        }
        
        System.out.println("Schema Fields: " + schema.getFields());
    }
}
