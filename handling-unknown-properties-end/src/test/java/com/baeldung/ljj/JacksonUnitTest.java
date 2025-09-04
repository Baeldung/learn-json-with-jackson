package com.baeldung.ljj;

import com.baeldung.ljj.domain.model.Campaign;
import com.baeldung.ljj.domain.model.CampaignWithIgnoreUnknown;
import com.baeldung.ljj.domain.model.CampaignWithSetUnknownProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JacksonUnitTest {

    @Test
    void givenUnknownProperty_whenUsingDefaultMapper_thenFail() {
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                  "code": "C2",
                  "name": "Campaign 2",
                  "description": "The description of Campaign 2",
                  "budget": 100
                }
                """;
        assertThrows(UnrecognizedPropertyException.class, () -> mapper.readValue(json, Campaign.class));
    }

    @Test
    void givenMapperConfiguredToIgnoreUnknown_thenDeserializationSucceeds() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = """
                {
                  "code": "C2",
                  "name": "Campaign 2",
                  "description": "The description of Campaign 2",
                  "budget": 500
                }
                """;
        Campaign campaign = mapper.readValue(json, Campaign.class);
        assertEquals("C2", campaign.getCode());
    }

    @Test
    void givenJsonIgnorePropertiesConfiguredToIgnoreUnknown_thenDeserializationSucceeds() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                  "code": "C2",
                  "name": "Campaign 2",
                  "description": "The description of Campaign 2",
                  "budget": 500
                }
                """;
        CampaignWithIgnoreUnknown campaign = mapper.readValue(json, CampaignWithIgnoreUnknown.class);
        assertEquals("C2", campaign.getCode());
    }

    @Test
    void givenJsonAnySetterConfiguredToRecordUnknown_thenDeserializationSucceeds() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                  "code": "C2",
                  "name": "Campaign 2",
                  "description": "The description of Campaign 2",
                  "budget": 500
                }
                """;
        CampaignWithSetUnknownProperties campaign = mapper.readValue(json, CampaignWithSetUnknownProperties.class);
        assertTrue(campaign.getUnknownProperties().containsKey("budget"));
    }
}