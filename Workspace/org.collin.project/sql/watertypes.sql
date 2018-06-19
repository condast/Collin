-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Host: 172.19.0.27
-- Generation Time: May 28, 2018 at 08:37 PM
-- Server version: 10.2.7-MariaDB-10.2.7+maria~jessie
-- PHP Version: 7.1.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Host: 172.19.0.27
-- Generation Time: May 28, 2018 at 08:40 PM
-- Server version: 10.2.7-MariaDB-10.2.7+maria~jessie
-- PHP Version: 7.1.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `waterdiertjes`
--

-- --------------------------------------------------------

--
-- Table structure for table `WATERTYPES`
--

CREATE TABLE `WATERTYPES` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PROPERTY` varchar(255) DEFAULT NULL,
  `SHAPE` int(11) DEFAULT NULL,
  `WATERFLOW` int(11) DEFAULT NULL,
  `WATERTYPE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `WATERTYPES`
--

INSERT INTO `WATERTYPES` (`ID`, `NAME`, `PROPERTY`, `SHAPE`, `WATERFLOW`, `WATERTYPE`) VALUES
(1, 'Sloot', '<10m breed', 1, 2, 1),
(2, 'Kanaal', '>10m breed', 1, 2, 2),
(3, 'Stadsgracht', '<10m breed', 1, 2, 3),
(4, 'Beek', '', 1, 1, 4),
(5, 'Rivier', '', 1, 1, 5),
(6, 'Poel', '<3m breed', 2, 2, 6),
(7, 'Plas/Ven', '', 2, 2, 7),
(8, 'Meer', '>voetbalveld', 2, 2, 8),
(9, 'Tuinvijver', '', 2, 2, 9),
(10, 'Stadsvijver', '', 2, 2, 10);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `WATERTYPES`
--
ALTER TABLE `WATERTYPES`
  ADD PRIMARY KEY (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;