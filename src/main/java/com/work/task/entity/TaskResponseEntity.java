package com.work.task.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TaskResponseEntity - consist of Count of Tasks - and Task Objects - {@link com.work.task.entity.Task}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseEntity {
    private int count;
    private List<Task> taskList;

}
