<template>
    <Card style="margin: 50px 0;">
        <div style="text-align:left">
        <div id="selecter"> <span  v-on:click="handleClick('line')" v-bind:class="{ active: lineActive }" class="selectItem" >分时线</span> <span  v-on:click="handleClick('day')" v-bind:class="{ active: dayActive }" class="selectItem">日 k 线</span> </div>
        <div id="myEchart" style="height: 600px;;" ref="myechart"></div>
        </div>
    </Card>
</template>
<script>
var echarts = require("echarts");
export default {
  name: "ETFChart",
  props: {
    lineMiddle:Number = 0,
  },
  data() {
    return {
      line: [],
      lineActive: true,
      dayActive: false,
      myChart: new Object()
    };
  },
  methods: {
    getLineData: function(first=false) {
      this.axios
        .get("/sse/line/510050", {
          params: {
            select: "time,price,volume",
          }
        })
        .then(re => {
          this.lineDate = (re.data.date+'-').substring(0, 4) + "-" + (re.data.date+'-').substring(4, 6) + "-" + (re.data.date+'-').substring(6, 8);
          if (first) {
            this.initLine(re.data.line);
          } else {
            this.freshLine(re.data.line);
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
      console.log(lineData);
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
            }">● </span>${params[0].seriesName}:${params[0].value}<br />
                                     <span style="color:${
                                       params[1].color
                                     }">● </span>${params[1].seriesName}:${
              params[1].value
            }`;
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
              console.log(that.lineMiddle, 23444);
              if(that.lineMiddle) {
                console.log(that.lineMiddle)
                const max =
                  value.max + value.min >= 2 * that.lineMiddle
                    ? value.max
                    : 2 * that.lineMiddle - value.min;
                return max.toFixed(4);  
              } else {
                console.log(value.max.toFixed(4), 123555);
                return value.max.toFixed(4);
              }

            },
            min: function(value) {
              if (that.lineMiddle ) {
                const min =
                  value.max + value.min >= 2 * that.lineMiddle
                    ? 2 * that.lineMiddle - value.max
                    : value.min;
                return min.toFixed(4);
              } else {
                return value.min.toFixed(4)
              }
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
                  yAxis: 2.45,
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
                        color0: '#FA0000',
                        color: '#06B800',
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
    handleClick(ms) {
      const that = this;
      this.myChart.showLoading();
      if (ms === "day") {
        this.dayActive = true;
        this.lineActive = false;
        setTimeout(function(){
          that.myChart.setOption(that.dayKOption,true, false, false);
          that.myChart.hideLoading();
        },1500)
      } else {
        this.dayActive = false;
        this.lineActive = true;
        this.getLineData(true);
      }   
    }
  },
  mounted() {
    console.log(this)
    this.myChart = echarts.init(this.$refs.myechart);
    this.getLineData(true)
    this.getDayKData();
    setInterval(this.getLineData, 7000);
  }
};
</script>

<style scoped>

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

.active {
  background-color: #25258e !important;
  color: white;
}
@media screen and (max-width: 1050px) {
  #myEchart{
    height: 350px;
  }
}
</style>