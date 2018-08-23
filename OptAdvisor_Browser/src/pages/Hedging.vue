<template>
  <div style="height: 100%">
    <div class="demo-split" v-bind:style="{height:demo_spilt_height}">
      <Split v-model="split1">
        <div slot="left" class="demo-split-pane">
          <collect_info ref="info"></collect_info>
          <p style="font-size: 10px;" v-bind:style="{'-webkit-text-fill-color':warn_color}">信息填写不完整</p>
          <Button type="primary" @click="search">查询结果</Button>
        </div>
        <div slot="right" class="demo-split-pane">
          <no_input ref="blank" v-bind:style="{display:ispage1}"></no_input>
          <display_result ref="result" v-bind:style="{display:ispage2}"></display_result>
        </div>
      </Split>
    </div>
  </div>

</template>

<script>
  import collect_info from "../components/hedging/CollectInfo";
  import display_result from "../components/hedging/DisplayResult"
  import no_input from "../components/hedging/NoInput"
    export default {
        name: "hedging",
      components:{collect_info, display_result, no_input},
      data () {
        return {
          split1: 0.3,
          demo_spilt_height:'500px',
          ispage1:'',
          ispage2:'none',
          warn_color:'#ffffff'
        }
      },
      methods:{
          search(){
            var array=this.$refs.info.$refs.formItem.fields
            var OpenInterest=array[0].fieldValue
            var rate=array[1].fieldValue
            var deadline=array[2].fieldValue
            var min_price=array[3].fieldValue
            if((OpenInterest=='')||(rate=='')||(deadline=='')||(min_price=='')||(min_price=='¥')){
              this.warn_color="#ed4014"
            }
            else{
              this.warn_color="#ffffff"
              var deadline_value=this.$refs.info.formItem.month[deadline.substr(5)-1]

              //写入数据
              var param={NO:OpenInterest,a:rate/100,s_exp:min_price,T:deadline_value}
              this.axios.post('/backend/recommend/hedging',param).then((response)=>{
                /**********************************查看data是否合规**************************************/
                var data=response.data
                //1.填充表格
                var forms={
                    persisentId:data.option.persisentId,
                    tradeCode:data.option.tradeCode, //交易代码
                    optionCode:data.option.optionCode, //期权代码
                    name:data.option.name,//例如:50ETF购8月2600
                    expireTime:data.option.expireTime,//到期时间
                    transactionPrice:data.option.transactionPrice,//成交价
                    quantity:data.option.quantity,//在组合中的份数,单独存在无意义
                    yclose:data.option.yclose,//期权前一天收盘价
                    price1:data.option.price1,//期权实时买入价格
                    price2:data.option.price2,//期权实时卖出价格
                    k:data.option.k,//期权行权价格
                    delta:data.option.delta,
                    gamma:data.option.gamma,
                    vega:data.option.vega,
                    theta:data.option.theta,
                    rho:data.option.rho,
                    beta:data.option.beta,
                  }
                  if(data.option.type=='1'){
                    forms.type='买入'
                  }
                  else if(data.option.type=='0'){
                    forms.type='卖出'
                  }
                  if(data.option.cp=='1'){
                    forms.cp='看涨'
                  }
                  else if(data.option.cp=='-1'){
                    forms.cp='看跌'
                  }
                this.$refs.result.$refs.option_group.TData=forms

                //2.填充预期最大亏损值
                this.$refs.result.expectedLoss=data.ik

                //3.填充折线图
                this.$refs.result.graph=data.graph
                this.$refs.result.drawLine()
              })
              this.ispage1="none"
              this.ispage2=""
              this.demo_spilt_height="700px"
            }
          }
      }
    }
</script>

<style scoped>
  .demo-split{
    border: 1px solid #dcdee2;
  }
  .demo-split-pane{
    padding: 10px;
  }
</style>
