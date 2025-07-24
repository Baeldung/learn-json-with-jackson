package com.baeldung.ljj;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.baeldung.ljj.domain.model.TaskStatus;
import com.baeldung.ljj.domain.model.TaskStatusJsonObject;
import com.baeldung.ljj.domain.model.TaskStatusJsonProperty;
import com.baeldung.ljj.domain.model.TaskStatusJsonValue;
import com.baeldung.ljj.domain.model.TaskStatusObjectMap;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class JacksonUnitTest {

    @Test
    void whenSerializingEnum_thenWritesName() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(TaskStatus.IN_PROGRESS);
        assertEquals("\"IN_PROGRESS\"", json);
    }

    @Test
    void whenDeserializingMatchingName_thenSucceeds() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        TaskStatus taskStatus = objectMapper.readValue("\"ON_HOLD\"", TaskStatus.class);
        assertEquals(TaskStatus.ON_HOLD, taskStatus);
    }

    @Test
    void whenUsingIndexFlag_thenWritesOrdinal() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        String json = mapper.writeValueAsString(TaskStatus.ON_HOLD);
        assertEquals("2", json);
    }

    @Test
    void whenUsingIndexFlag_thenDeserializeOrdinal() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        TaskStatus taskStatus = mapper.readValue("2", TaskStatus.class);
        assertEquals(TaskStatus.ON_HOLD, taskStatus);
    }

    @Test
    void whenUsingToStringFlag_thenWritesLabel() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        String json = mapper.writeValueAsString(TaskStatus.DONE);
        assertEquals("\"Done\"", json);
    }

    @Test
    void whenUsingJsonValue_thenLabelWritten() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(TaskStatusJsonValue.IN_PROGRESS);
        assertEquals("\"In Progress\"", json);
    }

    @Test
    void whenUsingJsonProperty_thenLabelWritten() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(TaskStatusJsonProperty.DONE);
        assertEquals("\"JsonProperty Done\"", json);
    }

    @Test
    void whenUsingJsonCreator_thenLabelParsed() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TaskStatusJsonValue taskStatus = mapper.readValue("\"On HoLd\"", TaskStatusJsonValue.class);
        assertEquals(TaskStatusJsonValue.ON_HOLD, taskStatus);
    }

    @Test
    void whenSerializing_thenJsonObjectIsProduced() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TaskStatusJsonObject label = TaskStatusJsonObject.TO_DO;
        String json = mapper.writeValueAsString(label);
        String expectedJson = "{\"label\":\"To Do\"}";
        assertEquals(expectedJson, json);
    }

    @Test
    void whenUsingDefaultValue_thenFallsBackToToDo() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
        TaskStatus status = mapper.readValue("\"UNKNOWN\"", TaskStatus.class);
        assertEquals(TaskStatus.TO_DO, status);
    }

    @Test
    void whenJsonValueOnEnum_thenMapKeysAreLabel() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TaskStatusObjectMap label = TaskStatusObjectMap.TO_DO;
        String json = mapper.writeValueAsString(label);
        String expectedJson = "{\"label\":\"To Do\"}";
        assertEquals(expectedJson, json);
    }
}
