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
          <div style="text-align: left;margin:5px 0; border-bottom: 4px solid  #4bbc7c;">
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
        <Col :span="2" style="color: grey;margin:5px 0;">
            行权价
        </Col>
        <Col span="11" >
          <Row style="text-align: left; margin:5px 0; color: grey">
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
            <option-detail :time="time" :optionCode="selectedItem.id"/>
          </Col>
          <Col offset="2" span="13">
              <option-chart :optionCode="selectedItem.id" />
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
              <Row v-for="i in 5" :key="i" class="dataList" >
                <Col span="8"><p style=" text-align: left;"> 买{{6-i}}</p></Col>
                <Col span="8"><p style="text-align: center;font-weight: 700;"  >{{buyList[5-i].price}}</p></Col>
                <Col span="8"><p style=" text-align: right;font-weight: 700;"  >{{buyList[5-i].volumes}}</p></Col>
              </Row>
            </Col>
            <Col offset="3" span="9">
              <Row v-for="i in 5" :key="i" class="dataList" >
                <Col span="8"><p style=" text-align: left;"> 卖{{6-i}}</p></Col>
                <Col span="8"><p style="text-align: center;font-weight: 700;"  >{{saleList[i-1].price}}</p></Col>
                <Col span="8"><p style=" text-align: right;font-weight: 700;"  >{{saleList[i-1].volumes}}</p></Col>
              </Row>
            </Col>
          </Row>
        </div>
      </div>
  </div>
</template>

<script>
    import MonthSelecter from "../components/50ETFOption/MonthSelecter";
    import OptionDetail from "../components/50ETFOption/OptionDetail";
    import OptionChart from "../components/50ETFOption/OptionChart";
    export default {
        name: "Display50ETFOption",
        components: {MonthSelecter, OptionDetail, OptionChart},
        data() {
          return {
            time: {
              date: '',
              differ: 0,
              ETFPrice: 0,
            },
            upList: [],
            downList: [],
            OptionsUpList: [],
            OptionsDownList: [],
            selectedItem: {},
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
            mainUpOption: {code: '0'},
            saleList: [{price:0, volume:0},{price:0, volume:0},{price:0, volume:0},{price:0, volume:0},{price:0, volume:0},],
            buyList: [{price:0, volume:0},{price:0, volume:0},{price:0, volume:0},{price:0, volume:0},{price:0, volume:0},]
          }
        },

        computed: {
          
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
            this.selectedItem = {id:'', name: ''};
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
                  const name = resData[i].split(' ')[1].split('=')[0].replace('hq_str_','');
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
                  const name = resData[i].split(' ')[1].split('=')[0].replace('hq_str_','');
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
              this.OptionsUpList = tempOptionsUpList;
              this.OptionsDownList = tempOptionsDownList;
              if(this.selectedItem.name === '') {
                this.selectedItem = tempOptionsUpList[0];
                this.saleList = tempOptionsUpList[0].saleList;
                this.buyList = tempOptionsUpList[0].buyList;
              }
              this.getMainOptionData();
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
            this.axios.get('/sinaOption/list='+ tempmainUpOption.id.replace('hq_str_CON_OP','CON_ZL'))
                .then(res => {
                  console.log( res.data.split(','),54654654);
                  const tempOption = {};
                  [, , , ,tempOption.openInterest,tempOption.openInterestRate, tempOption.new, tempOption.increase, tempOption.buyPrice, tempOption.salePrice, tempOption.high, tempOption.low, tempOption.volume, tempOption.delta , tempOption.gamma ,tempOption.theta, tempOption.vega, tempOption.volatility, tempOption.code, tempOption.price, tempOption.value] = res.data.split(',');
                  this.mainUpOption = tempOption;
                });
            this.axios.get('/sinaOption/list='+ tempmainDownOption.id.replace('hq_str_CON_OP','CON_ZL'))
                .then(res => {
                  const tempOption = {};
                  
                  [, , , ,tempOption.openInterest,tempOption.openInterestRate, tempOption.new, tempOption.increase, tempOption.buyPrice, tempOption.salePrice, tempOption.high, tempOption.low, tempOption.volume, tempOption.delta , tempOption.gamma ,tempOption.theta, tempOption.vega, tempOption.volatility, tempOption.code, tempOption.price, tempOption.value] = res.data.split(',');
                  this.mainDownOption = tempOption;
                })
          },
          handleOptionChange(e) {
            this.selectedItem = e;
            this.saleList = e.saleList;
            this.buyList = e.buyList;
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

</style>
