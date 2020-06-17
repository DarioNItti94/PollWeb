create table Admin
(
  `id`             int unsigned auto_increment,
  `email`          varchar(255),
  `hashedPassword` char(64),
  primary key (id)
);

create table Supervisor
(
  `id`             int unsigned auto_increment,
  `firstName`      varchar(255),
  `lastName`       varchar(255),
  `email`          varchar(255),
  `hashedPassword` char(64),
  primary key (id)
);

create table Survey
(
  `id`           int unsigned auto_increment,
  `title`        varchar(255),
  `openingText`  text,
  `closingText`  text,
  `isReserved`   boolean,
  `isActive`     boolean,
  `isClosed`     boolean,
  `supervisorID` int unsigned,
  primary key (id),
  foreign key (supervisorID) references Supervisor (id)
);

create table Participant
(
  `id`             int unsigned auto_increment,
  `firstName`      varchar(255),
  `lastName`       varchar(255),
  `email`          varchar(255),
  `hashedPassword` char(64),
  `isDisabled`     boolean,
  `surveyID`       int unsigned,
  primary key (id),
  foreign key (surveyID) references Survey (id)
);

create table Question
(
  `id`          int unsigned auto_increment,
  `type`        enum ('short','long', 'number', 'date', 'single', 'multiple'),
  `text`        text,
  `note`        varchar(255),
  `isMandatory` boolean,
  `number`      int unsigned,
  `surveyID`    int unsigned,
  primary key (id),
  foreign key (surveyID) references Survey (id)
);

create table Choice
(
  `id`         int unsigned auto_increment,
  `value`       varchar(255),
  `number`     int unsigned,
  `questionID` int unsigned,
  primary key (id),
  foreign key (questionID) references Question (id)
);

create table Submission
(
  `id`            int unsigned auto_increment,
  `timestamp`     timestamp,
  `surveyID`      int unsigned,
  `participantID` int unsigned,
  primary key (id),
  foreign key (surveyID) references Survey (id),
  foreign key (participantID) references Participant (id)
);

create table Response
(
  `id`            int unsigned auto_increment,
  `value`          varchar(255),
  `questionID`    int unsigned,
  `participantID` int unsigned,
  `submissionID`  int unsigned,
  primary key (id),
  foreign key (questionID) references Question (id),
  foreign key (participantID) references Participant (id),
  foreign key (submissionID) references Submission (id)
);