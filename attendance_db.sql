
DROP DATABASE IF EXISTS attendance_db;
CREATE DATABASE attendance_db;
USE attendance_db;


CREATE TABLE staff (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    qualification VARCHAR(50),
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);


CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_number VARCHAR(50) NOT NULL UNIQUE,
    department VARCHAR(30) NOT NULL
);


CREATE TABLE subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);


CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    attendance_date DATE NOT NULL,
    status ENUM('P', 'A') NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (subject_id) REFERENCES subjects(id)
);


INSERT INTO students (name, roll_number, department) VALUES
('John Doe',        'CSE21A001',      'CSE'),
('Gopi',       	    'CSE21A002',      'CSE'),
('Jane Smith',      'CSM66A001',      'CSE(AI & ML)'),
('Abu',		    'CSM66A002',      'CSE(AI & ML)'),
('Alice Johnson',   'CSD67A001',      'CSE(DS)'),
('G.Jayanth',	    'CSD67A002',      'CSE(DS)'),
('Bob Wilson',      'AIDS68A001',     'CSE(AI & DS)'),
('Barbra ',         'AIDS68A002',     'CSE(AI & DS)'),
('Charlie Brown',   'CSBS70A001',     'CSBS'),
('Charmi',   	    'CSBS70A002',     'CSBS'),
('David Miller',    'ECE04A001',      'ECE'),
('Suraj',    	    'ECE04A002',      'ECE'),
('Emily Davis',     'EEE02A001',      'EEE'),
('Rock Denis',      'EEE02A002',      'EEE'),
('Frank Thomas',    'BME01A001',      'BME'),
('Issac Newton',    'BME01A002',      'BME'),
('Grace Lee',       'CV07A001',       'CIVIL'),
('Han Lee',         'CV07A002',       'CIVIL'),
('Henry Clark',     'PHE20A001',      'PHE'),
('Boyles Charls',   'PHE20A002',      'PHE'),
('Irene Walker',    'MECH03A001',     'MECH'),
('Cassady Runner',  'MECH03A002',     'MECH'),
('Kevin Johnson',   'MBA77A001',      'MBA'),
('Peter parker',    'MBA77A002',      'MBA');


INSERT INTO subjects (name) VALUES
('Mathematics'),
('Physics'),
('Programming');


INSERT INTO staff (name, qualification, email, password) VALUES
('John Teacher', 'M Tech', 'john@college.edu', '1234'),
('Jane Prof',   'M Tech', 'jane@college.edu', '5678');


INSERT INTO attendance (student_id, subject_id, attendance_date, status) VALUES
(1, 1, '2025-12-01', 'P'),
(1, 2, '2025-12-01', 'A'),
(2, 1, '2025-12-01', 'P'),
(3, 3, '2025-12-02', 'P'),
(4, 1, '2025-12-02', 'A'),
(5, 1, '2025-12-03', 'P'),
(6, 2, '2025-12-03', 'A'),
(7, 1, '2025-12-01', 'P'),
(8, 2, '2025-12-01', 'A'),
(9, 1, '2025-12-01', 'P'),
(10, 3, '2025-12-02', 'P'),
(11, 1, '2025-12-02', 'A'),
(12, 1, '2025-12-03', 'P'),
(13, 2, '2025-12-03', 'A'),
(14, 1, '2025-12-01', 'P'),
(15, 2, '2025-12-01', 'A'),
(16, 1, '2025-12-01', 'P'),
(17, 3, '2025-12-02', 'P'),
(18, 1, '2025-12-02', 'A'),
(19, 1, '2025-12-03', 'P'),
(20, 2, '2025-12-03', 'A'),
(21, 1, '2025-12-01', 'P'),
(22, 2, '2025-12-01', 'A'),
(23, 1, '2025-12-01', 'P');
