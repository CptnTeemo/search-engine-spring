DROP TABLE IF EXISTS field;

CREATE TABLE field(
id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
name VARCHAR(255) NOT NULL,
selector VARCHAR(255) NOT NULL,
weight FLOAT NOT NULL
);

INSERT INTO field (id, name, selector, weight)
VALUES (1, 'title', 'title', 1);

INSERT INTO field (id, name, selector, weight)
VALUES (2, 'body', 'body', 0.8 );