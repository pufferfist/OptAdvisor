<template>
  <div>
    <Row>
      <Col offset="3"><h1 class="w-50">资产配置</h1></Col>
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
        <ResultDisplay class="pt4" :data="portfolio" :date="date" :portfolioProfit="portfolioProfit" :assetsProfit="assetsProfit" ></ResultDisplay>
        <Row class="pt4">
          <Button size="large" type="warning" @click="lastStep" class="fl forwardButton" ghost>
            <Icon type="ios-arrow-back"/>
            上一步
          </Button>
          <Button size="large" type="warning" @click="addPortfolio" class="fr addButton" ghost :loading="addPortfolioLoading">
            <Icon type="md-add" />
            加入我的组合
          </Button>
        </Row>
      </div>
    </transition>
  </div>
</template>

<script>
    import NineGrid from "../components/allocation/infoCollect/NineGrid";
    import AlForm from "../components/allocation/infoCollect/AlForm";
    import ResultDisplay from "../components/allocation/ResultDisplay";
    export default {
      name: "Allocation",
      data() {
        return {
          price: 1,
          volatility: 1,
          infoStep: false,
          loading: false,
          resultStep: true,
          date: [],
          portfolioProfit: [],//图表
          assetsProfit: [],
          data: {
            optionList: [
              {
                id: '510050C1808M02600',
                name: "50ETF购8月2600",
                number: "2",
                cp: 1,
                expireTime: "2018-8-22",
                k: 2.600,
                price1: 2.2,
                price2: 2.8,
                delta: 1,
                gamma: 2,
                theta: 3,
                vega: 4,
                rho: 5
              },
              {
                id: '510050C1809M02700',
                name: "50ETF沽9月2700",
                number: "-1",
                cp: -1,
                expireTime: "2018-9-26",
                k: 2.700,
                price1: 2.3,
                price2: 2.9,
                delta: 5,
                gamma: 4,
                theta: 3,
                vega: 2,
                rho: 1
              }
            ],
            portfolioInfo: {
              cost: 10000,
              securityDeposit: 100,
              z_delta: 1,
              z_gamma: 2,
              z_vega: 3,
              z_theta: 4,
              z_rho: 5,
              expectedRate: 6,
              risk: 7,
            },
            assetsInfo: {
              expectedRate: '60%',
              risk: '30%'
            },
          },
          addPortfolioLoading:false,
          portfolio:{},
          myPortfolio:{
            options:[],
            name:"temp",
            type:0,
            trackingStatus:false
          }
        }
      },
      components: {ResultDisplay, AlForm, NineGrid},
      methods: {
        handleOnGrid: function (event) {
          this.price = event.price;
          this.volatility = event.volatility;
        },
        handleNextStep: function () {
          this.loading = true;
          this.axios.post("backend/recommend/recommendPortfolio")
            .then((res)=>{
              this.portfolio=res.data;
              this.loading = false;
              this.infoStep = false;
              setTimeout(() => {
                this.resultStep = true;
              }, 200);
            });
        },
        lastStep: function () {
          this.resultStep = false;
          setTimeout(() => {
            this.infoStep = true;
          }, 200);
        },
        addPortfolio: function () {
          this.addPortfolioLoading=true;
          //异步请求添加组合并跳转到我的组合界面
          //理论上来说应该支持自定义组合名
          this.myPortfolio.options=this.portfolio.optionList;
          this.myPortfolio.type='RECOMMEND_PORTFOLIO';
          this.axios.post("/backend/portfolio", this.myPortfolio)
            .then((res) => {
            this.addPortfolioLoading=false;
            if (res.data.code === 0) {
                this.$router.push('/myPortfolio');
              } else {
                this.$Message.error('发生了预料之外的错误');
              }
            }
          )
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

  .forwardButton{
    color: #f90;
    background: 0 0;
    border-color: #f90;
  }

  .forwardButton:hover {
    color: #fff;
    background-color: #f90;
    border-color: #f90;
  }

  .addButton {
    color: #4169E1;
    background: 0 0;
    border-color: #4169E1;
  }

  .addButton:hover {
    color: #fff;
    background-color: #4169E1;
    border-color: #4169E1;
  }
</style>

