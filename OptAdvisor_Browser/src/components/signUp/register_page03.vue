<template>
    <div>
      <div style="font-size: 20px;margin-top: 50px">
        <div style="width: 70%;float: left">
          <p>&nbsp &nbsp 尊敬的投资者：</p>
          <p>&nbsp &nbsp 根据您填写的《投资者风险承受能力问卷》，本系统对您的风险能力进行了综合评估，现得到评估结果如下：</p>
          <P>&nbsp &nbsp 您的风险承受能力等级为：</P>
          <p style="-webkit-text-fill-color: #5cadff">&nbsp &nbsp {{type}}</p>
          <p>&nbsp &nbsp {{text1}}</p>
          <p>&nbsp &nbsp 该风险承受能力等级与</p>
          <p style="-webkit-text-fill-color: #5cadff">&nbsp &nbsp {{text2}}及以下风险等级类的产品或服务相匹配。</p>
        </div>
        <div style="width: 30%;float: left;margin-top: 50px">
          <img src="../../../static/graph/register.jpg" style="width: 100%"/>
        </div>
      </div>
      <div style="text-align: center">
        <br>
        <p style="-webkit-text-fill-color: #19be6b;font-size: 20px">&nbsp &nbsp 您已成功注册 <Button type="success" @click="login" style="font-size: 20px;-webkit-text-fill-color: #ffffff">快速进入Optadvisor</Button></p>
      </div>
    </div>
</template>

<script>
    export default {
        name: "register_page03",
      data(){
          return{
            type:"",
            text1:'',
            text2:'',
            username:'',
            password:''
          }
      },
      methods:{
          login(){
            this.$cookie.set("userName",this.username,"1d");
            this.$router.push("/home")
          }
      }
    }
</script>

<style scoped>

</style>
