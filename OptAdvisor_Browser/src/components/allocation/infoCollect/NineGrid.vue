<template>
  <div class="font0" @click="onGrid">
    <Row>
      <Col span="6" class="box">
        <span>波动率/价格</span>
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
      <Col span="6" class="box">
        <span>增加</span>
      </Col>
      <Col span="18" class="flex">
        <div class="grid" v-bind:class="{selected:volatility===1&&price===1}" v-on:click="volatility=1;price=1">a</div>
        <div class="grid" v-bind:class="{selected:volatility===1&&price===0}" v-on:click="volatility=1;price=0">a</div>
        <div class="grid" v-bind:class="{selected:volatility===1&&price===-1}" v-on:click="volatility=1;price=-1">a</div>
      </Col>
    </Row>
    <Row>
      <Col span="6" class="box">
        <span>中性</span>
      </Col>
      <Col span="18" class="flex">
        <div class="grid" v-bind:class="{selected:volatility===0&&price===1}" v-on:click="volatility=0;price=1">a</div>
        <div class="grid" v-bind:class="{selected:volatility===0&&price===0}" v-on:click="middle">a</div>
        <div class="grid" v-bind:class="{selected:volatility===0&&price===-1}" v-on:click="volatility=0;price=-1">a</div>
      </Col>
    </Row>
    <Row>
      <Col span="6" class="box">
        <span>降低</span>
      </Col>
      <Col span="18" class="flex">
        <div class="grid" v-bind:class="{selected:volatility===-1&&price===1}" v-on:click="volatility=-1;price=1">a
        </div>
        <div class="grid" v-bind:class="{selected:volatility===-1&&price===0}" v-on:click="volatility=-1;price=0">a
        </div>
        <div class="grid" v-bind:class="{selected:volatility===-1&&price===-1}" v-on:click="volatility=-1;price=-1">a
        </div>
      </Col>
    </Row>
  </div>
</template>

<script>
    export default {
      name: "NineGrid",
      data(){
        return {
          volatility:1,
          price:1
        }
      },
      methods:{
        onGrid:function(){
          this.$emit('onGrid',{volatility:this.volatility,price:this.price})
        },
        middle:function () {
          this.$Message.warning("不能选择双中性")
        }
      }

    }
</script>

<style scoped>
  span{
    font-size: 16px;
  }

  .font0{
    font-size: 0;
  }

  .box{
    display: table;
    height: 60px;
    width: 60px;
  }

  .box span{
    display: table-cell;
    vertical-align: middle;
  }

  .grid{
    display: inline-block;
    height: 60px;
    width: 60px;
    background-color: #9eebcf;
    border: #F8F8FF;
    border-style: solid;
    border-width: 1px;
    border-radius: 0.25rem;
    cursor: pointer;
    }

  .selected{
    border: #00bfff;
    border-style: solid;
    border-width: 1px;
  }
</style>
