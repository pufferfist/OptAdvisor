<template>
    <div style="padding-top: 7%">
      <Form ref="formItem" :model="formItem" :label-width="100">
        <p style="font-size: 25px;font-weight: bold">套期保值信息设置&nbsp &nbsp</p><br><br>
        <Timeline>
          <TimelineItem color="green">
            <FormItem label="持仓量" prop="OpenInterest">
              <InputNumber :min="10000" v-model="formItem.OpenInterest" placeholder="请输入持仓量" style="width: 100%"></InputNumber>
            </FormItem>
          </TimelineItem>
          <TimelineItem color="green">
            <FormItem label="套保比例" prop="rate">
              <div>
                <div style="float: left;width: 78%;height: 20px">
                  <Slider v-model="formItem.rate"></Slider>
                </div>
                <div style="float: left;width: 5%;height: 20px"></div>
                <div style="float: left;width: 17%;height:20px">
                  <Input style="width: 80%;float: left" v-model="formItem.rate"/>%
                </div>
              </div>
            </FormItem>
          </TimelineItem>
          <TimelineItem color="blue">
            <FormItem label="套保期限" prop="deadline">
              <Select v-model="formItem.deadline">
                <Option value="month1">{{formItem.month[0]}}</Option>
                <Option value="month2">{{formItem.month[1]}}</Option>
                <Option value="month3">{{formItem.month[2]}}</Option>
                <Option value="month4">{{formItem.month[3]}}</Option>
              </Select>
            </FormItem>
          </TimelineItem>
          <TimelineItem color="red">
            <FormItem style="text-align: left" label="预测最低值" prop="min_price">
              ¥&nbsp&nbsp<InputNumber :max="this.latestPrice" :min="0" v-model="formItem.min_price" style="width: 50%"></InputNumber>
              &nbsp&nbsp<span style="font-size: 13px">华夏上证50ETF <span  v-bind:style="{'-webkit-text-fill-color':infoColor}">({{this.latestPrice}}/{{this.rate}}%)</span></span>
            </FormItem>
          </TimelineItem>
        </Timeline>
      </Form>
    </div>
</template>

<script>
    export default {
        name: "hedging_collect_info",
      created:function(){
        this.getDeadLine()
        this.getLatestPrice()
        if(this.rate>0){
          this.infoColor='#ed4014'
        }
        else{
          this.infoColor='#19be6b'
        }
      },
      data(){
        return {
          formItem: {
            rate:25,
            min_price:0,
            OpenInterest: '',
            deadline: '',
            month:''
          },
          latestPrice:'',
          rate:'',
          infoColor:''
        }
      },
      methods:{
          getDeadLine(){
            this.axios.get('/sinaTime/StockOptionService.getStockName')
              .then(re=>{
                var temp=re.data.result.data.contractMonth
                this.formItem.month=[temp[1],temp[2],temp[3],temp[4]]
              })
          },
        getLatestPrice(){
            this.axios.get('/sse/snap/510050')
              .then(re=>{
                this.latestPrice=parseFloat(re.data.snap[5])
                this.rate=parseFloat(re.data.snap[7])
              })
        }
      }
    }
</script>

<style scoped>

</style>
