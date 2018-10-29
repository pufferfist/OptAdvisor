<template>
    <div>
      <div>
        <p style="text-align: left;font-size: 40px;font-weight: bold">&nbsp&nbsp&nbsp{{name}}</p>
        <span style="font-size: 15px">
          创建时间：{{buildTime}} &nbsp&nbsp 到期时间：{{time}} &nbsp&nbsp {{earnings}} &nbsp&nbsp&nbsp
        </span>
        <Button type="primary" v-bind:style="{display:show1}" @click="track">添加跟踪</Button>
        <Button type="primary" disabled v-bind:style="{display:show2}">已添加跟踪</Button>
      </div>

      <h3 style="text-align: left;-webkit-text-fill-color: #2b85e4; border-bottom: 2px solid #2b85e4;margin-left: 3%;margin-right: 3%">组合信息</h3>
      <br>
      <div v-bind:style="{display:showAllocation}" style="margin-bottom: 20px;width: 94%;margin-left:3%;border: 2px solid #f8f8f9;height: 85px;font-size: 15px;-webkit-text-fill-color: #515a6e;font-weight: bold">
        <div style="width: 47%;float: left">
          <p style="padding: 10px">本金：{{allocationPrinciple}}</p>
          <p style="padding: 10px">允许最大损失：{{allocationLargestLossAllowed}}</p>
        </div>
        <div style="width: 47%;float: left">
          <p style="padding: 10px">预测价格范围：{{allocationPredictFeeInterval}}</p>
          <p style="padding: 10px">预测波动率范围：{{allocationPredictFluctuationInterval}}</p>
        </div>
      </div>
      <div v-bind:style="{display:showHedging}" style="margin-bottom: 20px;width: 94%;margin-left:3%;border: 2px solid #f8f8f9;height: 85px;font-size: 15px;-webkit-text-fill-color: #515a6e;font-weight: bold">
        <div style="width: 47%;float: left">
          <p style="padding: 10px">持仓量：{{hedgingOpenInterest}}</p>
          <p style="padding: 10px">套保期限：{{hedgingDeadline}}</p>
        </div>
        <div style="width: 47%;float: left">
          <p style="padding: 10px">套保比例：{{hedgingRate}}</p>
          <p style="padding: 10px">预测价格最低值：{{hedgingPredictLowestPrice}}</p>
        </div>
      </div>

      <div style="width: 94%;text-align: center;margin-left: 3%;padding-top: 15px;border: 2px solid #f8f8f9">
        <table style="margin: auto" class="table1">
          <tr>
            <th>序号</th>
            <th>合约名称</th>
            <th>成本价</th>
            <th>买/卖</th>
            <th>数量(手)</th>
            <th>最新价</th>
          </tr>
          <tr v-for="(item,index) in tdata" @click="choose(index)" :id="getName(index)">
            <td>{{index+1}}</td>
            <td v-for="i in item">{{i}}</td>
          </tr>
        </table>
        <br>
        <div>
          <p></p>
        </div>

        <div v-bind:style="{display:showAllocation}" style="height: 35px">
          <div style="float: left;width: 25%;font-size: 15px;font-weight: bold;-webkit-text-fill-color: #515a6e">成本：{{this.tableData.cost}}</div>
          <div style="float: left;width: 25%;font-size: 15px;font-weight: bold;-webkit-text-fill-color: #515a6e">保证金：{{this.tableData.bond}}</div>
          <div style="float: left;width: 25%;font-size: 15px;font-weight: bold;-webkit-text-fill-color: #515a6e">组合杠杆：{{this.tableData.beta}}</div>
          <div style="float: left;width: 25%;font-size: 15px;font-weight: bold;-webkit-text-fill-color: #515a6e">资产收益率：{{this.tableData.returnOnAssets}}</div>
        </div>
        <div v-bind:style="{display:showDIY}" style="height: 35px">
          <div style="float: left;width: 33%;font-size: 15px;font-weight: bold;-webkit-text-fill-color: #515a6e">成本：{{this.tableData.cost}}</div>
          <div style="float: left;width: 33%;font-size: 15px;font-weight: bold;-webkit-text-fill-color: #515a6e">保证金：{{this.tableData.bond}}</div>
          <div style="float: left;width: 33%;font-size: 15px;font-weight: bold;-webkit-text-fill-color: #515a6e">组合杠杆：{{this.tableData.beta}}</div>
        </div>
      </div>
      <br>

      <display_result ref="result"></display_result>

    </div>
