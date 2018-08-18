<template>
    <div>
      <div>
        <div>
          <h1>{{name}}
            <span style="font-size: 15px">
          创建时间：{{time}} &nbsp&nbsp 分组类型：{{type}} &nbsp&nbsp 期望收益：{{earnings}} &nbsp&nbsp&nbsp
        </span>
            <Button type="primary">添加跟踪</Button>
          </h1>
        </div>
      </div>

      <groupTable></groupTable>

      <display_result></display_result>

    </div>
</template>

<script>
  import groupTable from "../myPortfolio/GroupTable"
  import display_result from "../myPortfolio/displayResult";
    export default {
        name: "Option",
      components:{display_result, groupTable},
      data () {
        return {
          name:'我的组合',
          time:'2018-09-09',
          type:'diy',
          earnings:'5230',
        }
      },

    }
</script>

<style scoped>

</style>
