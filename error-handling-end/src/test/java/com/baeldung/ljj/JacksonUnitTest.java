package com.baeldung.ljj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.baeldung.ljj.domain.model.Campaign;
import com.baeldung.ljj.domain.model.Task;
import com.baeldung.ljj.domain.model.TaskWithoutEmptyConstructor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

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
                "budget": 100
            }
            """;
        Campaign campaign = mapper.readValue(json, Campaign.class);
        assertEquals("C2", campaign.getCode());
    }

    @Test
    void givenObjectToArrayProperty_whenUsingDefaultMapper_thenFail() {
        ObjectMapper mapper = new ObjectMapper();
        String json = """
            {
                "code": "C2",
                "name": "Campaign 2",
                "description": "The description of Campaign 2",
                "tasks": {
                    "code": 101,
                    "name": "Task A"
                }
            }
            """;
        assertThrows(MismatchedInputException.class, () -> mapper.readValue(json, Campaign.class));
    }

    @Test
    void givenObjectToArrayProperty_whenMapperConfiguredToAcceptObjectAsArray_thenSuccess() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        String json = """
            {
                "code": "C2",
                "name": "Campaign 2",
                "description": "The description of Campaign 2",
                "tasks": {
                    "code": 101,
                    "name": "Task A"
                }
            }
            """;
        Campaign campaign = mapper.readValue(json, Campaign.class);
        assertEquals(1, campaign.getTasks().size());
    }

    @Test
    void givenNoDefaultConstructor_whenUsingDefaultMapper_thenFail() {
        ObjectMapper mapper = new ObjectMapper();
        String json = """
            {
                "code": "C2",
                "name": "Campaign 2",
                "description": "The description of Campaign 2"
            }
            """;
        assertThrows(InvalidDefinitionException.class, () -> mapper.readValue(json, TaskWithoutEmptyConstructor.class));
    }

    @Test
    void givenDefaultConstructorAdded_whenUsingDefaultMapper_thenSuccess() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = """
            {
                "code": "C2",
                "name": "Campaign 2",
                "description": "The description of Campaign 2"
            }
            """;
        Task task = mapper.readValue(json, Task.class);
        assertEquals("C2",task.getCode());
    }
}