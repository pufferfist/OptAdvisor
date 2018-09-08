<template>
  <div id="login">
    <Row class="middle">
      <i-col span="8" offset="8">
        <div id="form" class="login-form animated fadeInLeft tc">
          <div style="height:100px; width:500px;">
            <img style="width: 300px;height:70px;" src="../../static/graph/OptLogo.png" alt="">
          </div>
          <login-form class="pt3-l pl4 pr4"></login-form>
        </div>
      </i-col>
    </Row>
  </div>
</template>

<script>
    import LoginForm from "../components/login/loginForm";
    import Logo from "../components/util/Logo";
    export default {
        name: "Login",
      components: {Logo, LoginForm},
      mounted:function () {
        this.axios.post("backend/auth")
          .then(res=>{
            if(res.data.code===0){
              this.$router.push("/50ETF");
            }
          })
      }
    }
</script>

<style scoped>
  .blur{
    filter:blur(50px);
    -webkit-filter:blur(50px);
    -moz-filter:blur(50px);
    -ms-filter:blur(50px);
    -o-filter:blur(50px);
  }

  #login{
    background-image: url("../../static/graph/homeBack/10.jpg");
    background-size:1920px 1200px;
    background-repeat:no-repeat;
    height: 1080px;
  }

  .login-form {
    padding: 30px;
    -webkit-box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    -moz-box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    -o-box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    background: #eeeeeebb;
    position: fixed;
    border-radius: 5%;
  }

  #login {position: relative;}

  .middle {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    width: 100%;
    height: 50%;
    margin: auto;
  }

</style>
