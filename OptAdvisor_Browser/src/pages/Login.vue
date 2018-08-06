<template>
  <div id="login">
    <login-form></login-form>
  </div>
</template>

<script>
    import LoginForm from "../components/login/loginForm";
    export default {
        name: "Login",
      components: {LoginForm}
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
    background-image: url("../assets/loginBack.jpg");
  }

</style>
