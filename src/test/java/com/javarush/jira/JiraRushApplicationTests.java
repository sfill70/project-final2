package com.javarush.jira;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yaml")
@SpringBootTest
class JiraRushApplicationTests {
	@Test
	void contextLoads() {
	}
}
