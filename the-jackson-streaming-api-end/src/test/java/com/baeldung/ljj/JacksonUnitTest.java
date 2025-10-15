package com.baeldung.ljj;

import com.baeldung.ljj.domain.model.TaskStatus;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JacksonUnitTest {

    private static final String EXPECTED_JSON = """
        {
          "code" : "C-001",
          "name" : "Campaign 1",
          "tasks" : [ {
            "code" : "T-001",
            "name" : "Task 1",
            "status" : "TO_DO"
          }, {
            "code" : "T-002",
            "name" : "Task 2",
            "status" : "IN_PROGRESS"
          } ]
        }""";

    @Test
    void whenParsingTasksWithJsonParser_thenOnHoldTasksCountIsCorrect() throws IOException {
        int onHoldTasks = 0;
        URL resource = getClass().getClassLoader().getResource("tasks.json");

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(resource);

        if (jsonParser.nextToken() == JsonToken.START_ARRAY) {
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                TaskStatus taskStatus = null;
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    String fieldName = jsonParser.currentName();
                    if ("status".equals(fieldName)) {
                        jsonParser.nextToken();
                        taskStatus = TaskStatus.valueOf(jsonParser.getText());
                    }
                }
                if (TaskStatus.ON_HOLD.equals(taskStatus)) {
                    onHoldTasks++;
                }
            }
        }
        jsonParser.close();

        assertEquals(250, onHoldTasks);
    }

    @Test
    void whenWritingCampaignWithTasksUsingJsonGenerator_thenExpectedJsonStructureIsWritten() throws IOException {
        File outputFile = new File("src/test/resources/campaign-with-tasks.json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(outputFile, JsonEncoding.UTF8);
        jsonGenerator.setCodec(objectMapper);
        jsonGenerator.useDefaultPrettyPrinter();

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("code", "C-001");
        jsonGenerator.writeStringField("name", "Campaign 1");
        jsonGenerator.writeFieldName("tasks");
        jsonGenerator.writeStartArray();

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("code", "T-001");
        jsonGenerator.writeStringField("name", "Task 1");
        jsonGenerator.writeObjectField("status", TaskStatus.TO_DO);
        jsonGenerator.writeEndObject();

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("code", "T-002");
        jsonGenerator.writeStringField("name", "Task 2");
        jsonGenerator.writeObjectField("status", TaskStatus.IN_PROGRESS);
        jsonGenerator.writeEndObject();

        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        String writtenJson = Files.readString(outputFile.toPath());
        assertEquals(
            objectMapper.readTree(EXPECTED_JSON),
            objectMapper.readTree(writtenJson)
        );
    }

}