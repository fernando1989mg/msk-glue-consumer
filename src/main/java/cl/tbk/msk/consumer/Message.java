package cl.tbk.msk.consumer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.avro.generic.GenericRecord;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    public Message() {
    }

    public Message(String content) {
        this.setContent(content);
    }


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

    public void setContent(String content) {
        this.content = content;
    }

    // getters and setters
}
