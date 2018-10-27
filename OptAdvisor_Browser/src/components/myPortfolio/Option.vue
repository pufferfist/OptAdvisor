<template>
    <div>
      <div>
        <h1>{{name}}
          <span style="font-size: 15px">
          到期时间：{{time}} &nbsp&nbsp 分组类型：{{type}} &nbsp&nbsp {{earnings}} &nbsp&nbsp&nbsp
        </span>
          <Button type="primary" v-bind:style="{display:show1}" @click="track">添加跟踪</Button>
          <Button type="primary" disabled v-bind:style="{display:show2}">已添加跟踪</Button>
        </h1>
      </div>

      <h3 style="text-align: left;-webkit-text-fill-color: #2b85e4; border-bottom: 2px solid #2b85e4;margin-left: 3%;margin-right: 3%">组合信息</h3>
      <br>
      <div v-bind:style="{display:showAllocation}" style="margin-bottom: 20px;width: 94%;margin-left:3%;background-color: #f8f8f9;height: 180px;font-size: 15px;-webkit-text-fill-color: #515a6e;font-weight: bold">
        <div style="width: 47%;float: left">
          <p style="padding: 10px">波动率：</p>
          <p style="padding: 10px">价格：</p>
          <p style="padding: 10px">本金：</p>
          <p style="padding: 10px">允许最大损失：</p>
        </div>
        <div style="width: 47%;float: left">
          <p style="padding: 10px">价格有效时间：</p>
          <p style="padding: 10px">预测价格范围：</p>
          <p style="padding: 10px">预测波动率范围：</p>
        </div>
      </div>
      <div v-bind:style="{display:showHedging}" style="margin-bottom: 20px;width: 94%;margin-left:3%;background-color: #f8f8f9;height: 80px;font-size: 15px;-webkit-text-fill-color: #515a6e;font-weight: bold">
        <div style="width: 47%;float: left">
          <p style="padding: 10px">持仓量：</p>
          <p style="padding: 10px">套保期限：</p>
        </div>
        <div style="width: 47%;float: left">
          <p style="padding: 10px">套保比例：</p>
          <p style="padding: 10px">预测最低值：</p>
        </div>
      </div>

      <div style="width: 94%;text-align: center;background-color: #f8f8f9;margin-left: 3%;padding-top: 15px">
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
        <div v-bind:style="{display:showHedging}" style="height: 35px">
          <div style="float: left;width: 50%;font-size: 15px;font-weight: bold;-webkit-text-fill-color: #515a6e">成本：{{this.tableData.cost}}</div>
          <div style="float: left;width: 50%;font-size: 15px;font-weight: bold;-webkit-text-fill-color: #515a6e">保证金：{{this.tableData.bond}}</div>
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
          type:'',
          earnings:'',
          tdata:[],
          codes:[],
          lastSelectedLineIndex:0,
          tableData:{
            cost:'',
            bond:'',
            beta:'',
            returnOnAssets:''
          },
          options:'',
          show1:'',
          show2:'',
          showAllocation:'none',
          showHedging:'none',
          showDIY:'none',
          fluctuation:'',
          allocationFee:'',
          allocationPrinciple:'',
          largestLossAllowed:'',
          validTimeInterval:'',
          predictFeeInterval:'',
          predictFluctuationInterval:''
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
          this.options=optionData.options
          //1.初始化数据
          this.name=optionData.name
          this.time=optionData.options[0].expireTime
          if(optionData.type=='0'){
            this.type='资产配置'
            this.showAllocation=''
            this.showHedging='none'
            this.showDIY='none'
            this.earnings='期望收益：'+(optionData.em*100).toFixed(2)+'%';
          }
          else if(optionData.type=='1'){
            this.type='套期保值'
            this.showHedging=''
            this.showAllocation='none'
            this.showDIY='none'
            this.earnings='到达预期最大减损比率：'+(optionData.iK).toFixed(2)
          }
          else{
            this.type='DIY'
            this.showDIY='';
            this.showAllocation='none'
            this.showHedging='none'
            this.earnings='期望收益：'+(optionData.em*100).toFixed(2)+'%';
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
            this.$refs.result.lineName=['收益']
            this.$refs.result.line1=this.$refs.result.graph1[0]
            this.$refs.result.line2=this.$refs.result.graph1[1]
            this.$refs.result.totalPage=30
            this.$refs.result.graphTitle='不同标的价格下组合收益'
          }
          else if(optionData.type=='1'){
            this.$refs.result.type=1
            this.$refs.result.graph1=originData.graph
            this.$refs.result.lineName=['不持有的损失','持有的损失']
            this.$refs.result.line1=this.$refs.result.graph1[0]
            this.$refs.result.line2=this.$refs.result.graph1[1]
            this.$refs.result.line3=this.$refs.result.graph1[2]
            this.$refs.result.totalPage=10
            this.$refs.result.graphTitle=''
          }
          else if(optionData.type=='2'){
            this.$refs.result.type=2
            this.$refs.result.graph1=originData.assertPrice2Profit
            this.$refs.result.graph2=originData.historyProfit2Probability
            this.$refs.result.lineName=['收益']
            this.$refs.result.line1=this.$refs.result.graph1[0]
            this.$refs.result.line2=this.$refs.result.graph1[1]
            this.$refs.result.totalPage=20
            this.$refs.result.graphTitle='不同标的价格下组合收益'
          }
          this.$refs.result.drawLine()

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
