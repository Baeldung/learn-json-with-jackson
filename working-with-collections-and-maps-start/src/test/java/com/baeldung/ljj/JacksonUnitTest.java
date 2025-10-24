package com.baeldung.ljj;

import com.baeldung.ljj.domain.model.Task;
import com.baeldung.ljj.domain.model.TaskStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

class JacksonUnitTest {

    final ObjectMapper objectMapper = new ObjectMapper();

    Task task1 = new Task("T1", "Task 1", "Description of Task 1", null, TaskStatus.TO_DO, null);
    Task task2 = new Task("T2", "Task 2", "Description of Task 2", null, TaskStatus.TO_DO, null);

}