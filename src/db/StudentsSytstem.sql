CREATE TABLE student(
  student_id bigint(20) NOT NULL,
  student_name varchar(36) DEFAULT NULL
  class_id int(11) DEFAULT NULL,
  PRIMARY KEY (student_id)
	);

create table score(score_id bigint auto_increment,lesson_id int, student_id bigint,score float,close_status int,primary key(lesson_id,student_id),constraint LessonandStudent foreign key(student_id) references student(student_id));
create table sysuser(
	sysuser_id int not null auto_increment,
	sysuser_name varchar(24),
	sysuser_password varchar(24),
	sysuser_role int,
	foreign_id int,
	primary key (sysuser_id)
	);
create table teacher(teacher_id int auto_increment,
	teacher_name varchar(36),
	primary key(teacher_id));

insert teacher(teacher_name) values('teacher');
create table teachlession(teachlession_id int auto_increment,
	lession_id int,
	teacher_id int,
	primary key(teachlession_id));
insert teachlession(lession_id,teacher_id) values(2,1);
insert into sysuser(sysuser_name,sysuser_password,sysuser_role) values('admin','admin',1);

alter table student add foreign key(student_name) references sysuser(syuser_name) on delete cascade on update cascade;

create table lession(
	lession_id int not null auto_increment,
	lession_name varchar(40),
	primary key(lession_id)
	);
insert into score(lession_id,student_id,score,close_status) values(1,1,80,0);

select distinct score.lession_id as lession_id,class.class_name as class_name,student.class_id as class_id,
		lession.lession_name from score,student,lession,class where score.student_id=student.student_id and
		student.class_id=class.class_id and lession.lession_id=score.lession_id;
update score set close_status=1 where lession_id=1 and student_id in (select student_id from student where class_id=11;

select score from score where score.student_id=1 and score.lession_id=1;

select distinct student.student_name as student_name,
	lession.lession_name as lession_name,
	score.score as score
	from student,lession,score
	where score.lession_id=lession.lession_id
	and score.student_id=student.student_id order by student_name
	and student.class_id=11
	and student.student_name like '%yan%';



SELECT count(*) as count FROM 
	(select distinct student.student_name as student_name,
	lession.lession_name as lession_name,
	score.score as score
	from student,lession,score
	where score.lession_id=lession.lession_id
	and score.student_id=student.student_id
	and student.class_id=11
	and student.student_name like '%yan%') as tem GROUP BY student_name;

select distinct teachlession.teacher_id as teacher_id,
	teacher.teacher_name as teacher_name,
	lession.lession_id as lession_id,
	lession.lession_name as lession_name,
	teachlession.teachlession_id as teachlession_id
	from teacher,lession,teachlession
	where teachlession.teacher_id=teacher.teacher_id
	and teachlession.lession_id=lession.lession_id;