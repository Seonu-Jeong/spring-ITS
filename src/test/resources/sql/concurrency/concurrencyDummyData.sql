INSERT INTO user (email, password, name, phone_number, created_at, modified_at, status, role)
WITH RECURSIVE cte (n) AS
                   (SELECT 1
                    UNION ALL
                    SELECT n + 1
                    FROM cte
                    WHERE n < 5 -- 생성하고 싶은 더미 데이터의 개수
                   )
SELECT concat(n, '@email.com') AS email,
       'Password1234@'         AS password,
       concat('user', n)       AS name,
       '010-1234-5678'         AS phone_number,
       now()                   AS created_at,
       now()                   AS modified_at,
       'ACTIVATED'             AS status,
       'USER'                  AS role
FROM cte;

INSERT INTO hall (id, capacity, location, is_open, name)
VALUES (1, 1, 'SEOUL', true, 'TEST');

INSERT INTO seat (id, seat_number, hall_id)
VALUES (1, 1, 1);

INSERT INTO seat (id, seat_number, hall_id)
VALUES (2, 2, 1);

INSERT INTO concert (id, singer, title, price, hall_id, start_at, end_at, running_start_time, running_end_time)
VALUES (1, 'TEST_SINGER', 'TEST_TITLE', 1, 1, '2070-12-15', '2070-12-31', '20:00:00', '21:00:00');
