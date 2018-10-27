<template>
    <div style="text-align:left">
        <div id="selecter"> <span  v-on:click="handleClick('line')" v-bind:class="{ active: lineActive }" class="selectItem" >分时线</span> <span  v-on:click="handleClick('day')" v-bind:class="{ active: dayActive }" class="selectItem">日 k 线</span> </div>
        <div id="myEchart" ref="myechart"></div>
    </div>
</template>
<script>
    const echarts = require("echarts");
    export default {
      name:"OptionChart",
        data(){
            return {
                dayActive: false,
                lineActive: true,
                interval: Number,
                lineData: [],
                dayKData: [],
            }
        },
        props: {
            optionCode: String // hq_str_CON_OP_10001438
        },
        methods: {
            handleClick(ms) {
            const that = this;
            this.myChart.showLoading();
            if (ms === "day") {
              this.dayActive = true;
              this.lineActive = false;
              clearTimeout(this.timeout);
              this.timeout = setTimeout(function(){
                that.initDayK();
              },1500);
            } else {
              this.dayActive = false;
              this.lineActive = true;
              clearTimeout(this.timeout);
              this.timeout = setTimeout(this.initLine, 1000);
            }
          },
          initLine() {
            const that = this;
            const XDate = this.lineData.map(item => {
                   return item.i;
                  })
            const priceList = this.lineData.map((item) => {
              return item.p;
            });
            const volumeList = this.lineData.map((item) => {
              return item.v;
            })
            this.myChart.setOption({
                color:['black'],
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
                    return `${params[0].name}<br/> <span style="color:${
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
                      if(that.lineMiddle) {
                        const max =
                          value.max + value.min >= 2 * that.lineMiddle
                            ? value.max
                            : 2 * that.lineMiddle - value.min;
                        return max.toFixed(4);
                      } else {
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
          initDayK() {
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
              };
            this.dayKData.map(item => {
              data.dates.push(item.d);
              data.values.push([
                parseFloat(item.o), parseFloat(item.c), parseFloat(item.l), parseFloat(item.h),
              ]);
              data.volumes.push(parseInt(item.v))
            });
            this.dayKOption = {
              animation: false,
              legend: {
                  bottom: 10,
                  left: 'center',
                  data: [, 'MA5', 'MA10', 'MA20', 'MA30']
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
                      if(item.seriesType === "candlestick") {
                        str += `<br />${item.marker}<span style="font-size: 15px;">${item.seriesName}</span>
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
                      data: data.dates,
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
                      data: data.dates,
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
                      start: 50,
                      end: 100
                  },
                  {
                      show: true,
                      xAxisIndex: [0, 1],
                      type: 'slider',
                      top: '85%',
                      start: 50,
                      end: 100
                  }
              ],
              series: [
                  {
                      name: this.optionCode,
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
            this.myChart.setOption(this.dayKOption,true);
            this.myChart.hideLoading();

          },
          chartFresh() {
            if(this.dayActive){
              return false;
            }
            const that = this;
            const XDate = this.lineData.map(item => {
                   return item.i;
                  })
            const priceList = this.lineData.map((item) => {
              return item.p;
            });
            const volumeList = this.lineData.map((item) => {
              return item.v;
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
          getChartData(needDay=false) {
              if(!this.optionCode){
                  return;
              }
            const name = this.optionCode;
            if(this.lineActive) {
              this.axios.get('/sinaTime/StockOptionDaylineService.getOptionMinline', {
              params: {
                symbol: name,
                random: new Date().getTime(),
              }
              }).then(res => {
                let data = res.data.result.data;
                let count = 0;
                data.find((item) => {
                  if(parseFloat(item.a) === 0) {
                    count++;
                    return false;
                  } else {
                    return true
                  }
                })
                data.splice(0, count);
                this.lineData =data;
                this.chartFresh();

              });
            }
            if(!this.dayKData.length || needDay) {
              this.axios.get('/sinaTime/StockOptionDaylineService.getSymbolInfo', {
                params: {
                  symbol: name,
                  random: new Date().getTime(),
                }
              }).then(res => {
                this.dayKData = res.data.result.data;
              })
            }
          },
        },
        mounted() {
            this.myChart = echarts.init(this.$refs.myechart);
            this.initLine();
            this.interval = setInterval(this.getChartData(), 5000);
        },
        watch:{
            optionCode(cur, old){
                if(cur !==  ''){
                    clearInterval(this.interval);
                    this.handleClick('line');
                    this.getChartData();
                    setTimeout(this.myChart.resize(),200);
                    this.interval = setInterval(this.getChartData(), 5000);
                }

            }
        },
        destroyed() {
          clearInterval(this.interval);
        },

    }
</script>
<style>
  #selecter {
    font-size: 14px;
  display: inline-block;
  padding: 4px;
  border: 1px solid #e1e1e1;
  background-color: #fff;
  border-radius: 2px;
  margin: 10px;
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
#myEchart{
    height: 450px;
  }
.dataList {
  border-top: 1px dotted grey;
  line-height: 30px;
}
@media screen and (max-width: 1050px) {
  #myEchart{
    height: 300px;
  }
}
</style>

