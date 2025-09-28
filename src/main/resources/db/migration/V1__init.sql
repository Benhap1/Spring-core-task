CREATE TABLE "USERS" (
                         "ID" UUID PRIMARY KEY,
                         "FIRST_NAME" VARCHAR(50) NOT NULL,
                         "LAST_NAME" VARCHAR(50) NOT NULL,
                         "USERNAME" VARCHAR(100) UNIQUE NOT NULL,
                         "PASSWORD" VARCHAR(255) NOT NULL,
                         "IS_ACTIVE" BOOLEAN NOT NULL
);

CREATE TABLE "TRAINING_TYPE" (
                                 "ID" UUID PRIMARY KEY,
                                 "TRAINING_TYPE_NAME" VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE "TRAINEE" (
                           "ID" UUID PRIMARY KEY,
                           "DATE_OF_BIRTH" DATE,
                           "ADDRESS" VARCHAR(255),
                           CONSTRAINT "FK_TRAINEE_USER" FOREIGN KEY ("ID") REFERENCES "USERS"("ID") ON DELETE CASCADE
);

CREATE TABLE "TRAINER" (
                           "ID" UUID PRIMARY KEY,
                           "SPECIALIZATION_ID" UUID NOT NULL,
                           CONSTRAINT "FK_TRAINER_USER" FOREIGN KEY ("ID") REFERENCES "USERS"("ID") ON DELETE CASCADE,
                           CONSTRAINT "FK_TRAINER_SPECIALIZATION" FOREIGN KEY ("SPECIALIZATION_ID") REFERENCES "TRAINING_TYPE"("ID")
);

CREATE TABLE "TRAINER_TRAINEE" (
                                   "TRAINER_ID" UUID NOT NULL,
                                   "TRAINEE_ID" UUID NOT NULL,
                                   PRIMARY KEY ("TRAINER_ID", "TRAINEE_ID"),
                                   CONSTRAINT "FK_TT_TRAINER" FOREIGN KEY ("TRAINER_ID") REFERENCES "TRAINER"("ID") ON DELETE CASCADE,
                                   CONSTRAINT "FK_TT_TRAINEE" FOREIGN KEY ("TRAINEE_ID") REFERENCES "TRAINEE"("ID") ON DELETE CASCADE
);

CREATE TABLE "TRAINING" (
                            "ID" UUID PRIMARY KEY,
                            "TRAINEE_ID" UUID NOT NULL,
                            "TRAINER_ID" UUID NOT NULL,
                            "TRAINING_NAME" VARCHAR(200) NOT NULL,
                            "TRAINING_TYPE_ID" UUID NOT NULL,
                            "TRAINING_DATE" DATE NOT NULL,
                            "TRAINING_DURATION" INT NOT NULL CHECK ("TRAINING_DURATION" > 0),
                            CONSTRAINT "FK_TRAINING_TRAINEE" FOREIGN KEY ("TRAINEE_ID") REFERENCES "TRAINEE"("ID") ON DELETE CASCADE,
                            CONSTRAINT "FK_TRAINING_TRAINER" FOREIGN KEY ("TRAINER_ID") REFERENCES "TRAINER"("ID") ON DELETE CASCADE,
                            CONSTRAINT "FK_TRAINING_TYPE" FOREIGN KEY ("TRAINING_TYPE_ID") REFERENCES "TRAINING_TYPE"("ID")
);
