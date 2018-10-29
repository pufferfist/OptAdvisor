<template>
    <div>
      <h3 style="text-align: left;-webkit-text-fill-color: #2b85e4; border-bottom: 2px solid #2b85e4;">构建DIY组合</h3>

      <div style="margin: 10px 0">
        <div style="text-align: left;padding-bottom: 5px;padding-left: 40px">
          <p>
            上证50ETF期权&nbsp&nbsp
            <Select v-model="selectMonth" style="width: 100px" @on-change="changeMonth">
              <Option v-for="item in deadlineMonths" :value="item" :key="item">{{ item }}</Option>
            </Select>
          </p>
        </div>
        <div style="width: 100%;text-align: center">
          <table style="width: 100%;line-height:23px;" class="table1">
            <tr>
              <th colspan="9"> <div style="text-align: right; margin:5px 0; border-bottom: 4px solid  #eb6951;">
            看涨合约
          </div></th>
              <th></th>
              <th colspan="9"><div style="text-align: left; border-bottom: 4px solid  #4bbc7c;">
            看跌合约
          </div></th>
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
              <td @click="checkout('up',index)" v-for="up in item[0]" :class="getClassName(index,'up')">{{up}}</td>
              <td @click="checkout('up',index)" v-for="upRate in item[1]" :class="getClassName(index,'up')" v-if="upRate>0" style="-webkit-text-fill-color: #f16643">{{upRate}}%</td>
              <td @click="checkout('up',index)" v-for="upRate in item[1]" :class="getClassName(index,'up')" v-if="upRate<0" style="-webkit-text-fill-color: #19be6b">{{upRate}}%</td>
              <td @click="checkout('up',index)" v-for="upRate in item[1]" :class="getClassName(index,'up')" v-if="upRate==0">{{upRate}}%</td>
              <td v-for="middle in item[2]">{{middle}}</td>
              <td @click="checkout('down',index)" v-for="down in item[3]" :class="getClassName(index,'down')">{{down}}</td>
              <td @click="checkout('down',index)" v-for="downRate in item[4]" :class="getClassName(index,'down')" v-if="downRate>0" style="-webkit-text-fill-color: #f16643">{{downRate}}%</td>
              <td @click="checkout('down',index)"v-for="downRate in item[4]" :class="getClassName(index,'down')" v-if="downRate<0" style="-webkit-text-fill-color: #19be6b">{{downRate}}%</td>
              <td @click="checkout('down',index)"v-for="downRate in item[4]" :class="getClassName(index,'down')" v-if="downRate==0">{{downRate}}%</td>
              <td style="padding-left: 20px" :class="getClassName(index,'down')" :id="getClassName(index,'checkbox_down_')" @click="refreshId"><Checkbox @on-change="changeColor"></Checkbox></td>
            </tr>
          </table>
          <Card style="margin:10px 0;">
            <p slot="title">
              已选择的组合
              <!--<sapn style="font-weight: normal;font-size: 10px">(正买负卖)</sapn>-->
            </p>
            <Row >
              <div v-if="leftValue.length === 0 && rightValue.length === 0">
                请选择组合
              </div>
              <div v-else>
                <Col span="10" offset="2">
                  <div v-if="leftValue.length === 0">&nbsp;</div>
                  <li style="list-style-type:none;" v-for="(l,index) in leftValue" :key="index">
                    <Tag type="dot" >{{l.name}}</Tag> ×
                    <InputNumber style="width:45px;" size="small" type="number" v-model="l.value" class="numberInput" value="1" :id="getClassName(index,'left_input_number_')"></InputNumber >份
                  </li>
                </Col>
                <Col span="10" offset="2">
                  <li style="list-style-type:none;"  v-for="(r,index) in rightValue" :key="index">
                    <Tag type="dot" >{{r.name}}</Tag> ×
                    <InputNumber style="width:45px;" size="small" type="number" v-model="r.value" class="numberInput" value="1" :id="getClassName(index,'right_input_number_')"></InputNumber >份
                  </li>
                </Col>
              </div>
            </Row>
            <Row style="margin:10px 0;"><Button  @click="preview">预览</Button> <Button type="primary"  @click="newGroupName">添加至我的组合</Button></Row>
          </Card>
        </div>
      </div>
      <br>
      <h3 style="text-align: left;-webkit-text-fill-color: #2b85e4">合约分析</h3>
      <div style="width: 100%;height: 2px;background-color: #2b85e4"></div>
      <div>
        <div style="width: 30%;float: left;padding-top: 30px;padding-left: 30px">
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
        <div style="width: 70%;float: left;padding: 30px;text-align: center">
          <h3>组合表现展示&nbsp&nbsp<span style="font-size: 13px;-webkit-text-fill-color: #2baee9">{{this.graphTitle}}</span></h3>
          <div class="demo-spin-container2">
            <div style="background-color: #f8f8f9;margin-left: 5%;margin-right: 5%">
              <table class="table3" style="margin: auto">
                <tr>
                  <th>数量</th>
                  <td>{{this.resultTable.num}}</td>
                  <th>成本</th>
                  <td>{{this.resultTable.cost}}</td>
                  <th>保证金</th>
                  <td>{{this.resultTable.bond}}</td>
                  <th>beta</th>
                  <td>{{this.resultTable.beta}}</td>
                  <th>delta</th>
                  <td>{{this.resultTable.z_delta}}</td>
                </tr>
                <tr>
                  <th>gamma</th>
                  <td>{{this.resultTable.z_gamma}}</td>
                  <th>vega</th>
                  <td>{{this.resultTable.z_vega}}</td>
                  <th>theta</th>
                  <td>{{this.resultTable.z_theta}}</td>
                  <th>rho</th>
                  <td>{{this.resultTable.z_rho}}</td>
                </tr>
              </table>
            </div>

            <div id="myChart" style="width: 100%;height: 300px"></div>
            <Page :total="20" :current=currentPreview prev-text="Previous" next-text="Next" @on-change="ChangePage" />
            <Spin v-if="showPreview" fix>
              <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
              <div>加载中</div>
            </Spin>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
    export default {
        name: "diy",
      data () {
        return {
          deadlineMonths: [],
          upValue:[],
          downValue:[],
          value:[],
          leftValue:[],
          rightValue:[],
          allLeftValue:[],
          allRightValue:[],
          currentSelectedId:'',
          selectMonth:'',
          currentCodeLeft:[],
          currentCodeRight:[],
          lastSelectedLineIndex:0,
          lastSelectedLineType:'up',
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
          lineName:[],
          resultLeftCode:[],
          resultRightCode:[],
          line1:[],
          line2:[],
          graph1:[],
          graph2:[],
          resultTable:{},
          show1:false,
          show2:false,
          showPreview:false,
          interval: Number,
          currentPage:1,
          graphTitle:'',
          currentPreview:1

        }
      },
      beforeCreate:function () {
        this.axios.post("/backend/auth")
          .then((res)=>{
            if(res.data.code===1008){
              this.$router.push("/login");
            }
          });
      },
      async mounted() {
        var months
        await this.axios.get('/sinaTime/StockOptionService.getStockName')
          .then(re=>{
            months=re.data.result.data.contractMonth
          })
        await this.setSelectedMonth(months);
        await this.getValue(months[1]);
        await this.getAllLeftRightValues()
        this.interval = setInterval(this.circle, 5000);

      },
      methods: {
        ChangePage(page){
          this.currentPreview=page
            if(page=='1'){
              this.line1=this.graph1[0]
              this.line2=this.graph1[1]
              this.graphTitle="不同标的价格下组合收益"
              this.drawLine('标的价格/元','收益率(%)','','%')
            }
            else if(page=='2'){
              this.line1=this.graph2[0]
              this.line2=this.graph2[1]
              this.graphTitle="组合收益在历史市场内的概率分布"
              this.drawLine('历史收益率(%)','概率','%','')
            }
            else {
              alert("错了")
            }
          },
        setSelectedMonth(months){
          this.selectMonth=months[1]
          var result=[]
          for(var i=1;i<months.length;i++){
            result.push(months[i])
          }
          this.deadlineMonths=result;
        },
        async getValue(month){
          this.upValue=[]
          this.downValue=[]
          var date=month.substr(2,2)+month.substr(5)
          var address='/sinaOption/list=OP_UP_510050'+date+","+"OP_DOWN_510050"+date
          var subAddress
          await this.axios.get(address)
            .then(re=>{
              var parts=re.data.split(";")
              parts[0]=parts[0].substr(parts[0].indexOf("=")+2)
              parts[1]=parts[1].substr(parts[1].indexOf("=")+2)
              parts[0]=parts[0].substr(0,parts[0].length-1)
              parts[1]=parts[1].substr(0,parts[1].length-1)
              this.currentCodeLeft=parts[0].split(",")
              this.currentCodeRight=parts[1].split(",")
              subAddress='sinaOption/list='+parts[0]+parts[1]
            })
          await  this.axios.get(subAddress)
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
          this.checkout(this.lastSelectedLineType,this.lastSelectedLineIndex)
        },
        getAllLeftRightValues(){
          this.allLeftValue=[]
          this.allRightValue=[]
          for(var i=0;i<this.upValue.length;i++){
            this.allLeftValue.push({name:this.upValue[i][37],value:1,clicked:false})
            this.allRightValue.push({name:this.downValue[i][37],value:1,clicked:false})
          }
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
          this.resultLeftCode=[]
          this.resultRightCode=[]
          for(var i=0;i<this.value.length;i++){
            var colorl=document.getElementById("checkbox_up_"+i).style.backgroundColor
            if(colorl=='rgb(255, 238, 238)'){
              this.leftValue.push(this.allLeftValue[i])
              this.resultLeftCode.push(this.currentCodeLeft[i])
            }
            var colorr=document.getElementById("checkbox_down_"+i).style.backgroundColor
            if(colorr=='rgb(255, 238, 238)'){
              this.rightValue.push(this.allRightValue[i])
              this.resultRightCode.push(this.currentCodeRight[i])
            }
          }
        },
        circle(){
          if(this.deadlineMonths.length==0){
          }
          else{
            this.getValue(this.selectMonth)
          }
        },
        async changeMonth(){
          this.value=[]
          this.lastSelectedLineType='up'
          this.lastSelectedLineIndex=0
          await this.getValue(this.selectMonth)
          await this.getAllLeftRightValues()
          await this.setLeftOrRightValue()
        },
        checkout(upOrDown,index){
          //1.先复原
          if(this.lastSelectedLineIndex!=''){
            var ups=document.getElementsByClassName('up'+this.lastSelectedLineIndex)
            for(var i=0;i<ups.length;i++){
              ups[i].style.border='none'
            }
            var downs=document.getElementsByClassName('down'+this.lastSelectedLineIndex)
            for(var i=0;i<downs.length;i++){
              downs[i].style.border='none'
            }
          }
          else{
            var ups=document.getElementsByClassName('up0')
            for(var i=0;i<ups.length;i++){
              ups[i].style.border='none'
            }
            var downs=document.getElementsByClassName('down0')
            for(var i=0;i<downs.length;i++){
              downs[i].style.border='none'
            }
          }

          //2.选中
          var tds=document.getElementsByClassName(upOrDown+index)
          if(upOrDown=='up'){
            for(var i=1;i<tds.length;i++){
              tds[i].style.borderBottom='1px solid #2db7f5'
              tds[i].style.borderTop='1px solid #2db7f5'
            }
            tds[1].style.borderLeft='1px solid #2db7f5'
            tds[tds.length-1].style.borderRight='1px solid #2db7f5'
          }
          else{
            for(var i=0;i<tds.length-1;i++){
              tds[i].style.borderBottom='1px solid #2db7f5'
              tds[i].style.borderTop='1px solid #2db7f5'
            }
            tds[0].style.borderLeft='1px solid #2db7f5'
            tds[tds.length-2].style.borderRight='1px solid #2db7f5'
          }

          //3.赋值
          this.lastSelectedLineIndex=index;
          this.lastSelectedLineType=upOrDown

          //4.获取数据
          this.getSingleInfo(upOrDown)

        },
        getSingleInfo(upOrDown){
          var index=this.lastSelectedLineIndex
          if(upOrDown=='up'){
            var suffix=this.currentCodeLeft[index].substr(7)
            this.axios.get('/sinaOption/list=CON_SO_'+suffix)
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
            var suffix=this.currentCodeRight[index].substr(7)
            this.axios.get('/sinaOption/list=CON_SO_'+suffix)
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
        async drawLine(xName,yName,xFormat,yFormat,graph){
          // 基于准备好的dom，初始化echarts实例
          let myChart = this.$echarts.init(document.getElementById('myChart'))
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
            legend: {
              data:this.lineName
            },
            xAxis: {
              name:xName,
              nameTextStyle:{
                fontSize:9,
                padding:0
              },
              nameGap:2,
              type: 'category',
              data: this.line1.map(function (item) {
                if(yFormat=='%'){
                  return (parseFloat(item)*100).toFixed(2);
                }
                else{
                  return (parseFloat(item).toFixed(4))
                }
              }),
            },
            yAxis: {
              name:yName,
              type: 'value'
            },
            series: [{
              name:this.lineName[0],
              data: this.line2.map(function (item) {
                if(yFormat=='%'){
                  return (parseFloat(item)*100).toFixed(2);
                }
                else{
                  return (parseFloat(item).toFixed(4))
                }
              }),
              type: 'line'
            }]
          });
        },
        async confirm(name){
          this.handleSpinCustom()
          var origin_data
          await this.axios.post('/backend/recommend/customPortfolio',{options:this.getOptions()})
            .then(re=>{
              origin_data=re.data.data
            })
          var options=origin_data.optionList

          var data={}
          for(var key in origin_data){
            if(key=='optionList'){
              data['options']=origin_data[key]
            }
            else{
              data[key]=origin_data[key]
            }
          }
          data.name=name
          data.type=2
          data.trackingStatus=false

          await this.axios.post('/backend/portfolio',data)
            .then(re=>{
              if(re.data.msg=='Add portfolio success'){
                this.$Message.success("添加成功")
                this.$router.push('/myPortfolio')
              }
              else{
                this.$Message.error("添加失败")
              }
            })

        },
        newGroupName(){
          var name
          this.$Modal.confirm({
            title: '添加至我的组合',
            okText:'确认',
            cancelText:'取消',
            render: (h) => {
              return [h('Input', {
                props: {
                  value: '',
                  autofocus: true,
                  placeholder: '请输入新的组合名称'
                },
                on: {
                  input: (val) => {
                    name= val;
                  }
                }
              }),

              ]
            },
            onOk: () => {
              this.confirm(name)
            },
          });
        },
        getOptions(){
          var options=[]
          var deadline=this.calculateForthWednesday()
          for(var i=0;i<this.resultLeftCode.length;i++){
            var op={}
            op.optionCode="CON_OP_"+this.resultLeftCode[i].substr(7)
            op.expireTime=deadline
            op.type=this.leftValue[i].value
            op.cp=1
            options.push(op)
          }
          for(var i=0;i<this.resultRightCode.length;i++){
            var op={}
            op.optionCode="CON_OP_"+this.resultRightCode[i].substr(7)
            op.expireTime=deadline
            op.type=this.rightValue[i].value
            op.cp=-1
            options.push(op)
          }
          return options
        },
        calculateForthWednesday(){
          //1.先判断每月第一天是星期几
          var year=this.selectMonth.substr(0,4)
          var month=this.selectMonth.substr(5,2)
          var d=new Date()
          d.setFullYear(year)
          d.setMonth(month-1)
          d.setDate(1)
          var weekday=d.getDay()
          var day
          if(weekday>3){
            day=21+(7-weekday)+4
          }
          else if(weekday<=3){
            day=21+(4-weekday)
          }
          return year+"-"+month+"-"+(Array(2).join(0)+day).slice(-2)
        },
        preview(){
          this.ChangePage(1)
          this.showPreview=true
          this.axios.post('/backend/recommend/customPortfolio',{options:this.getOptions()})
            .then(re=>{
              if(re.data.msg=='custom portfolio finished'){
                this.resultTable.num=re.data.data.num
                this.resultTable.cost=re.data.data.cost.toFixed(4)
                this.resultTable.bond=re.data.data.bond.toFixed(4)
                this.resultTable.z_delta=re.data.data.z_delta.toFixed(4)
                this.resultTable.z_gamma=re.data.data.z_gamma.toFixed(4)
                this.resultTable.z_vega=re.data.data.z_vega.toFixed(4)
                this.resultTable.z_theta=re.data.data.z_theta.toFixed(4)
                this.resultTable.z_rho=re.data.data.z_rho.toFixed(4)
                this.resultTable.em=re.data.data.em.toFixed(4)
                this.resultTable.beta=re.data.data.beta.toFixed(4)
                this.resultTable.m0=re.data.data.m0
                this.resultTable.k=re.data.data.k.toFixed(4)
                this.resultTable.sigma1=re.data.data.sigma1.toFixed(4)
                this.resultTable.sigma2=re.data.data.sigma2.toFixed(4)
                this.resultTable.p1=re.data.data.p1.toFixed(4)
                this.resultTable.p2=re.data.data.p2.toFixed(4)
                this.graph1=re.data.data.assertPrice2Profit
                this.graph2=re.data.data.historyProfit2Probability
                console.log(re.data.data.assertPrice2Profit)
                this.line1=this.graph1[0]
                this.line2=this.graph1[1]
                this.graphTitle='不同标的价格下组合收益'
                this.drawLine('标的价格/元','收益率(%)','','%',re.data.data.assertPrice2Profit)
              }
              else{
                this.$Message.error("未能")
              }

              setTimeout(() => {
                this.showPreview = false;
              }, 2000);
            })
        },
        handleSpinCustom () {
          this.$Spin.show({
            render: (h) => {
              return h('div', [
                h('Icon', {
                  'class': 'demo-spin-icon-load',
                  props: {
                    type: 'ios-loading',
                    size: 18
                  }
                }),
                h('div', 'Loading')
              ])
            }
          });
          setTimeout(() => {
            this.$Spin.hide();
          }, 3000);
        }
      },
      destroyed() {
        clearInterval(this.interval);
      },
    }
