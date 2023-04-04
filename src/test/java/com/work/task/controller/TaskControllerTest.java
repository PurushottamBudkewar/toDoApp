package com.work.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.task.entity.Task;
import com.work.task.exception.TaskNotFoundException;
import com.work.task.service.TaskService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    private static final int TASK_ID = 1;
    private static final String TASK_NAME = "Certification";
    private static final String TASK_DESCRIPTION = "Certification description";
    private static final String TASK_STATUS = "In Progress";
    private static final String USERNAME = "ABC";

    @Autowired
    MockMvc mockMvc;
    @MockBean
    TaskService taskService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetAllTasks() throws Exception {
        Task task = createTask(TASK_ID, TASK_NAME, TASK_DESCRIPTION, TASK_STATUS, USERNAME);
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        given(taskService.getAllTasks()).willReturn(taskList);
        this.mockMvc.perform(get("/tasks/")).andExpect(status().isOk());
        verify(taskService, times(1)).getAllTasks();
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void testGetOneTask() throws Exception {
        Task task = createTask(TASK_ID, TASK_NAME, TASK_DESCRIPTION, TASK_STATUS, USERNAME);
        when(taskService.findTaskById(1)).thenReturn(task);
        this.mockMvc.perform(get("/tasks/{id}", 1)).andExpect(status().isOk());
        verify(taskService, times(1)).findTaskById(1);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void testAddTask() throws Exception {
        Task task = createTask(TASK_ID, TASK_NAME, TASK_DESCRIPTION, TASK_STATUS, USERNAME);
        ObjectMapper mapper = new ObjectMapper();
        given(taskService.saveTask(task)).willReturn(task);

        this.mockMvc.perform(post("/tasks/addTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isOk());
        verify(taskService, times(1)).saveTask(task);
        verifyNoMoreInteractions(taskService);

    }

    @Test
    void testAddTasks() throws Exception {
        Task task = createTask(TASK_ID, TASK_NAME, TASK_DESCRIPTION, TASK_STATUS, USERNAME);
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        ObjectMapper mapper = new ObjectMapper();
        given(taskService.saveTasks(taskList)).willReturn(taskList);

        this.mockMvc.perform(post("/tasks/addTasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskList)))
                .andExpect(status().isOk());
        verify(taskService, times(1)).saveTasks(taskList);
        verifyNoMoreInteractions(taskService);

    }

    @Test
    void testUpdateTask() throws Exception {
        Task task = createTask(TASK_ID, TASK_NAME, TASK_DESCRIPTION, TASK_STATUS, USERNAME);
        ObjectMapper mapper = new ObjectMapper();
        given(taskService.updateTask(task.getId(),task)).willReturn(task);

        this.mockMvc.perform(put("/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isOk());
        verify(taskService, times(1)).updateTask(1,task);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void testPatchTask() throws Exception {
        Task task = createTask(TASK_ID, TASK_NAME, TASK_DESCRIPTION, TASK_STATUS, USERNAME);
        ObjectMapper mapper = new ObjectMapper();
        given(taskService.patchTask(task.getId(),task)).willReturn(task);

        this.mockMvc.perform(patch("/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                .andExpect(status().isOk());
        verify(taskService, times(1)).patchTask(1,task);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void testDelete() throws Exception {
        given(taskService.deleteTaskById(1)).willReturn( "1 id -> task removed");
        this.mockMvc.perform(delete("/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(taskService, times(1)).deleteTaskById(1);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void testSearchByStatus() throws Exception {
        this.mockMvc.perform(get("/tasks/search/{status}", "In Progress")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(taskService, times(1)).searchByTaskStatus("In Progress");
        verifyNoMoreInteractions(taskService);

    }

    @Test
    void testSearchByUserName() throws Exception {
        this.mockMvc.perform(get("/tasks/search/user/{userName}", "ABC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(taskService, times(1)).searchByUserName("ABC");
        verifyNoMoreInteractions(taskService);

    }
    @Test
    void testTaskNotFoundException() throws Exception {
        Task task = createTask(TASK_ID, TASK_NAME, TASK_DESCRIPTION, TASK_STATUS, USERNAME);
        ObjectMapper mapper = new ObjectMapper();
        given(taskService.patchTask(task.getId(), task))
                .willThrow(new TaskNotFoundException("Task does not exist with id: " + task.getId()));

        this.mockMvc.perform(patch("/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task)))
                        .andExpect(status().is4xxClientError())
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                        .andExpect(result -> assertEquals("Task does not exist with id: 1",
                            result.getResolvedException().getMessage()));
        verify(taskService).patchTask(1, task);
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