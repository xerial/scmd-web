create table parameterlist (id int primary key, name text, displayname text, shortname text, scope text, datatype text, description text);
\copy parameterlist from parameterlist.txt

