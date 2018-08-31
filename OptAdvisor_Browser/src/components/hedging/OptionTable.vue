<template>
    <div style="width: 100%;text-align: center">
      <table style="margin: auto">
        <tr>
          <th>期权代码</th>
          <td>{{this.TData.optionCode}}</td>
          <th>买入\卖出</th>
          <td>{{this.TData.type}}</td>
          <th>看涨\看跌</th>
          <td>{{this.TData.cp}}</td>
          <th>到期时间</th>
          <td>{{this.TData.expireTime}}</td>
        </tr>
        <tr>
          <th>成交价</th>
          <td>{{this.TData.transactionPrice}}</td>
          <th>前一天收盘价</th>
          <td>{{this.TData.yclose}}</td>
          <th>实时买入价格</th>
          <td>{{this.TData.price1}}</td>
          <th>实时卖出价格</th>
          <td>{{this.TData.price2}}</td>
        </tr>
        <tr>
          <th>行权价格</th>
          <td>{{this.TData.k}}</td>
          <th>实时价格</th>
          <td>{{this.TData.realTimePrice}}</td>
          <th>delta</th>
          <td>{{this.TData.delta}}</td>
          <th>gamma</th>
          <td>{{this.TData.gamma}}</td>
        </tr>
        <tr>
          <th>vega</th>
          <td>{{this.TData.vega}}</td>
          <th>theta</th>
          <td>{{this.TData.theta}}</td>
          <th>rho</th>
          <td>{{this.TData.rho}}</td>
          <th>beta</th>
          <td>{{this.TData.beta}}</td>
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
  font-size: 13px;
  -webkit-text-fill-color: #2baee9;
  background-color: #f8f8f9;
}
  th{
    padding-left: 5px;
    padding-right: 5px;
    font-size: 13px;
    background-color: #f8f8f9;
    -webkit-text-fill-color: #515a6e;
  }
</style>
