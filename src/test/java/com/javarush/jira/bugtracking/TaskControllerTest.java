package com.javarush.jira.bugtracking;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.javarush.jira.bugtracking.TaskTestData.USER_MAIL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskControllerTest extends AbstractControllerTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserBelongRepository userBelongRepository;

    @Autowired
    private UserRepository userRepository;

    //6. Add new tags
    @Transactional
    @Test
    @WithUserDetails(value = USER_MAIL)
    void addTagToTask() throws Exception {

        Optional<Task> optionalTask = taskRepository.getAll().stream()
                .findAny();
        assertTrue(optionalTask.isPresent());

        Task task = optionalTask.get();
        String tag1 = "tag1";
        String rest_url = "/" + task.getId() +"/tag";
        perform(MockMvcRequestBuilders.post(rest_url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(tag1)))
                .andExpect(status().is3xxRedirection());

        @SuppressWarnings("DataFlowIssue")
        Task updatedTask = taskRepository.getExisted(task.getId());
        assertThat(updatedTask.getTags().toString(), Matchers.containsString(tag1));
    }

    //7. Add subscribe
    @Transactional
    @Test
    @WithUserDetails(value = USER_MAIL)
    void addUserToTask() throws Exception {
        Optional<Task> optionalTask = taskRepository.findAll().stream()
                .findAny();
        assertTrue(optionalTask.isPresent());

        Task task = optionalTask.get();

        UserBelong userBelongTask = new UserBelong();
        userBelongTask.setObjectId(task.getId());

        List<Long> alreadyBelongedUsers = userBelongRepository.findAll().stream()
                .filter(userBelong -> userBelong.getObjectId().equals(task.getId()))
                .map(UserBelong::getUserId)
                .toList();

        List<User> all = userRepository.findAll();
        assertTrue(all.size() > 0);

        Optional<User> needToAttachUser = all.stream()
                .filter(user -> !alreadyBelongedUsers.contains(user.getId()))
                .findAny();
        assertTrue(needToAttachUser.isPresent());

        User user = needToAttachUser.get();

        perform(MockMvcRequestBuilders.post("/task/" + task.getId() + "/user/" + user.getId()))
                .andExpect(status().isOk());

        userBelongTask.setUserId(user.getId());
        userBelongRepository.findOne(Example.of(userBelongTask)).ifPresentOrElse(
                userBelong -> {
                    assertThat(userBelong.getObjectId(), Matchers.equalTo(task.getId()));
                    assertThat(userBelong.getUserId(), Matchers.equalTo(user.getId()));
                },
                () -> {
                    throw new AssertionError("UserBelong not found");
                }
        );
    }

}