-- PhysicalED - Reset database (SQLite)
-- Questo script elimina e ricrea lo schema dati.

PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS score;
DROP TABLE IF EXISTS grading_scale;
DROP TABLE IF EXISTS speciality;
DROP TABLE IF EXISTS sport_category;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS class_section;
DROP TABLE IF EXISTS school_year;

CREATE TABLE school_year (
	id   INTEGER PRIMARY KEY AUTOINCREMENT,
	name TEXT NOT NULL UNIQUE
);

CREATE TABLE class_section (
	id             INTEGER PRIMARY KEY AUTOINCREMENT,
	name           TEXT NOT NULL,
	school_year_id INTEGER NOT NULL,
	CONSTRAINT fk_class_section_school_year
		FOREIGN KEY (school_year_id) REFERENCES school_year(id)
		ON DELETE CASCADE
);
CREATE INDEX idx_class_section_school_year_id ON class_section(school_year_id);

CREATE TABLE student (
	id               INTEGER PRIMARY KEY AUTOINCREMENT,
	first_name       TEXT NOT NULL,
	last_name        TEXT NOT NULL,
	gender           TEXT NOT NULL,
	class_section_id INTEGER NOT NULL,
	CONSTRAINT fk_student_class_section
		FOREIGN KEY (class_section_id) REFERENCES class_section(id)
		ON DELETE CASCADE
);
CREATE INDEX idx_student_class_section_id ON student(class_section_id);

CREATE TABLE sport_category (
	id   INTEGER PRIMARY KEY AUTOINCREMENT,
	name TEXT NOT NULL UNIQUE
);

CREATE TABLE speciality (
	id               INTEGER PRIMARY KEY AUTOINCREMENT,
	name             TEXT NOT NULL,
	sport_category_id INTEGER NOT NULL,
	CONSTRAINT fk_speciality_sport_category
		FOREIGN KEY (sport_category_id) REFERENCES sport_category(id)
		ON DELETE CASCADE,
	CONSTRAINT uq_speciality_name_per_sport UNIQUE (sport_category_id, name)
);
CREATE INDEX idx_speciality_sport_category_id ON speciality(sport_category_id);

CREATE TABLE grading_scale (
	id            INTEGER PRIMARY KEY AUTOINCREMENT,
	speciality_id INTEGER NOT NULL,
	gender        TEXT NOT NULL,
	min_value     REAL NOT NULL,
	max_value     REAL NOT NULL,
	grade         REAL NOT NULL,
	CONSTRAINT fk_grading_scale_speciality
		FOREIGN KEY (speciality_id) REFERENCES speciality(id)
		ON DELETE CASCADE
);
CREATE INDEX idx_grading_scale_speciality_id ON grading_scale(speciality_id);
CREATE INDEX idx_grading_scale_speciality_gender ON grading_scale(speciality_id, gender);

CREATE TABLE score (
	id            INTEGER PRIMARY KEY AUTOINCREMENT,
	student_id    INTEGER NOT NULL,
	speciality_id INTEGER NOT NULL,
	value         REAL NOT NULL,
	grade         REAL,
	CONSTRAINT fk_score_student
		FOREIGN KEY (student_id) REFERENCES student(id)
		ON DELETE CASCADE,
	CONSTRAINT fk_score_speciality
		FOREIGN KEY (speciality_id) REFERENCES speciality(id)
		ON DELETE CASCADE,
	CONSTRAINT uq_score_student_speciality UNIQUE (student_id, speciality_id)
);
CREATE INDEX idx_score_student_id ON score(student_id);
CREATE INDEX idx_score_speciality_id ON score(speciality_id);


