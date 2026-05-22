package com.baeldung.ljj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.baeldung.ljj.domain.model.Campaign;
import com.baeldung.ljj.domain.model.CampaignWithAnnotations;
import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.exc.InvalidDefinitionException;
import tools.jackson.databind.exc.UnrecognizedPropertyException;
import tools.jackson.databind.json.JsonMapper;

class JacksonUnitTest {

    @Test
    void givenDefaultObjectMapperInstance_whenSerializingAnObject_thenReturnJson() {
        JsonMapper mapper = JsonMapper.builder().build();

        Campaign campaign = new Campaign("A1", "JJ", "");
        String result = mapper.writeValueAsString(campaign);

        System.out.println(result);
    }

    @Test
    void givenEnableFeatureToggle_thenBehaviorIsAdjusted() {
        JsonMapper customMapper = JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).build();

        String result = customMapper.writeValueAsString(new Campaign("A1", "JJ", ""));

        System.out.println(result);
        assertTrue(result.contains("\n"));
    }

    @Test
    void givenDisableFeatureToggle_thenBehaviorIsAdjusted() {
        class EmptyClass {}

        JsonMapper defaultMapper = JsonMapper.builder().build();
        String defaultOutput = defaultMapper.writeValueAsString(new EmptyClass());
        assertEquals("{}", defaultOutput);

        JsonMapper strictMapper = JsonMapper.builder().enable(SerializationFeature.FAIL_ON_EMPTY_BEANS).build();
        assertThrows(InvalidDefinitionException.class, () -> strictMapper.writeValueAsString(new EmptyClass()));
    }

    @Test
    void givenConfigureFeatureToggle_thenBehaviorIsAdjusted() {
        String json = """
            {"code":"X1","name":"Extra","description":"-", "extraField":"ignored"}
            """;

        JsonMapper defaultMapper = JsonMapper.builder().build();
        Campaign campaignResult = defaultMapper.readValue(json, Campaign.class);
        // no exception: Jackson 3 ignores unknown properties by default
        assertEquals("X1", campaignResult.getCode());

        JsonMapper strictMapper = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .build();
        assertThrows(UnrecognizedPropertyException.class, () -> strictMapper.readValue(json, Campaign.class));
    }

    @Test
    void givenAnObjectWithNON_NULLFlagSet_whenSerializedTheObject_thenNullValuesExcluded() {
        Campaign exampleCampaign = new Campaign("A1", "JJ", null);

        JsonMapper defaultMapper = JsonMapper.builder().build();
        String defaultOutput = defaultMapper.writeValueAsString(exampleCampaign);
        assertTrue(defaultOutput.contains("null"));

        JsonMapper customMapper = JsonMapper.builder()
                .changeDefaultPropertyInclusion(v -> v.withValueInclusion(JsonInclude.Include.NON_NULL))
                .build();
        String output = customMapper.writeValueAsString(exampleCampaign);
        System.out.println(output);
        assertFalse(output.contains("null"));
    }

    @Test
    void givenGlobalAlways_thenClassNonNull_andFieldAlways_shouldRespectAnnotationPrecedence() {
        JsonMapper mapper = JsonMapper.builder()
                .changeDefaultPropertyInclusion(v -> v.withValueInclusion(JsonInclude.Include.ALWAYS))
                .build();

        // code = null (class-level NON_NULL should drop it)
        // description = null (field-level ALWAYS should force include)
        CampaignWithAnnotations campaign = new CampaignWithAnnotations(null, "JJ", null);

        String json = mapper.writeValueAsString(campaign);
        System.out.println(json);

        assertTrue(json.contains("\"description\":null"));
        assertFalse(json.contains("\"code\":"));
    }
}