<template>
  <div>
    <NavBar></NavBar>
    <router-view></router-view>
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

</style>
