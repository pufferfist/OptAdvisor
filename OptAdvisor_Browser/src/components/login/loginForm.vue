<template>
  <Form ref="formInline" :model="formInline" :rules="ruleInline" v-on:keyup.enter="handleSubmit('formInline')">
    <FormItem prop="user">
      <i-input type="text" v-model="formInline.user" placeholder="用户名" class="mb1">
      <Icon type="ios-person-outline" slot="prepend"></Icon>
      </i-input>
    </FormItem>
    <FormItem prop="password">
      <i-input type="password" v-model="formInline.password" placeholder="密码" class="mb4">
      <Icon type="ios-lock-outline" slot="prepend"></Icon>
      </i-input>
    </FormItem>
    <FormItem class="tl">
      <p>没有注册? <a href="/signUp">注册</a> | <a href="/forgetPassword">忘记密码?</a></p>
    </FormItem>
    <FormItem class="tc">
      <Button id="loginButton" type="primary" @click="handleSubmit('formInline')" class="Button">登录</Button>
    </FormItem>
  </Form>
</template>

<script>
  export default {
    name: "loginForm",
    data () {
      return {
        formInline: {
          user: '',
          password: ''
        },
        ruleInline: {
          user: [
            { required: true, message: '用户名不得为空', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '密码不得为空', trigger: 'blur' }
          ]
        },
      }
    },
    methods: { //待修改
      handleSubmit(name) {
        this.$refs[name].validate((valid) => {
          if (valid) {
            this.$Message.success('Success!');
          } else {
            this.$Message.error('Fail!');
          }
        });


        this.$cookie.set("userName",this.formInline.user);
        this.axios.post('/backend/login',{
          username:this.formInline.user,
          password:this.formInline.password
        })
          .then( (response)=> {
            console.log(response.data.code);
            console.log(response.data.msg);
        })
          .catch(function (error) {
            console.log(error);
          });
      }
    }
  }
</script>

<style scoped>
  .Button {
    padding: 0.5rem 1rem;
    font-size: 1rem !important;
    line-height: 1.5;
    border-radius: 0.3rem;
  }
    p {
    font-size: 14px;
    color: #9f9f9f;
    font-weight: 300;
  }
</style>
