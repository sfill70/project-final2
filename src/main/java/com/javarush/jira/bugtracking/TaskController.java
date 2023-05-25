package com.javarush.jira.bugtracking;

import com.javarush.jira.common.error.IllegalRequestDataException;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping
public class TaskController {

    private TaskService taskService;

    /*п. 6  */
    @PostMapping(path = "/{id}/tag", consumes = {"application/json", "application/x-www-form-urlencoded"})
    public String addTagToTask(@PathVariable("id") Long taskId, @RequestBody(required = false) @Size(min = 2, max = 32) String tag) {
        if (tag == null || tag.length() < 2 || tag.length() > 32) {
            throw new IllegalRequestDataException("wrong tag size");
        }
        taskService.addTagToTask(taskId, tag);
        return "redirect:/";
    }

}
