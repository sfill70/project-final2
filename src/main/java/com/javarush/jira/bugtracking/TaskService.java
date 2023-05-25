package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.TaskMapper;
import com.javarush.jira.bugtracking.internal.model.Activity;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.internal.repository.ActivityRepository;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.bugtracking.to.TaskTo;
import com.javarush.jira.common.error.NotFoundException;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskService extends BugtrackingService<Task, TaskTo, TaskRepository> {
    private final UserRepository userRepository;
    private final UserBelongRepository userBelongRepository;
    private final ActivityRepository activityRepository;
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository repository, TaskMapper mapper, UserRepository userRepository, UserBelongRepository userBelongRepository, ActivityRepository activityRepository, TaskRepository taskRepository) {
        super(repository, mapper);
        this.userRepository = userRepository;
        this.userBelongRepository = userBelongRepository;
        this.activityRepository = activityRepository;
        this.taskRepository = taskRepository;
    }

    public List<TaskTo> getAll() {
        return mapper.toToList(repository.getAll());
    }

    /*п. 6  */
    public void addTagToTask(Long taskId, String tag) {
        Task task = repository.getExisted(taskId);
        Set<String> tags = task.getTags();
        tags.add(tag);
        task.getTags().addAll(tags);
        repository.save(task);
    }

    //    6. Add feature new tags
    public Task addTagsToTask(Long taskId, Set<String> tags) {
        Optional<Task> task = taskRepository.findTaskById(taskId);
        if (task.isPresent()) {
            if (!task.get().getTags().containsAll(tags)) {
                task.get().getTags().addAll(tags);
                repository.save(task.get());
            }
        } else {
            throw new NotFoundException("Task with id=" + taskId + " not found");
        }
        return task.get();
    }


}
