CREATE SEQUENCE IF NOT EXISTS ACTIVITY_CATEGORY_SEQ START 510;
CREATE TABLE IF NOT EXISTS ACTIVITY_CATEGORY
(
    ID          SERIAL PRIMARY KEY,
    CODE        VARCHAR(20)  NOT NULL,
    DESCRIPTION VARCHAR(200) NOT NULL,
    LEVEL       INTEGER      NOT NULL,
    HELPTEXT    TEXT
);

CREATE SEQUENCE IF NOT EXISTS SEARCH_TERM_SEQ START 1;
CREATE TABLE IF NOT EXISTS SEARCH_TERM
(
    ID                 SERIAL PRIMARY KEY,
    ACTIVITY_CATEGORY_ID INTEGER      NOT NULL,
    TEXT               VARCHAR(200) NOT NULL,

    CONSTRAINT FK_SEARCHITEM_ACTIVITYCATEGORY
        FOREIGN KEY (ACTIVITY_CATEGORY_ID)
            REFERENCES ACTIVITY_CATEGORY (ID)
            ON DELETE CASCADE
);


CREATE SEQUENCE IF NOT EXISTS RESPONDENT_SEQ START 1;
CREATE TABLE IF NOT EXISTS RESPONDENT
(
    ID                  SERIAL PRIMARY KEY,
    NAME                VARCHAR,
    RESPONDENT_UUID     UUID NOT NULL,
    IO_NUMBER           INTEGER,
    PHONE               VARCHAR (20),
    EMAIL               VARCHAR (50),
    GENDER              VARCHAR (10),
    DATE_OF_BIRTH       DATE,
    AGE                 INTEGER,
    EDUCATION           VARCHAR(1),
    ADDRESS             VARCHAR (200),
    POSTCODE            VARCHAR (20),
    CITY                VARCHAR (50),
    REGION              VARCHAR(2),
    MUNICIPALITY_NUMBER VARCHAR(4),
    DWELLING_NUMBER     VARCHAR(5),
    DIARY_START         DATE,
    DIARY_END           DATE,
    STATUS_DIARY        VARCHAR,
    STATUS_SURVEY       VARCHAR,
    STATUS_RECRUITMENT  VARCHAR,
    STATUS_QUESTIONNAIRE VARCHAR,
    RECRUITMENT_START DATE,
    RECRUITMENT_END DATE,
    RECRUITMENT_MINUTES_SPENT INTEGER,
    ACCEPTED_INITIAL_DIARY_START BOOLEAN
);

-- CREATE SEQUENCE IF NOT EXISTS PURCHASE_SEQ START 1;
-- CREATE TABLE IF NOT EXISTS PURCHASE
-- (
--     ID               SERIAL PRIMARY KEY,
--     RESPONDENT_ID    INTEGER,
--     TOTALSUM         NUMERIC,
--     TIME_OF_PURCHASE DATE       NOT NULL,
--     CREATED_DATE     TIMESTAMP,
--     MODIFIED_DATE    TIMESTAMP,
--
--         CONSTRAINT FK_PURCHASE_RESPONDENT
--             FOREIGN KEY (RESPONDENT_ID)
--                 REFERENCES RESPONDENT (ID)
--                 ON DELETE CASCADE
-- );

-- CREATE SEQUENCE IF NOT EXISTS ITEM_SEQ START 1;
-- CREATE TABLE IF NOT EXISTS ITEM
-- (
--     ID                 SERIAL PRIMARY KEY,
--     PURCHASE_ID        INTEGER,
--     ACTIVITY_CATEGORY_ID INTEGER,
--     NAME               VARCHAR(200) NOT NULL,
--     TAX_PERCENTAGE     NUMERIC,
--     DISCOUNT           NUMERIC,
--     SUBTOTAL           NUMERIC      NOT NULL,
--     QUANTITY           NUMERIC,
--     UNIT               VARCHAR(20),
--     CREATED_DATE       TIMESTAMP,
--     MODIFIED_DATE      TIMESTAMP,
--
-- --     CONSTRAINT FK_ITEM_PURCHASE
-- --         FOREIGN KEY (PURCHASE_ID)
-- --             REFERENCES PURCHASE (ID)
-- --             ON DELETE CASCADE,
--     CONSTRAINT FK_ITEM_ACTIVITYCATEGORY
--         FOREIGN KEY (ACTIVITY_CATEGORY_ID)
--             REFERENCES ACTIVITY_CATEGORY (ID)
--             ON DELETE SET NULL
-- );

CREATE SEQUENCE IF NOT EXISTS RESPONDENT_MAPPER_SEQ START 1;
CREATE TABLE IF NOT EXISTS RESPONDENT_MAPPER
(
    ID              SERIAL PRIMARY KEY,
    RESPONDENT_UUID UUID NOT NULL,
    FNR             VARCHAR (11) NOT NULL
);


CREATE SEQUENCE IF NOT EXISTS COMMUNICATION_LOG_SEQ START 1;
CREATE TABLE IF NOT EXISTS COMMUNICATION_LOG
(
    ID                  SERIAL PRIMARY KEY,
    RESPONDENT_ID       INTEGER NOT NULL,
    DIRECTION           VARCHAR NOT NULL,
    COMMUNICATION_TRIGGERED  TIMESTAMP NOT NULL,
    CONFIRMED_SENT       TIMESTAMP,
    TYPE                VARCHAR NOT NULL,
    CATEGORY            VARCHAR NOT NULL,
    MESSAGE             TEXT,
    CREATED_BY          VARCHAR(20),
    SCHEDULED           TIMESTAMP,

    CONSTRAINT FK_COMMUNICATIONLOG_RESPONDENT
        FOREIGN KEY (RESPONDENT_ID)
            REFERENCES RESPONDENT (ID)
);

CREATE SEQUENCE IF NOT EXISTS APPOINTMENT_SEQ START 1;
CREATE TABLE IF NOT EXISTS APPOINTMENT
(
    ID                  SERIAL PRIMARY KEY,
    RESPONDENT_ID       INTEGER,
    APPOINTMENT_TIME    TIMESTAMP NOT NULL,
    DESCRIPTION         TEXT,
    ASSIGNED_TO         VARCHAR(20),
    CREATED_BY          VARCHAR(20),

    CONSTRAINT FK_APPOITNMENT_RESPONDENT
        FOREIGN KEY (RESPONDENT_ID)
            REFERENCES RESPONDENT (ID)
)
