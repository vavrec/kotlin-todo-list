CREATE TABLE to_do_list_user
(
    user_id UUID PRIMARY KEY,
    name    VARCHAR(50)  NOT NULL,
    email   VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT email_check CHECK (email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'
) );


CREATE TABLE list
(
    list_id UUID PRIMARY KEY,
    name    VARCHAR(150) NOT NULL
);


CREATE TABLE task
(
    task_id UUID PRIMARY KEY,
    name    VARCHAR,
    list_id UUID,
    FOREIGN KEY (list_id) REFERENCES list (list_id)
);

CREATE TABLE list_user
(
    user_id UUID,
    list_id UUID,
    PRIMARY KEY (user_id, list_id),
    FOREIGN KEY (user_id) REFERENCES to_do_list_user (user_id),
    FOREIGN KEY (list_id) REFERENCES list (list_id)
);