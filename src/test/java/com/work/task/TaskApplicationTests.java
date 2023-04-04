package com.work.task;

import com.work.task.controller.TaskController;
import com.work.task.entity.Task;
import com.work.task.repository.TaskRepository;
import com.work.task.service.TaskService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TaskApplicationTests {

    @Autowired
    TaskController taskController;

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRepository taskRepository;

    private static final int TASK_ID = 1;
    private static final String TASK_NAME = "AWS Certification";
    private static final String TASK_DESCRIPTION = "AWS Certification description";
    private static final String TASK_STATUS = "In Progress";

    private static final String USERNAME = "PSB";


    @Test
    void contextLoads() {
        Assertions.assertThat(taskController).isNotNull();
        Assertions.assertThat(taskService).isNotNull();
        Assertions.assertThat(taskRepository).isNotNull();

    }

    @Test
    public void testStringRepresentation() {
        Task task = createTask(TASK_ID, TASK_NAME, TASK_DESCRIPTION, TASK_STATUS, USERNAME);
        assertEquals(task.toString(), "Task(id=1, taskName=AWS Certification, taskDescription=AWS Certification description, taskStatus=In Progress, userName=PSB)");
    }

    private Task createTask(int taskId, String taskName, String taskDescription, String taskStatus, String username) {
        Task task = new Task();
        task.setId(taskId);
        task.setTaskName(taskName);
        task.setTaskDescription(taskDescription);
        task.setTaskStatus(taskStatus);
        task.setUserName(username);
        return task;
    }


}
