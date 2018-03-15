-- MySQL Script generated by MySQL Workbench
-- 03/15/18 16:30:11
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema ecosystem
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `ecosystem` ;

-- -----------------------------------------------------
-- Schema ecosystem
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ecosystem` DEFAULT CHARACTER SET utf8mb4 ;
USE `ecosystem` ;

-- -----------------------------------------------------
-- Table `UserAccount`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `UserAccount` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(255) NOT NULL,
  `lastname` VARCHAR(255) NOT NULL,
  `height` FLOAT(5,2) NOT NULL COMMENT 'given in centimeters, allows decimals',
  `birthdate` DATE NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL COMMENT 'needs to be cryptated',
  `email` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB
COMMENT = 'Shall be a useraccount which allows you to get access to personal info and give info to service provider which processes info and forwards it to trainer, which again produces a workout plan to the useraccount';


-- -----------------------------------------------------
-- Table `Weight`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Weight` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `currentweight` FLOAT(4,1) NOT NULL,
  `date` DATE NOT NULL,
  `measuredBy` INT NOT NULL,
  PRIMARY KEY (`id`, `measuredBy`),
  INDEX `fk_Weight_UserAccount_idx` (`measuredBy` ASC),
  CONSTRAINT `fk_Weight_UserAccount`
    FOREIGN KEY (`measuredBy`)
    REFERENCES `UserAccount` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `RestingHeartRate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RestingHeartRate` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATE NOT NULL,
  `heartrate` INT NOT NULL,
  `measuredBy` INT NOT NULL,
  PRIMARY KEY (`id`, `measuredBy`),
  INDEX `fk_RestingPulse_UserAccount1_idx` (`measuredBy` ASC),
  CONSTRAINT `fk_RestingPulse_UserAccount1`
    FOREIGN KEY (`measuredBy`)
    REFERENCES `UserAccount` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WorkoutSession`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WorkoutSession` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `time` TIMESTAMP NOT NULL,
  `intensity` INT NOT NULL,
  `KCal` DECIMAL NOT NULL,
  `AvgHeartRate` INT NOT NULL,
  `MaxHeartRate` INT NOT NULL,
  `DistanceRun` INT NOT NULL,
  `loggedBy` INT NOT NULL,
  PRIMARY KEY (`id`, `loggedBy`),
  INDEX `fk_WorkoutSession_UserAccount1_idx` (`loggedBy` ASC),
  CONSTRAINT `fk_WorkoutSession_UserAccount1`
    FOREIGN KEY (`loggedBy`)
    REFERENCES `UserAccount` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ServiceProvider`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ServiceProvider` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Goal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Goal` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` TEXT NOT NULL,
  `isCompleted` TINYINT NOT NULL,
  `isCurrent` TINYINT NOT NULL,
  `createdBy` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Goal_UserAccount1_idx` (`createdBy` ASC),
  CONSTRAINT `fk_Goal_UserAccount1`
    FOREIGN KEY (`createdBy`)
    REFERENCES `UserAccount` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ImprovifyTrainer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ImprovifyTrainer` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ServiceProvider`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ServiceProvider` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ServiceProviderPermissions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ServiceProviderPermissions` (
  `ServiceProvider_id` INT NOT NULL,
  `UserAccount_id` INT NOT NULL,
  `Weight` TINYINT(1) NOT NULL DEFAULT 0,
  `Height` TINYINT(1) NOT NULL DEFAULT 0,
  `Email` TINYINT(1) NOT NULL DEFAULT 0,
  `Name` TINYINT(1) NOT NULL DEFAULT 0,
  `Username` TINYINT(1) NOT NULL DEFAULT 0,
  `RestingHeartRate` TINYINT(1) NOT NULL DEFAULT 0,
  `WorkoutSession` TINYINT(1) NOT NULL DEFAULT 0,
  `Birthdate` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`ServiceProvider_id`, `UserAccount_id`),
  INDEX `fk_ServiceProvider_has_UserAccount_UserAccount1_idx` (`UserAccount_id` ASC),
  INDEX `fk_ServiceProvider_has_UserAccount_ServiceProvider1_idx` (`ServiceProvider_id` ASC),
  CONSTRAINT `fk_ServiceProvider_has_UserAccount_ServiceProvider1`
    FOREIGN KEY (`ServiceProvider_id`)
    REFERENCES `ServiceProvider` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ServiceProvider_has_UserAccount_UserAccount1`
    FOREIGN KEY (`UserAccount_id`)
    REFERENCES `UserAccount` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WorkoutPlan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WorkoutPlan` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` TEXT NULL,
  `createdForUserAccount_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_WorkoutPlan_UserAccount2_idx` (`createdForUserAccount_id` ASC),
  CONSTRAINT `fk_WorkoutPlan_UserAccount2`
    FOREIGN KEY (`createdForUserAccount_id`)
    REFERENCES `UserAccount` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PlanUsedInSession`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PlanUsedInSession` (
  `WorkoutSession_id` INT NOT NULL,
  `WorkoutSession_loggedBy` INT NOT NULL,
  `WorkoutPlan_id` INT NOT NULL,
  PRIMARY KEY (`WorkoutSession_id`, `WorkoutSession_loggedBy`, `WorkoutPlan_id`),
  INDEX `fk_PlanUsedInSession_WorkoutSession1_idx` (`WorkoutSession_id` ASC, `WorkoutSession_loggedBy` ASC),
  INDEX `fk_PlanUsedInSession_WorkoutPlan1_idx` (`WorkoutPlan_id` ASC),
  CONSTRAINT `fk_PlanUsedInSession_WorkoutSession1`
    FOREIGN KEY (`WorkoutSession_id` , `WorkoutSession_loggedBy`)
    REFERENCES `WorkoutSession` (`id` , `loggedBy`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_PlanUsedInSession_WorkoutPlan1`
    FOREIGN KEY (`WorkoutPlan_id`)
    REFERENCES `WorkoutPlan` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WorkoutPlanRow`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WorkoutPlanRow` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` TEXT NOT NULL,
  `durationSeconds` INT NULL,
  `intensity` VARCHAR(255) NULL,
  `comment` TEXT NULL,
  `WorkoutPlan_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_WorkoutPlanRow_WorkoutPlan1_idx` (`WorkoutPlan_id` ASC),
  CONSTRAINT `fk_WorkoutPlanRow_WorkoutPlan1`
    FOREIGN KEY (`WorkoutPlan_id`)
    REFERENCES `WorkoutPlan` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PeriodPlan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PeriodPlan` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `description` TEXT NULL,
  `periodStart` DATE NULL,
  `periodEnd` DATE NULL,
  `createdForUseraccount_id` INT NOT NULL,
  `createdByImprovifyTrainer_id` INT NOT NULL,
  `createdAt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_PeriodPlan_UserAccount2_idx` (`createdForUseraccount_id` ASC),
  INDEX `fk_PeriodPlan_ImprovifyTrainer2_idx` (`createdByImprovifyTrainer_id` ASC),
  CONSTRAINT `fk_PeriodPlan_UserAccount2`
    FOREIGN KEY (`createdForUseraccount_id`)
    REFERENCES `UserAccount` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_PeriodPlan_ImprovifyTrainer2`
    FOREIGN KEY (`createdByImprovifyTrainer_id`)
    REFERENCES `ImprovifyTrainer` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PlannedSession`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PlannedSession` (
  `date` DATE NOT NULL,
  `PeriodPlan_id` INT NOT NULL,
  `WorkoutPlan_id` INT NOT NULL,
  PRIMARY KEY (`date`, `PeriodPlan_id`, `WorkoutPlan_id`),
  INDEX `fk_PlannedSession_PeriodPlan1_idx` (`PeriodPlan_id` ASC),
  INDEX `fk_PlannedSession_WorkoutPlan1_idx` (`WorkoutPlan_id` ASC),
  CONSTRAINT `fk_PlannedSession_PeriodPlan1`
    FOREIGN KEY (`PeriodPlan_id`)
    REFERENCES `PeriodPlan` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_PlannedSession_WorkoutPlan1`
    FOREIGN KEY (`WorkoutPlan_id`)
    REFERENCES `WorkoutPlan` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
