-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Май 14 2019 г., 04:56
-- Версия сервера: 5.5.25
-- Версия PHP: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `jome`
--

-- --------------------------------------------------------

--
-- Структура таблицы `accounts`
--

CREATE TABLE IF NOT EXISTS `accounts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(16) NOT NULL,
  `password` varchar(8) NOT NULL,
  `friends` varchar(512) NOT NULL DEFAULT '',
  `placeName` varchar(128) NOT NULL DEFAULT 'None',
  `placeData` varchar(144) NOT NULL DEFAULT 'None',
  `coordinateX` varchar(12) NOT NULL DEFAULT '0',
  `coordinateY` varchar(12) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=27 ;

--
-- Дамп данных таблицы `accounts`
--

INSERT INTO `accounts` (`id`, `login`, `password`, `friends`, `placeName`, `placeData`, `coordinateX`, `coordinateY`) VALUES
(7, '1', '1', '12,16,13', 'McDonald''s', 'data=!4m5!3m4!1s0x46dbcfe955e5167f:0x87d4a1b7a6f70198!8m2!3d53.9023423!4d27.5484873', '0', '0'),
(12, 'Alyohea', '1234', '16,13,22,21,14', 'Центральный детский парк имени Максима Горького', 'data=!4m5!3m4!1s0x46dbcfb8f2b4b503:0x4c9e7714acb0549c!8m2!3d53.9029476!4d27.572878', '53.9029476', '27.572878'),
(13, 'Klimdev', '1', '16,7,12,22,14', 'McDonald''s', 'data=!4m5!3m4!1s0x46dbcfe955e5167f:0x87d4a1b7a6f70198!8m2!3d53.9023423!4d27.5484873', '53.9023423', '27.5484873'),
(14, 'ZubDestroy', '123', '16,12,13,21,22', 'Морже', 'data=!4m5!3m4!1s0x46dbcf094fd60cab:0x3bb29bf66426bb61!8m2!3d53.9253939!4d27.5954568', '53.9253939', '27.5954568'),
(16, 'Archeex', '1', '13,21,14,12,22,7', 'Ресторан белорусской кухни "Камяніца"', 'data=!4m5!3m4!1s0x46dbcfc7e9ea2829:0xb94da2cc74087191!8m2!3d53.9003488!4d27.5748883', '53.9003488', '27.5748883'),
(21, 'ololosha228', '123', '', 'Ресторан "Авиньон"', 'data=!4m5!3m4!1s0x46dbcfea65bacfa3:0x9b10b26cde1f191!8m2!3d53.9026556!4d27.5568035', '53.9026556', '27.5568035'),
(22, 'Ariel12', '123', '', 'None', 'None', '53.917018', '27.5808696');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
