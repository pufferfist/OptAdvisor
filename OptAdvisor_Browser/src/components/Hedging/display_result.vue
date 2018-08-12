<template>
    <div>
      <p style="font-weight: bold;font-size: 25px">套期保值效果展示</p>
      <div style="text-align: left">
        <div>
          <p style="font-size: 20px;font-weight: bold">展示期权：</p>
          <Table border :columns="columns" :data="data"></Table>
        </div>
        <br>
        <div>
          <p style="font-size: 20px;font-weight: bold">到达预期最大亏损：</p>
        </div>
        <br>
        <div>
          <p style="font-size: 20px;font-weight: bold">组合表现：</p>
          <div id="myChart" style="width: 80%;height: 300px"></div>
        </div>
      </div>
    </div>
</template>

<script>
    export default {
        name: "display_result",
      data () {
        return {
          columns: [
            {
              title: 'ID',
              key: 'id'
            },
            {
              title: '名称',
              key: 'name'
            },
            {
              title: '买入\卖出',
              key: 'type'
            },
            {
              title: '看涨\看跌',
              key: 'property'
            },
            {
              title: '到期时间',
              key: 'expireTime'
            },
            {
              title: '执行价格',
              key: 'executionPrice'
            },
            {
              title: '成交价格',
              key: 'transactionPrice'
            },
            {
              title: '比例',
              key: 'quantity'
            },
            {
              title: 'delta',
              key: 'delta'
            },
            {
              title: 'gamma',
              key: 'gamma'
            },
            {
              title: 'theta',
              key: 'theta'
            },
            {
              title: 'vega',
              key: 'vega'
            },
            {
              title: 'rho',
              key: 'rho'
            }
          ],
          data: [
            {
              id:'',
              name:'',
              type:'',
              property:'',
              expireTime:'',
              executionPrice:'',
              transactionPrice:'',
              quantity:'',
              delta:'',
              gamma:'',
              theta:'',
              vega:'',
              rho:''
            }
          ]
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
