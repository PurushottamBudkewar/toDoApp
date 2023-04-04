package com.work.task.controller;

import com.work.task.entity.Task;
import com.work.task.entity.TaskResponseEntity;
import com.work.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = { "http://localhost:8080"})
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;


    /**
     *get all Tasks and count of tasks in TaskResponseEntity
     * @return {@link com.work.task.entity.TaskResponseEntity}
     * having list of {@link com.work.task.entity.Task} objects and count
     */
    @GetMapping("/")
    public TaskResponseEntity getAllTasks(){
        List<Task> taskList = taskService.getAllTasks();
        TaskResponseEntity taskResponseEntity = new TaskResponseEntity();
        if(taskList!=null && taskList.size() > 0){
            taskResponseEntity.setTaskList(taskList);
            taskResponseEntity.setCount(taskList.size());
        }
        return  taskResponseEntity;
    }
    /**
     * Returns {@link org.springframework.hateoas.EntityModel} of
     * {@link com.work.task.entity.Task} Object
     * @param id id of record to be retrieved
     * @return Entity Model of Task entity with HATEOAS
     */
    @GetMapping("/{id}")
    public EntityModel<Task> getOneTask(@PathVariable int id){
        Task task = taskService.findTaskById(id);
        EntityModel<Task> model = EntityModel.of(task);
        WebMvcLinkBuilder linkTo =
                WebMvcLinkBuilder.linkTo(methodOn(this.getClass()).getAllTasks());
        model.add(linkTo.withRel("all-tasks"));
        return model;
    }

    /**
     *Adds given task object to Database
     * @param task task json object in RequestBody
     * @return Added task details
     */
    @PostMapping("/addTask")
    public Task addTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    /**
     * Adds list of tasks
     * @param tasks - list of tasks to be added
     * @return List of Tasks added
     */
    @PostMapping("/addTasks")
    public List<Task> addTasks(@RequestBody List<Task> tasks) {
        return taskService.saveTasks(tasks);
    }

    /**
     * Updates Task on the basis of id making use of PutMapping
     * @param id - id of record to be updated
     * @param task - task json object in RequestBody
     * @return Task - updated Task object
     */
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable int id,@RequestBody Task task){
         return taskService.updateTask(id,task);
    }


    /**
     * Updates Task with Patch mapping
     * @param id of the object to be updated
     * @param task json object to be updated
     * @return Updated Task object
     */
    @PatchMapping("/{id}")
    public Task patchTask(@PathVariable int id,@RequestBody Task task){
        return taskService.patchTask(id,task);
    }

    /**
     * Delete Task on the basis of id passed
     * @param id - id of record to be deleted
     * @return String - having id of deleted record
     */
    @DeleteMapping("/{id}")
    public String deleteTaskById(@PathVariable int id){
        return taskService.deleteTaskById(id);
    }

    /**
     * Search Tasks on the basis of Status
     * @param status status with which Tasks to be searched
     * @return List of Task matching the status
     */
    @GetMapping("/search/{status}")
    public List<Task> searchByTaskStatus(@PathVariable String status){
        return taskService.searchByTaskStatus(status);
    }


    /**
     * To find all tasks associated with given User
     * @param userName User's name
     * @return List of Tasks for that user
     */
    @GetMapping("/search/user/{userName}")
    public List<Task> searchByUserName(@PathVariable String userName){
        return taskService.searchByUserName(userName);
    }

}
