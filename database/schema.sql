CREATE TABLE user_authentication(
   id INT PRIMARY KEY(one or more columns),
   pass_hash TEXT,
   secretQuestion TEXT,
   answer_hash TEXT
);

CREATE TABLE user_data(
   id INT,
   title TEXT,
   firstName TEXT,
   middleName TEXT,
   lastName TEXT,
   dateOfBirth TEXT,
   gender TEXT,
   maritalStatus TEXT,
   livingSituation TEXT,
   motherMaidenName TEXT,
   nationality TEXT,
   countryOfBirth TEXT,
   cityOfBirth TEXT,
   previousAddressPostcode TEXT,
   insuranceNumber TEXT,
   mobileNumber TEXT,
   residentUK TEXT,
   over18 TEXT,
   verified BOOL,
   FOREIGN KEY (id) REFERENCES user_authentication(id)
);

CREATE TABLE account_data(
   id INT PRIMARY KEY(one or more columns),
   pass_hash TEXT,
   secretQuestion TEXT,
   answer_hash TEXT
);

CREATE TABLE logs_authentication(
   id INT PRIMARY KEY,
   text TEXT,
   date TEXT
);