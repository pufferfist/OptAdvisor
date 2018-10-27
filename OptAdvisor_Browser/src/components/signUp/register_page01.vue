<template>
  <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
    <div style="width: 100%">
      <div style="width: 80%;float: left">
        <FormItem label="姓名" prop="name">
          <Input v-model="formValidate.name" placeholder="请填写个人身份证姓名"></Input>
        </FormItem>
        <FormItem label="性别" prop="gender">
          <RadioGroup v-model="formValidate.gender">
            <Radio label="male" >男性</Radio>
            <Radio label="female">女性</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="用户名" prop="username">
          <Input v-model="formValidate.username" placeholder="请输入您的用户名"></Input>
        </FormItem>
        <FormItem label="输入密码" prop="passwd">
          <Input type="password" v-model="formValidate.passwd" placeholder="请键入您的密码"></Input>
        </FormItem>
        <FormItem label="确认密码" prop="passwdCheck">
          <Input type="password" v-model="formValidate.passwdCheck" placeholder="请再次键入确认您的密码"></Input>
        </FormItem>
        <FormItem label="出生日期" prop="date">
          <DatePicker type="date" placeholder="Select date" v-model="formValidate.date"></DatePicker>
        </FormItem>
        <FormItem label="常用邮箱" prop="mail">
          <Input v-model="formValidate.mail" placeholder="请填写您的常用邮箱"></Input>
        </FormItem>
        <FormItem label="联系电话" prop="telephone">
          <Input v-model="formValidate.telephone" placeholder="请填写您的常用电话"></Input>
        </FormItem>
      </div>
      <div style="width: 20%;float: left;text-align: center">
          <icon type="ios-contact" size="150"></icon>
      </div>
    </div>
  </Form>

</template>

<script>
    export default {
        name: "register",
      data () {
        const validatePass = (rule, value, callback) => {
          if (value === '') {
            callback(new Error('请输入您的密码'));
          } else {
            if (this.formValidate.passwdCheck !== '') {
              // 对第二个密码框单独验证
              this.$refs.formValidate.validateField('passwdCheck');
            }
            callback();
          }
        };
        const validatePassCheck = (rule, value, callback) => {
          if (value === '') {
            callback(new Error('请在此输入您的密码'));
          } else if (value !== this.formValidate.passwd) {
            callback(new Error('两次输入的密码不一致！'));
          } else {
            callback();
          }
        };
        const validateUsername = (rule, value, callback)=>{
          if(value===''){
            callback(new Error('用户名不能为空'));
          }
          else{
            this.axios.post('/backend/isUsernameUsed',{
              username:this.formValidate.username
            }).then(function (response) {
              if(response.data.data==true){
                callback(new Error("用户名已存在，请修改用户名"))
              }
              else{
                callback();
              }
            })
          }
        }
        return {
          formValidate: {
            isman:true,
            iswoman:false,
            username:'',
            passwd: '',
            passwdCheck: '',
            name: '',
            mail: '',
            gender: '',
            date: '',
          },
          ruleValidate: {
            username: [
              { required: true, validator:validateUsername, trigger: 'blur' }
            ],
            passwd: [
              { required: true,validator: validatePass, trigger: 'blur' }
            ],
            passwdCheck: [
              { required: true,validator: validatePassCheck, trigger: 'blur' }
            ],
            name: [
              { required: true, message: '真实姓名不能为空', trigger: 'blur' }
            ],
            mail: [
              { required: true, message: '常用邮箱不能为空', trigger: 'blur' },
              { type: 'email', message: '不正确的邮箱格式', trigger: 'blur' }
            ],
            telephone:[
              {required:true, message:'联系电话不能为空', trigger:'blur'}
            ],
            gender: [
              { required: true, message: '请选择用户性别', trigger: 'change' }
            ],
            date: [
              { required: true, type: 'date', message: '请选择出生日期', trigger: 'change' }
            ],
          }
        }
      }
          }
</script>

<style scoped>
</style>
