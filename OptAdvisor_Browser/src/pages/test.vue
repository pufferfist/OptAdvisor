<style>
  .demo-spin-icon-load{
    animation: ani-demo-spin 1s linear infinite;
  }
  @keyframes ani-demo-spin {
    from { transform: rotate(0deg);}
    50%  { transform: rotate(180deg);}
    to   { transform: rotate(360deg);}
  }
  .demo-spin-container{
    display: inline-block;
    width: 100%;
    height: 500px;
    position: relative;
    border: 1px solid #eee;
  }

</style>
<template>
  <div class="demo-spin-container">
    <Spin v-if="show" fix>
      <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
      <div>Loading</div>
    </Spin>
  </div>

</template>
<script>
  // 部分样式代码冗长，未作展示
  export default {
    name:'test',
    data(){
      return{
        show:false
      }
    },
    methods:{
      test(){
        this.show=true
      }
    }
  }
</script>
