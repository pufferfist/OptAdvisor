<template>
  <div>
    <Row>
      <ICol offset="3"><h1 class="w-50">资产配置</h1></ICol>
    </Row>
    <transition name="move-left">
      <div v-if="infoStep">
        <Row>
          <Col span="12" class="middleBorder">
            <Row class="verticalCenterFather nineGrid center">
              <NineGrid class="verticalCenterChild" v-on:onGrid="handleOnGrid"></NineGrid>
            </Row>
          </Col>
          <Col span="12" class="pl5">
            <AlForm :price="price" :volatility="volatility" v-on:nextStep="handleNextStep"></AlForm>
          </Col>
        </Row>
      </div>
    </transition>
    <transition name="fade">
      <Spin v-if="loading" fix class="spin">
        <div class="loader">
          <svg class="circular" viewBox="25 25 50 50">
            <circle class="path" cx="50" cy="50" r="20" fill="none" stroke-width="5" stroke-miterlimit="10"></circle>
          </svg>
        </div>
      </Spin>
    </transition>
    <transition name="move-right">
      <div v-if="resultStep">
        <h1>结果展示</h1>
        <Button size="large" type="warning" @click="lastStep" class="fr forwardButton" ghost>
          <Icon type="ios-arrow-back"/>
          上一步
        </Button>
      </div>
    </transition>
  </div>
</template>

<script>
    import NineGrid from "../components/allocation/infoCollect/NineGrid";
    import AlForm from "../components/allocation/infoCollect/AlForm";
    export default {
        name: "Allocation",
      data(){
        return {
          price:1,
          volatility:1,
          infoStep:true,
          loading:false,
          resultStep:false,
        }
      },
      components: {AlForm, NineGrid},
      methods:{
          handleOnGrid:function (event) {
            this.price=event.price;
            this.volatility=event.volatility;
          },
        handleNextStep:function () {
          this.loading=true;
          //异步请求
          setTimeout(()=>{
            this.loading=false;
            this.infoStep=false;
            setTimeout(()=>{
              this.resultStep=true;
            },200);
          },2000);
        },
        lastStep:function () {
          this.resultStep=false;
          setTimeout(()=>{
            this.infoStep=true;
          },200);
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
  .spin{
    position:fixed;
  }

  .loader {
    width: 30px;
    height: 30px;
    position: relative;
    margin: 0 auto
  }

  .circular {
    -webkit-animation: rotate 2s linear infinite;
    animation: rotate 2s linear infinite;
    height: 100%;
    -webkit-transform-origin: center center;
    transform-origin: center center;
    width: 100%;
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    margin: auto
  }

  .path {
    stroke-dasharray: 1, 200;
    stroke-dashoffset: 0;
    -webkit-animation: dash 1.5s ease-in-out infinite, color 6s ease-in-out infinite;
    animation: dash 1.5s ease-in-out infinite, color 6s ease-in-out infinite;
    stroke-linecap: round
  }

  @-webkit-keyframes rotate {
    to {
      -webkit-transform: rotate(1turn);
      transform: rotate(1turn)
    }
  }

  @keyframes rotate {
    to {
      -webkit-transform: rotate(1turn);
      transform: rotate(1turn)
    }
  }

  @-webkit-keyframes dash {
    0% {
      stroke-dasharray: 1, 200;
      stroke-dashoffset: 0
    }
    50% {
      stroke-dasharray: 89, 200;
      stroke-dashoffset: -35
    }
    to {
      stroke-dasharray: 89, 200;
      stroke-dashoffset: -124
    }
  }

  @keyframes dash {
    0% {
      stroke-dasharray: 1, 200;
      stroke-dashoffset: 0
    }
    50% {
      stroke-dasharray: 89, 200;
      stroke-dashoffset: -35
    }
    to {
      stroke-dasharray: 89, 200;
      stroke-dashoffset: -124
    }
  }

  @-webkit-keyframes color {
    0%, to {
      stroke: #d62d20
    }
    40% {
      stroke: #0057e7
    }
    66% {
      stroke: #008744
    }
    80%, 90% {
      stroke: #ffa700
    }
  }

  @keyframes color {
    0%, to {
      stroke: #d62d20
    }
    40% {
      stroke: #0057e7
    }
    66% {
      stroke: #008744
    }
    80%, 90% {
      stroke: #ffa700
    }
  }

  .asd-car {
    height: 190px;
    overflow: hidden
  }

  .forwardButton{
    color: #f90;
    background: 0 0;
    border-color: #f90;
  }

  .forwardButton:hover{
    color: #fff;
    background-color: #f90;
    border-color: #f90;
  }
</style>

