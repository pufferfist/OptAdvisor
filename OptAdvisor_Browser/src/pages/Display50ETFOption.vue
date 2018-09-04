<template>
  <div id="page">
      <month-selecter v-on:selectedTime = "selectedTime"></month-selecter>
      <Row style="margin: 19px 0 10px 0">
        <Col span="11">
          <div style="text-align: right; margin:5px 0; border-bottom: 4px solid  #eb6951;">
            看涨合约
          </div>
        </Col>
        <Col span="11" offset="2">
          <div style="text-align: left; border-bottom: 4px solid  #4bbc7c;">
            看跌合约
          </div>
        </Col>
      </Row>
      <Row style="font-size:13px; border-bottom: .5px solid  rgb(189, 187, 187);">
        <Col span="11">
          <Row style="padding-bottom: 5px; text-align: right; margin:5px 0;  color: grey">
            <Col span="3">
              买量
            </Col>
            <Col span="3">
              买价
            </Col>
            <Col span="3">
              最新价
            </Col>
            <Col span="3">
              卖价
            </Col>
            <Col span="3">
              卖量
            </Col>
            <Col span="3">
              持仓量
            </Col>
            <Col span="3">
              振幅
            </Col>
            <Col span="3">
              涨跌幅
            </Col>
          </Row>
        </Col>
        <Col :span="2" style="color: grey">
            行权价
        </Col>
        <Col span="11" >
          <Row style="text-align: left; color: grey">
            <Col span="3">
              买量
            </Col>
            <Col span="3">
              买价
            </Col>
            <Col span="3">
              最新价
            </Col>
            <Col span="3">
              卖价
            </Col>
            <Col span="2">
              卖量
            </Col>
            <Col span="3">
              持仓量
            </Col>
            <Col span="3">
              振幅
            </Col>
            <Col span="3">
              涨跌幅
            </Col>
          </Row>
        </Col>
      </Row>
      <Row style="min-height:350px; font-size:13px;">
        <Col span="11">
          <Row  @click.native="handleOptionChange(item)"  class="OptionsItem up" v-bind:class="[{selected:selectedItem.name === item.name},{isActual: item.isActual}]"  v-for="item in OptionsUpList" :key="item.id">
            <Col span="3">
              <Icon style="color: orange" v-if="item.isMain!=='0'" type="ios-star" /> {{item.buyVolumes}}
            </Col>
            <Col span="3">
              {{item.buyPrice}}
            </Col>
            <Col span="3">
              {{item.newPrice}}
            </Col>
            <Col span="3">
              {{item.salePrice}}
            </Col>
            <Col span="3">
              {{item.saleVolumes}}
            </Col>
            <Col span="3">
              {{item.openInterest}}
            </Col>
            <Col span="3">
              {{item.amplitude}}
            </Col>
            <Col v-bind:style="{color: item.increase[0] === '-'? 'green' :'red'}" span="3">
              {{item.increase}}%
            </Col>
          </Row>
        </Col>
        <Col span="2">
          <Row v-for="item in OptionsUpList" :key="item.id"> 
            <span style="display:inline-block; text-align: right; padding:3px;">
              {{item.strikePrice}}
            </span>
          </Row>
        </Col>
        <Col span="11">
          <Row @click.native="handleOptionChange(item)" class="OptionsItem down" v-bind:class="[{selected:selectedItem.name === item.name},{isActual: !item.isActual}]"  v-for="item in OptionsDownList" :key="item.id">
            <Col span="3" >
              {{item.buyVolumes}}
            </Col>
            <Col span="3" >
              {{item.buyPrice}}
            </Col>
            <Col span="3" >
              {{item.newPrice}}
            </Col>
            <Col span="3" >
              {{item.salePrice}}
            </Col>
            <Col span="2" >
              {{item.saleVolumes}}
            </Col>
            <Col span="3" >
              {{item.openInterest}}
            </Col>
            <Col span="3" >
              {{item.amplitude}}
            </Col>
            <Col v-bind:style="{color: item.increase[0] === '-'? 'green' :'red'}" span="4">
              {{item.increase}}% <Icon style="color: orange" v-if="item.isMain!=='0'" type="ios-star" />
            </Col>
          </Row>
          <Row style="text-align: right; font-size: 5px; color: grey;">
            红色背景为实值合约，白色背景为平值、虚值合约
            <p>标注★的合约为主力合约</p>
          </Row>
        </Col>
        <Spin size="large" fix v-if="isLoading"></Spin>
      </Row>
      <div style="text-align:left ;margin: 50px 0" ref="Contracting" id="Contracting">
        <div id="title" style="padding-bottom:10px;border-bottom: 3px solid black">
          <h2 style="display:inline-block; margin-right: 50px;">合约分析</h2><span >{{selectedItem.name}}</span>
        </div>
        <Row>
          <Col style="margin-top: 25px;min-width:300px;" offset="1" span="6">
            <table>
              <thead>
                <th style="width:150px;"></th>
                <th></th>
              </thead>
              <tbody style="line-height:30px;">
                <tr>
                  <td>交易代码</td>
                  <td>{{SO.code}}</td>
                </tr>
                <tr>
                  <td>理论价值</td>
                  <td>{{parseFloat(SO.value) !== 0 ? SO.value : '--'}}</td>
                </tr>
                <tr>
                  <td>价值状态</td>
                  <td>{{SOActual.priceMark}}</td>
                </tr>
                <tr>
                  <td style="text-align: right; padding-right:40px; ">内在价值</td>
                  <td>{{SOActual.innerPrice}}</td>
                </tr>
                <tr>
                  <td style="text-align: right; padding-right:40px; ">时间价值</td>
                  <td>{{SOActual.timeP}}</td>
                </tr>
                <tr>
                  <td>成交量</td>
                  <td>{{SO.count}}</td>
                </tr>
                <tr>
                  <td>Delta</td>
                  <td>{{SO.delta}}</td>
                </tr>
                <tr>
                  <td>Gamma</td>
                  <td>{{SO.gamma}}</td>
                </tr>
                <tr>
                  <td>Theta</td>
                  <td>{{SO.theta}}</td>
                </tr>
                <tr>
                  <td>Vega</td>
                  <td>{{SO.vega}}</td>
                </tr>
                <tr>
                  <td>隐含波动率</td>
                  <td>{{SO.volatility}}</td>
                </tr>
                <tr>
                  <td>最高价</td>
                  <td>{{SO.high}}</td>
                </tr>
                <tr>
                  <td>最低价</td>
                  <td>{{SO.low}}</td>
                </tr>
              </tbody>
            </table>
          </Col>
          <Col offset="2" span="13">
              <div style="text-align:left">
                <div id="selecter"> <span  v-on:click="handleClick('line')" v-bind:class="{ active: lineActive }" class="selectItem" >分时线</span> <span  v-on:click="handleClick('day')" v-bind:class="{ active: dayActive }" class="selectItem">日 k 线</span> </div>
                <div id="myEchart" ref="myechart"></div>
              </div>
          </Col>
        </Row>
      </div>
      <div style="text-align:left ;margin: 50px 0">
        <div id="title" style="padding-bottom:10px;border-bottom: 3px solid black">
          <h2 style="display:inline-block; margin-right: 50px;">主力合约</h2>
        </div>
        <div>
          <Row style="margin:30px 0; text-align:center; ">
            <Col span="12">
              <h3>主力看涨合约</h3>
              <div>
                <table style="width:100%; margin:20px 0;border-right: 1px solid black;">
                  <thead>
                    <th style="width:50%;"></th>
                    <th style="width:50%;"></th>
                  </thead>
                  <tbody style="line-height:30px;">
                    <tr>
                      <td>交易代码</td>
                      <td>{{mainUpOption.code}}</td>
                    </tr>
                    <tr>
                      <td>理论价值</td>
                      <td>{{parseFloat(mainUpOption.value) !== 0 ? mainUpOption.value : '--'}}</td>
                    </tr>
                    <tr>
                      <td>价值状态</td>
                      <td>{{mainUpOptionActual.priceMark}}</td>
                    </tr>
                    <tr>
                      <td style="text-align: right; padding-right:40px; ">内在价值</td>
                      <td>{{mainUpOptionActual.innerPrice}}</td>
                    </tr>
                    <tr>
                      <td style="text-align: right; padding-right:40px; ">时间价值</td>
                      <td>{{mainUpOptionActual.timeP}}</td>
                    </tr>
                    <tr>
                      <td>持仓量 / 占　比</td>
                      <td>{{mainUpOption.openInterest}} / {{mainUpOption.openInterestRate}}</td>
                    </tr>
                    <tr>
                      <td>最新价 / 涨　幅</td>
                      <td>{{mainUpOption.new}} / {{mainUpOption.increase}}</td>
                    </tr>
                    <tr>
                      <td>买　价 / 卖　价</td>
                      <td>{{mainUpOption.buyPrice}} / {{mainUpOption.salePrice}}</td>
                    </tr>
                    <tr>
                      <td>最高价 / 最低价</td>
                      <td>{{mainUpOption.low}}</td>
                    </tr>
                    <tr>
                      <td>成交量</td>
                      <td>{{mainUpOption.volume}}</td>
                    </tr>
                    <tr>
                      <td>Delta</td>
                      <td>{{mainUpOption.delta}}</td>
                    </tr>
                    <tr>
                      <td>Gamma</td>
                      <td>{{mainUpOption.gamma}}</td>
                    </tr>
                    <tr>
                      <td>Theta</td>
                      <td>{{mainUpOption.theta}}</td>
                    </tr>
                    <tr>
                      <td>Vega</td>
                      <td>{{mainUpOption.vega}}</td>
                    </tr>
                    <tr>
                      <td>隐含波动率</td>
                      <td>{{mainUpOption.volatility}}</td>
                    </tr>
                    <tr>
                      <td>最高价</td>
                      <td>{{mainUpOption.high}}</td>
                    </tr>
                    <tr>
                      <td>最低价</td>
                      <td>{{mainUpOption.low}}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </Col>
            <Col span="12">
              <h3>主力看跌合约</h3>
              <table style="width:100%; margin:20px 0;">
                  <thead>
                    <th style="width:50%;"></th>
                    <th style="width:50%;"></th>
                  </thead>
                  <tbody style="line-height:30px;">
                    <tr>
                      <td>交易代码</td>
                      <td>{{mainDownOption.code}}</td>
                    </tr>
                    <tr>
                      <td>理论价值</td>
                      <td>{{parseFloat(mainDownOption.value) !== 0 ? mainDownOption.value : '--'}}</td>
                    </tr>
                    <tr>
                      <td>价值状态</td>
                      <td>{{mainDownOptionActual.priceMark}}</td>
                    </tr>
                    <tr>
                      <td style="text-align: right; padding-right:40px; ">内在价值</td>
                      <td>{{mainDownOptionActual.innerPrice}}</td>
                    </tr>
                    <tr>
                      <td style="text-align: right; padding-right:40px; ">时间价值</td>
                      <td>{{mainDownOptionActual.timeP}}</td>
                    </tr>
                    <tr>
                      <td>持仓量 / 占　比</td>
                      <td>{{mainDownOption.openInterest}} / {{mainDownOption.openInterestRate}}</td>
                    </tr>
                    <tr>
                      <td>最新价 / 涨　幅</td>
                      <td>{{mainDownOption.new}} / {{mainDownOption.increase}}</td>
                    </tr>
                    <tr>
                      <td>买　价 / 卖　价</td>
                      <td>{{mainDownOption.buyPrice}} / {{mainDownOption.salePrice}}</td>
                    </tr>
                    <tr>
                      <td>最高价 / 最低价</td>
                      <td>{{mainDownOption.low}}</td>
                    </tr>
                    <tr>
                      <td>成交量</td>
                      <td>{{mainDownOption.volume}}</td>
                    </tr>
                    <tr>
                      <td>Delta</td>
                      <td>{{mainDownOption.delta}}</td>
                    </tr>
                    <tr>
                      <td>Gamma</td>
                      <td>{{mainDownOption.gamma}}</td>
                    </tr>
                    <tr>
                      <td>Theta</td>
                      <td>{{mainDownOption.theta}}</td>
                    </tr>
                    <tr>
                      <td>Vega</td>
                      <td>{{mainDownOption.vega}}</td>
                    </tr>
                    <tr>
                      <td>隐含波动率</td>
                      <td>{{mainDownOption.volatility}}</td>
                    </tr>
                    <tr>
                      <td>最高价</td>
                      <td>{{mainDownOption.high}}</td>
                    </tr>
                    <tr>
                      <td>最低价</td>
                      <td>{{mainDownOption.low}}</td>
                    </tr>
                  </tbody>
                </table>
            </Col>
          </Row>
        </div>
        <div>
          <Row>
            <div id="title" style="padding-bottom:10px;border-bottom: 3px solid black">
            <h2 style="display:inline-block; margin-right: 50px;">五档盘口</h2>
          </div>
          </Row>
          <Row style="margin:30px 0;">
            <Col offset="1" span="10">
              <Row class="dataList" >
                <Col span="8"><p style=" text-align: left;"> 买5</p></Col>
                <Col span="8"><p style=" text-align: center;font-weight: 700;"  >{{selectedItem.buyList[4].volumes}}</p></Col>
                <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{selectedItem.buyList[4].price}}</p></Col>
              </Row>
              <Row class="dataList" >
                <Col span="8"><p style=" text-align: left;"> 买4</p></Col>
                <Col span="8"><p style=" text-align: center;font-weight: 700;"  >{{selectedItem.buyList[3].volumes}}</p></Col>
                <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{selectedItem.buyList[3].price}}</p></Col>
              </Row>
              <Row class="dataList" >
                <Col span="8"><p style=" text-align: left;"> 买3</p></Col>
                <Col span="8"><p style=" text-align: center;font-weight: 700;"  >{{selectedItem.buyList[2].volumes}}</p></Col>
                <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{selectedItem.buyList[2].price}}</p></Col>
              </Row>
              <Row class="dataList" >
                <Col span="8"><p style=" text-align: left;"> 买2</p></Col>
                <Col span="8"><p style=" text-align: center;font-weight: 700;"  >{{selectedItem.buyList[1].volumes}}</p></Col>
                <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{selectedItem.buyList[1].price}}</p></Col>
              </Row>
              <Row class="dataList" >
                <Col span="8"><p style=" text-align: left;"> 买1</p></Col>
                <Col span="8"><p style=" text-align: center;font-weight: 700;"  >{{selectedItem.buyList[0].volumes}}</p></Col>
                <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{selectedItem.buyList[0].price}}</p></Col>
              </Row>
            </Col>
            <Col offset="3" span="9">
              <Row class="dataList" >
                <Col span="8"><p style=" text-align: left;"> 卖5</p></Col>
                <Col span="8"><p style=" text-align: center;font-weight: 700;"  >{{selectedItem.saleList[0].volumes}}</p></Col>
                <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{selectedItem.saleList[0].price}}</p></Col>
              </Row>
              <Row class="dataList" >
                <Col span="8"><p style=" text-align: left;"> 卖4</p></Col>
                <Col span="8"><p style=" text-align: center;font-weight: 700;"  >{{selectedItem.saleList[1].volumes}}</p></Col>
                <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{selectedItem.saleList[1].price}}</p></Col>
              </Row>
              <Row class="dataList" >
                <Col span="8"><p style=" text-align: left;"> 卖3</p></Col>
                <Col span="8"><p style=" text-align: center;font-weight: 700;"  >{{selectedItem.saleList[2].volumes}}</p></Col>
                <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{selectedItem.saleList[2].price}}</p></Col>
              </Row>
              <Row class="dataList" >
                <Col span="8"><p style=" text-align: left;"> 卖2</p></Col>
                <Col span="8"><p style=" text-align: center;font-weight: 700;"  >{{selectedItem.saleList[3].volumes}}</p></Col>
                <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{selectedItem.saleList[3].price}}</p></Col>
              </Row>
              <Row class="dataList" >
                <Col span="8"><p style=" text-align: left;"> 卖1</p></Col>
                <Col span="8"><p style=" text-align: center;font-weight: 700;"  >{{selectedItem.saleList[4].volumes}}</p></Col>
                <Col span="8"><p style="text-align: right;font-weight: 700;"  >{{selectedItem.saleList[4].price}}</p></Col>
              </Row>
            </Col>
          </Row>
        </div>
      </div>
  </div>
