CREATE DATABASE sfs;

DROP TABLE stock;
CREATE TABLE stock
(
    id SERIAL NOT NULL,
    code character varying(6) NOT NULL,
    CONSTRAINT pk_stock PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE stock OWNER TO postgres;

DROP TABLE shareholder;
CREATE TABLE shareholder
(
    id SERIAL NOT NULL,
    code character varying(6) NOT NULL,
    time character varying(10) NOT NULL,
    holderNum float,
    stockNum float,
    price float,
    money float,
    top10 float,
    CONSTRAINT pk_shareholder PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE shareholder OWNER TO postgres;

DROP TABLE team;
CREATE TABLE team
(
    id SERIAL NOT NULL,
    name character varying(20) NOT NULL,
    teamid int NOT NULL,
    CONSTRAINT pk_team PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE team OWNER TO postgres;

DROP TABLE board;
CREATE TABLE board
(
    id SERIAL NOT NULL,
    league character varying(10) NOT NULL,
    year int NOT NULL,
    rank int NOT NULL,
    team character varying(20) NOT NULL,
    matchcnt int,
    wincnt int,
    drawcnt int,
    losecnt int,
    goals int,
    losegoals int,
    CONSTRAINT pk_board PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE board OWNER TO postgres;

DROP TABLE board_sum;
CREATE TABLE board_sum
(
    id SERIAL NOT NULL,
    league character varying(10) NOT NULL,
    type int NOT NULL,
    year int NOT NULL,
    winratio float,
    drawratio float,
    loseratio float,
    CONSTRAINT pk_board_sum PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE board_sum OWNER TO postgres;

DROP TABLE match;
CREATE TABLE match
(
    id SERIAL NOT NULL,
    league character varying(10) NOT NULL,
    year int NOT NULL,
    time character varying(20) NOT NULL,
    turn int NOT NULL,
    host character varying(20) NOT NULL,
    score character varying(10) NOT NULL,
    guest character varying(20) NOT NULL,
    halfscore character varying(10),
    scoreret int NOT NULL,
    goalcnt int NOT NULL,
    CONSTRAINT pk_match PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE match OWNER TO postgres;

DROP TABLE matchinfo;
CREATE TABLE matchinfo
(
    id SERIAL NOT NULL,
    league character varying(10) NOT NULL,
    year int NOT NULL,
    time character varying(30) NOT NULL,
    turn int NOT NULL,
    host int NOT NULL,
    guest int NOT NULL,
    score character varying(10) NOT NULL,
    scoreret int NOT NULL,
    goalcnt int NOT NULL,
    bjopid int NOT NULL,
    CONSTRAINT pk_matchinfo PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE matchinfo OWNER TO postgres;

DROP TABLE bjop;
CREATE TABLE bjop
(
    id SERIAL NOT NULL,
    league character varying(10) NOT NULL,
    year int NOT NULL,
    bjopid int NOT NULL,
    avg character varying(20) NOT NULL,
    jc character varying(20) NOT NULL,
    lb character varying(20) NOT NULL,
    mc character varying(20) NOT NULL,
    hk character varying(20) NOT NULL,
    CONSTRAINT pk_bjop PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bjop OWNER TO postgres;

DROP TABLE matchsum;
CREATE TABLE matchsum
(
    id SERIAL NOT NULL,
    league character varying(10) NOT NULL,
    year int NOT NULL,
    turn int NOT NULL,
    winning3 float,
    winning1 float,
    winning0 float,
    stake int,
    matchcnt int,
    wincnt int,
    drawcnt int,
    losecnt int,
    CONSTRAINT pk_matchsum PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE matchsum OWNER TO postgres;

DROP TABLE odds;
CREATE TABLE odds
(
    id SERIAL NOT NULL,
    league character varying(10) NOT NULL,
    year integer NOT NULL,
    turn int,
    host character varying(20) NOT NULL,
    guest character varying(20) NOT NULL,
    jc3 real,
    jc1 real,
    jc0 real,
    mc3 real,
    mc1 real,
    mc0 real,
    hk3 real,
    hk1 real,
    hk0 real,
    lb3 real,
    lb1 real,
    lb0 real,
    avg3 real,
    avg1 real,
    avg0 real,
    file character varying(20) NOT NULL,
    CONSTRAINT pk_odds PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE odds OWNER TO postgres;

INSERT INTO matchinfo (league, year, time, turn, host, guest, score, scoreret, goalcnt, bjopid)  VALUES 
('arg', 2015, '2015 08-30 12:00:00.0', 299, 708, 34, '2:2', 1, 4, 1746577);
INSERT INTO matchinfo (league, year, time, turn, host, guest, score, scoreret, goalcnt, bjopid)  VALUES 
('arg', 2014, '08-30 12:00:00.0', 299, 708, 34, '2:2', 1, 4, 1746578);
INSERT INTO bjop (league, year, bjopid, avg, jc, lb, mc, hk)  VALUES 
('arg', 2014, 1746577, '2.91,2.92,2.46', '2.91,2.92,2.46', '2.91,2.92,2.46', '2.91,2.92,2.46', '2.91,2.92,2.46');
INSERT INTO bjop (league, year, bjopid, avg, jc, lb, mc, hk)  VALUES 
('arg', 2014, 1746578, '2.91,2.92,2.46', '2.91,2.92,2.46', '2.91,2.92,2.46', '2.91,2.92,2.46', '2.91,2.92,2.46');

INSERT INTO odds (league, year, turn, host, guest, jc3, jc1, jc0, mc3, mc1, mc0, hk3, hk1, hk0, lb3, lb1, lb0,avg3, avg1, avg0, file)  VALUES 
('arg', 2013, 31, '奥林匹奥', '拉普体操', 2.32, 2.85, 2.92, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,'500odds.txt');

INSERT INTO board VALUES (999999,'测试联赛',2014,1,'测试',10,8,2,0,30,10);
SELECT * FROM board;
DELETE FROM board;

INSERT INTO board_sum VALUES (999999,'测试联赛',1,2013,2.2,2.2,2.2);
SELECT * FROM board_sum;
DELETE FROM board_sum;