<template>
    <Row>
      <Col span="10" class="middleBorder mh4">
        <Row>
          <h1>资产配置</h1>
        </Row>
        <Row class="verticalCenterFather nineGrid center">
          <NineGrid class="verticalCenterChild" v-on:onGrid="handleOnGrid"></NineGrid>
        </Row>
      </Col>
      <Col span="12">
        <AlForm :price="price" :volatility="volatility"></AlForm>
      </Col>
    </Row>
</template>

<script>
    import NineGrid from "../components/allocation/infoCollect/NineGrid";
    import AlForm from "../components/allocation/infoCollect/AlForm";
    export default {
        name: "Allocation",
      data(){
        return {
          price:1,
          volatility:1
        }
      },
      components: {AlForm, NineGrid},
      methods:{
          handleOnGrid:function (event) {
            this.price=event.price;
            this.volatility=event.volatility;
          }
      }
    }
</script>

<style scoped>
  .nineGrid{
    height: 600px;
  }
  .middleBorder{
    border-right: 1px solid rgba( 0, 0, 0, .3 );
  }
</style>
