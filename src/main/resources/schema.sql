create table if not exists tester (
'id'   int  not null   AUTO_INCREMENT,
'dwadwa'   VARCHAR(50),
'dawwda'   TINYINT,
PRIMARY KEY (id),
);

create table if not exists who (
'id'   int UNIQUE   AUTO_INCREMENT,
'name'   VARCHAR(50),
'category'   TINYINT,
PRIMARY KEY (id),
FOREIGN KEY (id) REFERENCES tester(example),
);

