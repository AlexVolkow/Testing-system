-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`clases`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`clases` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `clases_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_users_clases_idx` (`clases_id` ASC),
  CONSTRAINT `fk_users_clases`
    FOREIGN KEY (`clases_id`)
    REFERENCES `mydb`.`clases` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`questions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`questions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(500) NOT NULL,
  `clases_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_questions_clases1_idx` (`clases_id` ASC),
  CONSTRAINT `fk_questions_clases1`
    FOREIGN KEY (`clases_id`)
    REFERENCES `mydb`.`clases` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`themes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`themes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `clases_id` INT NOT NULL,
  `questions_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_themes_clases1_idx` (`clases_id` ASC),
  INDEX `fk_themes_questions1_idx` (`questions_id` ASC),
  CONSTRAINT `fk_themes_clases1`
    FOREIGN KEY (`clases_id`)
    REFERENCES `mydb`.`clases` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_themes_questions1`
    FOREIGN KEY (`questions_id`)
    REFERENCES `mydb`.`questions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`right_answers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`right_answers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `text` VARCHAR(100) NOT NULL,
  `questions_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_right_answers_questions1_idx` (`questions_id` ASC),
  CONSTRAINT `fk_right_answers_questions1`
    FOREIGN KEY (`questions_id`)
    REFERENCES `mydb`.`questions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`users_has_themes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`users_has_themes` (
  `users_id` INT NOT NULL,
  `themes_id` INT NOT NULL,
  PRIMARY KEY (`users_id`, `themes_id`),
  INDEX `fk_users_has_themes_themes1_idx` (`themes_id` ASC),
  INDEX `fk_users_has_themes_users1_idx` (`users_id` ASC),
  CONSTRAINT `fk_users_has_themes_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_themes_themes1`
    FOREIGN KEY (`themes_id`)
    REFERENCES `mydb`.`themes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`answers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`answers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `text` VARCHAR(100) NULL,
  `questions_id` INT NOT NULL,
  `users_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_answers_questions1_idx` (`questions_id` ASC),
  INDEX `fk_answers_users1_idx` (`users_id` ASC),
  CONSTRAINT `fk_answers_questions1`
    FOREIGN KEY (`questions_id`)
    REFERENCES `mydb`.`questions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_answers_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
