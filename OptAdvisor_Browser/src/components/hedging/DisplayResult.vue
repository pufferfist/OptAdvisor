<template>
    <div>
      <p style="font-weight: bold;font-size: 25px">套期保值效果展示&nbsp&nbsp<Button type="primary" size="small" @click="addToMyGroup">添加至我的组合</Button></p>
      <div style="text-align: left">
        <div>
          <p style="font-size: 20px;font-weight: bold;padding-left: 20px">展示期权：</p>
          <optionGroup ref="option_group"></optionGroup>
        </div>
        <br>
        <div>
          <span style="font-size: 20px;font-weight: bold;padding-left: 20px">到达预期最大亏损：<p style="-webkit-text-fill-color: red;display: inline">{{expectedLoss}}</p></span>
        </div>
        <br>
        <div>
          <p style="font-size: 20px;font-weight: bold;padding-left: 20px">组合表现：</p>
          <div id="myChart" style="width: 700px;height: 300px"></div>
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
          lineName:['持有','不持有','两者之差'],
          groupName:'',
          originData:'',
          n:'',
          pAsset:'',
          sExp:'',
          flag:''
        }
      },
      methods: {
        drawLine(){
          // 基于准备好的dom，初始化echarts实例
          let myChart = this.$echarts.init(document.getElementById('myChart'))
          // 绘制图表
          myChart.setOption({
            tooltip: {
              trigger: 'axis'
            },
            legend: {
              data:this.lineName
            },
            xAxis: {
              type: 'category',
              data: this.graph[0]
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              name:this.lineName[0],
              data: this.graph[1],
              type: 'line'
            },
              {
                name:this.lineName[1],
                data: this.graph[2],
                type: 'line'
              },
              {
                name:this.lineName[2],
                data: this.graph[3],
                type: 'line'
              }]
          });
        },
        addToMyGroup(){
          this.$Modal.confirm({
            title: '新建组合',
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

              this.axios.post('/backend/portfolio',data)
                .then(re=>{
                  if(re.data.msg=='Add portfolio success'){
                    this.$Message.info('添加成功');
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
      }

    }
</script>

<style scoped>

</style>
