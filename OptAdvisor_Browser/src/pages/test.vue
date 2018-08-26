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
        this.axios.get('/sinaOption/list=CON_SO_10001407')
          .then(re=>{
            var parts=re.data.substr(re.data.indexOf("=")+2).split(",")

          })
      }
    }
  };
</script>
<style>

</style>
