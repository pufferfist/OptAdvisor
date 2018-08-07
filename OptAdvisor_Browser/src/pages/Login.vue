<template>
  <div id="login">
    <Row class="middle">
      <i-col span="8" offset="8">
        <div id="form" class="login-form animated  tc">
          <Logo></Logo>
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
      components: {Logo, LoginForm}
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
    background-image: url("../../static/graph/back1.jpg");
    background-size:1920px 1200px;
    background-repeat:no-repeat;
    height: 100%;
  }

  .login-form {
    padding: 30px;
    -webkit-box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    -moz-box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    -o-box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    box-shadow: -4px 7px 46px 2px rgba(0, 0, 0, 0.1);
    background: #ffffff;
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

  img {
    max-width: 100%;
    max-height: 100%;
  }

</style>
