<template>
    <div>
      <Row class="mt5">
        <i-input v-model="principal" placeholder="本金" class="w-70 cover">
          <span slot="append" class="f5">元</span>
        </i-input>
      </Row>
      <Row class="mt4 mb4">
        <i-input v-model="maxLost" placeholder="允许最大损失" class="w-70 cover">
          <span slot="append" class="f5">元</span>
        </i-input>
      </Row>
      <Row class="pb4 border">
        <Select v-model="time" placeholder="价格有效时间" style="width:200px" class="fl cover">
          <Option v-for="item in timeList" :value="item.time" :label="item.time" :key="item.time" style="height: 32px">
            <span style="float:left">{{item.time}}</span>
            <span style="float:right;color:#ccc">{{item.differ+"天"}}</span>
          </Option>
        </Select>
      </Row>
      <Row v-if="price===1" class="pt4">
        <h4 class="tl">预测价格范围</h4>
        <Slider  id="priceUp" v-model="priceForecast" :step="0.001" :tip-format="priceFormat" show-input :max="4" :min="currentPrice"></Slider>
        <p class="pt4">价格范围:{{currentPrice}}~{{priceForecast>currentPrice?priceForecast:currentPrice}}元</p>
      </Row>
      <Row v-if="price===-1" class="pt4">
        <h4 class="tl">预测价格范围</h4>
        <Slider id="priceDown" v-model="priceForecast" :step="0.001" :tip-format="priceFormat" show-input :max="currentPrice" :min="1"></Slider>
        <p class="pt4">价格范围:{{priceForecast}}~{{currentPrice}}元</p>
      </Row>
      <Row v-if="volatility===1" class="pt4 pb5">
        <h4 class="tl">预测波动率范围</h4>
        <Slider id="volatilityUp" v-model="volatilityForecast" :step="1" :tip-format="volatilityFormat" show-input :max="50" :min="currentVolatility"></Slider>
        <p class="pt4">波动率范围:{{currentVolatility}}~{{volatilityForecast}}%</p>
      </Row>
      <Row v-if="volatility===-1" class="pt4 pb5">
        <h4 class="tl">预测波动率范围</h4>
        <Slider id="volatilityDown" v-model="volatilityForecast" :step="1" :tip-format="volatilityFormat" show-input :max="currentVolatility" :min="0"></Slider>
        <p class="pt4">波动率范围:{{volatilityForecast}}~{{currentVolatility}}%</p>
      </Row>
      <Row class="pt5">
        <Button size="large" type="warning" @click="nextStep" class="fr forwardButton" ghost>
          下一步
          <Icon type="ios-arrow-forward" />
        </Button>
      </Row>
    </div>
</template>

<script>
    export default {
      name: "AlForm",
      data(){
        return{
          principal: "",
          maxLost: "",
          time: "",
          volatilityForecast:0,
          priceForecast:0,
          currentPrice:2,
          currentVolatility:10,
          timeList: [],
        }
      },
      props:["price","volatility"],
      methods:{
        timeDiffer:async function (time) {
          let temp=time.split('-').join("")+"01";
          let result;
          await this.axios.get("/sinaTime/StockOptionService.getRemainderDay",{
            params:{
              date:temp
            }
          })
            .then((res)=>{
              result=res.data.result.data.remainderDays;
            });
          return result;
        },
        volatilityFormat:function (val) {
          return val+"%"
        },
        priceFormat:function (val) {
          return val+"元"
        },
        isInFive: function () {
          let now=new Date();
          let year=now.getFullYear();
          let month=now.getMonth()+1;
          let day=now.getDate()
          let WeekDay;
          let theFourthWed;
          WeekDay=new Date(year,month-1,1).getDay();
          theFourthWed = WeekDay <= 3 ? 24 - WeekDay : 31 - WeekDay;
          return day >= theFourthWed - 4 && day <= theFourthWed + 1;
        },
        nextStep:function () {
          this.$emit("nextStep");
        }
      },
      created:function () {
        this.axios.get("/sinaTime/StockOptionService.getStockName")
          .then((res)=>{
            let tempList=res.data.result.data.contractMonth;
            tempList.splice(0,1);
            let requestList=[];
            for (let i = 0; i < tempList.length; i++) {
              requestList.push(this.timeDiffer(tempList[i])
                .then((res)=>{
                  this.timeList.push({
                    time: tempList[i],
                    differ: res
                  });
                }));
            }
            Promise.all(requestList).then((res)=>{
              let compare =(obj1, obj2) => {
                return obj1.differ<obj2.differ?-1:(obj1.differ>obj2.differ?1:0);
              };
              this.timeList.sort(compare);
              if(this.isInFive()){
                this.timeList.slice(0,1)
              }
            })
          });
        this.axios.get("/sigma", {params: {v: new Date().getTime()}})
          .then((res) => {
            let list = res.data.split(/\r?\n|\r/);
            this.currentVolatility = parseFloat(list[list.length - 2].split(',')[1].trim());
            this.volatilityForecast = this.currentVolatility;
          });
        this.axios.get("/sinaOption/list=f_510050")
          .then((res) => {
            this.currentPrice = parseFloat(res.data.split(',')[1]);
            this.priceForecast = this.currentPrice;
          });
      },
      watch:{
        price:function () {
          this.volatilityForecast=this.currentVolatility;
          this.priceForecast=this.currentPrice;
        },
        volatility:function () {
          this.volatilityForecast=this.currentVolatility;
          this.priceForecast=this.currentPrice;
        }
      }
    }
</script>

<style scoped>
  .border{
    border-bottom: 1px solid rgba( 0, 0, 0, .3 );
  }

  .forwardButton{
    color: #f90;
    background: 0 0;
    border-color: #f90;
  }

  .forwardButton:hover{
    color: #fff;
    background-color: #f90;
    border-color: #f90;
  }
</style>
