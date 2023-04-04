package com.work.task.service;

import com.work.task.entity.Task;
import com.work.task.exception.TaskNotFoundException;
import com.work.task.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final static  Logger log = LoggerFactory.getLogger(TaskService.class);
    @Autowired
    private TaskRepository taskRepository;

    /**
     * Saves given Task object
     * @param task Task object
     * @return Task which is saved
     */
    public Task saveTask (Task task){
        log.info("Task object-{}",task.toString());
        return taskRepository.save(task);
    }

    /**
     * Saves list of Tasks
     * @param tasks List of Task Objects
     * @return list of Tasks updated
     */
    public List<Task> saveTasks (List<Task> tasks){
        return taskRepository.saveAll(tasks);
    }

    /**
     * Retrieves all tasks
     * @return List of Tasks
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     *Find Task on the basis of id
     * @param id - id of task
     * @return Task
     */
    public Task findTaskById(int id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task does not exist with id: " + id));

    }

    /**
     * Deletes particular task on the basis of id
     * @param id id of task to be deleted
     * @return Message if id is removed
     */
    public String deleteTaskById(int id) {
        taskRepository.deleteById(id);
        return id + " id -> task removed.";
    }

    /**
     * Update existing task if not found add a new one.
     * @param id of Task to be updated
     * @param task Task object to be updated
     * @return task Updated Task object
     */
    public Task updateTask(int id, Task task) {
        Task updateTask = taskRepository.findById(id)
                .orElse(taskRepository.save(task));

        updateTask.setUserName(task.getUserName());
        updateTask.setTaskName(task.getTaskName());
        updateTask.setTaskDescription(task.getTaskDescription());
        updateTask.setTaskStatus(task.getTaskStatus());
        return taskRepository.save(updateTask);

    }

    /**
     * Update Task with patch API
     * @param id id of task to be patched
     * @param task task object to be patched
     * @return Task Object if not found then returns {@link com.work.task.exception.TaskNotFoundException }
     */
    public Task patchTask(int id, Task task) {
        Task updateTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task does not exist with id: " + id));

        if(StringUtils.hasLength(task.getUserName())){
            updateTask.setUserName(task.getUserName());
        }
        if(StringUtils.hasLength(task.getTaskName())) {
            updateTask.setTaskName(task.getTaskName());
        }
        if(StringUtils.hasLength(task.getTaskDescription())) {
            updateTask.setTaskDescription(task.getTaskDescription());
        }
        if(StringUtils.hasLength(task.getTaskStatus())) {
            updateTask.setTaskStatus(task.getTaskStatus());
        }
        return taskRepository.save(updateTask);
    }

    /**
     * Search Tasks on the basis of Status
     * @param status Task status to be searched
     * @return List of Tasks matching the status
     */
    public List<Task> searchByTaskStatus(String status) {
        return taskRepository.findAll().stream().filter(i -> i.getTaskStatus().equals(status)).
                collect(Collectors.toList());
    }

    /**
     * Search User's task on the basis of UserName
     * @param userName of the user
     * @return List of tasks associated with the userName
     */
    public List<Task> searchByUserName(String userName) {
        return taskRepository.findByUserName(userName);
    }
}
