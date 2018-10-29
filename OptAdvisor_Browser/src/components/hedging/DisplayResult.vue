<template>
    <div style="width: 100%">
      <p style="font-weight: bold;font-size: 25px">套期保值效果展示&nbsp&nbsp<Button type="primary" size="small" @click="addToMyGroup">添加至我的组合</Button></p>
      <br>
      <div style="text-align: left">
        <div>
          <p style="font-size: 20px;font-weight: bold;margin-left: 10%;-webkit-text-fill-color: #2b85e4">期权展示：</p>
          <div style="width: 80%;height: 2px;background-color: #2b85e4;margin-left: 10%"></div>
          <br>
          <div style="height: 180px;margin-left: 10%;margin-right: 10%">
            <optionGroup ref="option_group" style="width: 60%;float: left;border:2px solid #f8f8f9"></optionGroup>
            <div style="width: 5%;float: left;height: 180px"></div>
            <div style="width: 30%;float: left;-webkit-text-fill-color: #515a6e;font-size: 20px;border:2px solid #f8f8f9;height: 160px">
              <p style="padding-top: 10px">到达预期最大减损比率：</p>
              <p style="font-size: 50px;width: 80%;padding-top:20px;text-align: center;font-weight: bold;display: inline">&nbsp&nbsp{{expectedLoss}}</p><span>%</span>
            </div>
          </div>
        </div>
        <br>
        <div style="width: 100%;text-align: center">
          <p style="font-size: 20px;font-weight: bold;padding-left: 10%;text-align: left;-webkit-text-fill-color: #2b85e4">套期保值效果展示：</p>
          <div style="width: 80%;height: 3px;background-color: #2b85e4;margin-left: 10%"></div>
          <br>
          <div id="myChart" style="width:800px;height: 300px;margin: auto;border:2px solid #f8f8f9"></div>
        </div>
      </div>
    </div>
</template>

<script>
  import optionGroup from "../hedging/OptionTable"
    export default {
        name: "display_result",
      components:{optionGroup},
      data () {
        return {
          expectedLoss:'',
          graph:'',
          lineName:['持有','不持有'],
          groupName:'',
          originData:'',
          n:'',
          pAsset:'',
          sExp:'',
          flag:''
        }
      },
      methods: {
        drawLine(lineName,graph,xName,yName,xFormat,yFormat){
          var series=[]
          for(var i=0;i<lineName.length;i++){
            var temp=   {
              name:lineName[i],
              type:'line',
              smooth:true,
              symbol: 'none',
              sampling: 'average',
              data: graph[i+1].map(function (item) {
                if(yFormat=='%'){
                  return (parseFloat(item)*100).toFixed(2);
                }
                else{
                  return (parseFloat(item).toFixed(4))
                }
              }),
            }
            series.push(temp)
          }

          // 基于准备好的dom，初始化echarts实例
          let myChart = this.$echarts.init(document.getElementById('myChart'))
          // 绘制图表
          myChart.setOption({
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'cross',
                label: {
                  backgroundColor: '#6a7985'
                }
              },
              formatter: xName+":{b}<br/>"+yName+":{c}",
            },
            legend: {
              data:lineName,
              x: 'left'
            },
            xAxis: {
              name:xName,
              nameTextStyle:{
                fontSize:10,
                padding:0
              },
              nameGap:2,
              type: 'category',
              boundaryGap: false,
              data: graph[0].map(function (item) {
                if(xFormat=='%'){
                  return (parseFloat(item)*100).toFixed(2);
                }
                else{
                  return item
                }
              }),
            },
            yAxis: {
              name:yName,
              type: 'value',
              boundaryGap: [0, '100%']
            },
            series: series
          },true);
        },
        addToMyGroup(){
          this.$Modal.confirm({
            //title: '新建组合',
            okText:'确认',
            cancelText:'取消',
            render: (h) => {
              return h('Input', {
                props: {
                  autofocus: true,
                  placeholder: '请输入组合名'
                },
                on: {
                  input: (val) => {
                    this.groupName= val;
                  }
                }
              })
            },
            onOk: () => {
              var data=this.originData
              data.name=this.groupName
              data.trackingStatus=false
              data.type=1
              data.graph=this.originData.graph
              data.iK=this.originData.iK
              data.options=[this.originData.option]

              this.handleSpinCustom()
              this.axios.post('/backend/portfolio',data)
                .then(re=>{
                  if(re.data.msg=='Add portfolio success'){
                    this.$Message.info('添加成功');
                    this.$router.push('/myPortfolio');
                  }
                  else{
                    this.$Message.error('添加失败');
                  }
                })

            },
            onCancel: () => {
              this.current_clicked_id=''
            }
          });
        },
        handleSpinCustom () {
          this.$Spin.show({
            render: (h) => {
              return h('div', [
                h('Icon', {
                  'class': 'demo-spin-icon-load',
                  props: {
                    type: 'ios-loading',
                    size: 18
                  }
                }),
                h('div', '加载中')
              ])
            }
          });
          setTimeout(() => {
            this.$Spin.hide();
          }, 3000);
        }
      }

    }
</script>

<style scoped>
  .demo-spin-icon-load{
    animation: ani-demo-spin 1s linear infinite;
  }
</style>
