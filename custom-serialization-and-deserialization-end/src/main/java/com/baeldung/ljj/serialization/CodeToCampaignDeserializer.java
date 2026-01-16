package com.baeldung.ljj.serialization;

import java.io.IOException;

import com.baeldung.ljj.domain.model.Campaign;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CodeToCampaignDeserializer extends StdDeserializer<Campaign> {

    public CodeToCampaignDeserializer() {
        this(null);
    }

    public CodeToCampaignDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Campaign deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String code = p.getText();
        return new Campaign(code, null, null);
    }
}