</script>

<style scoped>
  .table1{
    border-collapse: collapse;
  }
  .table1 th{
    font-size: 13px;
    padding-left: 5px;
    padding-right: 5px;
  }
  .table1 td{
    font-size: 13px;
    padding-left: 5px;
    padding-right: 5px;
    background-color: #f8f8f9;
  }
  .table2{
    border-collapse: collapse;
    margin: auto;
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

  .table3{
    border-collapse: collapse;
  }

  .table3 td{
    text-align: left;
    font-size: 13px;
    padding-left: 10px;
    padding-right: 10px;
    background-color: #f8f8f9;
    min-width: 50px;
    text-align: center;
    -webkit-text-fill-color: #2baee9;
  }
  .table3 th{
    text-align: left;
    font-size: 13px;
    background-color: #f8f8f9;
    -webkit-text-fill-color: #515a6e;
  }

  li{
    padding: 5px;
  }

  .numberInput{
    background-color: #f8f8f9;
    border-width: 1px;
    border-color: #dcdee2;
    width: 40px;
  }

  .demo-spin-icon-load{
    animation: ani-demo-spin 1s linear infinite;
  }
  @keyframes ani-demo-spin {
    from { transform: rotate(0deg);}
    50%  { transform: rotate(180deg);}
    to   { transform: rotate(360deg);}
  }
  .demo-spin-container2{
    display: inline-block;
    width: 100%;
    min-height: 400px;
    position: relative;
  }

  .demo-spin-icon-load{
    animation: ani-demo-spin 1s linear infinite;
  }

</style>
