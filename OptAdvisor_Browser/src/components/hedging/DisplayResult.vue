<template>
    <div>
      <p style="font-weight: bold;font-size: 25px">套期保值效果展示</p>
      <div style="text-align: left">
        <div>
          <p style="font-size: 20px;font-weight: bold;padding-left: 50px">展示期权：</p>
          <optionGroup ref="option_group"></optionGroup>
        </div>
        <br>
        <div>
          <span style="font-size: 20px;font-weight: bold;padding-left: 50px">到达预期最大亏损：<p style="-webkit-text-fill-color: red;display: inline">{{expectedLoss}}</p></span>
        </div>
        <br>
        <div>
          <p style="font-size: 20px;font-weight: bold;padding-left: 50px">组合表现：</p>
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
          expectedLoss:'',
          graph:'',
          lineName:['data1','data2','data3']
        }
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
            legend: {
              data:this.lineName
            },
            xAxis: {
              type: 'category',
              data: this.graph[0]
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              name:this.lineName[0],
              data: this.graph[1],
              type: 'line'
            },
              {
                name:this.lineName[1],
                data: this.graph[2],
                type: 'line'
              },
              {
                name:this.lineName[2],
                data: this.graph[3],
                type: 'line'
              }]
          });
        }
      }

    }
</script>

<style scoped>

</style>
