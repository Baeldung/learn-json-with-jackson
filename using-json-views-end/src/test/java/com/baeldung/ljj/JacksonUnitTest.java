package com.baeldung.ljj;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.baeldung.ljj.domain.model.Campaign;
import com.baeldung.ljj.domain.model.Views;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

class JacksonUnitTest {

    ObjectMapper objectMapper = new ObjectMapper();
    Campaign campaign = new Campaign("C1", "Campaign 1", "Description of Campaign 1", null, false);

    @Test
    void givenCampaign_whenSerializingWithSummaryView_thenOnlySummaryFieldsAreIncluded() throws JsonProcessingException {
        objectMapper = JsonMapper.builder()
            .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
            .build();
        String json = objectMapper.writerWithView(Views.Summary.class)
            .writeValueAsString(campaign);

        assertTrue(json.contains("\"code\""));
        assertTrue(json.contains("\"name\""));
        assertFalse(json.contains("\"description\""));
        assertFalse(json.contains("\"tasks\""));
        assertFalse(json.contains("\"closed\""));
    }

    @Test
    void givenCampaign_whenSerializingWithDetailView_thenSummaryAndDetailFieldsAreIncluded() throws JsonProcessingException {
        objectMapper = JsonMapper.builder()
            .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
            .build();
        String json = objectMapper.writerWithView(Views.Detail.class)
            .writeValueAsString(campaign);

        assertTrue(json.contains("\"code\""));
        assertTrue(json.contains("\"name\""));
        assertTrue(json.contains("\"description\""));
        assertFalse(json.contains("\"tasks\""));
        assertFalse(json.contains("\"closed\""));
    }

    @Test
    void givenCampaign_whenSerializingWithoutView_thenAllFieldsAreIncluded() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(campaign);

        assertTrue(json.contains("\"code\""));
        assertTrue(json.contains("\"name\""));
        assertTrue(json.contains("\"description\""));
        assertTrue(json.contains("\"tasks\""));
        assertTrue(json.contains("\"closed\""));
    }

    @Test
    void givenFieldInMultipleViews_whenSerializingWithEachView_thenFieldIsIncluded() throws JsonProcessingException {
        objectMapper = JsonMapper.builder()
            .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
            .build();
        String summaryJson = objectMapper.writerWithView(Views.Summary.class)
            .writeValueAsString(campaign);
        assertTrue(summaryJson.contains("\"code\""));

        String internalJson = objectMapper.writerWithView(Views.Internal.class)
            .writeValueAsString(campaign);
        assertTrue(internalJson.contains("\"code\""));
    }

}