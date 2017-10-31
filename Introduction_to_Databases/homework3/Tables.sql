-- Sun Oct 29 16:31:46 201
-- Author: Shenzhi Zhang

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema RegistrationSystem
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `RegistrationSystem`;
CREATE SCHEMA IF NOT EXISTS `RegistrationSystem` DEFAULT CHARACTER SET utf8;
USE `RegistrationSystem` ;

-- -----------------------------------------------------
-- Table `RegistrationSystem`.`department`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RegistrationSystem`.`department` (
  `Department_id` CHAR(4) NOT NULL,
  `Name` VARCHAR(32) NULL DEFAULT NULL,
  PRIMARY KEY (`Department_id`),
  UNIQUE INDEX `name_UNIQUE` (`Name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `RegistrationSystem`.`person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RegistrationSystem`.`person` (
  `UNI` VARCHAR(12) NOT NULL,
  `Last_Name` VARCHAR(45) NOT NULL,
  `First_Name` VARCHAR(45) NOT NULL,
  `Type` CHAR(25) NOT NULL,
  PRIMARY KEY (`UNI`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `RegistrationSystem`.`courses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RegistrationSystem`.`courses` (
  `Department_id` CHAR(4) NOT NULL,
  `Level` ENUM('0', '1', '2', '3', '4', '5', '6', '7', '8', '9') NOT NULL,
  `Number` CHAR(3) NOT NULL,
  `Title` VARCHAR(32) NOT NULL,
  `Description` VARCHAR(128) NOT NULL,
  `Course_id` VARCHAR(12) GENERATED ALWAYS AS (concat(`Department_id`,`Level`,`Number`)) STORED,
  UNIQUE INDEX `course_id` (`Course_id` ASC),
  FULLTEXT INDEX `keywords` (`Title` ASC, `Description` ASC),
  INDEX `course2_dept_fk_idx` (`Department_id` ASC),
  CONSTRAINT `course2_dept_fk`
    FOREIGN KEY (`Department_id`)
    REFERENCES `RegistrationSystem`.`department` (`Department_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `RegistrationSystem`.`sections`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RegistrationSystem`.`sections` (
  `Course_id` VARCHAR(12) NOT NULL,
  `Section_no` VARCHAR(45) NOT NULL,
  `Year` INT(11) NOT NULL,
  `Semester` ENUM('1', '2', '3', '4') NOT NULL,
  `LimitNum` INT NOT NULL,
  `Section_key` VARCHAR(45) GENERATED ALWAYS AS (concat(`Year`,`Semester`,`Course_id`,`Section_no`)) STORED,
  PRIMARY KEY (`Section_key`),
  UNIQUE INDEX `unique` (`Course_id` ASC, `Section_no` ASC, `Year` ASC, `Semester` ASC),
  CONSTRAINT `section_course_fk`
    FOREIGN KEY (`Course_id`)
    REFERENCES `RegistrationSystem`.`courses` (`Course_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `RegistrationSystem`.`course_participant`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RegistrationSystem`.`course_participant` (
  `UNI` VARCHAR(12) NOT NULL,
  `Section_key` CHAR(45) NOT NULL,
  `Type` ENUM('Student', 'Faculty') NOT NULL,
  PRIMARY KEY (`UNI`, `Section_key`),
  INDEX `cp_section_fk` (`Section_key` ASC),
  CONSTRAINT `cp_participant_fk`
    FOREIGN KEY (`UNI`)
    REFERENCES `RegistrationSystem`.`person` (`UNI`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `cp_section_fk`
    FOREIGN KEY (`Section_key`)
    REFERENCES `RegistrationSystem`.`sections` (`Section_key`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `RegistrationSystem`.`course_prereqs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RegistrationSystem`.`course_prereqs` (
  `course_id` VARCHAR(12) NOT NULL,
  `prereq_id` VARCHAR(12) NOT NULL,
  PRIMARY KEY (`course_id`, `prereq_id`),
  INDEX `prereq_prereq_fk` (`prereq_id` ASC),
  CONSTRAINT `prereq_course_fk`
    FOREIGN KEY (`course_id`)
    REFERENCES `RegistrationSystem`.`courses` (`Course_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `prereq_prereq_fk`
    FOREIGN KEY (`prereq_id`)
    REFERENCES `RegistrationSystem`.`courses` (`Course_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `RegistrationSystem`.`student`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RegistrationSystem`.`student` (
  `UNI` VARCHAR(12) NOT NULL,
  `Last_Name` VARCHAR(45) NOT NULL,
  `First_Name` VARCHAR(45) NOT NULL,
  `Year` VARCHAR(4) NOT NULL DEFAULT '2017',
  `GPA` FLOAT NOT NULL,
  PRIMARY KEY (`UNI`),
  CONSTRAINT `student_fk_uni`
    FOREIGN KEY (`UNI`)
    REFERENCES `RegistrationSystem`.`person` (`UNI`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `RegistrationSystem`.`faculty`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RegistrationSystem`.`faculty` (
  `UNI` VARCHAR(12) NOT NULL,
  `Last_Name` VARCHAR(45) NOT NULL,
  `First_Name` VARCHAR(45) NOT NULL,
  `Pay_grade` ENUM('1', '2', '3', '7') NOT NULL,
  PRIMARY KEY (`UNI`),
  CONSTRAINT `faculty_fk_uni`
    FOREIGN KEY (`UNI`)
    REFERENCES `RegistrationSystem`.`person` (`UNI`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
