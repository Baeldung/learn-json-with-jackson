package com.baeldung.ljj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.baeldung.ljj.domain.model.Campaign;
import com.baeldung.ljj.domain.model.Task;
import com.baeldung.ljj.domain.model.TaskStatus;
import com.baeldung.ljj.serialization.CampaignToCodeSerializer;
import com.baeldung.ljj.serialization.CodeToCampaignDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class JacksonUnitTest {

    @Test
    void givenTask_whenSerializing_thenCustomSerializerUsed() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);

        SimpleModule module = new SimpleModule();
        module.addSerializer(Campaign.class, new CampaignToCodeSerializer());
        objectMapper.registerModule(module);

        Campaign campaign = new Campaign("C1", "Campaign one", "This is Campaign one");
        Task task = new Task("T1", "Task one", "This is Task one", LocalDate.of(2050, 1, 1), TaskStatus.TO_DO, campaign);
        String json = objectMapper.writeValueAsString(task);

        assertTrue(json.contains("\"campaign\" : \"C1\""));
        assertFalse(json.contains("\"name\" : \"Campaign one\""));

        System.out.println(json);
    }

    @Test
    void givenJsonWithCustomStatus_whenDeserializing_thenCustomDeserializerUsed() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Campaign.class, new CodeToCampaignDeserializer());
        objectMapper.registerModule(module);

        String json = """
              {
              "code" : "T1",
              "name" : "Task one",
              "description" : "This is Task one",
              "dueDate" : [ 2050, 1, 1 ],
              "status" : "TO_DO",
              "campaign" : "C1"
            }
            """;

        Task task = objectMapper.readValue(json, Task.class);
        assertNotNull(task.getCampaign());
        assertEquals("C1", task.getCampaign()
            .getCode());
        assertNull(task.getCampaign()
            .getName());
        assertNull(task.getCampaign()
            .getDescription());
    }

    @Test
    void givenTask_whenSerializing_thenAnnotationSerializerUsed() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        // no further module is added to objectMapper
        Campaign campaign = new Campaign("C1", "Campaign one", "This is Campaign one");
        Task task = new Task("T1", "Task one", "This is Task one", LocalDate.of(2050, 1, 1), TaskStatus.TO_DO, campaign);
        String json = objectMapper.writeValueAsString(task);

        assertTrue(json.contains("\"campaign\":\"C1\""));
        assertFalse(json.contains("\"name\":\"Campaign 1\""));

        System.out.println(json);
    }

    @Test
    void givenJsonWithCustomCampaign_whenDeserializing_thenAnnotationDeserializerUsed() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String json = """
              {
              "code" : "T1",
              "name" : "Task one",
              "description" : "This is Task one",
              "dueDate" : [ 2050, 1, 1 ],
              "status" : "TO_DO",
              "campaign" : "C1"
            }
            """;
        Task task = objectMapper.readValue(json, Task.class);
        assertNotNull(task.getCampaign());
        assertEquals("C1", task.getCampaign()
            .getCode());
        assertNull(task.getCampaign()
            .getName());
        assertNull(task.getCampaign()
            .getDescription());
    }

    @Test
    void givenAnnotationSerializerAndDeserializer_whenWorkingWithStandaloneCampaign_thenCustomSerializerSerializerNotUsed() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        Campaign campaign = new Campaign("C2", "Campaign two", "This is Campaign two.");

        // serializing
        String json = objectMapper.writeValueAsString(campaign);

        assertTrue(json.contains("\"code\" : \"C2\""));
        assertTrue(json.contains("\"name\" : \"Campaign two\""));

        System.out.println(json);

        // deserializing
        Campaign deserialized = objectMapper.readValue(json, Campaign.class);
        assertEquals(campaign.getCode(), deserialized.getCode());
        assertEquals(campaign.getName(), deserialized.getName());
        assertEquals(campaign.getDescription(), deserialized.getDescription());
        assertEquals(campaign.getTasks(), deserialized.getTasks());

    }
}