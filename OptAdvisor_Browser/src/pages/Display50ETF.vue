<template>
  <div id="page">
    <div id="title">
      <h2>华夏上证 50ETF </h2> <h3> (510050)</h3>
      <div id="subtitle">
        <h4>最新估值</h4>
        <h1 style="position:relative" v-bind:style="{ color:  currentPrice  > closingPrice ? 'rgb(187, 0, 0)' : (currentPrice === closingPrice ?  'black' : 'green')}">{{currentPrice}}</h1>
        <div id="current">
          <p class="currentItem" v-bind:style="{ color: rate > 0 ? 'rgb(187, 0, 0)' : (rate === 0 ?  'black' : 'green')}">{{rate.toFixed(2)}}{{rate===0?'': '%'}}</p>
          <p class="currentItem" v-bind:style="{ color: deta > 0 ? 'rgb(187, 0, 0)' : (deta === 0 ?  'black' : 'green')}">{{deta.toFixed(3)}}</p>
        </div>
      </div>
    </div>
      <Row>
        <Col :xs="24" :sm="20" :md="18" :lg="24">
          <Row  :gutter="48">
            <Col  span="6">
              <div class="baseItem">
                <p>成交量</p>
                <h2>{{volume}} 手</h2>
              </div>
            </Col>
            <Col span="6">
            <div class="baseItem">
                <p>成交额</p>
                <h2>{{unitConversion(turnover)}}</h2>
              </div>
            </Col>
            
            <Col span="6">
            <div class="baseItem">
                <p>状态</p>
                <h2>{{status}}</h2>
              </div>
            </Col>
            <Col span="6">
            <div class="baseItem">
                <p>时间</p>
                <h2>{{(nowTime)}}</h2>
              </div>
            </Col>
            
          </Row>
          <br>
          <Row style="margin-bottom:20px">
            <Col :xs="24" :sm="24" :md="24" :lg="11" >
                <Row class="dataList" >
                  <Col span="12"><p style="font-size: 12px; text-align: left;"> 昨日收盘价</p></Col>
                  <Col span="12"><p style="text-align: right;font-weight: 700;"  >{{closingPrice}}</p></Col>
                </Row>
            </Col>
            <Col :xs="24" :sm="24" :md="24" :lg="{span: 11, offset: 2}" >
                <Row class="dataList" >
                  <Col span="12"><p style="font-size: 12px; text-align: left;"> 今日开盘价</p></Col>
                  <Col span="12"><p style="text-align: right;font-weight: 700;  " v-bind:style="{ color:  openPrice  > closingPrice ? 'rgb(187, 0, 0)' : (openPrice === closingPrice ?  'black' : 'green')}" >{{openPrice}}</p></Col>
                </Row>
            </Col>
            <Col :xs="24" :sm="24" :md="24" :lg="11" >
                <Row class="dataList" >
                  <Col span="12"><p style="font-size: 12px; text-align: left;"> 最高价</p></Col>
                  <Col span="12"><p style="text-align: right;font-weight: 700;  " v-bind:style="{ color:  highPrice  > closingPrice ? 'rgb(187, 0, 0)' : (highPrice === closingPrice ?  'black' : 'green')}" >{{highPrice}}</p></Col>
                </Row>
            </Col> 
            <Col :xs="24" :sm="24" :md="24" :lg="{span: 11, offset: 2}" >
                <Row class="dataList" >
                  <Col span="12"><p style="font-size: 12px; text-align: left;"> 最低价</p></Col>
                  <Col span="12"><p style="text-align: right;font-weight: 700;  " v-bind:style="{ color:  lowPrice  > closingPrice ? 'rgb(187, 0, 0)' : (lowPrice === closingPrice ?  'black' : 'green')}" >{{lowPrice}}</p></Col>
                </Row>
            </Col>
          </Row>
          <h3 style="margin-top:20px">盘口</h3>
          <Row style="margin-bottom:20px">
            <Col :xs="24" :sm="24" :md="24" :lg="11" >
                <Row class="dataList" >
                  <Col span="8"><p style="font-size: 12px; text-align: left;"> 卖5</p></Col>
                  <Col span="8"><p style=" text-align: center;font-weight: 700;" v-bind:style="{ color:  saleList[8]  > closingPrice ? 'rgb(187, 0, 0)' : (saleList[8] === closingPrice ?  'black' : 'green')}" >{{saleList[8]}}</p></Col>
                  <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{saleList[9]}}</p></Col>
                </Row>
            </Col>
            <Col :xs="24" :sm="24" :md="24" :lg="{span: 11, offset: 2}">
                <Row class="dataList" >
                  <Col span="8"><p style="font-size: 12px; text-align: left;"> 卖4</p></Col>
                  <Col span="8"><p style=" text-align: center;font-weight: 700;" v-bind:style="{ color:  saleList[6]  > closingPrice ? 'rgb(187, 0, 0)' : (saleList[6] === closingPrice ?  'black' : 'green')}" >{{saleList[6]}}</p></Col>
                  <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{saleList[7]}}</p></Col>
                </Row>
            </Col>
            <Col :xs="24" :sm="24" :md="24" :lg="11" >
                <Row class="dataList" >
                  <Col span="8"><p style="font-size: 12px; text-align: left;"> 卖3</p></Col>
                  <Col span="8"><p style=" text-align: center;font-weight: 700;" v-bind:style="{ color:  saleList[4]  > closingPrice ? 'rgb(187, 0, 0)' : (saleList[4] === closingPrice ?  'black' : 'green')}" >{{saleList[4]}}</p></Col>
                  <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{saleList[5]}}</p></Col>
                </Row>
            </Col>
            <Col :xs="24" :sm="24" :md="24" :lg="{span: 11, offset: 2}">
                <Row class="dataList" >
                  <Col span="8"><p style="font-size: 12px; text-align: left;"> 卖2</p></Col>
                  <Col span="8"><p style=" text-align: center;font-weight: 700;" v-bind:style="{ color:  saleList[2]  > closingPrice ? 'rgb(187, 0, 0)' : (saleList[2] === closingPrice ?  'black' : 'green')}" >{{saleList[2]}}</p></Col>
                  <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{saleList[3]}}</p></Col>
                </Row>
            </Col>
            <Col :xs="24" :sm="24" :md="24" :lg="11" >
                <Row class="dataList" >
                  <Col span="8"><p style="font-size: 12px; text-align: left;"> 卖1</p></Col>
                  <Col span="8"><p style=" text-align: center;font-weight: 700;" v-bind:style="{ color:  saleList[0]  > closingPrice ? 'rgb(187, 0, 0)' : (saleList[0] === closingPrice ?  'black' : 'green')}" >{{saleList[0]}}</p></Col>
                  <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{saleList[1]}}</p></Col>
                </Row>
            </Col>
            <Col :xs="24" :sm="24" :md="24" :lg="{span: 11, offset: 2}" >
                <Row class="dataList" >
                  <Col span="8"><p style="font-size: 12px; text-align: left;"> 买1</p></Col>
                  <Col span="8"><p style=" text-align: center;font-weight: 700;" v-bind:style="{ color:  buyList[0]  > closingPrice ? 'rgb(187, 0, 0)' : (buyList[0] === closingPrice ?  'black' : 'green')}" >{{buyList[0]}}</p></Col>
                  <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{buyList[1]}}</p></Col>
                </Row>
            </Col>
            <Col :xs="24" :sm="24" :md="24"  :lg="11">
                <Row class="dataList" >
                  <Col span="8"><p style="font-size: 12px; text-align: left;"> 买2</p></Col>
                  <Col span="8"><p style=" text-align: center;font-weight: 700;" v-bind:style="{ color:  buyList[2]  > closingPrice ? 'rgb(187, 0, 0)' : (buyList[2] === closingPrice ?  'black' : 'green')}" >{{buyList[2]}}</p></Col>
                  <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{buyList[3]}}</p></Col>
                </Row>
            </Col>
            <Col :xs="24" :sm="24" :md="24" :lg="{span: 11, offset: 2}">
                <Row class="dataList" >
                  <Col span="8"><p style="font-size: 12px; text-align: left;"> 买3</p></Col>
                  <Col span="8"><p style=" text-align: center;font-weight: 700;" v-bind:style="{ color:  buyList[4]  > closingPrice ? 'rgb(187, 0, 0)' : (buyList[4] === closingPrice ?  'black' : 'green')}" >{{buyList[4]}}</p></Col>
                  <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{buyList[5]}}</p></Col>
                </Row>
            </Col>
            <Col :xs="24" :sm="24" :md="24" :lg="11" >
                <Row class="dataList" >
                  <Col span="8"><p style="font-size: 12px; text-align: left;"> 买4</p></Col>
                  <Col span="8"><p style=" text-align: center;font-weight: 700;" v-bind:style="{ color:  buyList[6]  > closingPrice ? 'rgb(187, 0, 0)' : (buyList[6] === closingPrice ?  'black' : 'green')}" >{{buyList[6]}}</p></Col>
                  <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{buyList[7]}}</p></Col>
                </Row>
            </Col>
            <Col :xs="24" :sm="24" :md="24" :lg="{span: 11, offset: 2}">
                <Row class="dataList" >
                  <Col span="8"><p style="font-size: 12px; text-align: left;"> 买5</p></Col>
                  <Col span="8"><p style=" text-align: center;font-weight: 700;" v-bind:style="{ color:  buyList[8]  > closingPrice ? 'rgb(187, 0, 0)' : (buyList[8] === closingPrice ?  'black' : 'green')}" >{{buyList[8]}}</p></Col>
                  <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{buyList[9]}}</p></Col>
                </Row>
            </Col>
            
          </Row>
          <Row>
            <Card style="margin: 50px 0;">
              <div style="text-align:left">
                <div id="selecter"> <span  v-on:click="handleClick('line')" v-bind:class="{ active: lineActive }" class="selectItem" >分时线</span> <span  v-on:click="handleClick('day')" v-bind:class="{ active: dayActive }" class="selectItem">日 k 线</span> </div>
                <div id="myEchart" ref="myechart"></div>
              </div>
            </Card> 
          </Row>
        </Col>
      </Row>
  </div>
