<template>
    <div style="width: 100%;text-align: center">
      <table style="margin: auto">
        <tr>
          <th>序号</th>
          <th>合约代码</th>
          <th>合约名称</th>
          <th>成本价</th>
          <th>最新价</th>
          <th>成本</th>
          <th>保证金</th>
          <th>市值</th>
          <th>盈亏</th>
        </tr>
        <tr v-for="(item,index) in tdata">
          <td>{{index+1}}</td>
          <td v-for="i in item">{{i}}</td>
        </tr>
      </table>
    </div>
</template>

<script>
    export default {
        name: "table",
      data(){
          return{
            tdata:[
              [123,123,123,123,123,123,123,123],
              [123,123,123,123,123,123,123,123],
              [123,123,123,123,123,123,123,123],
              [123,123,123,123,123,123,123,123],
              [123,123,123,123,123,123,123,123],
            ]
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
  background-color: #ffeeee;
}
  th{
    padding-left: 5px;
    padding-right: 5px;
    background-color: #f16643;
    -webkit-text-fill-color: #ffffff;
  }
</style>
