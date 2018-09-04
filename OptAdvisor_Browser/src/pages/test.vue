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
<template>
  <div>
    <Spin v-if="show">
      <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
      <div>Loading</div>
    </Spin>
    <button @click="test">XXX</button>
    <table v-if="show">
      <tr><td>XXXXX</td></tr>
    </table>
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
