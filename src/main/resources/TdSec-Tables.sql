-- USE trialdirect_sec;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `enum`;
CREATE TABLE  `enum` (
  `class` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(63) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `ordinal` int(11) DEFAULT NULL,
  PRIMARY KEY (`class`,`name`),
  UNIQUE KEY `withordinal` (`class`,`ordinal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `enum` (class) VALUES ('org.hibernate.envers.RevisionType');
INSERT INTO `enum` (class) VALUES ('com.tekenable.tdsec2.model.TdLoginStatus');
INSERT INTO `enum` (class) VALUES ('com.tekenable.tdsec2.model.TdLoginAttemptStatus');

drop table if exists `audit_trail_entity`;
CREATE TABLE `audit_trail_entity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` bigint(20) NOT NULL,
  `action` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `comments` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `guid` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `url` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_id` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT charset=utf8 COLLATE=utf8_unicode_ci;

drop table if exists `td_user`;
CREATE TABLE `td_user` (
  `pk` bigint(20) NOT NULL AUTO_INCREMENT,
  `id` varchar(36) NOT NULL,
  `email_hash` VARCHAR(60) NOT NULL,
  `user_type` VARCHAR(20) NOT NULL,
  `td_password` VARCHAR(20) NOT NULL,
  `lat` FLOAT(10,6) NOT NULL,
  `lng` FLOAT(10,6) NOT NULL,
  `encryption_data` longblob NOT NULL,
  PRIMARY KEY (`pk`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO td_user (id, email_hash, user_type, td_password, lat, lng)
VALUES
('1', 'user', 'HCP', 'password', '53.2673745', '-6.2014216', ''),
('2', 'user2', 'HCP', 'password', '53.263858', '-6.1972588', '');



drop table if exists `td_user_aud`;
CREATE TABLE `td_user_aud` (
  `pk` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `id` varchar(36) DEFAULT NULL,
  `email_hash` tinyblob DEFAULT NULL,
  `user_type` VARCHAR(20) DEFAULT NULL,
  `td_password` VARCHAR(20) NOT NULL,
  `lat` FLOAT(10,6) NOT NULL,
  `lng` FLOAT(10,6) NOT NULL,
  `encryption_data` longblob NOT NULL,
  PRIMARY KEY (`pk`,`REV`),
  CONSTRAINT `FK_TDUSER_AUD_REV` FOREIGN KEY (`REV`) REFERENCES `audit_trail_entity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

drop table if exists `td_login`;
CREATE TABLE `td_login` (
  `pk` bigint(20) NOT NULL AUTO_INCREMENT,
  `expiration_date` datetime DEFAULT NULL,
  `tduser_login_status` int(11) NOT NULL,
  `id` varchar(36) NOT NULL,
  `td_user_fk` bigint(20) NOT NULL,
  PRIMARY KEY (`pk`),
  CONSTRAINT `FK_TD_USER_LOGIN_TD_USER_FK` FOREIGN KEY (`td_user_fk`) REFERENCES `td_user` (`pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

drop table if exists `td_login_attempt`;
CREATE TABLE `td_login_attempt` (
  `pk` bigint(20) NOT NULL AUTO_INCREMENT,
  `encryption_data` longblob NOT NULL,
  `encryption_iv` tinyblob NOT NULL,
  `encryption_key` tinyblob NOT NULL,
  `accept_language` varchar(255) DEFAULT NULL,
  `attempt_status` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `exception` text DEFAULT NULL,
  `ip_address` varchar(40) NOT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

drop table if exists `tduser_password`;
CREATE TABLE `tduser_password` (
  `pk` bigint(20) NOT NULL AUTO_INCREMENT,
  `hashed_password` binary(64) NOT NULL,
  `iterations` int(11) NOT NULL,
  `salt` binary(64) NOT NULL,
  PRIMARY KEY (`pk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

drop table if exists `tduser_password_aud`;
CREATE TABLE `tduser_password_aud` (
  `pk` bigint(20) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `hashed_password` binary(64) DEFAULT NULL,
  `iterations` int(11) DEFAULT NULL,
  `salt` binary(64) DEFAULT NULL,
  PRIMARY KEY (`pk`,`REV`),
  CONSTRAINT `FK_TDUSER_PASSWORD_AUD_AUDIT_TRAIL_ENTITY` FOREIGN KEY (`REV`) REFERENCES `audit_trail_entity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
