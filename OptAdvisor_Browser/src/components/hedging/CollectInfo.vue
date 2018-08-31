<template>
    <div>
      <Form ref="formItem" :model="formItem" :label-width="100">
        <p style="font-size: 25px;font-weight: bold">套期保值信息设置&nbsp &nbsp</p><br><br>
        <Timeline>
          <TimelineItem color="green">
            <FormItem label="持仓量" prop="OpenInterest">
              <Input v-model="formItem.OpenInterest" placeholder="请输入持仓量"></Input>
            </FormItem>
          </TimelineItem>
          <TimelineItem color="green">
            <FormItem label="套保比例" prop="rate">
              <Slider v-model="formItem.rate" show-input></Slider>
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
            <FormItem style="text-align: left" label="预测价格最低值" prop="min_price">
              <InputNumber
                :max="10"
                v-model="formItem.min_price"
                :formatter="value => `¥ ${value}`.replace(/B(?=(d{3})+(?!d))/g, ',')"
                :parser="value => value.replace(/$s?|(,*)/g, '')" style="width: 100%"></InputNumber>
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
      },
      data(){
        return {
          formItem: {
            rate:25,
            min_price:'',
            OpenInterest: '',
            deadline: '',
            month:''
          },
        }
      },
      methods:{
          getDeadLine(){
            this.axios.get('/sinaTime/StockOptionService.getStockName')
              .then(re=>{
                var temp=re.data.result.data.contractMonth
                this.formItem.month=[temp[1],temp[2],temp[3],temp[4]]
              })
          }
      }
    }
</script>

<style scoped>

</style>
