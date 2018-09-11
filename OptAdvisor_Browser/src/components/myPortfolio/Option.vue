<template>
    <div>
      <div>
        <h1>{{name}}
          <span style="font-size: 15px">
          到期时间：{{time}} &nbsp&nbsp 分组类型：{{type}} &nbsp&nbsp 期望收益：{{earnings}} &nbsp&nbsp&nbsp
        </span>
          <Button type="primary" v-bind:style="{display:show1}" @click="track">添加跟踪</Button>
          <Button type="primary" disabled v-bind:style="{display:show2}">已添加跟踪</Button>
        </h1>
      </div>

      <div style="width: 100%;text-align: center">
        <table style="margin: auto" class="table1">
          <tr>
            <th>序号</th>
            <th>合约代码</th>
            <th>合约名称</th>
            <th>成本价</th>
            <th>最新价</th>
          </tr>
          <tr v-for="(item,index) in tdata" @click="choose(index)" :id="getName(index)">
            <td>{{index+1}}</td>
            <td v-for="i in item">{{i}}</td>
          </tr>
        </table>
        <br>
        <table style="margin: auto" class="table2">
          <tr>
            <th>成本</th>
            <td>{{this.tableData.cost}}</td>
            <th>保证金</th>
            <td>{{this.tableData.bond}}</td>
            <th>组合风险值</th>
            <td>{{this.tableData.beta}}</td>
            <th>资产收益率</th>
            <td>{{this.tableData.returnOnAssets}}</td>
          </tr>
        </table>
      </div>

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
          show2:''
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
          var graph=optionData.data.graph
          console.log(graph)
          optionData=optionData.data.portfolios[0]
          this.options=optionData.options
          //1.初始化数据
          this.name=optionData.name
          this.time=optionData.options[0].expireTime
          if(optionData.type=='0'){
            this.type='资产配置'
          }
          else if(optionData.type=='1'){
            this.type='套期保值'
          }
          else{
            this.type='DIY'
          }
          this.earnings=optionData.em.toFixed(4)
          this.tdata=[]
          for(var i=0;i<optionData.options.length;i++){
            var temp=[]
            temp.push(optionData.options[i].tradeCode)
            temp.push(optionData.options[i].name)
            temp.push(optionData.options[i].transactionPrice)
            this.codes.push(optionData.options[i].optionCode)
            this.tdata.push(temp)
          }
          this.getLatestPrice()
          this.tableData.cost=optionData.cost
          this.tableData.bond=optionData.bond
          this.tableData.beta=optionData.beta.toFixed(4)
          this.tableData.returnOnAssets=optionData.returnOnAssets.toFixed(4)
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
          this.$refs.result.text15=optionData.em.toFixed(4)
          this.$refs.result.text16=optionData.beta.toFixed(4)
          if(optionData.type=='0'){
            this.$refs.result.lineName=['回测收益']
            this.$refs.result.line1=graph[0]
            this.$refs.result.line2=graph[1]
          }
          else if(optionData.type=='1'){
            this.$refs.result.lineName=['不持有的损失','持有的损失']
            this.$refs.result.line1=graph[0]
            this.$refs.result.line2=graph[1]
            this.$refs.result.line3=graph[2]
          }
          else if(optionData.type=='2'){
            this.$refs.result.lineName=['组合收益','资产收益']
            this.$refs.result.line1=graph[0]
            this.$refs.result.line2=graph[1]
            this.$refs.result.line3=graph[2]
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
    background-color: #ffeeee;
  }
  .table1 th{
    padding-left: 5px;
    padding-right: 5px;
    background-color: #f16643;
    -webkit-text-fill-color: #ffffff;
  }
  .table2{
    border-collapse: collapse;
  }
  .table2 td{
    padding-left: 5px;
    padding-right: 5px;
    -webkit-text-fill-color: #2baee9;
  }
  .table2 th{
    padding-left: 5px;
    padding-right: 5px;
  }
  #tr0{
    border: 1px solid #2db7f5;
  }
</style>
