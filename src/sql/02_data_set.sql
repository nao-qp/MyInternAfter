USE overtimedb;

DELETE FROM users;
DELETE FROM departments;
DELETE FROM work_patterns;
DELETE FROM roles;



INSERT INTO users (id, account, pass, name, departments_id, roles_id, work_patterns_id)
VALUE (1, '1', '$2a$10$ma3yWLUZlHCD8gVP1fgB..eW1REVBmYh1SRdbGQzgIyct22uWTeGu', '社員A', 1, 1, 1);
INSERT INTO users (id, account, pass, name, departments_id, roles_id, work_patterns_id)
VALUE (2, '2', '$2a$10$ma3yWLUZlHCD8gVP1fgB..eW1REVBmYh1SRdbGQzgIyct22uWTeGu', '社員B', 1, 1, 1);
INSERT INTO users (id, account, pass, name, departments_id, roles_id, work_patterns_id)
VALUE (3, '3', '$2a$10$ma3yWLUZlHCD8gVP1fgB..eW1REVBmYh1SRdbGQzgIyct22uWTeGu', '社員C', 1, 1, 1);
INSERT INTO users (id, account, pass, name, departments_id, roles_id, work_patterns_id)
VALUE (4, '4', '$2a$10$ma3yWLUZlHCD8gVP1fgB..eW1REVBmYh1SRdbGQzgIyct22uWTeGu', '次長', 1, 2, 1);
INSERT INTO users (id, account, pass, name, departments_id, roles_id, work_patterns_id)
VALUE (5, '5', '$2a$10$ma3yWLUZlHCD8gVP1fgB..eW1REVBmYh1SRdbGQzgIyct22uWTeGu', '課長', 1, 3, 1);
INSERT INTO users (id, account, pass, name, departments_id, roles_id, work_patterns_id)
VALUE (6, '6', '$2a$10$ma3yWLUZlHCD8gVP1fgB..eW1REVBmYh1SRdbGQzgIyct22uWTeGu', '人事部社員', 2, 4, 1);


INSERT INTO departments (id, name)
VALUE (1, '営業部');
INSERT INTO departments (id, name)
VALUE (2, '人事部');

INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (1, '早出A', '05:30:00', '14:15:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (2, '早出B', '06:00:00', '14:45:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (3, '早出C', '06:30:00', '15:15:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (4, '早出D', '07:00:00', '15:45:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (5, '早出E', '07:30:00', '16:15:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (6, '早出F', '08:00:00', '16:45:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (7, '通常A', '08:30:00', '17:15:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (8, '通常B', '09:00:00', '17:45:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (9, '通常C', '09:30:00', '18:15:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (10, '遅出A', '10:00:00', '18:45:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (11, '遅出B', '10:30:00', '19:15:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (12, '遅出C', '11:00:00', '19:45:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (13, '遅出D', '11:30:00', '20:15:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (14, '遅出E', '12:00:00', '20:45:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (15, '遅出F', '12:30:00', '21:15:00');
INSERT INTO work_patterns (id, name, start_time, end_time)
VALUE (16, '遅出G', '13:00:00', '21:45:00');


INSERT INTO roles (id, name, role)
VALUE (1, '社員', '0');
INSERT INTO roles (id, name, role)
VALUE (2, '次長', '1');
INSERT INTO roles (id, name, role)
VALUE (3, '課長', '2');
INSERT INTO roles (id, name, role)
VALUE (4, '人事部', '3');

