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
        <div id="myChart1" style="width: 680px;height: 300px;margin: auto">
        </div>
      </div>
      <br>
      <div v-bind:style="{display:show2}" style="width: 100%;text-align: center;border: 2px solid #f8f8f9">
        <div id="myChart2" style="width: 680px;height: 300px;margin: auto">
        </div>
      </div>
      <br>
      <div v-bind:style="{display:show3}" style="width: 100%;text-align: center;border: 2px solid #f8f8f9">
        <div id="myChart3" style="width: 680px;height: 300px;margin: auto">
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
            this.drawLine('myChart1','不同标的价格下组合收益',['收益'],this.graph1,'标的价格/元','收益率(%)','','%')
            this.drawBar('myChart2','组合收益在预期市场内的概率分布',['概率'],this.graph2,'收益率(%)','概率','%','')
            this.drawLine('myChart3','组合收益在历史市场内的概率分布',['概率'],this.graph3,'历史收益率(%)','概率','%','')
            this.show1=''
            this.show2=''
            this.show3=''
          }
          else if(type=='hedging'){
            this.drawLine('myChart1','套期保值预期效果展示',['不持有的损失','持有的损失'],this.graph1,'标的价格/元','收益率(%)','','%')
            this.show1=''
            this.show2='none'
            this.show3='none'
          }
          else{
            this.drawLine('myChart1','不同标的价格下组合收益',['收益'],this.graph1,'标的价格/元','收益率(%)','','%')
            this.drawLine('myChart2','组合收益在历史市场内的概率分布',['概率'],this.graph2,'历史收益率(%)','概率','%','')
            this.show1=''
            this.show2=''
            this.show3='none'
          }
        },
        drawLine(graphname,title,lineName,graph,xName,yName,xFormat,yFormat){
          var series=[]
          for(var i=0;i<lineName.length;i++){
            var temp=   {
              name:lineName[i],
              type:'line',
              smooth:true,
              symbol: 'none',
              sampling: 'average',
              data: graph[i+1].map(function (item) {
                if(yFormat=='%'){
                  return (parseFloat(item)*100).toFixed(2);
                }
                else{
                  return (parseFloat(item).toFixed(4))
                }
              }),
            }
            series.push(temp)
          }

          // 基于准备好的dom，初始化echarts实例
          let myChart = this.$echarts.init(document.getElementById(graphname))
          // 绘制图表
          myChart.setOption({
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'cross',
                label: {
                  backgroundColor: '#6a7985'
                }
              },
              formatter: xName+":{b}<br/>"+yName+":{c}",
            },
            title : {
              text: title,
              x: 'center',
              align: 'right'
            },
            legend: {
              data:lineName,
              x: 'left'
            },
            xAxis: {
              name:xName,
              nameTextStyle:{
                fontSize:10,
                padding:0
              },
              nameGap:2,
              type: 'category',
              boundaryGap: false,
              data: graph[0].map(function (item) {
                if(xFormat=='%'){
                  return (parseFloat(item)*100).toFixed(2);
                }
                else{
                  return item
                }
              }),
            },
            yAxis: {
              name:yName,
              type: 'value',
              boundaryGap: [0, '100%']
            },
            series: series
          },true);
        },
        drawBar(graphname,title,lineName,graph,xName,yName){
          // 基于准备好的dom，初始化echarts实例
          let myChart = this.$echarts.init(document.getElementById(graphname))
          // 绘制图表
          myChart.setOption({
            title : {
              text: title,
              x: 'center',
              align: 'right'
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'cross',
                label: {
                  backgroundColor: '#6a7985'
                }
              },
              formatter: xName+":{b}<br/>"+yName+":{c}",
            },
            xAxis: {
              name:xName,
              nameTextStyle:{
                fontSize:10,
                padding:0
              },
              nameGap:2,
              type: 'category',
              data: graph[0]
            },
            yAxis: {
              name:yName,
              type: 'value'
            },
            series: [{
              name: '概率',
              type: 'bar',
              itemStyle:{
                normal:{
                  lineStyle: {
                    width:2
                  },
                }
              },
              data: graph[1],
            }]
          },true);
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
