package com.work.task;

import com.work.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@RunWith(SpringRunner.class)
class TaskApplicationRepoTest {

    @Autowired
    TaskRepository taskRepository;

    @Test
    public void testCanFindUsersTask(){
        assertNotNull(taskRepository.findByUserName("ABC"));
    }

}