</template>

<script>
    const echarts = require("echarts");
    import MonthSelecter from "../components/MonthSelecter/MonthSelecter";    
    export default {
        name: "Display50ETFOption",
        components: {MonthSelecter},
        data() {
          return {
            time: {
              date: '',
              differ: 0,
              ETFPrice: 0,
            },
            upList: [],
            downList: [],
            OptionsUpList: [{
                    id: 0,
                    buyVolumes: 1,
                    buyPrice: 0,
                    newPrice:0,
                    salePrice: 0,
                    saleVolumes: 0,
                    openInterest:0,
                    increase: 0,
                    strikePrice: 0,
                    isActual: -1,
                    closePrice:0,
                    openPrice: 0,
                    hardenPrice: 0,
                    limitPrice: 0,
                    saleList: [
                      {
                        id: 'sale5',
                        price: 0,
                        volumes: 0,
                      },
                      {
                        id: 0,
                        price: 0,
                        volumes: 0,
                      },
                      {
                        id: 0,
                        price:0,
                        volumes: 0,
                      },
                      {
                        id: 'sale2',
                        price: 0,
                        volumes: 0,
                      },
                      {
                        id: 'sale1',
                        price: 0,
                        volumes:0,
                      },
                    ],
                    buyList: [
                      {
                        id: 'buy1',
                        price: 0,
                        volumes: 0,
                      },
                      {
                        id: 'buy2',
                        price: 0,
                        volumes:0,
                      },
                      {
                        id: 'buy3',
                        price: 0,
                        volumes: 0,
                      },
                      {
                        id: 'buy4',
                        price:0,
                        volumes:0,
                      },
                      {
                        id: 'buy5',
                        price: 0,
                        volumes: 0,
                      },
                    ],
                    time: 0,
                    isMain: 0,
                    status: 0,
                    targetType: 0,
                    targetShares: 0,
                    name:0,
                    amplitude: 0,
                    hightPrice: 0,
                    lowPrice:0,
                    volumes:0,
                    turnover:0,
                  }],
            OptionsDownList: [],
            selectedItem: {
              name:'',
              saleList: [
                      {
                        id: 'sale5',
                        price: 0,
                        volumes: 0,
                      },
                      {
                        id: 0,
                        price: 0,
                        volumes: 0,
                      },
                      {
                        id: 0,
                        price:0,
                        volumes: 0,
                      },
                      {
                        id: 'sale2',
                        price: 0,
                        volumes: 0,
                      },
                      {
                        id: 'sale1',
                        price: 0,
                        volumes:0,
                      },
                    ],
                    buyList: [
                      {
                        id: 'buy1',
                        price: 0,
                        volumes: 0,
                      },
                      {
                        id: 'buy2',
                        price: 0,
                        volumes:0,
                      },
                      {
                        id: 'buy3',
                        price: 0,
                        volumes: 0,
                      },
                      {
                        id: 'buy4',
                        price:0,
                        volumes:0,
                      },
                      {
                        id: 'buy5',
                        price: 0,
                        volumes: 0,
                      },
                    ],
            },
            SO:{
              code: '0'
            },
            Interval: Number,
            lineActive: true,
            dayActive: false,
            myChart: Object,
            lineData: [],
            dayKData: [],
            timeout: 1,
            isLoading: true,
            mainDownOption: {code: '0'},
            mainUpOption: {code: '0'}
          }
        },

        computed: {
          SOActual: function() {
            console.log(this.SO.price,3333333);
            
            const POC  = this.SO.code[6];
            if(POC === 'C') {
              const priceMark =  this.time.ETFPrice - parseFloat(this.SO.price);
              const innerPrice = priceMark > 0 ? (priceMark).toFixed(4) : 0;
              const timeP = parseFloat(this.SO.timeP) - innerPrice > 0 ?( parseFloat(this.SO.timeP) - innerPrice).toFixed(4) : 0; 
              return {
                priceMark: priceMark > 0? '实值' : priceMark <0 ? '虚值' : '平值',
                innerPrice: innerPrice == 0? '-' : innerPrice,
                timeP : timeP == 0 ? '-' : timeP,
              }
            } else {
              const priceMark =  this.time.ETFPrice - parseFloat(this.SO.price);
              const innerPrice = priceMark < 0 ? -priceMark.toFixed(4) : 0
              const timeP = (parseFloat(this.SO.timeP) - innerPrice).toFixed(4); 
              return {
                priceMark: priceMark < 0? '实值' : priceMark >0 ? '虚值' : '平值',
                innerPrice: innerPrice == 0? '-' : innerPrice,
                timeP : timeP == 0 ? '-' : timeP,
              }
            }
          },
          mainUpOptionActual: function() {
            const POC  = this.mainUpOption.code[6];
            if(POC === 'C') {
              const priceMark =  this.time.ETFPrice - parseFloat(this.mainUpOption.price);
              const innerPrice = priceMark > 0 ? (priceMark).toFixed(4) : 0;
              const timeP = parseFloat(this.mainUpOption.new) - innerPrice > 0 ?( parseFloat(this.mainUpOption.new) - innerPrice).toFixed(4) : 0; 
              return {
                priceMark: priceMark > 0? '实值' : priceMark <0 ? '虚值' : '平值',
                innerPrice: innerPrice == 0? '-' : innerPrice,
                timeP : timeP == 0 ? '-' : timeP,
              }
            } else {
              const priceMark =  this.time.ETFPrice - parseFloat(this.mainUpOption.price);
              const innerPrice = priceMark < 0 ? -priceMark.toFixed(4) : 0

              const timeP = (parseFloat(this.mainUpOption.new) - innerPrice).toFixed(4); 
              return {
                priceMark: priceMark < 0? '实值' : priceMark >0 ? '虚值' : '平值',
                innerPrice: innerPrice == 0? '-' : innerPrice,
                timeP : timeP == 0 ? '-' : timeP,
              }
            }
          },
          mainDownOptionActual: function() {
            console.log(this.mainDownOption.price,444444444444);
            
            const POC  = this.mainDownOption.code[6];
            if(POC === 'C') {
              const priceMark =  this.time.ETFPrice - parseFloat(this.mainDownOption.price);
              const innerPrice = parseFloat(priceMark > 0 ? (priceMark).toFixed(4) : 0);
              const timeP = parseFloat(this.mainDownOption.new) - innerPrice > 0 ?( parseFloat(this.mainDownOption.new) - innerPrice).toFixed(4) : 0; 
              
              return {
                priceMark: priceMark > 0? '实值' : priceMark <0 ? '虚值' : '平值',
                innerPrice: innerPrice == 0? '-' : innerPrice,
                timeP : timeP == 0 ? '-' : timeP,
              }
            } else {
              const priceMark =  this.time.ETFPrice - parseFloat(this.mainDownOption.price);
              const innerPrice = priceMark < 0 ? -priceMark.toFixed(4) : 0
              const timeP = (parseFloat(this.mainDownOption.new) - innerPrice).toFixed(4); 
              return {
                priceMark: priceMark < 0? '实值' : priceMark >0 ? '虚值' : '平值',
                innerPrice: innerPrice == 0? '-' : innerPrice,
                timeP : timeP == 0 ? '-' : timeP,
              }
            }
          },
        },
        methods: {
          selectedTime(e) {
            this.time = e;
            this.getOptionsList(e.date);
            this.selectedItem = { name: ''};
            this.isLoading = true;
          },
          getOptionsList(time) {
            const Ptime =  '510050'+time.slice(2,7).replace('-','');
            this.axios
              .get(`/sinaOption/list=OP_UP_${Ptime},OP_DOWN_${Ptime}`)
              .then(res => {
                const temp1  = res.data.split('"')[1].split(',');
                temp1.pop();
                this.upList = temp1.map(item=> {
                  if(item)
                    return  item
                });
                const temp2  = res.data.split('"')[3].split(',');
                temp2.pop();
                this.downList = temp2.map(item=> {
                  if(item)
                    return item
                });
                clearInterval(this.Interval);
                this.getOptionListBaseData();
                this.Interval = setInterval(this.getOptionListBaseData, 5000);
                this.isLoading = false;
              });
          },
          getOptionListBaseData() {
            const tempOptionsUpList = [];
            const tempOptionsDownList = [];
            this.axios
              .get('/sinaOption/list='+this.upList.join(',')+','+this.downList.join(','))
              .then(res => {
                const resData  = res.data.split('"');
                for(let i = 0; i< resData.length/2 -1 ;i++) {
                  const name = resData[i].split(' ')[1].split('=')[0];
                  const data = resData[++i].split(',')
                  tempOptionsUpList.push({
                    id: name,
                    buyVolumes: data[0],
                    buyPrice: data[1],
                    newPrice: data[2],
                    salePrice: data[3],
                    saleVolumes: data[4],
                    openInterest: data[5],
                    increase: data[6],
                    strikePrice: data[7],
                    isActual: this.time.ETFPrice > data[7] ? true : (this.time.ETFPrice < data[7] ? false : -1),
                    closePrice: data[8],
                    openPrice: data[9],
                    hardenPrice: data[10],
                    limitPrice: data[11],
                    saleList: [
                      {
                        id: 'sale5',
                        price: data[12],
                        volumes: data[13],
                      },
                      {
                        id: 'sale4',
                        price: data[14],
                        volumes: data[15],
                      },
                      {
                        id: 'sale3',
                        price: data[16],
                        volumes: data[17],
                      },
                      {
                        id: 'sale2',
                        price: data[18],
                        volumes: data[19],
                      },
                      {
                        id: 'sale1',
                        price: data[20],
                        volumes: data[21],
                      },
                    ],
                    buyList: [
                      {
                        id: 'buy1',
                        price: data[22],
                        volumes: data[23],
                      },
                      {
                        id: 'buy2',
                        price: data[24],
                        volumes: data[25],
                      },
                      {
                        id: 'buy3',
                        price: data[26],
                        volumes: data[27],
                      },
                      {
                        id: 'buy4',
                        price: data[28],
                        volumes: data[29],
                      },
                      {
                        id: 'buy5',
                        price: data[30],
                        volumes: data[31],
                      },
                    ],
                    time: data[32],
                    isMain: data[33],
                    status: data[34],
                    targetType: data[35],
                    targetShares: data[36],
                    name: data[37],
                    amplitude: data[38],
                    hightPrice: data[39],
                    lowPrice: data[40],
                    volumes: data[41],
                    turnover: data[42],
                  });
                }
                for(let i = parseInt(resData.length/2) ; i< resData.length-1 ;i++) {
                  const name = resData[i].split(' ')[1].split('=')[0];
                  const data = resData[++i].split(',')
                  tempOptionsDownList.push({
                    id: name,
                    buyVolumes: data[0],
                    buyPrice: data[1],
                    newPrice: data[2],
                    salePrice: data[3],
                    saleVolumes: data[4],
                    openInterest: data[5],
                    increase: data[6],
                    strikePrice: data[7],
                    isActual: this.time.ETFPrice > data[7] ? true : (this.time.ETFPrice < data[7] ? false : -1),
                    closePrice: data[8],
                    openPrice: data[9],
                    hardenPrice: data[10],
                    limitPrice: data[11],
                    saleList: [
                      {
                        id: 'sale5',
                        price: data[12],
                        volumes: data[13],
                      },
                      {
                        id: 'sale4',
                        price: data[14],
                        volumes: data[15],
                      },
                      {
                        id: 'sale3',
                        price: data[16],
                        volumes: data[17],
                      },
                      {
                        id: 'sale2',
                        price: data[18],
                        volumes: data[19],
                      },
                      {
                        id: 'sale1',
                        price: data[20],
                        volumes: data[21],
                      },
                    ],
                    buyList: [
                      {
                        id: 'buy1',
                        price: data[22],
                        volumes: data[23],
                      },
                      {
                        id: 'buy2',
                        price: data[24],
                        volumes: data[55],
                      },
                      {
                        id: 'buy3',
                        price: data[26],
                        volumes: data[27],
                      },
                      {
                        id: 'buy4',
                        price: data[28],
                        volumes: data[29],
                      },
                      {
                        id: 'buy5',
                        price: data[30],
                        volumes: data[31],
                      },
                    ],
                    time: data[32],
                    isMain: data[33],
                    status: data[34],
                    targetType: data[35],
                    targetShares: data[36],
                    name: data[37],
                    amplitude: data[38],
                    hightPrice: data[39],
                    lowPrice: data[40],
                    volumes: data[41],
                    turnover: data[42],
                  });
                }
              this.OptionsUpList = tempOptionsUpList;
              this.OptionsDownList = tempOptionsDownList;
              if(this.selectedItem.name === '') {
                this.selectedItem = tempOptionsUpList[0];
                this.handleClick('line');
              }
              this.getOptionData(this.selectedItem.id);
              this.getMainOptionData();
              this.getChartData();
              }
            );
          },
          getMainOptionData() {
            const  tempmainUpOption = this.OptionsUpList.find(item => {
                return item.isMain!=='0';
            });
            const tempmainDownOption = this.OptionsDownList.find(item => {
                return item.isMain!=='0';
            });
            console.log(tempmainUpOption, 11, tempmainDownOption);
            
            this.axios.get('/sinaOption/list='+ tempmainUpOption.id.replace('hq_str_CON_OP','CON_ZL'))
                .then(res => {
                  console.log(res.data.split(','));
                  const tempOption = {};
                  [, , , ,tempOption.openInterest,tempOption.openInterestRate, tempOption.new, tempOption.increase, tempOption.buyPrice, tempOption.salePrice, tempOption.high, tempOption.low, tempOption.volume, tempOption.delta , tempOption.gamma ,tempOption.theta, tempOption.vega, tempOption.volatility, tempOption.code, tempOption.price, tempOption.value] = res.data.split(',');
                  this.mainUpOption = tempOption;
                });
            this.axios.get('/sinaOption/list='+ tempmainDownOption.id.replace('hq_str_CON_OP','CON_ZL'))
                .then(res => {
                  const tempOption = {};
                  console.log(res.data.split(','));
                  
                  [, , , ,tempOption.openInterest,tempOption.openInterestRate, tempOption.new, tempOption.increase, tempOption.buyPrice, tempOption.salePrice, tempOption.high, tempOption.low, tempOption.volume, tempOption.delta , tempOption.gamma ,tempOption.theta, tempOption.vega, tempOption.volatility, tempOption.code, tempOption.price, tempOption.value] = res.data.split(',');
                  this.mainDownOption = tempOption;
                })
          },
          getOptionData(id) {
            this.axios
              .get('/sinaOption/list='+id.replace('hq_str_','').replace('OP','SO'))
              .then(res => {
                const tempSO = {};
                [, , , ,tempSO.count,tempSO.delta, tempSO.gamma, tempSO.theta, tempSO.vega, tempSO.volatility, tempSO.high, tempSO.low, tempSO.code, tempSO.price , tempSO.timeP ,tempSO.value] = res.data.split(',');
                this.SO = tempSO;
            })
          },
          getChartData(needDay=false) {
            const name = this.selectedItem.id.replace('hq_str_','');
            if(this.lineActive) {
              this.axios.get('/sinaTime/StockOptionDaylineService.getOptionMinline', {
              params: {
                symbol: name,
                random: new Date().getTime(),
              }
              }).then(res => {
                let data = res.data.result.data;
                let count = 0;
                data.find((item) => {
                  if(parseFloat(item.a) === 0) {
                    count++;
                    return false;
                  } else {
                    console.log(count);
                    return true
                  }
                })
                console.log(data);
                data.splice(0, count);
                this.lineData =data;
                console.log(this.lineData, 12334444444444444444444);
                
                this.chartFresh();       

              });
            }
            if(!this.dayKData.length || needDay) {
              this.axios.get('/sinaTime/StockOptionDaylineService.getSymbolInfo', {
                params: {
                  symbol: name,
                  random: new Date().getTime(),
                }
              }).then(res => {
                this.dayKData = res.data.result.data;
              })
            }
          },
          handleOptionChange(e) {
            this.selectedItem = e;
            console.log(e);
            
            this.getOptionData(this.selectedItem.id);
            this.getChartData(true);
            this.handleClick('line');
            this.scrollInterval = setInterval(()=> {
              const scrollTop = document.scrollingElement.scrollTop;
              const offsetTop = this.$refs.Contracting.offsetTop;
              const clientHeight = this.$refs.Contracting.clientHeight;
              const innerHeight = window.innerHeight;

              if(scrollTop >= offsetTop + clientHeight - innerHeight) {
                clearInterval(this.scrollInterval);
                return ;
              }
              const speed = (offsetTop + clientHeight - innerHeight)/6;
              document.scrollingElement.scrollTop = speed + document.scrollingElement.scrollTop;
            }, 15);
          },
          handleClick(ms) {
            const that = this;
            this.myChart.showLoading();
            if (ms === "day") {
              this.dayActive = true;
              this.lineActive = false;
              clearTimeout(this.timeout);
              this.timeout = setTimeout(function(){
                that.initDayK();
              },1500);
            } else {
              this.dayActive = false;
              this.lineActive = true;
              clearTimeout(this.timeout);
              this.timeout = setTimeout(this.initLine, 1000);
            }   
          },
          initLine() {
            const that = this;
            const XDate = this.lineData.map(item => {
                   return item.i;
                  })
            const priceList = this.lineData.map((item) => {
              return item.p;
            });
            const volumeList = this.lineData.map((item) => {
              return item.v;
            })            
            this.myChart.setOption({
                color:['black'],
                tooltip: {
                  trigger: "axis",
                  axisPointer: {
                    type: "cross"
                  },
                  backgroundColor: "rgba(245, 245, 245, 0.8)",
                  borderWidth: 1,
                  borderColor: "#ccc",
                  padding: 10,
                  textStyle: {
                    color: "#000"
                  },
                  extraCssText: "width: 170px",
                  formatter(params, ticket, callback) {
                    return `${params[0].name}<br/> <span style="color:${
                      params[0].color
                    }">● </span>${params[0].seriesName}:${params[0].value}<br />
                                            <span style="color:${
                                              params[1].color
                                            }">● </span>${params[1].seriesName}:${
                      params[1].value
                    }`;
                  }
                },
                axisPointer: {
                  link: { xAxisIndex: "all" }
                },
                grid: [
                  {
                    left: "10%",
                    right: "8%",
                    height: "50%"
                  },
                  {
                    left: "10%",
                    right: "8%",
                    bottom: "20%",
                    height: "15%"
                  }
                ],
                xAxis: [
                  {
                    type: "category",
                    data: XDate,
                  },
                  {
                    gridIndex: 1,
                    type: "category",
                    data: XDate,
                    scale: true,
                    boundaryGap: false,
                    axisLine: { onZero: false },
                    axisTick: { show: false },
                    splitLine: { show: false },
                    axisLabel: { show: false },
                    splitNumber: 20
                  }
                ],
                yAxis: [
                  {
                    name: "价格",
                    type: "value",
                    position: "left",
                    max: function(value) {
                      if(that.lineMiddle) {
                        const max =
                          value.max + value.min >= 2 * that.lineMiddle
                            ? value.max
                            : 2 * that.lineMiddle - value.min;
                        return max.toFixed(4);  
                      } else {
                        return value.max.toFixed(4);
                      }

                    },
                    min: function(value) {
                      if (that.lineMiddle ) {
                        const min =
                          value.max + value.min >= 2 * that.lineMiddle
                            ? 2 * that.lineMiddle - value.max
                            : value.min;
                        return min.toFixed(4);
                      } else {
                        return value.min.toFixed(4)
                      }
                    }
                  },
                  {
                    scale: true,
                    gridIndex: 1,
                    splitNumber: 2,
                    axisLabel: { show: false },
                    axisLine: { show: false },
                    axisTick: { show: false },
                    splitLine: { show: false }
                  }
                ],
                dataZoom: [
                  {
                    type: "inside",
                    xAxisIndex: [0, 1],
                    start: 0,
                    end: 100
                  },
                  {
                    show: true,
                    xAxisIndex: [0, 1],
                    type: "slider",
                    top: "85%",
                    start: 0,
                    end: 100
                  }
                ],
        
                series: [
                  {
                    name: "价格",
                    data: priceList,
                    type: "line",
                    itemStyle: {
                      color: 'interval'
                    },
                    markLine: {
                      name: "昨日收盘价",
                      lineStyle: {
                        type: "solid"
                      },
                      label: {
                        data: 'heh'
                      },
                      data: [
                        {
                          yAxis: 2.45,
                        }
                      ]
                    }
                  },
                  {
                    xAxisIndex: 1,
                    yAxisIndex: 1,
                    name: "成交量",
                    data: volumeList,
                    type: "bar",
                    itemStyle: {
                      color: '#25258e',
                      opacity: 0.5,
                    },
                  },
                ]
              },true);
            window.addEventListener("resize", function() {
                that.myChart.resize();
              });
            this.myChart.hideLoading();
          },
          initDayK() {
            const data = {
              values: [],
              dates: [],
              volumes: [],
            }
            function calculateMA(dayCount, data) {
              const result = [];
              for (let i = 0, len = data.values.length; i < len; i++) {
                      if (i < dayCount) {
                          result.push('-');
                          continue;
                      }
                      let sum = 0;
                      for (let j = 0; j < dayCount; j++) {
                          sum += data.values[i - j][1];
                      }
                      result.push(+(sum / dayCount).toFixed(3));
                  }
                  return result;
              };
            this.dayKData.map(item => {
              data.dates.push(item.d);
              data.values.push([
                parseFloat(item.o), parseFloat(item.c), parseFloat(item.l), parseFloat(item.h), 
              ]);
              data.volumes.push(parseInt(item.v))
            });
            this.dayKOption = {
              animation: false,
              legend: {
                  bottom: 10,
                  left: 'center',
                  data: [, 'MA5', 'MA10', 'MA20', 'MA30']
              },
              tooltip: {
                  trigger: 'axis',
                  axisPointer: {
                      type: 'cross'
                  },
                  backgroundColor: "rgba(245, 245, 245, 0.8)",
                  borderWidth: 1,
                  borderColor: "#ccc",
                  padding: 10,
                  textStyle: {
                    color: "#000"
                  },
                  extraCssText: "width: 170px",
                  formatter(params, ticket, callback) {
                    let str = '' ;
                    str  += params[0].name;
                    params.map((item)=> {
                      let temp = '';
                      if(item.seriesType === "candlestick") {
                        str += `<br />${item.marker}<span style="font-size: 15px;">${item.seriesName}</span>
                                <br />开盘:${item.value[1]}
                                <br />收盘:${item.value[2]}
                                <br />最低:${item.value[3]}
                                <br />最高:${item.value[4]}`
                      } else if(item.seriesName === 'Volumn'){
                        str += `<br />${item.marker}成交量:${item.value}`
                      } else {
                        str += `<br />${item.marker}${item.seriesName}:${item.value}` 
                      }
                    });
                    return str;
                }
              },
              axisPointer: {
                  link: {xAxisIndex: 'all'},
                  label: {
                      backgroundColor: '#777'
                  }
              },
              grid: [
                  {
                      left: '10%',
                      right: '8%',
                      height: '50%'
                  },
                  {
                      left: '10%',
                      right: '8%',
                      bottom: '20%',
                      height: '15%'
                  }
              ],
              xAxis: [
                  {
                      type: 'category',
                      data: data.dates,
                      scale: true,
                      boundaryGap : false,
                      axisLine: {onZero: false},
                      splitLine: {show: false},
                      splitNumber: 20,
                      min: 'dataMin',
                      max: 'dataMax',
                      axisPointer: {
                          z: 100
                      }
                  },
                  {
                      type: 'category',
                      gridIndex: 1,
                      data: data.dates,
                      scale: true,
                      boundaryGap : false,
                      axisLine: {onZero: false},
                      axisTick: {show: false},
                      splitLine: {show: false},
                      axisLabel: {show: false},
                      splitNumber: 20,
                      min: 'dataMin',
                      max: 'dataMax',

                  }
              ],
              yAxis: [
                  {
                      scale: true,
                      splitArea: {
                          show: true
                      }
                  },
                  {
                      scale: true,
                      gridIndex: 1,
                      splitNumber: 2,
                      axisLabel: {show: false},
                      axisLine: {show: false},
                      axisTick: {show: false},
                      splitLine: {show: false}
                  }
              ],
              dataZoom: [
                  {
                      type: 'inside',
                      xAxisIndex: [0, 1],
                      start: 50,
                      end: 100
                  },
                  {
                      show: true,
                      xAxisIndex: [0, 1],
                      type: 'slider',
                      top: '85%',
                      start: 50,
                      end: 100
                  }
              ],
              series: [
                  {
                      name: this.selectedItem.name,
                      type: 'candlestick',
                      data: data.values,
                      itemStyle: {
                          normal: {
                              color0: '#FA0000',
                              color: '#06B800',
                              borderColor: null,
                              borderColor0: null
                          }
                      },
                  },
                  {
                      name: 'MA5',
                      type: 'line',
                      data: calculateMA(5, data),
                      smooth: true,
                      lineStyle: {
                          normal: {opacity: 0.5}
                      }
                  },
                  {
                      name: 'MA10',
                      type: 'line',
                      data: calculateMA(10, data),
                      smooth: true,
                      lineStyle: {
                          normal: {opacity: 0.5}
                      }
                  },
                  {
                      name: 'MA20',
                      type: 'line',
                      data: calculateMA(20, data),
                      smooth: true,
                      lineStyle: {
                          normal: {opacity: 0.5}
                      }
                  },
                  {
                      name: 'MA30',
                      type: 'line',
                      data: calculateMA(30, data),
                      smooth: true,
                      lineStyle: {
                          normal: {opacity: 0.5}
                      }
                  },
                  {
                      name: 'Volumn',
                      type: 'bar',
                      xAxisIndex: 1,
                      yAxisIndex: 1,
                      data: data.volumes
                  }
              ]
            };
            this.myChart.setOption(this.dayKOption,true);
            this.myChart.hideLoading();

          },
          chartFresh() {
            if(this.dayActive){
              return false;
            }
            const that = this;
            const XDate = this.lineData.map(item => {
                   return item.i;
                  })
            const priceList = this.lineData.map((item) => {
              return item.p;
            });
            const volumeList = this.lineData.map((item) => {
              return item.v;
            });
            this.myChart.setOption({
              xAxis: [
                {
                  data: XDate,
                },
                {
                  data: XDate,
                }
              ],
              series: [
                {
                  data: priceList,
                },
                {
                  data: volumeList,
                }
              ]
            });
          },
        },
        created:function () {

        },
        watch: {

        },
        mounted() {
         this.myChart = echarts.init(this.$refs.myechart);
         this.initLine();
        },
        destroyed() {
          clearInterval(this.Interval);
        },
        
    }
