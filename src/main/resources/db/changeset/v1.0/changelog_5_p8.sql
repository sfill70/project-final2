-- insert into ACTIVITY ( ID, AUTHOR_ID, TASK_ID, UPDATED, STATUS_CODE ) values
--     (1,2, 3, '2023-04-09 23:05:05', 'in progress');

INSERT INTO activity (id, author_id, task_id, updated, comment, title, description, estimate, type_code, status_code,
                      priority_code)
VALUES (1, 2, 3, '2023-04-08 15:39:29', 'test comment', 'Task-2', 'test 2 task', NULL, 'bug', 'in progress',        'normal'),
       (3, 2, 3, '2023-04-11 10:39:29.462', 'lower time', 'Task-2', 'test 2 task', NULL, 'bug', 'done', 'normal'),
       (4, 2, 3, '2023-04-12 16:39:29.462', 'new comment', 'Task-2', 'test 2 task', NULL, 'bug', NULL, 'normal'),
       (5, 2, 3, '2023-04-14 17:39:29.462', 'test comment', 'Task-2', 'test 2 task', NULL, 'bug', 'ready', 'normal'),
       (6, 2, 3, NULL, 'short test task', 'Task-2', 'test 2 task', NULL, 'bug', 'ready', 'normal');