</template>

<script>
  import display_result from "../myPortfolio/displayResult";
    export default {
        name: "Option",
      components:{display_result},
      data () {
        return {
          id:'',
          name:'',
          time:'',
          buildTime:'',
          earnings:'',
          tdata:[],
          codes:[],
          lastSelectedLineIndex:0,
          tableData:{
            cost:'',
            bond:'',
            beta:'',
            returnOnAssets:'',
            assertLeverage:''
          },
          options:'',
          show1:'',
          show2:'',
          showAllocation:'none',
          showHedging:'none',
          showDIY:'none',
          allocationPrinciple:'',
          allocationLargestLossAllowed:'',
          allocationPredictFeeInterval:'',
          allocationPredictFluctuationInterval:'',
          hedgingOpenInterest:'',
          hedgingRate:'',
          hedgingDeadline:'',
          hedgingPredictLowestPrice:''
        }
      },
      methods:{
        getName(index){
          return 'tr'+index
        },
        choose(index){
          //1.恢复线条
          document.getElementById("tr"+this.lastSelectedLineIndex).style.border="none"
          //2.勾画线条
          document.getElementById("tr"+index).style.border="1px solid #2db7f5"
          //3.赋值
          this.lastSelectedLineIndex=index
          //4.初始化displayResult
          if(parseInt(this.options[index].cp)>0){
            this.$refs.result.getSingleInfo('up',this.options[index].optionCode.substr(7))
          }
          else{
            this.$refs.result.getSingleInfo('down',this.options[index].optionCode.substr(7))
          }

        },
        getLatestPrice(){
          var suffix=''
          for(var i=0;i<this.codes.length;i++){
            suffix=suffix+this.codes[i]+","
          }
          this.axios.get('/sinaOption/list='+suffix)
            .then(re=>{
              var parts1=re.data.split("var")
              for(var i=1;i<parts1.length;i++){
                var tempparts=parts1[i].split(",")
                this.tdata[i-1].push(tempparts[2])
              }
            })
        },
        initial(optionData){
          var originData=optionData.data
          optionData=optionData.data.portfolios[0]
          console.log(optionData)
          this.options=optionData.options
          var date=new Date(optionData.buildTime)
          this.buildTime=optionData.buildTime.substr(0,10)
          if(date.getHours()<10){
            this.buildTime=this.buildTime+" 0"+date.getHours()
          }
          else{
            this.buildTime=this.buildTime+" "+date.getHours()
          }
          if(date.getMinutes()<10){
            this.buildTime=this.buildTime+":0"+date.getMinutes()
          }
          else{
            this.buildTime=this.buildTime+":"+date.getMinutes()
          }
          if(date.getSeconds()<10){
            this.buildTime=this.buildTime+":0"+date.getSeconds()
          }
          else{
            this.buildTime=this.buildTime+":"+date.getSeconds()
          }
          //1.初始化数据
          this.name=optionData.name
          this.time=optionData.options[0].expireTime

          //资产配置
          this.allocationPrinciple=optionData.m0
          this.allocationLargestLossAllowed=(optionData.k*100).toFixed(2)+"%"
          this.allocationPredictFeeInterval=optionData.p1.toFixed(3)+" ～ "+optionData.p2.toFixed(3)
          this.allocationPredictFluctuationInterval=optionData.sigma1.toFixed(4)+" ～ "+optionData.sigma2.toFixed(4)
          //套期保值
          this.hedgingOpenInterest=optionData.n0
          this.hedgingRate=optionData.a
          this.hedgingDeadline=this.time
          this.hedgingPredictLowestPrice=optionData.sExp
          //DIY


          if(optionData.type=='0'){
            // this.type='资产配置'
            this.showAllocation=''
            this.showHedging='none'
            this.showDIY='none'
            this.earnings='期望收益：'+(optionData.em*100).toFixed(2)+'%';
          }
          else if(optionData.type=='1'){
            // this.type='套期保值'
            this.showHedging=''
            this.showAllocation='none'
            this.showDIY='none'
            this.earnings='到达预期最大减损比率：'+(optionData.iK).toFixed(2)
          }
          else{
            // this.type='DIY'
            this.showDIY='';
            this.showAllocation='none'
            this.showHedging='none'
            //this.earnings='期望收益：'+(optionData.em*100).toFixed(2)+'%';
          }
          this.tdata=[]
          for(var i=0;i<optionData.options.length;i++){
            var temp=[]
            //temp.push(optionData.options[i].tradeCode)
            temp.push(optionData.options[i].name)
            temp.push(optionData.options[i].transactionPrice)
            if(parseInt(optionData.options[i].type)>0){
              temp.push("买")
              temp.push(Math.abs(optionData.options[i].type))
            }
            else{
              temp.push("卖")
              temp.push(Math.abs(optionData.options[i].type))
            }
            this.codes.push(optionData.options[i].optionCode)
            this.tdata.push(temp)
          }
          this.getLatestPrice()
          this.tableData.cost=optionData.cost.toFixed(2)
          this.tableData.bond=optionData.bond.toFixed(2)
          this.tableData.beta=optionData.beta.toFixed(2)
          this.tableData.assertLeverage=optionData.assertLeverage
          this.tableData.returnOnAssets=(optionData.returnOnAssets*100).toFixed(2)+'%'
          this.id=optionData.id
          if(optionData.trackingStatus==false){
            this.show1=''
            this.show2='none'
          }
          else if (optionData.trackingStatus==true) {
            this.show1='none'
            this.show2=''
          }

          //2.同时画echarts
          this.$refs.result.text15=(optionData.em*100).toFixed(2)+'%'
          this.$refs.result.text16=optionData.beta.toFixed(2)
          if(optionData.type=='0'){
            this.$refs.result.type=0
            this.$refs.result.graph1=originData.assertPrice2Profit
            this.$refs.result.graph2=originData.profit2Probability
            this.$refs.result.graph3=originData.historyProfit2Probability
            this.$refs.result.drawGraph('allocation');
          }
          else if(optionData.type=='1'){
            this.$refs.result.type=1
            this.$refs.result.graph1=originData.graph
            this.$refs.result.drawGraph('hedging');
          }
          else if(optionData.type=='2'){
            this.$refs.result.type=2
            this.$refs.result.graph1=originData.assertPrice2Profit
            this.$refs.result.graph2=originData.historyProfit2Probability
            this.$refs.result.drawGraph('diy');
          }

          //3.调用choose方法默认选中第一项
          if(parseInt(this.options[0].cp)>0){
            this.$refs.result.getSingleInfo('up',this.options[0].optionCode.substr(7))
          }
          else{
            this.$refs.result.getSingleInfo('down',this.options[0].optionCode.substr(7))
          }

        },
        track(){
          this.axios.patch('/backend/portfolio/'+this.id+'/track')
            .then(re=>{
              if(re.data.msg=='Update portfolio risk tracking success'){
                this.$Message.success("追踪成功")
                this.show1=false
                this.show2=true
              }
              else{
                this.$Message.error("追踪失败，请稍后重试")
              }
            })
        },
        fixArray(array){
          // var result=[];
          // // for(var i=0;i<array.length;i++){
          // //   var temp=[];
          // //   for(var j=0;j<array[i].length;j++){
          // //     console.log(i+"  "+j+"  "+parseFloat(array[i][j]).toFixed(2))
          // //     temp.push(parseFloat(array[i][j]).toFixed(2))
          // //   }
          // // }
          // for(var j=0;j<array[num].length;j++){
          //   console.log(num+"  "+j+"  "+parseFloat(array[num][j]).toFixed(2))
          //   result.push(parseFloat(array[num][j]).toFixed(2))
          // }
          // return result;
          return array
        }
      }

    }
</script>

<style scoped>
  .table1{
    border-collapse: collapse;
  }
  .table1 td{
    padding-left: 5px;
    padding-right: 5px;
    background-color: #ffffff;
  }
  .table1 th{
    padding-left: 5px;
    padding-right: 5px;
    background-color: #ffffff;
    -webkit-text-fill-color: #515a6e;
  }

  #tr0{
    border: 1px solid #2db7f5;
  }
</style>
