package com.baeldung.ljj;

class JacksonUnitTest {
    private static final String CAMPAIGN_WITH_TASKS = """
        {
          "code": "C-001",
          "nullableField": null,
          "details": {
            "name": "Campaign 1",
            "description": "Campaign 1 description",
            "closed": false
          },
          "tasks": [
            {
              "code": "T-001",
              "name": "Task 1",
              "status": "IN_PROGRESS"
            },
            {
              "code": "T-002",
              "name": "Task 2",
              "status": "IN_PROGRESS"
            }
          ]
        }""";
}