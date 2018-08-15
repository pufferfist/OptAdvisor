<template>
    <div>
      <div>
        <div>
          <h1 style="text-align: left">{{name}}
            <span style="font-size: 15px">
          创建时间：{{time}} &nbsp&nbsp 分组类型：{{type}} &nbsp&nbsp 期望收益：{{earnings}} &nbsp&nbsp&nbsp
        </span>
            <Button type="primary">添加跟踪</Button>
          </h1>
        </div>
      </div>
      <Table stripe :columns="columns1" :data="data1"></Table>
      <div>
        <div style="width: 25%;float: left;padding: 30px">
          <div style="width: 200px">
            <h3>期权及相关信息</h3>
            <ul style="text-align: left">
              <li>买入\卖出：{{text1}}</li>
              <li>看涨\看跌：{{text2}}</li>
              <li>到期时间T：{{text3}}</li>
              <li>执行价格：{{text4}}</li>
              <li>成交价格：{{text5}}</li>
              <li>比例：{{text6}}</li>
              <li>delta:{{text7}}</li>
              <li>gamma:{{text8}}</li>
              <li>theta:{{text9}}</li>
              <li>vega:{{text10}}</li>
              <li>rho:{{text11}}</li>
              <li>成本：{{text12}}</li>
              <li>保证金：{{text13}}</li>
            </ul>
          </div>

        </div>
        <div style="width: 75%;float: left;padding: 30px;text-align: center">
          <h3>组合表现展示</h3>
          <span>组合的期望收益率E/M：XXX &nbsp&nbsp 组合的风险值β：123</span>
          <div id="myChart" style="width: 100%;height: 325px"></div>
        </div>
      </div>
    </div>
</template>

<script>
    export default {
        name: "Option",
      data () {
        return {
          name:'我的组合',
          time:'2018-09-09',
          type:'diy',
          earnings:'5230',
          text1:'',
          text2:'',
          text3:'',
          text4:'',
          text5:'',
          text6:'',
          text7:'',
          text8:'',
          text9:'',
          text10:'',
          text11:'',
          text12:'',
          text13:'',


          columns1: [
            {
              title: '合约代码',
              key: 'no'
            },
            {
              title: '合约名称',
              key: 'name'
            },
            {
              title: '成本价',
              key: 'cost'
            },
            {
              title: '最新价',
              key: 'latest_price'
            },
            {
              title: '成本',
              key: 'cost_price'
            },
            {
              title: '保证金',
              key: 'cash_deposit'
            },
            {
              title: '市值',
              key: 'portfolio_value'
            },
            {
              title: '盈亏',
              key: 'profit_and_loss'
            }
          ],
          data1: [
            {
              name: 'John Brown',
              age: 18,
              address: 'New York No. 1 Lake Park',
              date: '2016-10-03'
            },
            {
              name: 'Jim Green',
              age: 24,
              address: 'London No. 1 Lake Park',
              date: '2016-10-01'
            },
            {
              name: 'Joe Black',
              age: 30,
              address: 'Sydney No. 1 Lake Park',
              date: '2016-10-02'
            },
            {
              name: 'Jon Snow',
              age: 26,
              address: 'Ottawa No. 2 Lake Park',
              date: '2016-10-04'
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
