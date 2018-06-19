-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: May 01, 2018 at 02:15 PM
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
-- Table structure for table `LOCALEDESCRIPTION`
--

CREATE TABLE `LOCALEDESCRIPTION` (
  `ID` bigint(20) NOT NULL,
  `ALTERNATIVE` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  `MORPHO_ID` bigint(20) NOT NULL,
  `DESCRIPTIONS_ID` bigint(20) NOT NULL,
  `DESCRIPTIONS_KEY` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `LOCALEDESCRIPTION`
--

INSERT INTO `LOCALEDESCRIPTION` (`ID`, `ALTERNATIVE`, `DESCRIPTION`, `NAME`, `MORPHO_ID`, `DESCRIPTIONS_ID`, `DESCRIPTIONS_KEY`) VALUES
(10100, '', 'Mosseltjes filteren het water om zo hun eten te krijgen', 'Zoetwatermossel', 10100, 10100, 'NL'),
(10110, '', '', 'Schildersmossel', 10110, 10110, 'NL'),
(10120, '', '', 'Zwanenmossel', 10120, 10120, 'NL'),
(10200, '', 'Grootste schijfhoornslak die wel 3.5 cm groot kan worden.', 'Posthoornslak', 10200, 10200, 'NL'),
(10300, '', 'Eén van de grootste waterslakken die leeft in plantenrijk water.', 'Poelslak', 10300, 10300, 'NL'),
(10400, '', 'Deze slakjes leven vaak op stenen in het water.', 'Kaphoornslak', 10400, 10400, 'NL'),
(20100, '', 'ook wel paardehaarworm genoemd dat wel 50 cm lang kan worden', 'Koordwormen', 20100, 20100, 'NL'),
(20200, '', 'Leven vaak in de bodem en zijn voedsel voor andere dieren', 'Worm', 20200, 20200, 'NL'),
(20210, '', '', 'Tubifex', 20210, 20210, 'NL'),
(20300, '', 'Ongevaarlijk voor de mens (op één zeldzame soort na), eten andere kleine dieren', 'Bloedzuiger', 20300, 20300, 'NL'),
(20310, 'Onechte paardenbloedzuiger', '', 'Paardenbloedzuiger', 20310, 20310, 'NL'),
(20400, '', 'rovende dieren die als het ware over de bodem schuiven als ze zich voortbewegen', 'Platwormen', 20400, 20400, 'NL'),
(20500, '', 'de meeste steekmuggen hangen aan de onderzijde van de waterspiegel om adem te halen', 'Steekmuglarve', 20500, 20500, 'NL'),
(20510, '', 'Worden ook wel spookmuggen genoemd omdat ze doorzichtig zijn met twee donkere luchtkamers', 'Larve van pluimmug', 20510, 20510, 'NL'),
(20530, '', 'kenmerkend is de U-vormige houding met de kop net in het water', 'Larve van meniscusmug', 20530, 20530, 'NL'),
(20540, '', 'Dans of vedermug die wit, bruin, geel of groen zijn of geheel onopvallend', 'Larve van vedermug', 20540, 20540, 'NL'),
(20550, '', 'een pop is de fase tussen de larve in het water en het volwassen insekt op het land en duurt enkele dagen', 'Pop van mug', 20550, 20550, 'NL'),
(20560, '', 'volwassen knutjes bijten ook mensen', 'Larve van een knaasje', 20560, 20560, 'NL'),
(20570, '', 'dieren van stromend water die met de waaiervormige antennes voedsel vangen', 'Larve van kriebelmug', 20570, 20570, 'NL'),
(20580, '', 'de meeste langpootmug larven leven in moerassen en ondiep water', 'Larve van langpootmug', 20580, 20580, 'NL'),
(20600, '', 'de bijvlieg leeft in water met heel veel rottend organisch materiaal', 'Rattenstaartlarve', 20600, 20600, 'NL'),
(30100, '', 'Kokerjuffers bouwen huisjes van materiaal dat ze kunnen vinden zoals zand en takjes', 'Kokerjuffer met koker', 30100, 30100, 'NL'),
(30110, '', 'deze kokerjuffers bouwen helemaal geen huisjes', 'Kokerjuffer zonder koker', 30110, 30110, 'NL'),
(30200, '', 'Larve van de kevers, die er heel anders uitzien dan de volwassen exemplaren', 'Keverlarven', 30200, 30200, 'NL'),
(30210, '', '', 'Larve grote spinnende watertor', 30210, 30210, 'NL'),
(30220, 'Larve geelgerande waterkever', '', 'Larve geelgerande watertor', 30220, 30220, 'NL'),
(30230, '', '', 'Larve schrijvertje', 30230, 30230, 'NL'),
(40100, 'Zwemmende haftennimfen', 'Larven van eendagsvliegen die heel snel door het water kunnen zwemmen.', 'Haftenlarf - zwemmend', 40100, 40100, 'NL'),
(40110, 'Gravende haftennimfen', '0]]', 'Haftenlarf - gravend', 40110, 40110, 'NL'),
(40120, 'Platte haftennimfen', 'Heel gestroomlijnde dieren om goed in hard stromend water te kunnen leven.', 'Haftenlarf - plat', 40120, 40120, 'NL'),
(40200, 'Nimf van waterjuffer', 'Onvolwassen exemplaren van de ranke waterjuffers', 'Larve van waterjuffer', 40200, 40200, 'NL'),
(40210, '', 'Waterjuffer met typische antenne, waarvan de volwassen een mooie blauwe kleur ehbben', 'Beekjuffer', 40210, 40210, 'NL'),
(40300, 'Nimfen van glazenmakers', 'onvolwassen dieren van echte libellen die schoon water nodig hebben.', 'Libellenlarve', 40300, 40300, 'NL'),
(40400, 'Nimfen van steenvliegen', 'Steenvliegen leven het liefst in koud water.', 'Larf van steenvlieg', 40400, 40400, 'NL'),
(40500, 'Bootsmannetje', 'De bekende bootsman die omgekeerd door het water zwemt', 'Ruggenzwemmers', 40500, 40500, 'NL'),
(40600, '', 'Hebben net als de ruggenzwemmers een luchtbel om zich heen waaruit ze zuurstof haeln', 'Duikerwants', 40600, 40600, 'NL'),
(40650, '', '', 'Zwemwants', 40650, 40650, 'NL'),
(40710, '', 'Kevertjes die zowel op het oppervlakte kunnen leven als onderwater kunnen zwemmen', 'Schrijvertje', 40710, 40710, 'NL'),
(40720, '', '', 'Grote spinnende watertor', 40720, 40720, 'NL'),
(40721, 'Geelgerande waterkever', '', 'Geelgerande watertor', 40721, 40721, 'NL'),
(40800, '', 'Een schorpioen die ongevaarlijk is voor mensen', 'Waterschorpioen', 40800, 40800, 'NL'),
(40810, '', 'Met de lange staart halen ze adem', 'Staafwants', 40810, 40810, 'NL'),
(40850, '', 'Dieren die leven in schone bodems', 'Slijkvlieglarve', 40850, 40850, 'NL'),
(40900, '', 'Supersmalle dieren, ook wel waternaalden genoemd', 'Vijverloper', 40900, 40900, 'NL'),
(40910, '', 'Bewoner van beekjes waar het op het oppervlak loopt', 'Beekloper', 40910, 40910, 'NL'),
(40920, '', 'Lopen op het wateroppervlak', 'Schaatsenrijder', 40920, 40920, 'NL'),
(40950, '', '', 'Karperluis', 40950, 40950, 'NL'),
(50100, '', 'Vrij grote dieren die vooral \'s nachts actief zijn.', 'Zoetwaterkreeft', 50100, 50100, 'NL'),
(50200, 'Zoetwatervlokreeft', 'Vlokreeften helpen met het opruimen van dode blaadjes in het water.', 'Vlokreeft', 50200, 50200, 'NL'),
(50300, '', 'De zoetwaterpissebed is familie van de pissebedden die je in je tuin kan vinden.', 'Zoetwaterpissebed', 50300, 50300, 'NL'),
(60100, '', 'Bouwt onder water een prachtig spinnenweb waarmee zuurstof wordt gevangen', 'Waterspin', 60100, 60100, 'NL'),
(60200, '', 'Hele kleine rover met acht poten in het water.', 'Watermijt', 60200, 60200, 'NL'),
(70100, '', 'Filteren het water op zoek naar algen wat als voedsel dient', 'Watervlo', 70100, 70100, 'NL'),
(70200, '', 'ookwel aangeduid als roeipootkreefjes.', 'Eenoogkreeftjes', 70200, 70200, 'NL'),
(70300, '', 'Kleine diertjes die op het wateroppervlak kunnen springen', 'Springstaarten', 70300, 70300, 'NL'),
(70400, '', 'vaak op waterplanten levend', 'Zoetwaterpoliep', 70400, 70400, 'NL'),
(80100, 'Dikkopje', '', 'Kikkervisje', 80100, 80100, 'NL'),
(80200, '', '', 'Pad', 80200, 80200, 'NL'),
(80300, '', '', 'Groene kikker', 80300, 80300, 'NL'),
(80400, '', '', 'Kleine water salamander', 80400, 80400, 'NL'),
(90100, '', '', 'Eierstreng poelslak', 90100, 90100, 'NL'),
(90200, '', '', 'Kikkerdril', 90200, 90200, 'NL'),
(1010100, '', '', 'Bivalves', 10100, 10100, 'EN'),
(1010110, '', '', 'Freshwater mussel', 10110, 10110, 'EN'),
(1010120, '', '', 'River mussel', 10120, 10120, 'EN'),
(1010200, '', '', 'Ramshorn snail', 10200, 10200, 'EN'),
(1010300, '', '', 'Pond snail', 10300, 10300, 'EN'),
(1010400, '', '', 'Freshwater limpet', 10400, 10400, 'EN'),
(1020100, '', '', 'Hairworms', 20100, 20100, 'EN'),
(1020200, '', '', 'Worms', 20200, 20200, 'EN'),
(1020210, '', '', 'Sludge worm', 20210, 20210, 'EN'),
(1020300, '', '', 'Leeches', 20300, 20300, 'EN'),
(1020310, '', '', 'Horse-leech', 20310, 20310, 'EN'),
(1020400, '', '', 'Flatworms', 20400, 20400, 'EN'),
(1020500, '', '', 'Mosquito larva', 20500, 20500, 'EN'),
(1020510, '', '', 'Phantom midge larva', 20510, 20510, 'EN'),
(1020530, '', '', 'Meniscus midge larva', 20530, 20530, 'EN'),
(1020540, '', '', 'Non-biting midge larva', 20540, 20540, 'EN'),
(1020550, '', '', 'Midge pupa', 20550, 20550, 'EN'),
(1020560, '', '', 'Biting midge larva', 20560, 20560, 'EN'),
(1020570, '', '', 'Blackfly larva', 20570, 20570, 'EN'),
(1020580, '', '', 'Cranefly larva', 20580, 20580, 'EN'),
(1020600, '', '', 'Rat-tailed maggot', 20600, 20600, 'EN'),
(1030100, '', '', 'Cased caddis fly larvae', 30100, 30100, 'EN'),
(1030110, '', '', 'Caseless caddis fly larvae', 30110, 30110, 'EN'),
(1030200, '', '', 'Beetle larvae', 30200, 30200, 'EN'),
(1030210, '', '', 'Great silver water beetle larvae', 30210, 30210, 'EN'),
(1030220, '', '', 'Great diving beetle larvae', 30220, 30220, 'EN'),
(1030230, '', '', 'Whirligig beetle larvae', 30230, 30230, 'EN'),
(1040100, '', '', 'Swimming mayfly nymph', 40100, 40100, 'EN'),
(1040110, '', '', 'Burrowing mayfly nymph', 40110, 40110, 'EN'),
(1040120, '', '', 'Flattened mayfly nymph', 40120, 40120, 'EN'),
(1040200, '', '', 'Damselfly nymphs', 40200, 40200, 'EN'),
(1040210, '', '', 'Demoiselle', 40210, 40210, 'EN'),
(1040300, '', '', 'Dragonfly nymphs', 40300, 40300, 'EN'),
(1040400, '', '', 'Stonefly nymphs', 40400, 40400, 'EN'),
(1040500, '', '', 'Greater water boatman', 40500, 40500, 'EN'),
(1040600, '', '', 'Lesser water boatman', 40600, 40600, 'EN'),
(1040650, '', '', 'Creeping water bug', 40650, 40650, 'EN'),
(1040710, '', '', 'Whirligig beetle', 40710, 40710, 'EN'),
(1040720, '', '', 'Great silver water beetle', 40720, 40720, 'EN'),
(1040721, '', '', 'Great diving beetle', 40721, 40721, 'EN'),
(1040800, '', '', 'Water scorpion', 40800, 40800, 'EN'),
(1040810, '', '', 'Water stick insect', 40810, 40810, 'EN'),
(1040850, '', '', 'Alderfly larvae', 40850, 40850, 'EN'),
(1040900, '', '', 'Water measurer', 40900, 40900, 'EN'),
(1040910, '', '', 'Water cricket', 40910, 40910, 'EN'),
(1040920, '', '', 'Pond skater', 40920, 40920, 'EN'),
(1040950, '', '', 'Carp lice', 40950, 40950, 'EN'),
(1050100, '', '', 'Freshwater crayfish', 50100, 50100, 'EN'),
(1050200, '', '', 'Freshwater shrimp', 50200, 50200, 'EN'),
(1050300, '', '', 'Freshwater hoglouse', 50300, 50300, 'EN'),
(1060100, '', '', 'Water spider', 60100, 60100, 'EN'),
(1060200, '', '', 'Water mite', 60200, 60200, 'EN'),
(1070100, '', '', 'Waterfleas', 70100, 70100, 'EN'),
(1070200, '', '', 'Copepod', 70200, 70200, 'EN'),
(1070300, '', '', 'Springtails', 70300, 70300, 'EN'),
(1070400, '', '', 'Hydra', 70400, 70400, 'EN'),
(1080100, '', '', 'Tadpole', 80100, 80100, 'EN'),
(1080200, '', '', 'Toad', 80200, 80200, 'EN'),
(1080300, '', '', 'Edible Frog', 80300, 80300, 'EN'),
(1080400, '', '', 'Newt', 80400, 80400, 'EN'),
(1090100, '', '', 'Pond snail eggs', 90100, 90100, 'EN'),
(1090200, '', '', 'Frogspawn', 90200, 90200, 'EN'),
(2010100, '', 'Jij ving mij, maar ik word ook vaak door watervogels gevangen als lekker hapje. ', '', 10100, 10100, 'MATTHIJS'),
(2010110, '', '', '', 10110, 10110, 'MATTHIJS'),
(2010120, '', '', '', 10120, 10120, 'MATTHIJS'),
(2010200, '', 'Je ving mij, de grootste schijfhoornslak. Ik kan wel 3.5 cm groot worden.', '', 10200, 10200, 'MATTHIJS'),
(2010300, '', 'Leuk dat ik tussen je vangsten zat. Je ving me waarschijnlijk tussen de waterplanten.', '', 10300, 10300, 'MATTHIJS'),
(2010400, '', 'Dat je me hebt gevonden is knap, ik zit onopvallend op stenen in het water.', '', 10400, 10400, 'MATTHIJS'),
(2020100, '', 'Van alle dieren die je vandaag ving, kan ik het langst worden, tot wel 50 cm.', '', 20100, 20100, 'MATTHIJS'),
(2020200, '', 'Jij hebt mij gevangen, maar meestal wordt ik gevangen door waterdieren die me opeten.', '', 20200, 20200, 'MATTHIJS'),
(2020210, '', '', '', 20210, 20210, 'MATTHIJS'),
(2020300, '', 'Jij ving mij en ik vang als je me loslaat weer kleine andere dieren.', '', 20300, 20300, 'MATTHIJS'),
(2020310, '', '', '', 20310, 20310, 'MATTHIJS'),
(2020400, '', 'Leuk dat je me ving! Wist je dat als je mij in drie stukken snijdt, ik uitgroei tot weer 3 platwormen?', '', 20400, 20400, 'MATTHIJS'),
(2020500, '', 'Leuk dat je ving. Ik hang meestal aan de onderzijde van de waterspiegel om adem te halen.', '', 20500, 20500, 'MATTHIJS'),
(2020510, '', 'Pluim voor jou dat je ving! Ik word ook wel spookmug genoemd omdat ik doorzichtig ben met twee donkere luchtkamers!', '', 20510, 20510, 'MATTHIJS'),
(2020530, '', 'Leuk dat je me ving! Ik lig altijd krom en je herkent me dus aan mijn U vorm.', '', 20530, 20530, 'MATTHIJS'),
(2020540, '', 'Leuk dat je me ving! Je kunt me terugzetten in het water, want ik dans alleen maar als mugje boven het water en steek geen mensen!', '', 20540, 20540, 'MATTHIJS'),
(2020550, '', 'Ik was makkelijk te vangen, want als pop beweeg ik niet. Over een paar dagen wordt ik een mug!', '', 20550, 20550, 'MATTHIJS'),
(2020560, '', 'Dat je mij ving, zal ik je betaald zetten, want als volwassen knutje zal ik je bijten!', '', 20560, 20560, 'MATTHIJS'),
(2020570, '', 'Leuk dat je me ving! Zelf vang ik mijn voedsel met m\'n antennes!', '', 20570, 20570, 'MATTHIJS'),
(2020580, '', 'Leuk dat je me ving. Ik word straks een onschuldige langpootmug!', '', 20580, 20580, 'MATTHIJS'),
(2020600, '', 'Leuk dat je me ving! Ik adem met mijn telescoop snorkel en wordt straks een Blinde Bij!', '', 20600, 20600, 'MATTHIJS'),
(2030100, '', 'Goed dat je me door had, terwijl ik me verstopte in mijn zelfgebouwde huisje. Zet me maar terug, dan kan ik straks een schietmot worden!', '', 30100, 30100, 'MATTHIJS'),
(2030110, '', 'Leuk dat je me ving, mijn familieleden bouwen een huisje, maar ik vind het zonder ook prima. Als volwassene ben ik een schietmot!', '', 30110, 30110, 'MATTHIJS'),
(2030200, '', 'Leuk dat je me ving! Als volwassen dier zie ik er straks heel anders uit!', '', 30200, 30200, 'MATTHIJS'),
(2030210, '', '', '', 30210, 30210, 'MATTHIJS'),
(2030220, '', 'Leuk dat je me ving! Ik ben een echte rover en eet bijvoorbeeld kikkervisjes!', '', 30220, 30220, 'MATTHIJS'),
(2030230, '', '', '', 30230, 30230, 'MATTHIJS'),
(2040100, '', 'Zet je me zo weer terug? Ik wil namelijk verder groeien om straks een mooie eendagsvlieg te worden!', '', 40100, 40100, 'MATTHIJS'),
(2040110, '', 'Zet je me zo weer terug? Ik wil namelijk verder groeien om straks een mooie eendagsvlieg te worden!', '', 40110, 40110, 'MATTHIJS'),
(2040120, '', 'Zet je me zo weer terug? Ik wil namelijk verder groeien om straks een mooie eendagsvlieg te worden!', '', 40120, 40120, 'MATTHIJS'),
(2040200, '', 'Leuk dat je me ving, maar zet me maar weer snel terug, dan kan ik een mooie waterjuffer worden.', '', 40200, 40200, 'MATTHIJS'),
(2040210, '', 'Leuk dat je me ving, maar zet me maar weer snel terug, dan kan ik een mooie blauwe beekjuffer worden.', '', 40210, 40210, 'MATTHIJS'),
(2040300, '', 'Leuk dat je me ving, maar zet me maar weer snel terug, dan kan ik een mooie libel worden.', '', 40300, 40300, 'MATTHIJS'),
(2040400, '', 'Leuk dat je me ving, maar zet me maar snel terug, want ik hou van fris koud water.', '', 40400, 40400, 'MATTHIJS'),
(2040500, '', 'Leuk dat je me ving, maar let op, ik kan ook lelijk bijten en als je niet oppast vlieg ik gewoon weg! ', '', 40500, 40500, 'MATTHIJS'),
(2040600, '', 'Leuk dat je me ving. Ik heb een luchtbel bij me waar ik zuurstof uit haal.', '', 40600, 40600, 'MATTHIJS'),
(2040650, '', '', '', 40650, 40650, 'MATTHIJS'),
(2040710, '', 'Leuk dat je me ving! Zag je me over het water scheren/schrijven?', '', 40710, 40710, 'MATTHIJS'),
(2040720, '', '', '', 40720, 40720, 'MATTHIJS'),
(2040721, '', 'Leuk dat je me ving! Ik ben de meest bekende watertor van het land!', '', 40721, 40721, 'MATTHIJS'),
(2040800, '', 'Gaaf dat je me ving! Die steel aan m\'n achterlijf gebruik ik niet om te steken, maar als snorkel!', '', 40800, 40800, 'MATTHIJS'),
(2040810, '', 'Tof dat je me ving. Die steel aan mijn achterkant is een adembuis of snorkel.', '', 40810, 40810, 'MATTHIJS'),
(2040850, '', 'Bijzonder dat je hebt gevangen. Ik leef alleen in schone bodems.', '', 40850, 40850, 'MATTHIJS'),
(2040900, '', 'Je ving me terwijl ik over het water liep. Ik ben zo smal dat ze me ook wel waternaald noemen!', '', 40900, 40900, 'MATTHIJS'),
(2040910, '', 'Je ving me terwijl ik over het water liep. Ik loop zoals mijn naam al zegt het liefst over beken!', '', 40910, 40910, 'MATTHIJS'),
(2040920, '', 'Je ving me terwijl ik over het water liep. Ik ben zo licht dat ik geen ijs nodig heb om te kunnen schaatsen!', '', 40920, 40920, 'MATTHIJS'),
(2040950, '', '', '', 40950, 40950, 'MATTHIJS'),
(2050100, '', 'Bijzonder dat je me gevangen hebt! Ik ben een exoot en meestal \'s nachts actief', '', 50100, 50100, 'MATTHIJS'),
(2050200, '', 'Zet je me zo weer terug? Dan ga ik weer verder met het opruimen van dode blaadjes in het water!', '', 50200, 50200, 'MATTHIJS'),
(2050300, '', 'Je herkende me vast makkelijk. Ik ben familie van de pissebedden die je in je tuin vindt!', '', 50300, 50300, 'MATTHIJS'),
(2060100, '', 'Leuk dat je me ving! Ik bouw onderwater een spinnenweb waarin ik een luchtbel bewaar.', '', 60100, 60100, 'MATTHIJS'),
(2060200, '', 'Leuk dat je me ving! Ik heb 8 poten. Had je gedacht dat ik een roofdier ben?', '', 60200, 60200, 'MATTHIJS'),
(2070100, '', 'geen macrofauna DOEN NIET MEE IN BEREKENING', '', 70100, 70100, 'MATTHIJS'),
(2070200, '', 'geen macrofauna DOEN NIET MEE IN BEREKENING', '', 70200, 70200, 'MATTHIJS'),
(2070300, '', 'Je hebt goede ogen dat je me vond! Ik spring rond op het wateroppervlak.', '', 70300, 70300, 'MATTHIJS'),
(2070400, '', 'Je hebt goede ogen dat je me vond! Ik vang kleine waterdiertjes met mijn tentakels!', '', 70400, 70400, 'MATTHIJS'),
(2080100, '', 'geen macrofauna DOEN NIET MEE IN BEREKENING', '', 80100, 80100, 'MATTHIJS'),
(2080200, '', 'geen macrofauna DOEN NIET MEE IN BEREKENING', '', 80200, 80200, 'MATTHIJS'),
(2080300, '', 'geen macrofauna DOEN NIET MEE IN BEREKENING', '', 80300, 80300, 'MATTHIJS'),
(2080400, '', 'geen macrofauna DOEN NIET MEE IN BEREKENING', '', 80400, 80400, 'MATTHIJS'),
(2090100, '', '', '', 90100, 90100, 'MATTHIJS'),
(2090200, '', 'geen macrofauna DOEN NIET MEE IN BEREKENING', '', 90200, 90200, 'MATTHIJS');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `LOCALEDESCRIPTION`
--
ALTER TABLE `LOCALEDESCRIPTION`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FK_LOCALEDESCRIPTION_MORPHO_ID` (`MORPHO_ID`),
  ADD KEY `FK_LOCALEDESCRIPTION_DESCRIPTIONS_ID` (`DESCRIPTIONS_ID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `LOCALEDESCRIPTION`
--
ALTER TABLE `LOCALEDESCRIPTION`
  ADD CONSTRAINT `FK_LOCALEDESCRIPTION_DESCRIPTIONS_ID` FOREIGN KEY (`DESCRIPTIONS_ID`) REFERENCES `MORPHOLOGICALCODE` (`ID`),
  ADD CONSTRAINT `FK_LOCALEDESCRIPTION_MORPHO_ID` FOREIGN KEY (`MORPHO_ID`) REFERENCES `MORPHOLOGICALCODE` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
