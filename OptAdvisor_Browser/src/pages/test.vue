<template>
  <div>
    <Spin v-if="show">
      <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
      <div>请您稍后</div>
    </Spin>
    <button @click="test">XXX</button>
  </div>
</template>

<script>
  export default {
    name:'test',
    data(){
      show:false
    },
    methods:{
      test(){
        this.show=true
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
