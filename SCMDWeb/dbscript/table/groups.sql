create table groups (id int primary key, stain char, name text, abnormality text, description text);
\copy groups from groups.txt
