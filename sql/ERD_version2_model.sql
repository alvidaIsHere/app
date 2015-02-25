SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `sloca_db` ;
CREATE SCHEMA IF NOT EXISTS `sloca_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `sloca_db` ;

-- -----------------------------------------------------
-- Table `sloca_db`.`Student`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sloca_db`.`student` ;

CREATE TABLE IF NOT EXISTS `sloca_db`.`student` (
  `mac_address` VARCHAR(40) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  `email` VARCHAR(254) NOT NULL,
  `gender` VARCHAR(1) NOT NULL,
  UNIQUE (`mac_address`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sloca_db`.`Location_lookup`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sloca_db`.`location_lookup` ;

CREATE TABLE IF NOT EXISTS `sloca_db`.`location_lookup` (
  `location_id` INT NOT NULL,
  `semantic_name` VARCHAR(40) NOT NULL,
  UNIQUE (`location_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sloca_db`.`Location`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sloca_db`.`location` ;

CREATE TABLE IF NOT EXISTS `sloca_db`.`location` (
  `rowNumber` INT NOT NULL,
  `time` DATETIME NOT NULL,
  `student_mac_address` VARCHAR(40) NOT NULL,
  `location_id` INT NOT NULL,
  UNIQUE `Unique_Column`(`student_mac_address`, `time`),
  INDEX `Time_Index`(`time`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sloca_db`.`Admin`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sloca_db`.`admin` ;

CREATE TABLE IF NOT EXISTS `sloca_db`.`admin` (
  `admin_username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  UNIQUE (`admin_username`))
ENGINE = InnoDB;

INSERT INTO `admin` VALUES ('admin', 'password');


SET SQL_MODE=@OLD_SQL_MODE;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
