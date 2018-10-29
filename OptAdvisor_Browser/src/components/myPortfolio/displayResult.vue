<template>
  <div>
    <div style="width: 100%;float: left;padding-left: 3%;padding-right: 3%">
      <div>
        <h3 style="text-align: left;-webkit-text-fill-color: #2b85e4; border-bottom: 2px solid #2b85e4;">期权及相关信息</h3>
        <br>
        <div style="width: 100%;height: 200px;border: 2px solid #f8f8f9;padding-top: 5px;padding-bottom: 5px">
          <div style="width: 50%;float: left;text-align: center">
            <table class="table2" style="margin: auto">
              <tr>
                <th>合约简称</th>
                <td>{{text1}}</td>
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
                <th>隐含波动率</th>
                <td>{{text12}}</td>
              </tr>
            </table>
          </div>
          <div style="width: 50%;float: left;text-align: center">
            <table class="table2" style="margin: auto">
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
        <br>
      </div>
    </div>
    <div style="width: 100%;float: left;padding-left:3%; padding-right:3%;text-align: center">
      <h3 style="text-align: left;-webkit-text-fill-color: #2b85e4; border-bottom: 2px solid #2b85e4;">组合表现展示</h3>
      <br>
      <div v-bind:style="{display:show1}" style="width: 100%;text-align: center;border: 2px solid #f8f8f9">
        <div id="myChart1" style="width: 600px;height: 300px;margin: auto">
        </div>
      </div>
      <br>
      <div v-bind:style="{display:show2}" style="width: 100%;text-align: center;border: 2px solid #f8f8f9">
        <div id="myChart2" style="width: 600px;height: 300px;margin: auto">
        </div>
      </div>
      <br>
      <div v-bind:style="{display:show3}" style="width: 100%;text-align: center;border: 2px solid #f8f8f9">
        <div id="myChart3" style="width: 600px;height: 300px;margin: auto">
        </div>
      </div>
    </div>
    <br>
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
            text15:'',
            text16:'',
            graph1:[],
            graph2:[],
            graph3:[],
            type:'',
            show1:'none',
            show2:'none',
            show3:'none'
          }
      },
      methods: {
        drawGraph(type){
          if(type=='allocation'){
            this.drawLine('myChart1','不同标的价格下组合收益',['收益'],this.graph1)
            this.drawLine('myChart2','组合收益在预期市场内的概率分布',['概率'],this.graph2)
            this.drawLine('myChart3','组合收益在历史市场内的概率分布',['概率'],this.graph3)
            this.show1=''
            this.show2=''
            this.show3=''
          }
          else if(type=='hedging'){
            this.drawLine('myChart1','',['不持有的损失','持有的损失'],this.graph1)
            this.show1=''
            this.show2='none'
            this.show3='none'
          }
          else{
            this.drawLine('myChart1','不同标的价格下组合收益',['收益'],this.graph1)
            this.drawLine('myChart2','组合收益在历史市场内的概率分布',['概率'],this.graph2)
            this.show1=''
            this.show2=''
            this.show3='none'
          }
        },
        drawLine(graphname,title,lineName,graph){
          // 基于准备好的dom，初始化echarts实例
          let myChart = this.$echarts.init(document.getElementById(graphname))
          // 绘制图表
          myChart.setOption({
            title : {
              text: title,
              x: 'center',
              align: 'right'
            },
            legend: {
              data:lineName,
              x: 'left'
            },
            tooltip: {
              trigger: 'axis',
              position: function (pt) {
                return [pt[0], '10%'];
              }
            },
            xAxis: {
              type: 'category',
              boundaryGap: false,
              data: graph[0]
            },
            yAxis: {
              type: 'value',
              boundaryGap: [0, '100%']
            },
            series: [
              {
                name:lineName[0],
                type:'line',
                smooth:true,
                symbol: 'none',
                sampling: 'average',
                itemStyle: {
                  normal: {
                    color: 'rgb(25, 191, 107)'
                  }
                },
                data: graph[1]
              },
              {
                name:lineName[1],
                type:'line',
                smooth:true,
                symbol: 'none',
                sampling: 'average',
                itemStyle: {
                  normal: {
                    color: 'rgb(254, 64, 20)'
                  }
                },
                data: graph[2]
              },
              {
                name:lineName[2],
                type:'line',
                smooth:true,
                symbol: 'none',
                sampling: 'average',
                itemStyle: {
                  normal: {
                    color: 'rgb(255, 153, 0)'
                  }
                },
                data: graph[3]
              },
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
    font-size: 15px;
  }
  .table2 th{
    font-size: 15px;
    text-align: left;
    min-width: 130px;
    -webkit-text-fill-color: #515a6e;
    padding-top: 3px;
    padding-bottom: 3px;
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
