<template>
  <div>
    <button @click="getCode">XX</button>
  </div>
</template>

<script>
  export default {
    name:'test',
    data(){
      return {
        show: true,
        count: '',
        timer: null,
      }
    },
    methods:{
      getCode(){
        alert(parseInt("0")+5)
      }
    }
  };
</script>
<style>

</style>
