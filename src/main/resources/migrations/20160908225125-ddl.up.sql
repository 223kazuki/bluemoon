CREATE TABLE PROJECT (
  ID NUMBER(12) PRIMARY KEY,
  START_DATE CHAR(8),
  NAME VARCHAR(50) NOT NULL
);
CREATE TABLE MEMBER (
  ID NUMBER(12) PRIMARY KEY,
  NAME VARCHAR(50) NOT NULL
);
CREATE TABLE PROJECT_MEMBER (
  PROJECT_ID NUMBER(12),
  MEMBER_ID NUMBER(12),
  PRIMARY KEY(PROJECT_ID, MEMBER_ID),
  FOREIGN KEY(PROJECT_ID) REFERENCES PROJECT(ID),　　
  FOREIGN KEY(MEMBER_ID) REFERENCES MEMBER(ID)
);

CREATE TABLE TASK (
  ID NUMBER(12) PRIMARY KEY,
  PROJECT_ID NUMBER(12) NOT NULL,
  MEMBER_ID NUMBER(12),
  NAME VARCHAR(50) NOT NULL,
  TYPE CHAR(2) NOT NULL,
  MAN_HOUR DOUBLE,
  TASK_ORDER NUMBER(12) NOT NULL,
  FOREIGN KEY(PROJECT_ID) REFERENCES PROJECT(ID),　　
  FOREIGN KEY(MEMBER_ID) REFERENCES MEMBER(ID)
);

CREATE TABLE CALENDAR_DATE (
  YYYYMMDD CHAR(8),
  PROJECT_ID NUMBER(12) NOT NULL,
  RUNNING NUMBER(12) NOT NULL,
  HOLIDAY CHAR(1),
  FOREIGN KEY(PROJECT_ID) REFERENCES PROJECT(ID),
  PRIMARY KEY(PROJECT_ID, YYYYMMDD)
);

CREATE TABLE MEMBER_CALENDAR_DATE (
  YYYYMMDD CHAR(8),
  PROJECT_ID NUMBER(12) NOT NULL,
  MEMBER_ID NUMBER(12) NOT NULL,
  RUNNING NUMBER(12) NOT NULL,
  HOLIDAY CHAR(1),
  FOREIGN KEY(PROJECT_ID) REFERENCES CALENDAR_DATE(PROJECT_ID),
  FOREIGN KEY(YYYYMMDD) REFERENCES CALENDAR_DATE(YYYYMMDD),
  FOREIGN KEY(MEMBER_ID) REFERENCES MEMBER(ID),
  PRIMARY KEY(PROJECT_ID, MEMBER_ID, YYYYMMDD)
);

CREATE SEQUENCE PROJECT_ID_SEQ;
CREATE SEQUENCE MEMBER_ID_SEQ;
CREATE SEQUENCE TASK_ID_SEQ;
