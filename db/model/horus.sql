-- MySQL dump 10.13  Distrib 5.1.41, for Win32 (ia32)
--
-- Host: localhost    Database: horus
-- ------------------------------------------------------
-- Server version	5.1.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary table structure for view `horus_produto`
--

DROP TABLE IF EXISTS `horus_produto`;
/*!50001 DROP VIEW IF EXISTS `horus_produto`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `horus_produto` (
  `codigo` varbinary(14),
  `id` int(11),
  `id_fornecedor` int(2) unsigned zerofill,
  `codigo_tipo_produto` varchar(3),
  `nome` varchar(45),
  `descricao` varchar(1000),
  `valor_entrada` decimal(10,2),
  `valor_saida` decimal(10,2),
  `codigo_origem_fornecedor` varchar(20),
  `ic_ativo` int(1) unsigned,
  `qtd_estoque` int(11),
  `qtd_consig` int(11),
  `qtd_total` bigint(12)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_contato_fornecedor`
--

DROP TABLE IF EXISTS `t_contato_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_contato_fornecedor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(60) DEFAULT NULL,
  `cpf` varchar(11) DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `telefone1` varchar(10) DEFAULT NULL,
  `telefone2` varchar(10) DEFAULT NULL,
  `id_fornecedor` int(2) unsigned zerofill NOT NULL,
  PRIMARY KEY (`id`),
  KEY `t_contato_fornecedor_ibfk_1` (`id_fornecedor`),
  CONSTRAINT `t_contato_fornecedor_ibfk_1` FOREIGN KEY (`id_fornecedor`) REFERENCES `t_fornecedor` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_fornecedor`
--

DROP TABLE IF EXISTS `t_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_fornecedor` (
  `id` int(2) unsigned zerofill NOT NULL,
  `nome_fantasia` varchar(60) NOT NULL,
  `razao_social` varchar(60) DEFAULT NULL,
  `cnpj` varchar(14) DEFAULT NULL,
  `telefone1` varchar(10) DEFAULT NULL,
  `telefone2` varchar(10) DEFAULT NULL,
  `site` varchar(40) DEFAULT NULL,
  `ic_ativo` int(1) NOT NULL DEFAULT '1',
  `logradouro` varchar(40) DEFAULT NULL,
  `numero` varchar(10) DEFAULT NULL,
  `complemento` varchar(40) DEFAULT NULL,
  `bairro` varchar(40) DEFAULT NULL,
  `municipio` varchar(40) DEFAULT NULL,
  `uf` varchar(2) DEFAULT NULL,
  `cep` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_log_estoque`
--

DROP TABLE IF EXISTS `t_log_estoque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_log_estoque` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fluxo` varchar(1) NOT NULL,
  `id_motivo` int(11) NOT NULL,
  `id_produto` int(11) NOT NULL,
  `dt_movimentacao` datetime NOT NULL,
  `qtd_produto` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_motivo` (`id_motivo`),
  KEY `id_produto` (`id_produto`),
  CONSTRAINT `t_log_estoque_ibfk_1` FOREIGN KEY (`id_motivo`) REFERENCES `t_motivo_movimento_estoque` (`id`),
  CONSTRAINT `t_log_estoque_ibfk_3` FOREIGN KEY (`id_produto`) REFERENCES `t_produto` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_mostruario`
--

DROP TABLE IF EXISTS `t_mostruario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_mostruario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_revendedora` int(11) NOT NULL,
  `data_retirada` date NOT NULL,
  `data_acerto` date NOT NULL,
  `data_fechamento` date DEFAULT NULL,
  `ic_status` int(1) NOT NULL DEFAULT '0',
  `qtd_venda` int(11) DEFAULT NULL,
  `vl_total` decimal(10,2) DEFAULT NULL,
  `comissao` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_revendedora` (`id_revendedora`),
  CONSTRAINT `t_mostruario_ibfk_1` FOREIGN KEY (`id_revendedora`) REFERENCES `t_revendedora` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_motivo_movimento_estoque`
--

DROP TABLE IF EXISTS `t_motivo_movimento_estoque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_motivo_movimento_estoque` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(100) NOT NULL,
  `ic_ativo` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_pgto_mostruario`
--

DROP TABLE IF EXISTS `t_pgto_mostruario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_pgto_mostruario` (
  `id` int(11) NOT NULL,
  `id_mostruario` int(11) NOT NULL,
  `data_prev_pgto` date NOT NULL,
  `data_rlzd_pgto` date DEFAULT NULL,
  `vl_pgto` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`,`id_mostruario`),
  KEY `fk_pgto_mostruario` (`id_mostruario`),
  CONSTRAINT `fk_pgto_mostruario` FOREIGN KEY (`id_mostruario`) REFERENCES `t_mostruario` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_produto`
--

DROP TABLE IF EXISTS `t_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_produto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_fornecedor` int(2) unsigned zerofill NOT NULL,
  `codigo_tipo_produto` varchar(3) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `descricao` varchar(1000) DEFAULT NULL,
  `valor_entrada` decimal(10,2) DEFAULT NULL,
  `valor_saida` decimal(10,2) NOT NULL,
  `codigo_origem_fornecedor` varchar(20) DEFAULT NULL,
  `ic_ativo` int(1) unsigned NOT NULL DEFAULT '1',
  `qtd_estoque` int(11) NOT NULL DEFAULT '0',
  `qtd_consig` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `id_fornecedor` (`id_fornecedor`),
  KEY `codigo_tipo_produto` (`codigo_tipo_produto`),
  CONSTRAINT `t_produto_ibfk_1` FOREIGN KEY (`id_fornecedor`) REFERENCES `t_fornecedor` (`id`),
  CONSTRAINT `t_produto_ibfk_2` FOREIGN KEY (`codigo_tipo_produto`) REFERENCES `t_tipo_produto` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_produto_mostruario`
--

DROP TABLE IF EXISTS `t_produto_mostruario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_produto_mostruario` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `id_mostruario` int(11) NOT NULL,
  `id_produto` int(11) NOT NULL,
  `ic_vendido` int(1) NOT NULL DEFAULT '0',
  `vl_produto` decimal(10,2) NOT NULL,
  `data_inclusao` date DEFAULT NULL,
  PRIMARY KEY (`id`,`id_mostruario`),
  KEY `id_mostruario` (`id_mostruario`),
  KEY `id_produto` (`id_produto`),
  CONSTRAINT `t_produto_mostruario_ibfk_1` FOREIGN KEY (`id_mostruario`) REFERENCES `t_mostruario` (`id`),
  CONSTRAINT `t_produto_mostruario_ibfk_2` FOREIGN KEY (`id_produto`) REFERENCES `t_produto` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_revendedora`
--

DROP TABLE IF EXISTS `t_revendedora`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_revendedora` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(60) NOT NULL,
  `cpf` varchar(11) DEFAULT NULL,
  `data_nascimento` varchar(10) DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `telefone1` varchar(10) DEFAULT NULL,
  `telefone2` varchar(10) DEFAULT NULL,
  `comissao` decimal(5,2) NOT NULL,
  `ic_ativo` int(1) NOT NULL DEFAULT '1',
  `logradouro` varchar(40) DEFAULT NULL,
  `numero` varchar(10) DEFAULT NULL,
  `complemento` varchar(40) DEFAULT NULL,
  `bairro` varchar(40) DEFAULT NULL,
  `municipio` varchar(40) DEFAULT NULL,
  `uf` varchar(2) DEFAULT NULL,
  `cep` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_tipo_produto`
--

DROP TABLE IF EXISTS `t_tipo_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_tipo_produto` (
  `codigo` varchar(3) NOT NULL,
  `descricao` varchar(100) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `horus_produto`
--

/*!50001 DROP TABLE IF EXISTS `horus_produto`*/;
/*!50001 DROP VIEW IF EXISTS `horus_produto`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `horus_produto` AS select concat(`t_produto`.`codigo_tipo_produto`,`t_produto`.`id`) AS `codigo`,`t_produto`.`id` AS `id`,`t_produto`.`id_fornecedor` AS `id_fornecedor`,`t_produto`.`codigo_tipo_produto` AS `codigo_tipo_produto`,`t_produto`.`nome` AS `nome`,`t_produto`.`descricao` AS `descricao`,`t_produto`.`valor_entrada` AS `valor_entrada`,`t_produto`.`valor_saida` AS `valor_saida`,`t_produto`.`codigo_origem_fornecedor` AS `codigo_origem_fornecedor`,`t_produto`.`ic_ativo` AS `ic_ativo`,`t_produto`.`qtd_estoque` AS `qtd_estoque`,`t_produto`.`qtd_consig` AS `qtd_consig`,(`t_produto`.`qtd_estoque` + `t_produto`.`qtd_consig`) AS `qtd_total` from `t_produto` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-08-12 16:42:52
