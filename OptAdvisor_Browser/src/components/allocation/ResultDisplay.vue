<template>
  <Row>
    <Col span="12" class="middleBorder">
      <h2>组合内容</h2>
      <CellGroup>
        <Cell v-for="item in optionList" :title="item.name" :extra="item.number>0?'买'+item.number:'卖'+(-item.number)" :key="item.name"/>
      </CellGroup>
    </Col>
    <Col span="12" class="">
      <h2>组合评价</h2>
    </Col>
  </Row>
</template>

<script>
    export default {
      name: "ResultDisplay",
      data() {
        return {
          optionList: [
            {
              name: "50ETF购8月2600",
              number: "2"
            },
            {
              name: "50ETF沽9月2700",
              number: "-1"
            }
          ]
        }
      }
    }
</script>

<style scoped>
  .middleBorder{
    border-right: 1px solid rgba( 0, 0, 0, .3 );
  }
</style>
