create table group (id int primary key, strain char, name text, abnormality text, description text);
\copy group from group.txt
