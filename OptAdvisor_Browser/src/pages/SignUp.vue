<template>
  <div id="main_part">
    <div id="fill_part" v-bind:style="{position:PO}">
      <logo></logo><br>
      <div style="text-align: center">
        <Steps :current="current">
          <Step title="完善信息"></Step>
          <Step title="填写问卷"></Step>
          <Step title="完成注册"></Step>
        </Steps><br>
      </div>

      <div id="page1" v-bind:style="{display:ispage01}">
        <register01 ref="information"></register01>
      </div>
      <div id="page2" v-bind:style="{display:ispage02}">
        <register02 ref="questions"></register02>
      </div>
      <div id="page3" v-bind:style="{display:ispage03}">
        <register03 ref="results"></register03>
      </div>

      <div style="text-align: center" v-bind:style="{display:isLastPage}">
        <Button type="primary" @click="next">下一步</Button>
      </div>

      <button @click="handleSubmit(1,2)">CC</button>
     </div>

   </div>



</template>

<script>
  //import "../../static/font-awesome-4.7.0/css/font-awesome.css";
  import Logo from "../components/util/Logo";
  import register01 from "../components/signUp/register_page01";
  import register02 from "../components/signUp/register_page02";
  import register03 from "../components/signUp/register_page03";
    export default {
        name: "SignUp",
      components:{Logo,register01,register02,register03},
      data () {
        return {
          current: 0,
          ispage01:"",
          ispage02:"none",
          ispage03:"none",
          isLastPage:"",
          PO:"absolute"
        }
      },
      methods: {
        next () {
          if(this.current==0){
            this.$refs.information.$refs.formValidate.validate((valid) => {
              if (valid) {
                //提交信息
                //换页
                this.ispage01="none"
                this.ispage02=""
                this.PO="relative"
                this.current +=1
              } else {
                this.$Message.error('没有按要求填写信息');
              }
            })
          }
          else if(this.current==1){
            this.$refs.questions.$refs.formValidate.validate((valid) => {
              if (valid) {
                //计算
                var points=this.calculateMarks();
                //提交
                if(points<=10){
                  this.handleSubmit()
                  this.$refs.results.type="C1（安逸型）"
                  this.$refs.results.text1="在投资时会以降低风险承受度为前提，风险承受能力低，对收益要求不高，但追求资金本金的绝对安全。预期报酬率优于中长期存款利率，并确保本金在通货膨胀中发挥保值功能。"
                  this.$refs.results.text2="R1（低风险）"
                }
                else if(points<=20){
                  this.$refs.results.type="C2（保守型）"
                  this.$refs.results.text1="风险承受能力较低，能容忍一定程度的本金损失，止损意识强。资产配置以低风险品种为主，少量参与股票投资。"
                  this.$refs.results.text2="R2（中低风险）"
                }
                else if(points<=30){
                  this.$refs.results.type="C3（稳健型）"
                  this.$refs.results.text1="从总体投资来看，在风险较小的情况下获得一定的收益是您主要的投资目的。您通常愿意使本金面临一定的风险，但在做投资决定时，对风险总是客观存在的道理有清楚的认识，会仔细地对将要面临的风险进行认真的分析。总体来看，愿意承受市场的平均风险。"
                  this.$refs.results.text2="R3（中等风险）"
                }
                else if(points<=40){
                  this.$refs.results.type="C4（积极型）"
                  this.$refs.results.text1="偏向于激进的资产配置，对风险有较高的承受能力，投资收益预期相对较高，资产配置以股票等高风险的品种为主，资产市直波动相对较大，除获取资本利得之外，也寻求投资差价收益。"
                  this.$refs.results.text2="R4（中高风险）"
                }
                else if(points<=50){
                  this.$refs.results.type="C5（激进型）"
                  this.$refs.results.text1="具有较强的主观风险承受意愿。并试图尝试较高收益的投资，乐意承担一定的风险，希望获得比一般人更高的收益。"
                  this.$refs.results.text2="R5（高风险）"
                }
                //设置
                this.ispage02="none";
                this.ispage03=""

                this.current+=1
                this.PO="absolute"
                this.isLastPage="none"
              } else {
                this.$Message.error('问卷题目未答全');
              }
            })

          }
          else {
            this.current += 1;
          }
        },
        handleReset () {
          this.$refs.information.$refs.formValidate.resetFields();
        },
        calculateMarks(){
          var total=0;
          var answers=this.$refs.questions.$refs.formValidate.$data.fields;

          if(answers[0].fieldValue=="A"){
            total+=8
          }
          else if(answers[0].fieldValue=="B"){
            total+=7
          }
          else if(answers[0].fieldValue=="C"){
            total+=4
          }
          else if(answers[0].fieldValue=="D"){
            total+=6
          }
          else if(answers[0].fieldValue=="E"){
            total+=1
          }

          if(answers[1].fieldValue=="A"){
            total+=1
          }
          else if(answers[1].fieldValue=="B"){
            total+=2
          }
          else if(answers[1].fieldValue=="C"){
            total+=4
          }
          else if(answers[1].fieldValue=="D"){
            total+=6
          }
          else if(answers[1].fieldValue=="E"){
            total+=8
          }

          if(answers[2].fieldValue=="A"){
            total+=8
          }
          else if(answers[2].fieldValue=="B"){
            total+=3
          }
          else if(answers[2].fieldValue=="C"){
            total+=5
          }
          else if(answers[2].fieldValue=="D"){
            total+=2
          }

          if(answers[3].fieldValue=="A"){
            total+=3
          }
          else if(answers[3].fieldValue=="B"){
            total+=6
          }
          else if(answers[3].fieldValue=="C"){
            total+=8
          }

          if(answers[4].fieldValue=="A"){
            total+=8
          }
          else if(answers[4].fieldValue=="B"){
            total+=6
          }
          else if(answers[4].fieldValue=="C"){
            total+=4
          }
          else if(answers[4].fieldValue=="D"){
            total+=2
          }

          if(answers[5].fieldValue=="A"){
            total+=1
          }
          else if(answers[5].fieldValue=="B"){
            total+=2
          }
          else if(answers[5].fieldValue=="C"){
            total+=4
          }
          else if(answers[5].fieldValue=="D"){
            total+=6
          }
          else if(answers[5].fieldValue=="E"){
            total+=10
          }

          return total;
        },
        handleSubmit(w1,w2){
          var array=this.$refs.information.$refs.formValidate.fields
          var name=array[0].fieldValue
          var gender=array[1].fieldValue
          var username=array[2].fieldValue
          var password=array[3].fieldValue
          var passcheck=array[4].fieldValue
          var birth=array[5].fieldValue
          var email=array[6].fieldValue
          var phone=array[7].fieldValue
          // //console.log(name,gender,username,password,passcheck,birth,email,phone )
          //user={}
          // user.username=username
          // user.password=password
          // user.name=name
          // user.birthday=birth
          // user.telephone=phone
          // user.email=email
          // user.gender=gender
          // user.avatarPath=""
          // user.w1=w1
          // user.w2=w2
          var user={}
          user.username=username
          user.password=password
          user.name=name
          user.birthday=birth
          user.telephone=phone
          user.email=email
          user.gender=gender
          user.avatarPath=""
          user.w1=w1
          user.w2=w2
          $.ajax({
            type: "POST",
            url: "/backend/signUp",
            contentType:"application/json",
            data: user,
            success:function(data){
              alert("success")
            },
            error:function (data) {
              alert("error")
            }
          })

          // this.axios.post('http://localhost:8088/signUp', {
          //   name:array[0].fieldValue,
          //   gender:array[1].fieldValue,
          //   username:array[2].fieldValue,
          //   password:array[3].fieldValue,
          //   passcheck:array[4].fieldValue,
          //   birth:array[5].fieldValue,
          //   email:array[6].fieldValue,
          //   phone:array[7].fieldValue,
          //   avatarPath:"",
          //   w1:w1,
          //   w2:w2,
          // }).then((res)=>{
          //   console.log(res.data.code)
          // })

        }
      }

    }
</script>

<style scoped>
  #main_part{
    background-image: url("../../static/graph/back1.jpg");
    background-repeat:no-repeat;
    min-height: 100%;
  }
  #fill_part{
    position: absolute;
    width: 60%;
    min-height: 100%;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    margin: auto;
    padding: 30px;
    -webkit-box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    -moz-box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    -o-box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    background: #ffffff;
  }
  #page1{
    height: 500px;
  }
</style>
