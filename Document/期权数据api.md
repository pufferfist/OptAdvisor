1. http://stock.finance.sina.com.cn/futures/api/openapi.php/StockOptionService.getStockName 

获得期权都有什么月份的合约   **{T(后四个日期)}**

```
返回值：{"result":{"status":{"code":0},"data":{"cateList":["50ETF","50ETF"],"contractMonth":["2018-08","2018-08","2018-09","2018-12","2019-03"],"stockId":"510050","cateId":"510050C1809"}}}
```

contractMonth是今天买的期权的到期时间，为当月、下月及随后两个季月（季月为3、6、9、12月） 见流程说明[1]



2. http://stock.finance.sina.com.cn/futures/api/openapi.php/StockOptionService.getRemainderDay?date=201808  ·**{到期日，剩余天数}**

获得合约的到期日和剩余天数 ,参数为上个接口的时间



3. http://hq.sinajs.cn/list=OP_UP_5100501808,OP_DOWN_5100501808

   查询某月到期看涨看跌合约列表

   UP代表看涨，DOWN代表看跌，510050是固定的代表50ETF，后面四位代表时间，即接口1中的四个时间

例：OP_DOWN_5100501808代表2018年8月看跌的合约列表

```
返回值：var hq_str_OP_DOWN_5100501808="CON_OP_10001408,CON_OP_10001394,CON_OP_10001395,CON_OP_10001396,CON_OP_10001397,CON_OP_10001398,CON_OP_10001399,CON_OP_10001400,CON_OP_10001401,CON_OP_10001402,CON_OP_10001404,CON_OP_10001414,CON_OP_10001416,";
```



4. http://hq.sinajs.cn/list=s_sh510050,sh510050

   这两个是50ETF相关信息**{最新价}**

   ```
   参数为s_sh510050,返回值var hq_str_s_sh510050="50ETF,2.455,-0.002,-0.08,6107523,150570";

   ```


   第一项是名字,第二项是当前价格,其他暂时未知





​	http://hq.sinajs.cn/list=f_510050,**{S0}**





5. http://hq.sinajs.cn/list=CON_OP_10001407

   OP代表行情**{Option}**

   参数为合约代码，及接口3返回的结果

   ```
   返回值：var hq_str_CON_OP_10001407="5,0.2610,0.2609,0.2624,1,3024,-0.76,2.2000,0.2629,0.2687,0.5086,0.0172,0.2650,1,0.2629,1,0.2628,1,0.2626,10,0.2624,1,0.2610,5,0.2607,1,0.2606,1,0.2605,5,0.2600,1,2018-08-06 14:56:44,0,E 01,EBS,510050,50ETF购8月2200,20.39,0.2990,0.2454,3580,9723504.00,M";
   ```

   各个字段的含义：var hq_str_CON_OP_代码=“买量(0)，买价，最新价，卖价，卖量，持仓量，涨幅，行权价，昨收价，开盘价，涨停价，跌停价(11), 申卖 价五，申卖量五，申卖价四，申卖量四，申卖价三，申卖量三，申卖价二，申卖量二，申卖价一，申卖量一，申买价一，申买量一 ，申买价二，申买量二，申买价三，申买量三，申买价四，申买量四，申买价五，申买量五，行情时间，主力合约标识，状态码， 标的证券类型，标的股票，期权合约简称，振幅(38)，最高价，最低价，成交量，成交额


6. http://hq.sinajs.cn/list=CON_SO_10001407

   SO代表合约分析

   参数仅将接口三返回的合约代码的OP换为SO，后面数字编码不变

   ```
   返回值：var hq_str_CON_SO_10001407="50ETF购8月2200,,,,3580,0.9949,0.1365,-0.1053,0.0075,0.2955,0.2990,0.2454,510050C1808M02200,2.2000,0.2609,0.259,M";
   ```

   各个字段的含义：var hq_str_CON_SO_代码=“期权合约简称,,,,成交量,Delta,Gamma,Theta,Vega,隐含波动率,最高价,最低价,交易代码,行权价,最新价,理论价值”


7. http://hq.sinajs.cn/list=CON_ZL_10001392

   ZL代表主力合约相关信息,理论上同一到期日的所有看涨(跌)合约中持仓量最大的合约是主力合约,实际上后面的代码填其他合约代码也能正常返回信息,合约代码同上个接口

   ```
   返回值:var hq_str_CON_ZL_10001392="50ETF购8月2600,,,,125196,2023.21%,0.0134,-82.48%,0.0134,0.0138,0.0193,0.0118,86396,0.1055,1.7003,-0.2335,0.0938,0.2787,510050C1808M02600,2.6000,0.0053,M";
   ```

   各个字段的含义:var hq_str_CON_SO_代码="期权合约简称,,,,持仓量,持仓量占比,最新价,涨幅,买价,卖价,最高价,最低价,成交量,Delta,Gamma,Theta,Vega,隐含波动率,交易代码,行权价,理论价值"

8. 补充

   关于价值状态,内在价值,时间价值

   ![1533545734905](Graph\示意图.png)

   ```
   //新浪财经源码
   var up_down=change;
   var price_mark=(A_price-arr[13]).toFixed(4);//A_price为50ETF价格,见接口4;arr为合约分析数组,arr[13]即行权价,见接口6

   switch(up_down)
   {
     case 'up':
     json.price=price_mark>0?'实值':price_mark<0?'虚值':'平值'; //价值状态
     json.in_price=price_mark>0?price_mark:0; //内在价值
     json.out_price=(arr[14]-json.in_price<=0)?'0.00':(arr[14]-json.in_price).toFixed(4); //时间价值
     break;
     case 'down':
     json.price=price_mark<0?'实值':price_mark>0?'虚值':'平值';
     json.in_price=price_mark<0?-price_mark:0;
     json.out_price=(arr[14]-json.in_price).toFixed(4);
     break;
   }
   ```