</template>

<script >
var echarts = require("echarts");
// import ETFChart from '../components/50ETFChart/50ETFChart';
export default {
  name: "Display50ETF",
  data() {
    return {
      dataDate: 0,
      dataTime: 0,
      currentPrice: 0,
      rate: 0,
      deta: 0,
      volume: 0,
      turnover: 0,
      closingPrice: 0,
      openPrice: 0,
      highPrice: 0,
      lowPrice: 0,
      buyList: [],
      saleList: [],
      line: [],
      lineActive: true,
      dayActive: false,
      status: "收盘",
      myChart: new Object(),
      Interval: Number,
    };
  },
  // components: {ETFChart},
  computed: {
    nowTime: function() {
      console.log(this.dataTime);
      if((this.dataTime +'').length === 6){
        console.log(111);
        
        return (this.dataDate+'').slice(0,4) + '-' + (this.dataDate+'').slice(4,6) + '-' + (this.dataDate+'').slice(6,9) + ' ' + (this.dataTime+'').slice(0,2) + ':' + (this.dataTime+'').slice(2,4) + ':' + (this.dataTime+'').slice(4,7)
      } else if ((this.dataTime+'').length === 5) {
        return (this.dataDate+'').slice(0,4) + '-' + (this.dataDate+'').slice(4,6) + '-' + (this.dataDate+'').slice(6,9) + ' ' + (this.dataTime+'').slice(0,1) + ':' + (this.dataTime+'').slice(1,3) + ':' + (this.dataTime+'').slice(3,6)
      }
    }
  },
  methods: {
    getSnapData(first=false) {
      this.axios
        .get("/sse/snap/510050")
        .then(re => {
          [
            ,
            this.closingPrice,
            this.openPrice, 
            this.highPrice,
            this.lowPrice,
            this.currentPrice,
            this.deta,
            this.rate,
            this.volume,
            this.turnover,
            ,
            ,
            ,
            this.buyList,
            this.saleList
          ] = re.data.snap;
          this.dataDate = re.data.date;
          this.dataTime = re.data.time;
          this.status = this.getStatus();
          if(this.lineActive) {
            if(first){
              this.getLineData();
            } else {
              this.getLineData(true);
            }
          }
        })
        .catch(error => {});
    },
    getStatus() {
      const a = new Date();
      const day = a.getDate();
      const minutes = a.getMinutes();
      const hour = a.getHours();
      if(day === 0 || day === 6) {
        return '休市';
      } else {
        if( hour<9 || hour > 14 || ( minutes <15 && hour===9 ) ){
          return '休市';
        } else if( hour===9 && minutes < 30 && minutes > 15) {
          return '集合竞价结束';
        } else if( (hour === 9 && minutes >= 30) || hour === 10 || (hour === 11 && minutes<=30) || hour ===13 || hour === 14 || (hour === 15 && minutes === 0)) {
          return '连续竞价';
        } else {
          return '收盘';
        }
        }
      },

    getLineData: function(fresh=false) {
      this.axios
        .get("/sse/line/510050", {
          params: {
            select: "time,price,volume",
          }
        })
        .then(re => {
          this.lineDate = (re.data.date+'-').substring(0, 4) + "-" + (re.data.date+'-').substring(4, 6) + "-" + (re.data.date+'-').substring(6, 8);
          if (fresh) {
            this.freshLine(re.data.line);
          } else {
            this.initLine(re.data.line);
          }
        })
        .catch(error => {});
    },
    getDayKData: function() {
      this.axios
        .get("/sse/dayk/510050", {
          params: {
            select: "date,open,high,low,close,volume",
            begin: -300,
            end: -1,
          }
        })
        .then(res => {
          this.dayKData = res.data.kline;
          this.setDayKOptions();
        });
    },
    initLine(lineData) {
      const that = this;
      const XDate = lineData.map(item => {
              const hour =
                Math.floor(item[0] / 10000) > 9
                  ? Math.floor(item[0] / 10000)
                  : "0" + Math.floor(item[0] / 10000);
              const minutes = (item[0] % 10000) / 100;
              return `${hour}:${minutes>9? minutes : '0'+minutes}`;
            })
      const priceList = lineData.map((item) => {
        return item[1];
      });
      const volumeList = lineData.map((item) => {
        return item[2];
      })
      this.myChart.setOption({
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross"
          },
          backgroundColor: "rgba(245, 245, 245, 0.8)",
          borderWidth: 1,
          borderColor: "#ccc",
          padding: 10,
          textStyle: {
            color: "#000"
          },
          extraCssText: "width: 170px",
          formatter(params, ticket, callback) {
            return `2018-8-13<br />${params[0].name}<br/> <span style="color:${
              params[0].color
            }">● </span>${params[0].seriesName}:${params[0].value}元<br />
                                     <span style="color:${
                                       params[1].color
                                     }">● </span>${params[1].seriesName}:${
              params[1].value
            }手`;
          }
        },
        axisPointer: {
          link: { xAxisIndex: "all" }
        },
        grid: [
          {
            left: "10%",
            right: "8%",
            height: "50%"
          },
          {
            left: "10%",
            right: "8%",
            bottom: "20%",
            height: "15%"
          }
        ],
        xAxis: [
          {
            type: "category",
            data: XDate,
          },
          {
            gridIndex: 1,
            type: "category",
            data: XDate,
            scale: true,
            boundaryGap: false,
            axisLine: { onZero: false },
            axisTick: { show: false },
            splitLine: { show: false },
            axisLabel: { show: false },
            splitNumber: 20
          }
        ],
        yAxis: [
          {
            name: "价格",
            type: "value",
            position: "left",
            max: function(value) {
              const max =
                value.max + value.min >= 2 * that.closingPrice
                  ? value.max
                  : 2 * that.closingPrice - value.min;
              return max.toFixed(4);
            },
            min: function(value) {
              const min =
                value.max + value.min >= 2 * that.closingPrice
                  ? 2 * that.closingPrice - value.max
                  : value.min;
              return min.toFixed(4);
            }
          },
          {
            scale: true,
            gridIndex: 1,
            splitNumber: 2,
            axisLabel: { show: false },
            axisLine: { show: false },
            axisTick: { show: false },
            splitLine: { show: false }
          }
        ],
        dataZoom: [
          {
            type: "inside",
            xAxisIndex: [0, 1],
            start: 0,
            end: 100
          },
          {
            show: true,
            xAxisIndex: [0, 1],
            type: "slider",
            top: "85%",
            start: 0,
            end: 100
          }
        ],
 
        series: [
          {
            name: "价格",
            data: priceList,
            type: "line",
            itemStyle: {
              color: 'interval'
            },
            markLine: {
              name: "昨日收盘价",
              lineStyle: {
                type: "solid"
              },
              label: {
                data: 'heh'
              },
              data: [
                {
                  yAxis: that.closingPrice
                }
              ]
            }
          },
          {
            xAxisIndex: 1,
            yAxisIndex: 1,
            name: "成交量",
            data: volumeList,
            type: "bar",
            itemStyle: {
              color: '#25258e',
              opacity: 0.5,
            },
          },
        ]
      }, true);
      window.addEventListener("resize", function() {
        that.myChart.resize();
      });
      this.myChart.hideLoading();
    },
    freshLine(lineData) {
      if(this.dayActive){
        return false;
      }
      const that = this;
      const XDate = lineData.map(item => {
              const hour =
                Math.floor(item[0] / 10000) > 9
                  ? Math.floor(item[0] / 10000)
                  : "0" + Math.floor(item[0] / 10000);
              const minutes = (item[0] % 10000) / 100;
              return `${hour}:${minutes>9? minutes : '0'+minutes}`;
            })
      const priceList = lineData.map((item) => {
        return item[1];
      });
      const volumeList = lineData.map((item) => {
        return item[2];
      });
      this.myChart.setOption({
         xAxis: [
          {
            data: XDate,
          },
          {
            data: XDate,
          }
        ],
        series: [
          {
            data: priceList,
          },
          {
            data: volumeList,
          }
        ]
      });
      this.myChart.hideLoading();
    },

    setDayKOptions() {
      const data = {
        values: [],
        dates: [],
        volumes: [],
      }
      function calculateMA(dayCount, data) {
        const result = [];
        for (let i = 0, len = data.values.length; i < len; i++) {
                if (i < dayCount) {
                    result.push('-');
                    continue;
                }
                let sum = 0;
                for (let j = 0; j < dayCount; j++) {
                    sum += data.values[i - j][1];
                }
                result.push(+(sum / dayCount).toFixed(3));
            }
            return result;
        }
      this.dayKData.map(item => {
        data.dates.push([item[0]]);
        data.values.push([
          item[1], item[4], item[3],item[2], 
        ]);
        data.volumes.push(item[5])
      })
      this.dayKOption = {
        animation: false,
        legend: {
            bottom: 10,
            left: 'center',
            data: ['50ETF', 'MA5', 'MA10', 'MA20', 'MA30']
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            },
            backgroundColor: "rgba(245, 245, 245, 0.8)",
            borderWidth: 1,
            borderColor: "#ccc",
            padding: 10,
            textStyle: {
              color: "#000"
            },
            extraCssText: "width: 170px",
            formatter(params, ticket, callback) {
              let str = '' ;
              str  += params[0].name;
              params.map((item)=> {
                let temp = '';
                if(item.seriesName === '50ETF') {
                  str += `<br />${item.marker}<span style="font-size: 20px;"> 50ETF</span>
                          <br />开盘:${item.value[1]}
                          <br />收盘:${item.value[2]}
                          <br />最低:${item.value[3]}
                          <br />最高:${item.value[4]}`
                } else if(item.seriesName === 'Volumn'){
                  str += `<br />${item.marker}成交量:${item.value}`
                } else {
                  str += `<br />${item.marker}${item.seriesName}:${item.value}` 
                }
              });
              return str;
          }
        },
        axisPointer: {
            link: {xAxisIndex: 'all'},
            label: {
                backgroundColor: '#777'
            }
        },
        grid: [
            {
                left: '10%',
                right: '8%',
                height: '50%'
            },
            {
                left: '10%',
                right: '8%',
                bottom: '20%',
                height: '15%'
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: data.dates.map(item => {
                    return (item[0]+'').slice(0,4) + '-' + (item[0]+'').slice(4,6) + '-' + (item[0]+'').slice(6,9)
                  }),
                scale: true,
                boundaryGap : false,
                axisLine: {onZero: false},
                splitLine: {show: false},
                splitNumber: 20,
                min: 'dataMin',
                max: 'dataMax',
                axisPointer: {
                    z: 100
                }
            },
            {
                type: 'category',
                gridIndex: 1,
                data: data.dates.map(item => {
                    return (item[0]+'').slice(0,4) + '-' + (item[0]+'').slice(4,6) + '-' + (item[0]+'').slice(6,9)
                  }),
                scale: true,
                boundaryGap : false,
                axisLine: {onZero: false},
                axisTick: {show: false},
                splitLine: {show: false},
                axisLabel: {show: false},
                splitNumber: 20,
                min: 'dataMin',
                max: 'dataMax',

            }
        ],
        yAxis: [
            {
                scale: true,
                splitArea: {
                    show: true
                }
            },
            {
                scale: true,
                gridIndex: 1,
                splitNumber: 2,
                axisLabel: {show: false},
                axisLine: {show: false},
                axisTick: {show: false},
                splitLine: {show: false}
            }
        ],
        dataZoom: [
            {
                type: 'inside',
                xAxisIndex: [0, 1],
                start: 85,
                end: 100
            },
            {
                show: true,
                xAxisIndex: [0, 1],
                type: 'slider',
                top: '85%',
                start: 85,
                end: 100
            }
        ],
        series: [
            {
                name: '50ETF',
                type: 'candlestick',
                data: data.values,
                itemStyle: {
                    normal: {
                        color: '#06B800',
                        color0: '#FA0000',
                        borderColor: null,
                        borderColor0: null
                    }
                },
            },
            {
                name: 'MA5',
                type: 'line',
                data: calculateMA(5, data),
                smooth: true,
                lineStyle: {
                    normal: {opacity: 0.5}
                }
            },
            {
                name: 'MA10',
                type: 'line',
                data: calculateMA(10, data),
                smooth: true,
                lineStyle: {
                    normal: {opacity: 0.5}
                }
            },
            {
                name: 'MA20',
                type: 'line',
                data: calculateMA(20, data),
                smooth: true,
                lineStyle: {
                    normal: {opacity: 0.5}
                }
            },
            {
                name: 'MA30',
                type: 'line',
                data: calculateMA(30, data),
                smooth: true,
                lineStyle: {
                    normal: {opacity: 0.5}
                }
            },
            {
                name: 'Volumn',
                type: 'bar',
                xAxisIndex: 1,
                yAxisIndex: 1,
                data: data.volumes
            }
        ]
      };
    },

    // 单位转换
    unitConversion(data) {
      if (data > 10000) {
        return `${(data / 10000).toFixed(0)} 万元`;
      } else {
        return data + " 元";
      }
    },
    handleClick(ms) {
      const that = this;
      this.myChart.showLoading();
      if (ms === "day") {
        this.dayActive = true;
        this.lineActive = false;
        clearTimeout(this.waitTimeout)
        this.waitTimeout = setTimeout(function(){
          that.myChart.setOption(that.dayKOption,true, false, false);
          that.myChart.hideLoading();
        },1500)
      } else {
        this.dayActive = false;
        this.lineActive = true;
        clearTimeout(this.waitTimeout);
        this.myChart.showLoading();
        this.waitTimeout = setTimeout(this.getLineData, 1000);
      }   
    }
  },
  mounted() {
    this.myChart = echarts.init(this.$refs.myechart);
    this.getSnapData(true);
    this.getDayKData();
    this.Interval = setInterval(this.getSnapData, 7000);
  },
  destroyed() {
    clearInterval(this.Interval);
  },
};
</script>

