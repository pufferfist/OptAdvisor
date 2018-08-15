<template>
  <div class="pt4 bt b--black-30">
    <Row class="pb3 tc">
      <Col span="8">
        <h3>站内链接</h3>
        <LinkGroup :items="items1"></LinkGroup>
      </Col>
      <Col span="8">
        <h3>相关</h3>
        <LinkGroup :items="items2"></LinkGroup>
      </Col>
      <Col span="8">
        <h3>关于我们</h3>
        <LinkGroup :items="items3"></LinkGroup>
      </Col>
    </Row>
    <Row class="tc pt3">
      <div>OptAdvisor loves you</div>
    </Row>
  </div>
</template>

<script>
    import LinkGroup from "./LinkGroup";
    export default {
      name: "myFooter",
      data(){
          return{
            items1:[
              {name:"主页",address:"/home"}
            ],
            items2:[
              {name:"新浪期权",address:"http://stock.finance.sina.com.cn/option/quotes.html"}
            ],
            items3:[
              {name:"Github",address:"https://github.com/840326461/OptAdvisor"}
            ],
          }
      },
      components: {LinkGroup}
    }
</script>

<style scoped>

</style>
