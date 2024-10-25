--2024-10-22
--javaUser / mysql / jabadb

--게시판(board)
create table board(
bno int auto_increment,
title varchar(500) not null,
writer varchar(100) not null,
content text,
regdate datetime default now(),
moddate datetime default now(),
primary key(bno));

--2024-10-23
-- 댓글(comment)
create table comment(
cno int auto_increment,
bno int not null,
content varchar(2000) not null,
writer varchar(500) default "unknown",
regdate datetime default now(),
primary key(cno));

--2024-10-24
-- 게시판(board)에 조회수(readCount, int) 칼럼 추가
alter table board
add column readCount int default 0;
-- 게시판에 이미지 파일 칼럼 추가
alter table board add imageFile varchar(500);

--2024-10-25
-- 멤버(member) 테이블 생성
create table member(
id varchar(100),
pwd varchar(100) not null,
email varchar(200) not null,
phone varchar(50),
regdate datetime default now(),
laselogin datetime default now(),
primary key(id));
-- 잘못된 칼럼명 수정
ALTER TABLE member 
CHANGE laselogin lastlogin DATETIME DEFAULT NOW();
-- 멤버에 이미지 파일 칼럼 추가
alter table member add imageFile varchar(500);

