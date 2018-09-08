<template>
  <div id="forget" style="padding-top: 100px;padding-left: 25%">
    <Modal v-model="dialogue" width="360">
      <p slot="header" style="text-align:center" v-bind:style="{color:title_color}">
        <Icon type="ios-information-circle"></Icon>
        <span>{{change_result}}</span>
      </p>
      <div style="text-align:center">
        <p>{{alarm}}</p>
      </div>
      <div slot="footer">
        <Button :type="this.type" size="large" long @click="handleResult">确认</Button>
      </div>
    </Modal>
    <div id="form">
      <Logo style="margin-bottom: 20px"></Logo>
      <p>请您根据提示按步骤找回密码</p><br>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80" id="main">
        <FormItem label="用户名" prop="username">
          <Input v-model="formValidate.username" placeholder="请输入用户名"></Input>
        </FormItem>
        <FormItem label="新密码" prop="passwd">
          <Input type="password" v-model="formValidate.passwd" placeholder="请输入新密码"></Input>
        </FormItem>
        <FormItem label="确认密码" prop="passwdCheck">
          <Input type="password" v-model="formValidate.passwdCheck" placeholder="请在此输入新密码"></Input>
        </FormItem>
        <FormItem label="验证码" prop="code">
          <Input v-model="formValidate.code" placeholder="请输入验证码" style="width: 40%;float: left"></Input>
          <Button v-show="show" @click="getVerification">{{text}}</Button>
          <Button v-show="!show" class="count" disabled>{{count}} s后重新获取</Button>
        </FormItem>
        <FormItem :label-width="0" v-bind:style="{display:this.correct}">
          <Button @click="checkVerification('formValidate')" type="primary" style="width: 80%">修改密码</Button>
        </FormItem>
      </Form>
    </div>
  </div>
</template>

<script>
  import Logo from "../components/util/Logo";
    export default {
        name: "ForgetPassword",
      components:{Logo},
      data () {
        const validatePass = (rule, value, callback) => {
          if ((value === '')||(value==null)) {
            callback(new Error('请输入新密码'));
          } else {
            if (this.formValidate.passwdCheck !== '') {
              // 对第二个密码框单独验证
              this.$refs.formValidate.validateField('passwdCheck');
            }
            callback();
          }
        };
        const validatePassCheck = (rule, value, callback) => {
          if ((value === '')||(value==null)) {
            callback(new Error('请再次输入新密码'));
          } else if (value !== this.formValidate.passwd) {
            callback(new Error('两次输入的密码不一致'));
          } else {
            callback();
          }
        };
        return {
          formValidate: {
            username:'',
            code:''
          },
          ruleValidate: {
            username: [
              { required: true, message: '用户名不能为空', trigger: 'blur' }
            ],
            passwd: [
              { required: true,validator: validatePass, trigger: 'blur' }
            ],
            passwdCheck: [
              { required: true,validator: validatePassCheck, trigger: 'blur' }
            ],
            code: [
              { required: true, message: '请填写验证码', trigger: 'blur' }
            ]
          },
          show:true,
          count:'',
          timer:null,
          text:'获取验证码',
          correct:'none',
          passwd: '',
          passwdCheck: '',
          dialogue:false,
          change_result:'',
          alarm:'',
          title_color:'',
          type:''
        }
      },
      beforeCreate:function () {
        this.axios.post("backend/auth")
          .then((res)=>{
            if(res.data.code===1008){
              this.$router.push("/login");
            }
          });
      },
      methods: {
        getVerification(){
          if(this.username==''){
            this.$Message.error('用户名不得为空')
          }
          else{
            this.axios.post('/backend/sendVerifyCode',{username:this.formValidate.username})
            var seconds=60
            if (!this.timer) {
              this.count = seconds;
              this.show = false;
              this.correct=''
              this.timer = setInterval(() => {
                if (this.count > 0 && this.count <= seconds) {
                  this.count--;
                } else {
                  this.show = true;
                  clearInterval(this.timer);
                  this.timer = null;
                  this.text='重获验证码'
                }
              }, 1000)
            }
          }
        },
        checkVerification(name){
          this.$refs[name].validate((valid) => {
            if (valid) {
              this.axios.post('/backend/checkVerifyCode',{verifyCode:this.formValidate.code,newPassword:this.formValidate.passwd})
                .then(re=>{
                  if(re.data.msg=='Check verify code and set new password success'){
                    this.title_color='#19be6b'
                    this.type='success'
                    this.change_result='修改成功'
                    this.alarm='下面将跳转至登陆界面，请您重新登陆'
                    this.dialogue=true
                  }
                  else if(re.data.msg=='Never send verify code or verify code has expired'){
                    this.title_color='#ff9900'
                    this.type='warning'
                    this.change_result='修改失败'
                    this.alarm='您填写的验证码已过期，请您重新获取并修改您的密码'
                    this.dialogue=true
                  }
                  else if(re.data.msg=='Wrong verify code'){
                    this.title_color='#ff9900'
                    this.type='warning'
                    this.change_result='修改失败'
                    this.alarm='您填写的验证码有误，请您重新填写验证码'
                    this.dialogue=true
                  }
                  else if(re.data.msg=='Unknown username'){
                    this.title_color='#ff9900'
                    this.type='warning'
                    this.change_result='修改失败'
                    this.alarm='您的用户名有误，请您重新填写'
                    this.dialogue=true
                  }
                  else{
                    this.title_color='#ff9900'
                    this.type='warning'
                    this.change_result='修改失败'
                    this.alarm='由于某些未知的原因，您并未成功修改密码，请您重新获取验证码再次修改'
                    this.dialogue=true
                  }
                })
            } else {
              this.$Message.error('信息填写不完整');
            }
          })
        },
        handleResult(){
          if(this.type=='success'){
            this.$router.push('/login')
          }
          else{
            this.dialogue=false
          }
        }
      }
    }
</script>

<style scoped>
#forget{
  background-image: url("../../static/graph/back1.jpg");
  background-size:1920px 1200px;
  background-repeat:no-repeat;
  height: 1080px;
}
  #form{
    width: 50%;
    min-height: 500px;
    text-align: center;
    padding: 30px;
    -webkit-box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    -moz-box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    -o-box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    background: #ffffff;
    position: fixed;
  }
  #main{
    padding-left: 25%;
    padding-right: 25%;
    vertical-align: center;
  }
</style>
