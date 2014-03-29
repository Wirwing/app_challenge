SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `challenge` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `challenge` ;

-- -----------------------------------------------------
-- Table `challenge`.`Plan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `challenge`.`Plan` (
  `id` INT NOT NULL,
  `nombre` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `challenge`.`Alumno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `challenge`.`Alumno` (
  `id` INT NOT NULL,
  `password` VARCHAR(45) NULL,
  `Plan_id` INT NOT NULL,
  PRIMARY KEY (`id`, `Plan_id`),
  INDEX `fk_Alumno_Plan1_idx` (`Plan_id` ASC),
  CONSTRAINT `fk_Alumno_Plan1`
    FOREIGN KEY (`Plan_id`)
    REFERENCES `challenge`.`Plan` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `challenge`.`Asignaturas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `challenge`.`Asignaturas` (
  `id` INT NOT NULL,
  `creditos` INT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `challenge`.`asignaturas_alumno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `challenge`.`asignaturas_alumno` (
  `situacion` INT(1) NULL,
  `tipo` INT(1) NULL,
  `Asignaturas_id` INT NOT NULL,
  `Alumno_id` INT NOT NULL,
  `periodo` DATE NOT NULL,
  PRIMARY KEY (`Asignaturas_id`, `Alumno_id`, `periodo`),
  INDEX `fk_asignaturas_alumno_Alumno1_idx` (`Alumno_id` ASC),
  CONSTRAINT `fk_asignaturas_alumno_Asignaturas1`
    FOREIGN KEY (`Asignaturas_id`)
    REFERENCES `challenge`.`Asignaturas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_asignaturas_alumno_Alumno1`
    FOREIGN KEY (`Alumno_id`)
    REFERENCES `challenge`.`Alumno` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `challenge`.`Dependencias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `challenge`.`Dependencias` (
  `asignatura_id` INT NOT NULL,
  `requisito_id` INT NOT NULL,
  PRIMARY KEY (`asignatura_id`, `requisito_id`),
  INDEX `fk_Asignaturas_has_Asignaturas_Asignaturas1_idx` (`requisito_id` ASC),
  INDEX `fk_Asignaturas_has_Asignaturas_Asignaturas_idx` (`asignatura_id` ASC),
  CONSTRAINT `fk_Asignaturas_has_Asignaturas_Asignaturas`
    FOREIGN KEY (`asignatura_id`)
    REFERENCES `challenge`.`Asignaturas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Asignaturas_has_Asignaturas_Asignaturas1`
    FOREIGN KEY (`requisito_id`)
    REFERENCES `challenge`.`Asignaturas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `challenge`.`Dependencias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `challenge`.`Dependencias` (
  `asignatura_id` INT NOT NULL,
  `requisito_id` INT NOT NULL,
  PRIMARY KEY (`asignatura_id`, `requisito_id`),
  INDEX `fk_Asignaturas_has_Asignaturas_Asignaturas1_idx` (`requisito_id` ASC),
  INDEX `fk_Asignaturas_has_Asignaturas_Asignaturas_idx` (`asignatura_id` ASC),
  CONSTRAINT `fk_Asignaturas_has_Asignaturas_Asignaturas`
    FOREIGN KEY (`asignatura_id`)
    REFERENCES `challenge`.`Asignaturas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Asignaturas_has_Asignaturas_Asignaturas1`
    FOREIGN KEY (`requisito_id`)
    REFERENCES `challenge`.`Asignaturas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `challenge`.`Asignaturas_has_Plan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `challenge`.`Asignaturas_has_Plan` (
  `Asignaturas_id` INT NOT NULL,
  `Plan_id` INT NOT NULL,
  PRIMARY KEY (`Asignaturas_id`, `Plan_id`),
  INDEX `fk_Asignaturas_has_Plan_Plan1_idx` (`Plan_id` ASC),
  INDEX `fk_Asignaturas_has_Plan_Asignaturas1_idx` (`Asignaturas_id` ASC),
  CONSTRAINT `fk_Asignaturas_has_Plan_Asignaturas1`
    FOREIGN KEY (`Asignaturas_id`)
    REFERENCES `challenge`.`Asignaturas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Asignaturas_has_Plan_Plan1`
    FOREIGN KEY (`Plan_id`)
    REFERENCES `challenge`.`Plan` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `challenge`.`Oferta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `challenge`.`Oferta` (
  `Asignaturas_id` INT NOT NULL,
  `Profesor` INT NOT NULL,
  `periodo` DATE NOT NULL,
  PRIMARY KEY (`Asignaturas_id`, `Profesor`, `periodo`),
  INDEX `fk_Asignaturas_has_Oferta_Asignaturas1_idx` (`Asignaturas_id` ASC),
  CONSTRAINT `fk_Asignaturas_has_Oferta_Asignaturas1`
    FOREIGN KEY (`Asignaturas_id`)
    REFERENCES `challenge`.`Asignaturas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `challenge`.`Horario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `challenge`.`Horario` (
  `Oferta_Asignaturas_id` INT NOT NULL,
  `Oferta_Profesor` INT NOT NULL,
  `Oferta_periodo` DATE NOT NULL,
  `dia` INT NULL,
  `hora_inicial` TIME NULL,
  `hora_final` TIME NULL,
  PRIMARY KEY (`Oferta_Asignaturas_id`, `Oferta_Profesor`, `Oferta_periodo`),
  CONSTRAINT `fk_Horario_Oferta1`
    FOREIGN KEY (`Oferta_Asignaturas_id` , `Oferta_Profesor` , `Oferta_periodo`)
    REFERENCES `challenge`.`Oferta` (`Asignaturas_id` , `Profesor` , `periodo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
