package com.baeldung.ljj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.baeldung.ljj.domain.model.Campaign;
import com.baeldung.ljj.domain.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class JacksonUnitTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void givenFieldAndGetter_whenSerializing_thenJacksonBehaviorChecked() throws JsonProcessingException {
        // Given
        Campaign campaign = new Campaign("CODE01", "Campaign Name", "Description");

        // When
        String json = objectMapper.writeValueAsString(campaign);
        System.out.println(json);

        // Then
        /* Changing the getter name and no annotations on field or getter 
        assertFalse(json.contains("\"code\""));
        assertTrue(json.contains("\"theCode\""));
        */
        assertTrue(json.contains("\"code\":\"code01\""));
    }

    @Test
    void givenPropertyWithCustomName_whenSerializing_thenCustomNameAppearsInJson() throws JsonProcessingException {
        // Given
        Campaign original = new Campaign("T101", "Rename", "Custom property name");

        // When
        String json = objectMapper.writeValueAsString(original);
        System.out.println(json);

        // Then
        assertTrue(json.contains("\"is_closed\""));
        assertFalse(json.contains("\"closed\""));
    }

    @Test
    void givenPropertyWithCustomName_whenDeserializing_thenFieldIsCorrectlyDeserialized() throws JsonProcessingException {
        // Given
        String json = """
            {"code":"T101","name":"Rename","description":"Custom property name","tasks":[],"is_closed":false}
            """;

        // When
        Campaign renameCampaign = objectMapper.readValue(json, Campaign.class);

        // Then
        assertFalse(renameCampaign.isClosed());
    }

    @Test
    void givenJsonPropertyOrder_whenSerializing_thenKeysAppearInSequence() throws JsonProcessingException {
        // Given
        Campaign original = new Campaign("T101", "Rename", "JSON property order");

        // When
        String json = objectMapper.writeValueAsString(original);
        System.out.println(json);
        int idxCode = json.indexOf("\"code\"");
        int idxName = json.indexOf("\"name\"");
        int idxDescription = json.indexOf("\"description\"");
        int idxClosed = json.indexOf("\"is_closed\"");
        int idxTasks = json.indexOf("\"tasks\"");

        // Then
        assertTrue(idxCode < idxName);
        assertTrue(idxName < idxDescription);
        assertTrue(idxDescription < idxClosed);
        assertTrue(idxClosed < idxTasks);
    }

    @Test
    void givenJsonValue_whenSerializingTask_thenSingleStringProduced() throws JsonProcessingException {
        // Given
        Task task = new Task("T101", "Task101", "JSON value example", LocalDate.now(), null, null);
        Campaign original = new Campaign("T102", "Campaign102", "Big Campaign");
        original.setTasks(Set.of(task));

        // When
        String json = objectMapper.writeValueAsString(original);

        // Then
        String expectedJson = """
            {"code":"t102","name":"Campaign102","description":"Big Campaign","is_closed":false,"tasks":["Task: code= T101, name=Task101, description=JSON value example."]}""";
        assertEquals(expectedJson, json);
    }
}