</script>

<style scoped>
  a{
    border-left: 1ch dotted rgb(189, 187, 187)
  }
  .OptionsItem{
    padding:3px;
  }
  .up{
    text-align: right;
  }
  .down{
    text-align: left;
  }
  .OptionsItem:hover{
    background: rgb(221, 221, 221);
    cursor: pointer;
  }
  .selected{
    padding: 0!important;
    border: 3px solid darkcyan;
  }
  .isActual{
    background: rgb(255, 234, 241);
  }
  #page{
    min-width: 910px;
  }
  #mainContract{
    position: absolute;
    right: 10%;
    top: 10%;
  }
  #selecter {
    font-size: 14px;
  display: inline-block;
  padding: 4px;
  border: 1px solid #e1e1e1;
  background-color: #fff;
  border-radius: 2px;
  margin: 10px;
}
.selectItem {
  display: inline-block;
  padding: 4px 12px;
  cursor: pointer;
}
.selectItem:hover {
  background-color: #e1e1e1;
}
.active {
  background-color: #25258e !important;
  color: white;
}
#myEchart{
    height: 450px;
  }
.dataList {
  border-top: 1px dotted grey;
  line-height: 30px;
}
@media screen and (max-width: 1050px) {
  #myEchart{
    height: 300px;
  }
}
</style>