<style scoped>
#page {
  height: 900px;
  padding: 0;
}
#current {
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: 15px;
  margin-left: 50px;
  width: 50px;
}
.currentItem{
  display:inline; 
  font-size:14px;
  font-weight: 900; 
}
#title {
  text-align: center;
  margin: 0 0 50px 0;
  padding: 20px 0 0 0 ;
  position: relative;
}
#subtitle {
  margin: 20px 0 0 0;
}
h1 {
    margin-top: 0;
  }
h2 {
  line-height: 40px;
}
h1,
h2,
h3 {
  display: inline-block;
}
.dataList {
  border-top: 1px dotted grey;
  line-height: 30px;
}
#selecter {
  display: inline-block;
  padding: 4px;
  border: 1px solid #e1e1e1;
  background-color: #fff;
  border-radius: 2px;
}
.selectItem {
  display: inline-block;
  padding: 4px 12px;
  cursor: pointer;
}
.selectItem:hover {
  background-color: #e1e1e1;
}
.baseItem{
  text-align: left;
}
.active {
  background-color: #25258e !important;
  color: white;
}
#myEchart{
    height: 450px;
  }
@media screen and (max-width: 1050px) {
  h2 {
    font-size: 1em;
  }
  #subtitle {
    margin-top: 10px;
  }
  #title {
    margin-bottom: 20px;
  }
  #myEchart{
    height: 300px;
  }
}
</style>
