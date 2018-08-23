<template>
  <div>
    <Row>
      <h4 class="item" style="display:inline;line-height: 32px;text-align: left;">上交所</h4>
      <h4 class="item" style="display:inline;line-height: 32px;text-align: left;">50ETF</h4>
        <Select  class="item cover"  style="text-align:left;width:200px;" v-model="date" @on-change="handleSelect($event)">
          <Option v-for="item in timeList" :value="item.date" :label="item.date" :key="item.date" style="height: 32px">
            <span>{{item.date}}</span>
          </Option>
        </Select>
        <h4 class="item" style="display:inline;line-height: 32px;text-align: left;">到期日：{{expiry}} ({{differ}} 天)</h4>
        <h4 class="item" style="display:inline;line-height: 32px;text-align: left;"> 标的资产：华夏上证 50ETF（<span  v-bind:style="{ color:  ETF.rate  > 0 ? 'rgb(187, 0, 0)' : (ETF.rate === 0 ?  'black' : 'green')}">{{ETF.price}}</span> / <span v-bind:style="{ color:  ETF.rate  > 0 ? 'rgb(187, 0, 0)' : (ETF.rate === 0 ?  'black' : 'green')}">{{ETF.rate}}</span>）</h4>
      </Row>
  </div>
</template>

<script>
    export default {
        name: "MonthSelecter",
        data() {
          return {
            timeList: [],
            date: '',
            differ: 0,
            ETF: {
              price: 0,
              deta: 0,
              rate: 0,
            }
          }
        },

        computed: {
          expiry: function() {
            return [new Date(new Date().setDate(this.differ + new Date().getDate())).getFullYear(),
                    new Date(new Date().setDate(this.differ + new Date().getDate())).getMonth() > 8 ? new Date(new Date().setDate(this.differ + new Date().getDate())).getMonth()+ 1 : '0' + ( new Date(new Date().setDate(this.differ + new Date().getDate())).getMonth() + 1),
                    new Date(new Date().setDate(this.differ + new Date().getDate())).getDate() > 9 ? new Date(new Date().setDate(this.differ + new Date().getDate())).getDate() : '0' + new Date(new Date().setDate(this.differ + new Date().getDate())).getDate(),].join('-');
          } 
        },
        methods: {
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
          handleSelect(e) {
            this.differ =  this.timeList.find((item) =>{
              return item.date === e;
            }).differ;
            this.$emit('selectedTime', {date: e, differ: this.differ, ETFPrice: this.ETF.price});
          }
        },
        created:function () {
          this.axios.get("/sinaTime/StockOptionService.getStockName")
            .then((res)=>{
              let temp = res.data.result.data.contractMonth;
              temp.shift(0,1);
              const tempRequest = [];  
              temp.map(item => {
                tempRequest.push(this.timeDiffer(item).then(res=> {
                  console.log(res);
                  this.timeList.push({
                    date: item,
                    differ: res
                  });
                })
              )});
              Promise.all(tempRequest).then(res => {
                this.timeList.sort((ob1, ob2) => {
                  return ob1.differ > ob2.differ;
                })
                this.date = this.timeList[0].date;
                this.differ = this.timeList[0].differ;
                this.$emit('selectedTime', {date: this.date, differ: this.differ,ETFPrice: this.ETF.price});
              });
            });
        },
        mounted() {
          this.axios
            .get('/sinaOption/list=s_sh510050')
            .then(res => {
              console.log(res.data.split('"')[1].split(','));
              
              [,this.ETF.price,this.ETF.deta,this.ETF.rate,,,] = res.data.split('"')[1].split(',');
              console.log(this.ETF);
            })  
        },
    }
</script>

<style scoped>
.item {
  margin: 0 10px;
}
</style>
