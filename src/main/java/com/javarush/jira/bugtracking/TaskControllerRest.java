package com.javarush.jira.bugtracking;


import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.common.error.IllegalRequestDataException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Slf4j
@Controller
@AllArgsConstructor
@RestController
public class TaskControllerRest {
    private TaskService taskService;

    /*п. 6  */
    @PutMapping("/tasks/{id}")
    public ResponseEntity<?> addTagsTask(@Valid @PathVariable("id") Long id, @RequestBody @Size(min = 2, max = 32) String[] tags) {
        if (tags.length == 0) {
            throw new IllegalRequestDataException("empty list of tags");
        }
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].isEmpty() || tags[i].length() < 2 || tags[i].length() > 32) {
                throw new IllegalRequestDataException("incorrect tag length with number=" + (i + 1));
            }
        }
        Task task = taskService.addTagsToTask(id, Set.of(tags));
        return ResponseEntity.ok(task);
    }

}
