<template>
    <div style="width: 100%;text-align: center">
      <table style="margin: auto">
        <tr>
          <th>合约名称</th>
          <td>{{this.TData.optionCode}}</td>
          <th>买入\卖出</th>
          <td>{{this.TData.type}}</td>
        </tr>
        <tr>
          <th>数量(手)</th>
          <td>{{this.TData.iNum}}</td>
          <th>到期时间</th>
          <td>{{this.TData.expireTime}}</td>
        </tr>
        <tr>
          <th>成交价</th>
          <td>{{this.TData.transactionPrice}}</td>
          <th>行权价格</th>
          <td>{{this.TData.k}}</td>
        </tr>
        <tr>
          <th>实时价格</th>
          <td>{{this.TData.realTimePrice}}</td>
        </tr>

      </table>
    </div>
</template>

<script>
    export default {
        name: "table",
      data(){
          return{
            TData:''
        }
         
      }
    }
</script>

<style scoped>
  table{
    border-collapse: collapse;
  }
td{
  padding-left: 5px;
  padding-right: 5px;
  height: 40px;
  font-size: 15px;
  -webkit-text-fill-color: #2baee9;
}
  th{
    padding-left: 5px;
    padding-right: 5px;
    height: 40px;
    font-size: 15px;
    -webkit-text-fill-color: #515a6e;
  }
</style>
