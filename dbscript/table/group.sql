create table groups (id int primary key, strain char, name text, abnormality text, description text);
\copy groups from group.txt
