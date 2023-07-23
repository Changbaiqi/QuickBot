package com.cbq.quickbot.entity;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class SenderDeserialize extends StdDeserializer<Sender> {
    public SenderDeserialize(){
        this(null);
    }

    public SenderDeserialize(Class<?> vc){
        super(vc);
    }

    @Override
    public Sender deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        Sender sender  = new Sender();
        sender.setAge(jsonNode.get("age").asInt());
        sender.setArea(jsonNode.get("area").asText());
        sender.setCard(jsonNode.get("card").asText());
        sender.setLevel(jsonNode.get("level").asText());
        sender.setNickname(jsonNode.get("nickname").asText());
        sender.setRole(jsonNode.get("role").asText());
        sender.setSex(jsonNode.get("sex").asText());
        sender.setTitle(jsonNode.get("title").asText());
        sender.setUser_id(jsonNode.get("user_id").asLong());
        return sender;
    }
}
