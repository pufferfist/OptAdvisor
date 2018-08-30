use utf8db;
drop table if exists time_series_data;
drop table if exists option_basic_info;
drop table if exists option_tsd;
CREATE TABLE `time_series_data` (
  `id` bigint(20) NOT NULL,
  `last_trade_date` varchar(255) DEFAULT NULL,
  `close_price` double NOT NULL,
  `open_price` double NOT NULL,
  `high_price` double NOT NULL,
  `low_price` double NOT NULL,
  `amt` double NOT NULL,
  `volume` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `option_basic_info` (
  `code_name` varchar(255) NOT NULL,
  `type` tinyint(1) NOT NULL,
  `abbr` varchar(255) DEFAULT NULL,
  `list_date` varchar(255) DEFAULT NULL,
  `end_date` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `multiplier` int(11) NOT NULL,
  `duration` int(11) NOT NULL,
  `trade_date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`code_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `option_tsd` (
  `id` bigint(20) NOT NULL,
  `latest_date` varchar(255) DEFAULT NULL,
  `code_name` varchar(255) DEFAULT NULL,
  `close_price` double DEFAULT NULL,
  `avg_price` double DEFAULT NULL,
  `pre_close_price` double DEFAULT NULL,
  `pre_end_price` double NOT NULL,
  `end_price` double NOT NULL,
  `theory_price` double DEFAULT NULL,
  `delta` double DEFAULT NULL,
  `gamma` double DEFAULT NULL,
  `vega` double DEFAULT NULL,
  `theta` double DEFAULT NULL,
  `rho` double DEFAULT NULL,
  `implied_volatility` double DEFAULT NULL,
  `history_volatility` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

load data infile '\\root\\historyData\\Option50ETF_TimeSeriesData20180416plus.txt' ignore
into table option_tsd
character set utf8
fields terminated by ','
optionally enclosed by '"'
lines terminated by '\n';

load data infile '\\root\\historyData\\Option50ETF_TimeSeriesData20180416.txt' ignore
into table option_tsd
character set utf8
fields terminated by ','
optionally enclosed by '"'
lines terminated by '\n';

load data infile '\\root\\historyData\\ETF50_Time_Series_Data.txt' ignore
into table time_series_data
character set utf8
fields terminated by ','
optionally enclosed by '"'
lines terminated by '\n';

load data infile '\\root\\historyData\\Option50etf_basic info.txt' ignore
into table option_basic_info
character set utf8
fields terminated by ','
optionally enclosed by '"'
lines terminated by '\n';