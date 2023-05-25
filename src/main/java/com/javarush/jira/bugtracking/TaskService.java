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

import static com.javarush.jira.common.util.Util.getFormattedDuration;

@Service
public class TaskService extends BugtrackingService<Task, TaskTo, TaskRepository> {
    private final UserRepository userRepository;
    private final UserBelongRepository userBelongRepository;
    private final ActivityRepository activityRepository;
    private final TaskRepository taskRepository;
    public static final String STATUS_IN_PROGRESS = "in progress";
    public static final String STATUS_DONE = "done";
    public static final String STATUS_READY = "ready";

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

    /* п.7*/
    public void addUserToTask(Long taskId, Long userId) {
        Task task = repository.getExisted(taskId);
        User user = userRepository.getExisted(userId);
        UserBelong userBelongTask = new UserBelong();
        userBelongTask.setObjectId(task.getId());
        userBelongTask.setObjectType(ObjectType.TASK);
        userBelongTask.setUserId(user.getId());
        userBelongTask.setUserTypeCode(user.getRoles().stream().toList().get(user.getRoles().size() - 1).toString());
//        userBelongTask.setUserTypeCode("admin");
        userBelongRepository.save(userBelongTask);
    }

    // 8.add task summary
//    not NullPointerException method
    public Map<String, String> getTaskSummary(Long taskId) {
        Map<String, String> summary = new HashMap<>();
        Task task = null;
        try {
            task = repository.getExisted(taskId);
        } catch (Exception e) {
            summary.put(STATUS_IN_PROGRESS, "NO THIS TASK");
            summary.put(STATUS_DONE, "NO TASK");
            return summary;
        }
        List<Activity> activityList = activityRepository.findByTaskAndUpdatedNotNullAndStatusCodeNotNullOrderByUpdated(task);
        if (activityList.size() == 0) {
            summary.put(STATUS_IN_PROGRESS, "NO DATA");
            summary.put(STATUS_DONE, "NO DATA");
            return summary;
        }
        Map<String, LocalDateTime> updateMap = new HashMap<>();
        for (Activity activity : activityList) {
            updateMap.put(activity.getStatusCode(), activity.getUpdated());
        }
        String ready = updateMap.containsKey(STATUS_IN_PROGRESS) && updateMap.containsKey(STATUS_READY)
                ? getFormattedDuration(updateMap.get(STATUS_READY), updateMap.get(STATUS_IN_PROGRESS))
                : "TASK IN WORK";
        String done = updateMap.containsKey(STATUS_IN_PROGRESS) && updateMap.containsKey(STATUS_DONE)
                ? getFormattedDuration(updateMap.get(STATUS_DONE), updateMap.get(STATUS_IN_PROGRESS))
                : "TEST IN WORK";
        summary.put(STATUS_READY, ready);
        summary.put(STATUS_DONE, done);
        return summary;
    }

}
