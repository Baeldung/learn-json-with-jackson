package com.baeldung.ljj.serialization;

import java.io.IOException;

import com.baeldung.ljj.domain.model.Campaign;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CampaignToCodeSerializer extends StdSerializer<Campaign> {

    public CampaignToCodeSerializer() {
        this(null);
    }

    public CampaignToCodeSerializer(Class<Campaign> t) {
        super(t);
    }

    @Override
    public void serialize(Campaign value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.getCode());
    }
}