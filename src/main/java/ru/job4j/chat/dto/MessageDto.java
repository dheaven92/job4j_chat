package ru.job4j.chat.dto;

import lombok.Getter;
import lombok.Setter;
import ru.job4j.chat.model.Message;

@Getter
@Setter
public class MessageDto {

    private int id;

    private String body;

    public Message toMessage() {
        Message message = new Message();
        message.setId(id);
        message.setBody(body);
        return message;
    }

    public static MessageDto fromMessage(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setBody(message.getBody());
        return messageDto;
    }
}
