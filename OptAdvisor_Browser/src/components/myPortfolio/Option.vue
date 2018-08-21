<template>
    <div>
      <div>
        <h1>{{name}}
          <span style="font-size: 15px">
          创建时间：{{time}} &nbsp&nbsp 分组类型：{{type}} &nbsp&nbsp 期望收益：{{earnings}} &nbsp&nbsp&nbsp
        </span>
          <Button type="primary">添加跟踪</Button>
        </h1>
      </div>

      <div style="width: 100%;text-align: center">
        <table style="margin: auto">
          <tr>
            <th>序号</th>
            <th>合约代码</th>
            <th>合约名称</th>
            <th>成本价</th>
            <th>最新价</th>
          </tr>
          <tr v-for="(item,index) in tdata" @click="choose(index)" :id="getName(index)">
            <td>{{index+1}}</td>
            <td v-for="i in item">{{i}}</td>
          </tr>
        </table>
        <p>成本：{{text1}}&nbsp&nbsp保证金：{{text2}}&nbsp&nbsp市值：{{text3}}&nbsp&nbsp盈亏：{{text4}}</p>
      </div>

      <display_result ref="result"></display_result>

    </div>
</template>

<script>
  import display_result from "../myPortfolio/displayResult";
    export default {
        name: "Option",
      components:{display_result},
      data () {
        return {
          name:'我的组合',
          time:'2018-09-09',
          type:'diy',
          earnings:'5230',
          tdata:[
            [123,123,123,123],
            [123,123,123,123],
            [123,123,123,123],
            [123,123,123,123]
          ],
          lastSelectedLineIndex:0,
          text1:'1111',
          text2:'1111',
          text3:'1111',
          text4:'1111'
        }
      },
      methods:{
          getName(index){
            return 'tr'+index
          },
          choose(index){
            //1.恢复线条
            document.getElementById("tr"+this.lastSelectedLineIndex).style.border="none"
            //2.勾画线条
            document.getElementById("tr"+index).style.border="1px solid #2db7f5"
            //3.赋值
            this.lastSelectedLineIndex=index
            //4.初始化displayResult
            this.$refs.result.getSingleInfo('up','10001407')
            this.$refs.result.drawLine()
          },
          getLatestPrice(address){
          //address是10004125这种
            this.axios.get('/sinaOption/list=CON_SO_'+address)
              .then(re=>{
                var parts=re.data.substr(re.data.indexOf("=")+2).split(",")
                return parts[14]
              })
        },
          initial(name){
            //1.先根据name在后台拿到相应的数据
            //2.初始化数据
            this.name
            this.time
            this.type
            this.earnings
            this.tdata
            this.text1
            this.text2
            this.text3
            this.text4
            //3.调用getLatestPrice实时的更改表格中最新价数据
            //4.调用choose方法默认选中第一项，同时画echarts
          }
      }

    }
</script>

<style scoped>
  table{
    border-collapse: collapse;
  }
  td{
    padding-left: 5px;
    padding-right: 5px;
    background-color: #ffeeee;
  }
  th{
    padding-left: 5px;
    padding-right: 5px;
    background-color: #f16643;
    -webkit-text-fill-color: #ffffff;
  }
</style>
