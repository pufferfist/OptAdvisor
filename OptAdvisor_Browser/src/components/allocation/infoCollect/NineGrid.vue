<template>
  <div class="font0" @click="onGrid">
    <Row>
      <Col span="6" class="box">
        <div class="out">
          <span class="up">波动率</span>
          <span class="down">价格</span>
        </div>
      </Col>
      <Col span="6" class="box">
        <span>上涨</span>
      </Col>
      <Col span="6" class="box">
        <span>中性</span>
      </Col>
      <Col span="6" class="box">
        <span>下跌</span>
      </Col>
    </Row>
    <Row>
      <Col span="6" class="boxLeft">
        <span>增加</span>
      </Col>
      <Col class="flex">
        <div class="outGrid verticalCenterFather">
          <span class="verticalCenterChild">
            <div class="grid center" v-bind:class="{selected:volatility===1&&price===1}" v-on:click="volatility=1;price=1"></div>
          </span>
        </div>
        <div class="outGrid verticalCenterFather">
          <span class="verticalCenterChild">
            <div class="grid center" v-bind:class="{selected:volatility===1&&price===0}" v-on:click="volatility=1;price=0"></div>
          </span>
        </div>
        <div class="outGrid verticalCenterFather">
          <span class="verticalCenterChild">
            <div class="grid center" v-bind:class="{selected:volatility===1&&price===-1}" v-on:click="volatility=1;price=-1"></div>
          </span>
        </div>
      </Col>
    </Row>
    <Row>
      <Col span="6" class="boxLeft">
        <span>中性</span>
      </Col>
      <Col class="flex">
        <div class="outGrid verticalCenterFather">
          <span class="verticalCenterChild">
            <div class="grid center" v-bind:class="{selected:volatility===0&&price===1}" v-on:click="volatility=0;price=1"></div>
          </span>
        </div>
        <div class="outGrid verticalCenterFather">
          <span class="verticalCenterChild">
            <div class="middleGrid center" v-bind:class="{selected:volatility===0&&price===0}" v-on:click="middle"></div>
          </span>
        </div>
        <div class="outGrid verticalCenterFather">
          <span class="verticalCenterChild">
            <div class="grid center" v-bind:class="{selected:volatility===0&&price===-1}" v-on:click="volatility=0;price=-1"></div>
          </span>
        </div>
      </Col>
    </Row>
    <Row>
      <Col span="6" class="boxLeft">
        <span>降低</span>
      </Col>
      <Col class="flex">
        <div class="outGrid verticalCenterFather">
          <span class="verticalCenterChild">
            <div class="grid center" v-bind:class="{selected:volatility===-1&&price===1}" v-on:click="volatility=-1;price=1"></div>
          </span>
        </div>
        <div class="outGrid verticalCenterFather">
          <span class="verticalCenterChild">
            <div class="grid center" v-bind:class="{selected:volatility===-1&&price===0}" v-on:click="volatility=-1;price=0"></div>
          </span>
        </div>
        <div class="outGrid verticalCenterFather">
          <span class="verticalCenterChild">
            <div class="grid center" v-bind:class="{selected:volatility===-1&&price===-1}" v-on:click="volatility=-1;price=-1"></div>
          </span>
        </div>
      </Col>
    </Row>
    <Row class="pt5 tl">
      <span class="db" style="font-size: 20px">
        您的选择
      </span>
      <span class="db pv3 bigFont">
        波动率:
        <span v-bind:class="{orange:volatility===1,blue:volatility===-1}" class="bigFont">
          {{volatility===1?'增加':(volatility===0?'中性':'降低')}}
        </span>
      </span>
      <span style="font-size: 20px" class="bigFont">
        价格:
        <span v-bind:class="{red:price===1,green:price===-1}" class="bigFont">
          {{price===1?'上涨':(price===0?'中性':'下跌')}}
        </span>
      </span>
    </Row>
  </div>
</template>

<script>
  export default {
    name: "NineGrid",
    data() {
      return {
        volatility: 1,
        price: 1
      }
    },
    methods: {
      onGrid: function () {
        this.$emit('onGrid', {volatility: this.volatility, price: this.price})
      },
      middle: function () {
        this.$Message.warning("不能选择双中性")
      }
    }

  }
</script>

<style scoped>
  span {
    font-size: 16px;
  }

  .bigFont{
    font-size: 24px !important;
  }

  .font0 {
    font-size: 0;
  }

  .orange{
    color: #f90;
  }

  .box {
    display: table;
    height: 60px;
    width: 60px;
  }

  .box span {
    display: table-cell;
    vertical-align: middle;
    padding-bottom: 17px;
  }

  .boxLeft {
    display: table;
    height: 60px;
    width: 90px;
    padding-right: 20px;
  }

  .boxLeft span {
    display: table-cell;
    vertical-align: middle;
  }

  .outGrid {
    height: 60px;
    width: 60px;
  }

  .grid {
    height: 30px;
    width: 30px;
    background-color: #9eebcf;
    border: #F8F8FF;
    border-style: solid;
    border-width: 1px;
    border-radius: 50%;
    cursor: pointer;
  }

  .middleGrid {
    height: 60px;
    width: 60px;
    border: #ffffff;
    border-style: solid;
    border-width: 1px;
  }

  @keyframes bop {
    0% {
      width: 30px;
      height: 30px
    }
    25% {
      width: 35px;
      height: 35px
    }
    50% {
      width: 40px;
      height: 40px
    }
    75% {
      width: 45px;
      height: 45px
    }
    100% {
      width: 50px;
      height: 50px
    }
  }

  .selected {
    /*border: #00bfff;*/
    /*border-style: solid;*/
    /*border-width: 1px;*/
    -webkit-animation: bop 0.1s;
    -o-animation: bop 0.1s;
    -moz-animation: bop 0.1s;
    animation: bop 0.1s;
    animation-fill-mode: both;
  }

  .out {
    height: 60px;
    width: 90px;
    background-image: url("../../../../static/graph/line.png");
    background-size: 100% 100%;
  }

  .up {
    font-style: normal;
    display: block;
    position: absolute;
    top: 37px;
    left: 10px;
    height: 60px;
  }

  .down {
    font-style: normal;
    display: block;
    position: absolute;
    top: 10px;
    left: 50px;
  }
</style>
