<template>
    <div>
      <p style="font-size: 30px;font-weight: bold;text-align: left">DIY</p>
      <h3 style="text-align: left;-webkit-text-fill-color: #2b85e4">构建DIY组合</h3>
      <div style="width: 100%;height: 2px;background-color: #2b85e4"></div>
      <myGroup></myGroup>
      <br>
      <h3 style="text-align: left;-webkit-text-fill-color: #2b85e4">合约分析</h3>
      <div style="width: 100%;height: 2px;background-color: #2b85e4"></div>
      <myAnalysis></myAnalysis>

    </div>
</template>

<script>
  import myGroup from "../components/diy/select"
  import myAnalysis from "../components/myPortfolio/displayResult"
    export default {
        name: "diy",
      components:{myGroup,myAnalysis}
    }
</script>

<style scoped>

</style>
