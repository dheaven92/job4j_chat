package ru.job4j.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.repository.MessageRepository;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public Message findById(int id) {
        return messageRepository.findById(id).orElse(null);
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public void deleteAllByRoomId(int roomId) {
        messageRepository.deleteAllByRoomId(roomId);
    }
}
