<template>
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
</template>
<script>
    export default {
        data(){
            return {
                SO: {
                    code: ''
                }
            }
        },
        props: {
            optionCode: String,
            time: Object
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
        },
        methods: {
            getOptionData(id) {
                this.axios
                .get('/sinaOption/list='+id.replace('hq_str_','').replace('OP','SO'))
                .then(res => {
                    const tempSO = {};
                    [, , , ,tempSO.count,tempSO.delta, tempSO.gamma, tempSO.theta, tempSO.vega, tempSO.volatility, tempSO.high, tempSO.low, tempSO.code, tempSO.price , tempSO.timeP ,tempSO.value] = res.data.split(',');
                    this.SO = tempSO;
                })
            }
          },
          watch: {
            optionCode:function(cur, old){
                if(cur !==''){
                    this.getOptionData(cur);
                }
            }
          }
        }
</script>
