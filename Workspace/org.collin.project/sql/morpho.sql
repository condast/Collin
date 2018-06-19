-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: May 01, 2018 at 02:16 PM
-- Server version: 10.1.26-MariaDB-0+deb9u1
-- PHP Version: 7.0.27-0+deb9u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `test`
--

-- --------------------------------------------------------

--
-- Table structure for table `MORPHOLOGICALCODE`
--

CREATE TABLE `MORPHOLOGICALCODE` (
  `ID` bigint(20) NOT NULL,
  `CREATEDATE` datetime NOT NULL,
  `MORPHOLOGICALCODE` int(11) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  `SEARCHMAPGLOBE` tinyint(1) DEFAULT '0',
  `SEARCHMAPIVN` tinyint(1) DEFAULT '0',
  `UPDATEDATE` datetime NOT NULL,
  `MAINCATEGORY` varchar(255) DEFAULT '',
  `SUBCATEGORY` varchar(255) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `MORPHOLOGICALCODE`
--

INSERT INTO `MORPHOLOGICALCODE` (`ID`, `CREATEDATE`, `MORPHOLOGICALCODE`, `NAME`, `SEARCHMAPGLOBE`, `SEARCHMAPIVN`, `UPDATEDATE`, `MAINCATEGORY`, `SUBCATEGORY`) VALUES
(10100, '2018-04-30 15:10:51', 10100, 'Bivalvia', 1, 0, '2018-04-30 15:10:51', 'weekdieren', 'tweekleppig'),
(10110, '2018-04-30 15:10:53', 10110, 'Unio ', 0, 1, '2018-04-30 15:10:53', 'weekdieren', 'tweekleppig'),
(10120, '2018-04-30 15:10:53', 10120, 'Anodonta', 0, 1, '2018-04-30 15:10:53', 'weekdieren', 'tweekleppig'),
(10200, '2018-04-30 15:10:51', 10200, 'Planorbidae', 1, 1, '2018-04-30 15:10:51', 'weekdieren', 'posthoorn orientatie'),
(10300, '2018-04-30 15:10:51', 10300, 'Lymnaeidae', 1, 1, '2018-04-30 15:10:51', 'weekdieren', 'poelslak orientatie'),
(10400, '2018-04-30 15:10:51', 10400, 'Ancylidae', 1, 0, '2018-04-30 15:10:51', 'weekdieren', 'kaphoorn orientatie'),
(20100, '2018-04-30 15:10:51', 20100, 'Nematomorpha', 1, 0, '2018-04-30 15:10:51', 'wormachtig', 'koordwormen'),
(20200, '2018-04-30 15:10:51', 20200, 'Oligochaeta', 1, 0, '2018-04-30 15:10:51', 'wormachtig', 'wormen'),
(20210, '2018-04-30 15:10:53', 20210, 'Tubificidae', 0, 1, '2018-04-30 15:10:53', 'wormachtig', 'wormen'),
(20300, '2018-04-30 15:10:51', 20300, 'Hirudinea', 1, 0, '2018-04-30 15:10:51', 'wormachtig', 'bloedzuigers'),
(20310, '2018-04-30 15:10:53', 20310, 'Haemopidae', 0, 1, '2018-04-30 15:10:53', 'wormachtig', 'bloedzuigers'),
(20400, '2018-04-30 15:10:51', 20400, 'Plathelminthes', 1, 1, '2018-04-30 15:10:51', 'wormachtig', 'platwormen'),
(20500, '2018-04-30 15:10:52', 20500, 'Culicidae', 1, 1, '2018-04-30 15:10:52', 'wormachtig', 'larve mug'),
(20510, '2018-04-30 15:10:52', 20510, 'Chaoboridae', 1, 0, '2018-04-30 15:10:52', 'wormachtig', 'larve mug'),
(20530, '2018-04-30 15:10:52', 20530, 'Dixidae', 1, 0, '2018-04-30 15:10:52', 'wormachtig', 'larve mug'),
(20540, '2018-04-30 15:10:52', 20540, 'Chironomidae', 1, 0, '2018-04-30 15:10:52', 'wormachtig', 'larve mug'),
(20550, '2018-04-30 15:10:52', 20550, 'Chironomidae pop', 1, 0, '2018-04-30 15:10:52', 'wormachtig', 'larve mug'),
(20560, '2018-04-30 15:10:52', 20560, 'Ceratopogonidae', 1, 0, '2018-04-30 15:10:52', 'wormachtig', 'larve mug'),
(20570, '2018-04-30 15:10:52', 20570, 'Simuliidae', 1, 0, '2018-04-30 15:10:52', 'wormachtig', 'larve mug'),
(20580, '2018-04-30 15:10:52', 20580, 'Tipulidae', 1, 0, '2018-04-30 15:10:52', 'wormachtig', 'larve mug'),
(20600, '2018-04-30 15:10:52', 20600, 'Eristalis', 1, 1, '2018-04-30 15:10:52', 'wormachtig', 'larve vlieg'),
(30100, '2018-04-30 15:10:52', 30100, 'Trichoptera met ', 1, 1, '2018-04-30 15:10:52', 'Wormachtige larven met poten', 'kokerjuffer'),
(30110, '2018-04-30 15:10:52', 30110, 'Trichoptera zonder', 1, 0, '2018-04-30 15:10:52', 'Wormachtige larven met poten', 'kokerjuffer'),
(30200, '2018-04-30 15:10:52', 30200, 'Coleoptera larve', 1, 0, '2018-04-30 15:10:52', 'Wormachtige larven met poten', 'kever'),
(30210, '2018-04-30 15:10:52', 30210, 'Hydrophilidae larve', 0, 1, '2018-04-30 15:10:52', 'Wormachtige larven met poten', 'kever'),
(30220, '2018-04-30 15:10:52', 30220, 'Dytiscidae larve', 0, 1, '2018-04-30 15:10:52', 'Wormachtige larven met poten', 'kever'),
(30230, '2018-04-30 15:10:53', 30230, 'Gyrinidae', 0, 1, '2018-04-30 15:10:53', 'Wormachtige larven met poten', 'kever'),
(40100, '2018-04-30 15:10:52', 40100, 'Baetidae', 1, 1, '2018-04-30 15:10:52', 'insekten', 'haft'),
(40110, '2018-04-30 15:10:52', 40110, 'Ephemerellidae', 1, 0, '2018-04-30 15:10:52', 'insekten', 'haft'),
(40120, '2018-04-30 15:10:52', 40120, 'Heptageniidae', 1, 0, '2018-04-30 15:10:52', 'insekten', 'haft'),
(40200, '2018-04-30 15:10:52', 40200, 'Zygoptera', 1, 1, '2018-04-30 15:10:52', 'insekten', 'juffer'),
(40210, '2018-04-30 15:10:52', 40210, 'Calopteryx', 1, 0, '2018-04-30 15:10:52', 'insekten', 'juffer'),
(40300, '2018-04-30 15:10:52', 40300, 'Anisoptera', 1, 1, '2018-04-30 15:10:52', 'insekten', 'libel'),
(40400, '2018-04-30 15:10:52', 40400, 'Plecoptera', 1, 1, '2018-04-30 15:10:52', 'insekten', 'steenvlieg'),
(40500, '2018-04-30 15:10:52', 40500, 'Notonectidae', 1, 1, '2018-04-30 15:10:52', 'insekten', 'rugzwemmers'),
(40600, '2018-04-30 15:10:52', 40600, 'Corixidae', 1, 1, '2018-04-30 15:10:52', 'insekten', 'duikerwantsen'),
(40650, '2018-04-30 15:10:53', 40650, 'Naucoridae', 0, 1, '2018-04-30 15:10:53', 'insekten', 'zwemwants'),
(40710, '2018-04-30 15:10:52', 40710, 'Gyrinidae', 1, 1, '2018-04-30 15:10:52', 'insekten', 'kevers'),
(40720, '2018-04-30 15:10:52', 40720, 'Hydrophilidae', 0, 1, '2018-04-30 15:10:52', 'insekten', 'kevers'),
(40721, '2018-04-30 15:10:52', 40721, 'Dytiscidae', 0, 1, '2018-04-30 15:10:52', 'insekten', 'kevers'),
(40800, '2018-04-30 15:10:52', 40800, 'Nepa', 1, 1, '2018-04-30 15:10:52', 'insekten', 'waterwantsen'),
(40810, '2018-04-30 15:10:52', 40810, 'Ranatra', 1, 1, '2018-04-30 15:10:52', 'insekten', 'waterwantsen'),
(40850, '2018-04-30 15:10:52', 40850, 'Sialidae', 1, 0, '2018-04-30 15:10:52', 'insekten', 'slijkvlieglarve'),
(40900, '2018-04-30 15:10:52', 40900, 'Hydrometridae', 1, 0, '2018-04-30 15:10:52', 'insekten', 'lopers'),
(40910, '2018-04-30 15:10:52', 40910, 'Veliidae', 1, 0, '2018-04-30 15:10:52', 'insekten', 'lopers'),
(40920, '2018-04-30 15:10:52', 40920, 'Gerridae', 1, 1, '2018-04-30 15:10:52', 'insekten', 'lopers'),
(40950, '2018-04-30 15:10:53', 40950, 'Argulus foliaceues', 0, 1, '2018-04-30 15:10:53', 'insekten', 'luis'),
(50100, '2018-04-30 15:10:52', 50100, 'Astacidea', 1, 0, '2018-04-30 15:10:52', 'decapoda', 'kreeft'),
(50200, '2018-04-30 15:10:52', 50200, 'Gammaridae', 1, 1, '2018-04-30 15:10:52', 'decapoda', 'vlokreeft'),
(50300, '2018-04-30 15:10:52', 50300, 'Asellidae', 1, 1, '2018-04-30 15:10:52', 'decapoda', 'pissebed'),
(60100, '2018-04-30 15:10:51', 60100, 'Argyroneta', 1, 1, '2018-04-30 15:10:51', 'Spin', 'spin'),
(60200, '2018-04-30 15:10:51', 60200, 'Hydracarina', 1, 1, '2018-04-30 15:10:51', 'Spin', 'mijt'),
(70100, '2018-04-30 15:10:52', 70100, 'Cladocera', 1, 1, '2018-04-30 15:10:52', 'Kleiner dan 2mm', 'watervlo'),
(70200, '2018-04-30 15:10:52', 70200, 'Copepoda', 1, 1, '2018-04-30 15:10:52', 'Kleiner dan 2mm', 'eenoog'),
(70300, '2018-04-30 15:10:52', 70300, 'Collembola', 1, 0, '2018-04-30 15:10:52', 'Kleiner dan 2mm', 'springstaart'),
(70400, '2018-04-30 15:10:51', 70400, 'Hydra', 1, 0, '2018-04-30 15:10:51', 'Kleiner dan 2mm', 'poliep'),
(80100, '2018-04-30 15:10:53', 80100, 'Anura', 0, 1, '2018-04-30 15:10:53', 'amfibie', 'kikkervis'),
(80200, '2018-04-30 15:10:53', 80200, 'Bufo', 0, 1, '2018-04-30 15:10:53', 'amfibie', 'pad'),
(80300, '2018-04-30 15:10:53', 80300, 'Rana esculenta', 0, 1, '2018-04-30 15:10:53', 'amfibie', 'groene kikker'),
(80400, '2018-04-30 15:10:53', 80400, 'Lissotriton vulgaris', 0, 1, '2018-04-30 15:10:53', 'amfibie', 'Kleine water salamander'),
(90100, '2018-04-30 15:10:53', 90100, 'Lymnaeidae eierstreng', 0, 1, '2018-04-30 15:10:53', 'eieren', 'slak'),
(90200, '2018-04-30 15:10:53', 90200, 'Anura eggs', 0, 1, '2018-04-30 15:10:53', 'eieren', 'kikker');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `MORPHOLOGICALCODE`
--
ALTER TABLE `MORPHOLOGICALCODE`
  ADD PRIMARY KEY (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;