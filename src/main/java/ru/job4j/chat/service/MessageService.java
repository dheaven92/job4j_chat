package ru.job4j.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.repository.MessageRepository;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public Message create(Message message) {
        return messageRepository.save(message);
    }

    public void deleteAllByRoomId(int roomId) {
        messageRepository.deleteAllByRoomId(roomId);
    }
}
