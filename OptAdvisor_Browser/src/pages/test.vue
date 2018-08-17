<template>
  <p id="test">A</p>
</template>

<script>
  export default {
    name:'test',
    mounted() {
      this.getElevatorList();
      setInterval(this.getElevatorList, 5000);
    },
    methods:{
      getElevatorList(){
        document.getElementById("test").innerHTML+=1;
      }
    }
  };
</script>
<style>
</style>
