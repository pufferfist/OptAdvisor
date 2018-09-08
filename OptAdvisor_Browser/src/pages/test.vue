<template>
  <div>
    <input v-model="data[1]" type="text"/>
    <button @click="test">XX</button>
  </div>
</template>

<script>
  export default {
    name:'test',
    data(){
      return{
        data:[0,1]
      }
    },
    methods:{
      test(){
        alert(this.data[1])
      }
    }
  };
</script>
<style>
  .demo-spin-icon-load{
    animation: ani-demo-spin 1s linear infinite;
  }
  @keyframes ani-demo-spin {
    from { transform: rotate(0deg);}
    50%  { transform: rotate(180deg);}
    to   { transform: rotate(360deg);}
  }
</style>
