<template>
  <div>
    <div style="width: 40%;float: left;padding-top: 30px;padding-left: 30px">
      <div style="width: 200px">
        <h3>期权及相关信息</h3>
        <table class="table2">
          <tr>
            <th>合约简称</th>
            <td>{{text1}}</td>
          </tr>
          <tr>
            <th>交易代码</th>
            <td>{{text2}}</td>
          </tr>
          <tr>
            <th>理论价值</th>
            <td>{{text3}}</td>
          </tr>
          <tr>
            <th>价值状态</th>
            <td style="-webkit-text-fill-color: red">{{text4}}</td>
          </tr>
          <tr>
            <th>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp内在价值</th>
            <td style="-webkit-text-fill-color: red">{{text5}}</td>
          </tr>
          <tr>
            <th>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp时间价值</th>
            <td style="-webkit-text-fill-color: red">{{text6}}</td>
          </tr>
          <tr>
            <th>成交量</th>
            <td>{{text7}}</td>
          </tr>
          <tr>
            <th>Delta</th>
            <td>{{text8}}</td>
          </tr>
          <tr>
            <th>Gamma</th>
            <td>{{text9}}</td>
          </tr>
          <tr>
            <th>Theta</th>
            <td>{{text10}}</td>
          </tr>
          <tr>
            <th>Vega</th>
            <td>{{text11}}</td>
          </tr>
          <tr>
            <th>隐含波动率</th>
            <td>{{text12}}</td>
          </tr>
          <tr>
            <th>最高价</th>
            <td>{{text13}}</td>
          </tr>
          <tr>
            <th>最低价</th>
            <td>{{text14}}</td>
          </tr>
        </table>
      </div>
    </div>
    <div style="width: 60%;float: left;padding: 30px;text-align: center">
      <h3>组合表现展示</h3>
      <table class="table1" style="margin: auto">
        <tr>
          <th style="-webkit-text-fill-color: #2baee9">创建：</th>
          <th>组合的期望收益率E/M</th>
          <td style="-webkit-text-fill-color: #19be6b">{{text15}}</td>
          <th>组合的风险值β</th>
          <td style="-webkit-text-fill-color: #ed4014">{{text16}}</td>
        </tr>
        <tr>
          <th style="-webkit-text-fill-color: #2baee9">实时：</th>
          <th>组合的期望收益率E/M</th>
          <td style="-webkit-text-fill-color: #19be6b">{{text17}}</td>
          <th>组合的风险值β</th>
          <td style="-webkit-text-fill-color: #ed4014">{{text18}}</td>
        </tr>
      </table>
      <div id="myChart" style="width: 100%;height: 325px">
      </div>
    </div>
  </div>
</template>

<script>
    export default {
        name: "displayResult",
      data(){
          return{
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
            text14:'',
            text15:'aaa',
            text16:'aaa',
            text17:'aaa',
            text18:'aaa'
          }
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
        },
        getSingleInfo(upOrDown,address){
          //address是10004125这种
          if(upOrDown=='up'){
            this.axios.get('/sinaOption/list=CON_SO_'+address)
              .then(re=>{
                var parts=re.data.substr(re.data.indexOf("=")+2).split(",")
                this.text1=parts[0]
                this.text2=parts[12]
                this.text3=parts[15]
                var price_mark=(parts[14]-parts[13]).toFixed(4)
                if(price_mark>0){
                  this.text4='实值'
                  this.text5=price_mark
                }
                else if(price_mark<0){
                  this.text4='虚值'
                  this.text5=0
                }
                else if(price_mark==0){
                  this.text4='平值'
                  this.text5=0
                }
                if(parts[14]-this.text5<=0){
                  this.text6=0.00
                }
                else{
                  this.text6=(parts[14]-this.text5).toFixed(4)
                }
                this.text7=parts[4]
                this.text8=parts[5]
                this.text9=parts[6]
                this.text10=parts[7]
                this.text11=parts[8]
                this.text12=parts[9]
                this.text13=parts[10]
                this.text14=parts[11]
              })
          }
          else if(upOrDown=='down'){
            this.axios.get('/sinaOption/list=CON_SO_'+address)
              .then(re=>{
                var parts=re.data.substr(re.data.indexOf("=")+2).split(",")
                this.text1=parts[0]
                this.text2=parts[12]
                this.text3=parts[15]
                var price_mark=(parts[14]-parts[13]).toFixed(4)
                if(price_mark<0){
                  this.text4='实值'
                  this.text5=0-price_mark
                }
                else if(price_mark>0){
                  this.text4='虚值'
                  this.text5=0
                }
                else if(price_mark==0){
                  this.text4='平值'
                  this.text5=0
                }
                this.text6=(parts[14]-this.text5).toFixed(4)
                this.text7=parts[4]
                this.text8=parts[5]
                this.text9=parts[6]
                this.text10=parts[7]
                this.text11=parts[8]
                this.text12=parts[9]
                this.text13=parts[10]
                this.text14=parts[11]
              })
          }

        },
      }
    }
</script>

<style scoped>
  .table2{
    border-collapse: collapse;
  }
  .table2 td{
    text-align: left;
  }
  .table2 th{
    text-align: left;
    min-width: 130px;
    -webkit-text-fill-color: #808695;
    font-weight: normal;
  }

  .table1{
    border-collapse: collapse;
  }
  .table1 td{
    text-align: left;
    padding-left: 5px;
    padding-right: 5px;
    font-size: 13px;
  }
  .table1 th{
    text-align: left;
    -webkit-text-fill-color: #808695;
    font-weight: normal;
    font-size: 13px;
  }
</style>
