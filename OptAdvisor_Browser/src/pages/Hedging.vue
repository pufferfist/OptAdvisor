<template>
  <div>
    <div  v-bind:style="{display:ispage1}">
      <collect_info ref="info" style="padding-left: 20%;padding-right: 20%"></collect_info>
      <br>
      <Button type="primary" style="width: 20%" @click="search">查询结果</Button>
      <p style="font-size: 10px;" v-bind:style="{'-webkit-text-fill-color':warn_color}">{{this.warn_data}}</p>
    </div>
    <div  v-bind:style="{display:ispage2}">
      <display_result ref="result"></display_result>
    </div>
    <div>
      <Spin v-if="showResult" fix>
      <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
      <div>Loading</div>
      </Spin>
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
          demo_spilt_height:'700px',
          ispage1:'',
          ispage2:'none',
          warn_color:'#ffffff',
          warn_data:'',
          showResult:false
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
      methods:{
          search(){
            var array=this.$refs.info.$refs.formItem.fields
            var OpenInterest=array[0].fieldValue
            var rate=array[1].fieldValue
            var deadline=array[2].fieldValue
            var min_price=array[3].fieldValue
            if((OpenInterest=='')||(rate=='')||(deadline=='')||(min_price=='')){
              this.warn_data='信息填写不完整'
              this.warn_color="#ed4014"
            }
            else{
              this.showResult=true
              this.warn_data=''
              this.warn_color="#ffffff"
              var deadline_value=this.$refs.info.formItem.month[deadline.substr(5)-1]
              //写入数据
              var param={n0:OpenInterest,a:rate/100,s_exp:min_price.toFixed(2),t:deadline_value}
              this.axios.post('/backend/recommend/hedging',param).then((response)=>{
                if(response.data.msg=='No eligible options'){
                  //this.$refs.blank.text='没有合适的期权'
                }
                else if(response.data.msg=='IOException occurs'){
                 // this.$refs.blank.text='输入输出报错，请您稍后重试'
                }
                else if(response.data.msg=='Hedging success'){
                  //this.$refs.blank.text='暂无数据，等待输入'
                  this.$refs.result.originData=response.data.data

                  var data=response.data.data
                  //1.填充表格
                  var forms={
                    optionCode:data.option.optionCode, //期权代码
                    expireTime:data.option.expireTime,//到期时间
                    transactionPrice:data.option.transactionPrice,//成交价
                    yclose:data.option.yclose,//期权前一天收盘价
                    price1:data.option.price1,//期权实时买入价格
                    price2:data.option.price2,//期权实时卖出价格
                    k:data.option.k,//期权行权价格
                    realTimePrice:data.option.realTimePrice,
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
                  this.$refs.result.expectedLoss=data.iK

                  //3.填充折线图
                  this.$refs.result.graph=data.graph
                  this.$refs.result.drawLine()

                  this.ispage1="none"
                  this.ispage2=""
                  this.demo_spilt_height="700px"
                }
                setTimeout(() => {
                  this.showResult = false;
                }, 200);
              })

            }
          }
      },
      destroyed() {
        clearInterval(this.Interval);
      },
    }
</script>

<style scoped>
  .demo-spin-icon-load{
    animation: ani-demo-spin 1s linear infinite;
  }
  @keyframes ani-demo-spin {
    from { transform: rotate(0deg);}
    50%  { transform: rotate(180deg);}
    to   { transform: rotate(360deg);}
  }
  .demo-spin-container{
    display: inline-block;
    width: 100%;
    min-height: 700px;
    position: relative;
  }
</style>
