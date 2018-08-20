<template>
    <div>
      <p style="font-weight: bold;font-size: 25px">套期保值效果展示</p>
      <div style="text-align: left">
        <div>
          <p style="font-size: 20px;font-weight: bold">展示期权：</p>
          <optionGroup></optionGroup>
        </div>
        <br>
        <div>
          <p style="font-size: 20px;font-weight: bold">到达预期最大亏损：</p>
        </div>
        <br>
        <div>
          <p style="font-size: 20px;font-weight: bold">组合表现：</p>
          <div id="myChart" style="width: 700px;height: 300px"></div>
        </div>
      </div>
    </div>
</template>

<script>
  import optionGroup from "../hedging/OptionTable"
    export default {
        name: "display_result",
      components:{optionGroup},
      data () {
        return {
          graph:{
            
          }
        }
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
            tooltip: {
              trigger: 'axis'
            },
            toolbox: {
              show: true,
              feature: {
                dataZoom: {
                  yAxisIndex: 'none'
                },
                dataView: {readOnly: false},
                magicType: {type: ['line', 'bar']},
                restore: {},
                saveAsImage: {}
              }
            },
            legend: {
              data:['最高气温','最低气温']
            },
            xAxis: {
              type: 'category',
              data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              name:'最高气温',
              data: [820, 932, 901, 934, 1290, 1330, 1320],
              type: 'line'
            },
              {
                name:'最低气温',
                data: [82, 92, 91, 94, 129, 130, 120],
                type: 'line'
              }]
          });
        }
      }

    }
</script>

<style scoped>

</style>
