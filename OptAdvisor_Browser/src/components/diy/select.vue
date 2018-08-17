<template>
    <div>
      <div style="text-align: left;padding-bottom: 5px">
        <p>
          上交所 &nbsp;&nbsp;
          50ETF  &nbsp;&nbsp;
          <Select v-model="selectMonth" style="width: 100px" @on-change="changeMonth">
            <Option v-for="item in deadlineMonths" :value="item" :key="item">{{ item }}</Option>
          </Select>
        </p>
      </div>
      <div style="width: 100%;text-align: center">
        <table>
          <tr>
            <th colspan="9" style="background-color: #f16643">看涨合约</th>
            <th style="background-color: #f2f2f2"></th>
            <th colspan="9" style="background-color: #19be6b">看跌合约</th>
          </tr>
          <tr>
            <td></td>
            <td>买量</td>
            <td>买价</td>
            <td>最新价</td>
            <td>卖价</td>
            <td>卖量</td>
            <td>持仓量</td>
            <td>振幅</td>
            <td>涨跌幅</td>
            <td>行权价</td>
            <td>买量</td>
            <td>买价</td>
            <td>最新价</td>
            <td>卖价</td>
            <td>卖量</td>
            <td>持仓量</td>
            <td>振幅</td>
            <td>涨跌幅</td>
            <td></td>
          </tr>
          <tr v-for="(item,index) in value">
            <td :class="getClassName(index,'up')" :id="getClassName(index,'checkbox_up_')" @click="refreshId"><Checkbox @on-change="changeColor"></Checkbox></td>
            <td v-for="up in item[0]" :class="getClassName(index,'up')">{{up}}</td>
            <td v-for="upRate in item[1]" :class="getClassName(index,'up')" v-if="upRate>0" style="-webkit-text-fill-color: #f16643">{{upRate}}%</td>
            <td v-for="upRate in item[1]" :class="getClassName(index,'up')" v-if="upRate<0" style="-webkit-text-fill-color: #19be6b">{{upRate}}%</td>
            <td v-for="upRate in item[1]" :class="getClassName(index,'up')" v-if="upRate==0">{{upRate}}%</td>
            <td v-for="middle in item[2]">{{middle}}</td>
            <td v-for="down in item[3]" :class="getClassName(index,'down')">{{down}}</td>
            <td v-for="downRate in item[4]" :class="getClassName(index,'down')" v-if="downRate>0" style="-webkit-text-fill-color: #f16643">{{downRate}}%</td>
            <td v-for="downRate in item[4]" :class="getClassName(index,'down')" v-if="downRate<0" style="-webkit-text-fill-color: #19be6b">{{downRate}}%</td>
            <td v-for="downRate in item[4]" :class="getClassName(index,'down')" v-if="downRate==0">{{downRate}}%</td>
            <td style="padding-left: 20px" :class="getClassName(index,'down')" :id="getClassName(index,'checkbox_down_')" @click="refreshId"><Checkbox @on-change="changeColor"></Checkbox></td>
          </tr>
          <tr>
            <th colspan="19" style="background-color: #2db7f5">我的组合（请在上表中勾选自己的DIY项目）</th>
          </tr>
          <tr>
            <td></td>
            <td colspan="8" style="vertical-align: top">
              <li v-for="l in leftValue">{{l}}</li>
            </td>
            <td></td>
            <td colspan="8" style="vertical-align: top">
              <li v-for="r in rightValue">{{r}}</li>
            </td>
            <td></td>
          </tr>
          <tr>
            <td colspan="19">&nbsp;</td>
          </tr>
          <tr>
            <td colspan="19"><Button type="primary" style="width: 250px" @click="confirm">确认</Button></td>
          </tr>
          <tr>
            <td colspan="19">&nbsp;</td>
          </tr>
        </table>
      </div>
    </div>
</template>

