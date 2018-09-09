<template>
  <div>
    <Row class="mt5">
      <i-input v-model="principal" placeholder="本金" class="w-70 cover">
        <span slot="append" class="f5">元</span>
      </i-input>
    </Row>
    <Row class="mt4 mb4">
      <i-input v-model="maxLost" placeholder="允许最大损失" class="w-70 cover">
        <span slot="append" class="f5">%</span>
      </i-input>
    </Row>
    <Row class="pb4 border" style="z-index: 1002">
      <Select v-model="time" placeholder="价格有效时间" style="width:200px" class="fl cover">
        <Option v-for="item in timeList" :value="item.time" :label="item.time" :key="item.time" style="height: 32px">
          <span style="float:left">{{item.time}}</span>
          <span style="float:right;color:#ccc">{{item.differ+"天"}}</span>
        </Option>
      </Select>
    </Row>
    <Row v-if="price===1" class="pt4">
      <h4 class="tl">预测价格范围</h4>
      <!--<Slider id="priceUp" v-model="priceForecast" :step="0.001" :tip-format="priceFormat" range :max="4" :min="currentPrice"></Slider>-->
      <el-slider id="priceUp" v-model="priceForecast" :step="0.001" :format-tooltip="priceFormat" range :max="4" :min="currentPrice"></el-slider>
      <p class="pt4">价格范围:{{priceForecast[0]}}~{{priceForecast[1]}}元</p>
    </Row>
    <Row v-if="price===-1" class="pt4">
      <h4 class="tl">预测价格范围</h4>
      <!--<Slider id="priceDown" v-model="priceForecast" :step="0.001" :tip-format="priceFormat" range :max="currentPrice" :min="1"></Slider>-->
      <el-slider id="priceDown" v-model="priceForecast" :step="0.001" :format-tooltip="priceFormat" range :max="currentPrice" :min="1"></el-slider>
      <p class="pt4">价格范围:{{priceForecast[0]}}~{{priceForecast[1]}}元</p>
    </Row>
    <Row v-if="volatility===1" class="pt4 pb5">
      <h4 class="tl">预测波动率范围</h4>
      <!--<Slider id="volatilityUp" v-model="volatilityForecast" :step="1" :tip-format="volatilityFormat" range :max="50" :min="currentVolatility"></Slider>-->
      <el-slider id="volatilityUp" v-model="volatilityForecast" :step="1" :format-tooltip="volatilityFormat" range :max="50" :min="currentVolatility"></el-slider>
      <p class="pt4">波动率范围:{{volatilityForecast[0]}}~{{volatilityForecast[1]}}%</p>
    </Row>
    <Row v-if="volatility===-1" class="pt4 pb5">
      <h4 class="tl">预测波动率范围</h4>
      <!--<Slider id="volatilityDown" v-model="volatilityForecast" :step="1" :tip-format="volatilityFormat" range :max="currentVolatility" :min="0"></Slider>-->
      <el-slider id="volatilityDown" v-model="volatilityForecast" :step="1" :format-tooltip="volatilityFormat" range :max="currentVolatility" :min="0"></el-slider>
      <p class="pt4">波动率范围:{{volatilityForecast[0]}}~{{volatilityForecast[1]}}%</p>
    </Row>
    <Row class="pt5">
      <Button size="large" type="warning" @click="nextStep" class="fr forwardButton" ghost>
        下一步
        <Icon type="ios-arrow-forward"/>
      </Button>
    </Row>
  </div>
</template>

