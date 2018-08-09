<template>
  <div>
    <NavBar class="nav"></NavBar>
    <router-view class="child"></router-view>
  </div>
</template>

<script>
    import NavBar from "../components/util/NavBar";
    export default {
        name: "FatherPage",
      components: {NavBar}
    }
</script>

<style scoped>
  .nav{
    width:100%;
    height:60px;
    position:fixed;
    top:0;left:0;
  }
  .child{
    margin-top: 60px;
    padding-top: 20px;
  }
</style>
