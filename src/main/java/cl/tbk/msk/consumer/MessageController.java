package cl.tbk.msk.consumer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageRepository messageRepository;

    public MessageController(MessageRepository repository) {
        this.messageRepository = repository;
    }

    @GetMapping
    public Iterable<Message> getMessages() {
        return messageRepository.findAll();
    }
}
