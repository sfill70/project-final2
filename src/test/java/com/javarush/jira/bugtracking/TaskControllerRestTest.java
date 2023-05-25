package com.javarush.jira.bugtracking;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.login.internal.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.javarush.jira.bugtracking.TaskTestData.SUMMARY_URL;
import static com.javarush.jira.bugtracking.TaskTestData.USER_MAIL;
import static com.javarush.jira.common.util.Util.getFormattedDuration;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerRestTest extends AbstractControllerTest {
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
    void addTagsTas() throws Exception {

        Optional<Task> optionalTask = taskRepository.getAll().stream()
                .findAny();
        assertTrue(optionalTask.isPresent());

        Task task = optionalTask.get();
        String tag1 = "tag1";
        String tag2 = "tag2";
        Set<String> tags = task.getTags();
        while (tags.contains(tag1) || tags.contains(tag2)) {
            if (tags.contains(tag1)) {
                tag1 += "1";
            }
            if (tags.contains(tag2)) {
                tag2 += "2";
            }
        }
        List<String> newTags = List.of(tag1, tag2);

        String rest_url = "/tasks/" + task.getId();
        perform(MockMvcRequestBuilders.put(rest_url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTags)))
                .andExpect(status().isOk());
        @SuppressWarnings("DataFlowIssue")
        Task updatedTask = taskRepository.getExisted(task.getId());
        assertThat(updatedTask.getTags(), Matchers.containsInAnyOrder(tag1, tag2));
    }

    // 8. Add phase summary
    @Test
    @WithUserDetails(value = USER_MAIL)
    void getSummaryValid() throws Exception {

        LocalDateTime inProgress = LocalDateTime.of(2023, Month.APRIL, 10, 10, 0, 48);
        LocalDateTime inTest = LocalDateTime.of(2023, Month.APRIL, 15, 15, 22, 50);
        LocalDateTime done = LocalDateTime.of(2023, Month.MAY, 18, 17, 59, 59);

        String expectedProgress = getFormattedDuration(inProgress, inTest);
        String expectedTest = getFormattedDuration(inProgress, done);
        String resultString = "{" + "ready:" + expectedProgress + "," + "done" + ":" + expectedTest + "}";

        perform(MockMvcRequestBuilders.post(SUMMARY_URL))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString().replaceAll("-", "")
                            .replaceAll("\"", "").replaceAll(" ", "");
                    assertThat(contentAsString, Matchers.containsString(resultString.replaceAll(" ", "")));
                });
    }

}
