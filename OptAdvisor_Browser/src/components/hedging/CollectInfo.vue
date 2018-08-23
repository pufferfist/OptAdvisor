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
            // var date=new Date();
            // //今天是几号
            // var d=date.getDate();
            // //今天是星期几,0为星期日
            // var week=date.getDay()
            // //当前是几月,getmonth方法的月份是从0开始取的
            // var month=date.getMonth()
            //
            // //判断是否是当月的第几个星期三
            // var wednesdays=d/7
            // var remainder=d%7
            // var temp=week
            // for(var i=0;i<remainder;i++){
            //   if(temp==3){
            //     wednesdays++
            //     break
            //   }
            //   temp--
            //   if(temp<0){
            //     temp+=7
            //   }
            // }
            //
            // var month1
            // var month2
            // var month3
            // var month4
            // //若是第四个星期三及之前
            // if((wednesdays<4)||((wednesdays==4)&&(week==3))){
            //   month1=month+1
            //   month2=(month+1)%12+1
            // }
            // else{
            //   month1=(month+1)%12+1
            //   month2=(month+2)%12+1
            // }
            //
            // if(month2<3){
            //   month3=3
            //   month4=6
            // }
            // else if(month2<6){
            //   month3=6
            //   month4=9
            // }
            // else if(month2<9){
            //   month3=9
            //   month4=12
            // }
            // else if(month2<12){
            //   month3=12
            //   month4=3
            // }
            // else{
            //   month3=3
            //   month4=6
            // }
            //
            // var str1
            // var str2
            // var str3
            // var str4
            // var year=date.getFullYear();
            // if((month1>=date.getMonth()+1)&&(month1<=12)){
            //   str1=year+"-"+month1;
            //   if((month2>month1)&&(month2<=12)){
            //     str2=year+"-"+month2;
            //     if((month3>month2)&&(month3<=12)){
            //       str3=year+"-"+month3;
            //       if((month4>month3)&&(month4<=12)){
            //         str4=year+"-"+month4;
            //       }
            //       else{
            //         str4=(year+1)+"-"+month4
            //       }
            //     }
            //     else{
            //       str3=(year+1)+"-"+month3;
            //       str4=(year+1)+"-"+month4;
            //     }
            //   }
            //   else{
            //     str2=(year+1)+"-"+month2;
            //     str2=(year+1)+"-"+month3;
            //     str4=(year+1)+"-"+month4
            //   }
            // }
            // else{
            //   str1=(year+1)+"-"+month1;
            //   str2=(year+1)+"-"+month2;
            //   str3=(year+1)+"-"+month3;
            //   str4=(year+1)+"-"+month4;
            // }
            //
            //
            // return [str1,str2,str3,str4]
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
