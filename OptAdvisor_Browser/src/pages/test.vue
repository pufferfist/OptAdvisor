<template>
  <div id="myChart" :style="{width: '300px', height: '300px'}"></div>
</template>

<script>
    export default {
        name: "test",
      data () {

      },
      mounted(){
        this.drawLine();
      },
      methods: {
        drawLine(){
          // 基于准备好的dom，初始化echarts实例
          let myChart = this.$echarts.init(document.getElementById('myChart'))
          // 绘制图表
          myChart.setOption({
            legend: {
              data:['最高气温','最低气温','ppp']
            },

            xAxis:  {
              type: 'category',
              boundaryGap: false,
              data: ['周一','周二','周三','周四','周五','周六','周日']
            },
            yAxis: {
              type: 'value',
              axisLabel: {
                formatter: '{value} °C'
              }
            },
            series: [
              {
                name:'最高气温',
                type:'line',
                data:[11, 11, 15, 13, 12, 13, 10],
                markLine: {
                  data: [
                    {type: 'average', name: '平均值'}
                  ]
                }
              },
              {
                name:'最低气温',
                type:'line',
                data:[1, -2, 2, 5, 3, 2, 0],
                markLine: {
                  data: [
                    {type: 'average', name: '平均值'},
                  ]
                }
              },
              {
                name:'ppp',
                type:'line',
                data:[9, -12, 12, 15, 31, 20,10],
                markLine: {
                  data: [
                    {type: 'average', name: '平均值'},
                  ]
                }
              }
            ]
          });
        }
      }

    }
</script>

<style scoped>

</style>