<script>
    export default {
        name: "select",
      data () {
        return {
          deadlineMonths: [],
          upValue:[],
          downValue:[],
          value:[],
          leftValue:[],
          rightValue:[],
          currentSelectedId:'',
          selectMonth:''
        }
      },
      mounted() {
        this.axios.get('/sinaTime/StockOptionService.getStockName')
          .then(re=>{
            var months=re.data.result.data.contractMonth
            this.setSelectedMonth(months);
            this.getValue(months[1])
          })
        setInterval(this.circle, 5000);

      },
      methods: {
        setSelectedMonth(months){
          this.selectMonth=months[1]
          var result=[]
          for(var i=1;i<months.length;i++){
            result.push(months[i])
          }
          this.deadlineMonths=result;
        },
        getValue(month){
          this.upValue=[]
          this.downValue=[]
          var date=month.substr(2,2)+month.substr(5)
          var address='/sinaOption/list=OP_UP_510050'+date+","+"OP_DOWN_510050"+date
          this.axios.get(address)
            .then(re=>{
              var parts=re.data.split(";")
              parts[0]=parts[0].substr(parts[0].indexOf("=")+2)
              parts[1]=parts[1].substr(parts[1].indexOf("=")+2)
              parts[0]=parts[0].substr(0,parts[0].length-1)
              parts[1]=parts[1].substr(0,parts[1].length-1)
              var subAddress='sinaOption/list='+parts[0]+parts[1]
              this.axios.get(subAddress)
                .then(re=>{
                  var datas=re.data.split("var")
                  var arrayLength=(datas.length-1)/2
                  var result=[]
                  for(var i=1;i<=arrayLength;i++){
                    var temp1=datas[i].substr(datas[i].indexOf("=")+2).split(",")
                    this.upValue.push(temp1)
                    var single1=[temp1[0],temp1[1],temp1[2],temp1[3],temp1[4],temp1[5],temp1[38]+"%"]
                    var single2=[temp1[6]]
                    var single3=[temp1[7]]

                    var temp2=datas[i+arrayLength].substr(datas[i+arrayLength].indexOf("=")+2).split(",")
                    this.downValue.push(temp2)
                    var single4=[temp2[0],temp2[1],temp2[2],temp2[3],temp2[4],temp2[5],temp2[38]+"%"]
                    var single5=[temp2[6]]
                    result.push([single1,single2,single3,single4,single5])
                  }
                  this.value=result;
                })
            })
        },
        getClassName(index,upOrDown){
          return upOrDown+index
        },
        changeColor(){
          var id=this.currentSelectedId
          var parts=id.split("_")
          var classname=this.getClassName(parts[2],parts[1])
          var doms=document.getElementsByClassName(classname)
          //取消点击
          if(doms[0].style.backgroundColor=='rgb(255, 238, 238)'){
            for(var i=0;i<doms.length;i++){
              doms[i].style.backgroundColor="#f8f8f9"
            }
          }
          //点击
          else{
            for(var i=0;i<doms.length;i++){
              doms[i].style.backgroundColor="#FFEEEE"
            }
          }
          this.setLeftOrRightValue()
        },
        refreshId(){
          this.currentSelectedId=event.currentTarget.id
        },
        setLeftOrRightValue(){
          this.leftValue=[]
          this.rightValue=[]
          for(var i=0;i<this.value.length;i++){
            var colorl=document.getElementById("checkbox_up_"+i).style.backgroundColor
            if(colorl=='rgb(255, 238, 238)'){
              this.leftValue.push(this.upValue[i][37])
            }
            var colorr=document.getElementById("checkbox_down_"+i).style.backgroundColor
            if(colorr=='rgb(255, 238, 238)'){
              this.rightValue.push(this.downValue[i][37])
            }
          }
        },
        circle(){
          if(this.deadlineMonths.length==0){
            //do nothing
          }
          else{
            this.getValue(this.selectMonth)
          }
        },
        changeMonth(){
          this.value=[]
          this.getValue(this.selectMonth)
          this.setLeftOrRightValue()
        },
        confirm(){

        }
      }
    }
</script>

<style scoped>
table{
  border-collapse: collapse;
}
th{
  font-size: 13px;
  /*border: 1px solid black;*/
  padding-left: 5px;
  padding-right: 5px;
}
td{
  font-size: 13px;
  /*border: 1px solid black;*/
  padding-left: 5px;
  padding-right: 5px;
  background-color: #f8f8f9;
}

  li{
    padding: 5px;
  }

</style>