<script>
  export default {
    name: "AlForm",
    data() {
      return {
        principal: "",
        maxLost: "",
        time: "",
        volatilityForecast: [0,0],
        priceForecast: [0,0],
        currentPrice: 2,
        currentVolatility: 10,
        timeList: [],
      }
    },
    props: ["price", "volatility"],
    methods: {
      timeDiffer: async function (time) {
        let temp = time.split('-').join("") + "01";
        let result;
        await this.axios.get("/sinaTime/StockOptionService.getRemainderDay", {
          params: {
            date: temp
          }
        })
          .then((res) => {
            result = res.data.result.data.remainderDays;
          });
        return result;
      },
      volatilityFormat: function (val) {
        return val + "%"
      },
      priceFormat: function (val) {
        return val + "元"
      },
      isInFive: function () {
        let now = new Date();
        let year = now.getFullYear();
        let month = now.getMonth() + 1;
        let day = now.getDate()
        let WeekDay;
        let theFourthWed;
        WeekDay = new Date(year, month - 1, 1).getDay();
        theFourthWed = WeekDay <= 3 ? 24 - WeekDay : 31 - WeekDay;
        return day >= theFourthWed - 4 && day <= theFourthWed + 1;
      },
      validateInput:function(){
        if(this.principal===""||this.maxLost===""||this.time===""){
          this.$Message.warning("参数必须填写完整");
          return false;
        }
        if(this.maxLost<=0){
          this.$Message.warning("最大损失至少为1%");
          return false;
        }
        if(this.maxLost>100){
          this.$Message.warning("最大损失不能大于100%");
          return false;
        }
        return true;
      },
      nextStep: function () {
        if(!this.validateInput()){
          return;
        }
        let param = {
          t: this.time
        };
        param.m0 = this.principal;
        param.k = this.maxLost;
        if (this.volatility > 0) {
          if (this.price > 0) {
            param.combination = 'A';
            param.p1 = this.priceForecast[0];
            param.p2 = this.priceForecast[1];
            param.sigma1 = this.volatilityForecast[0];
            param.sigma2 = this.volatilityForecast[1];
          } else if (this.price === 0) {
            param.combination = 'B';
            param.p1 = this.currentPrice;
            param.p2 = this.currentPrice;
            param.sigma1 = this.volatilityForecast[0];
            param.sigma2 = this.volatilityForecast[1];
          } else {
            param.combination = 'C';
            param.p1 = this.priceForecast[0];
            param.p2 = this.priceForecast[1];
            param.sigma1 = this.volatilityForecast[0];
            param.sigma2 = this.volatilityForecast[1];
          }
        } else if (this.volatility === 0) {
          if (this.price > 0) {
            param.combination = 'D';
            param.p1 = this.priceForecast[0];
            param.p2 = this.priceForecast[1];
            param.sigma1 = this.currentVolatility;
            param.sigma2 = this.currentVolatility;
          } else {
            param.combination = 'E';
            param.p1 = this.priceForecast[0];
            param.p2 = this.priceForecast[1];
            param.sigma1 = this.currentVolatility;
            param.sigma2 = this.currentVolatility;
          }
        } else {
          if (this.price > 0) {
            param.combination = 'F';
            param.p1 = this.priceForecast[0];
            param.p2 = this.priceForecast[1];
            param.sigma1 = this.volatilityForecast[0];
            param.sigma2 = this.volatilityForecast[1];
          } else if (this.price === 0) {
            param.combination = 'G';
            param.p1 = this.currentPrice;
            param.p2 = this.currentPrice;
            param.sigma1 = this.volatilityForecast[0];
            param.sigma2 = this.volatilityForecast[1];
          } else {
            param.combination = 'H';
            param.p1 = this.priceForecast[0];
            param.p2 = this.priceForecast[1];
            param.sigma1 = this.volatilityForecast[0];
            param.sigma2 = this.volatilityForecast[1];
          }
        }
        this.$emit("nextStep", param);
      }
    },
    created: function () {
      this.axios.get("/sinaTime/StockOptionService.getStockName")
        .then((res) => {
          let tempList = res.data.result.data.contractMonth;
          tempList.splice(0, 1);
          let requestList = [];
          for (let i = 0; i < tempList.length; i++) {
            requestList.push(this.timeDiffer(tempList[i])
              .then((res) => {
                this.timeList.push({
                  time: tempList[i],
                  differ: res
                });
              }));
          }
          Promise.all(requestList).then((res) => {
            let compare = (obj1, obj2) => {
              return obj1.differ < obj2.differ ? -1 : (obj1.differ > obj2.differ ? 1 : 0);
            };
            this.timeList.sort(compare);
            if (this.isInFive()) {
              this.timeList.slice(0, 1)
            }
          })
        });
    },
    mounted:function(){
      this.axios.get("/sigma", {params: {v: new Date().getTime()}})
        .then((res) => {
          let list = res.data.split(/\r?\n|\r/);
          let index;
          let length = list.length;
          if (list[length - 2].split(',')[1].trim() !== "") {
            index = length - 1;
          } else {
            for (index = 0; index < length; index++) {
              if (list[index].split(',')[1].trim() === '') {
                break;
              }
            }
          }
          this.currentVolatility = parseFloat(list[index - 1].split(',')[1].trim());
          this.volatilityForecast[0] = this.currentVolatility;
          this.volatilityForecast[1] = this.currentVolatility;
        });
      this.axios.get("/sinaOption/list=f_510050")
        .then((res) => {
          this.currentPrice = parseFloat(res.data.split(',')[3]);
          this.priceForecast[0] = this.currentPrice;
          this.priceForecast[1] = this.currentPrice;
        });
    },
    watch: {
      price: function () {
        this.volatilityForecast[0] = this.currentVolatility;
        this.volatilityForecast[1] = this.currentVolatility;
        this.priceForecast[0] = this.currentPrice;
        this.priceForecast[1] = this.currentPrice;
      },
      volatility: function () {
        this.volatilityForecast[0] = this.currentVolatility;
        this.volatilityForecast[1] = this.currentVolatility;
        this.priceForecast[0] = this.currentPrice;
        this.priceForecast[1] = this.currentPrice;
      }
    }
  }
</script>

<style scoped>
  .border {
    border-bottom: 1px solid rgba(0, 0, 0, .3);
  }

  .forwardButton {
    color: #f90;
    background: 0 0;
    border-color: #f90;
  }

  .forwardButton:hover {
    color: #fff;
    background-color: #f90;
    border-color: #f90;
  }
</style>
