<template>
  <div style="height: 100%">
    <div class="demo-split" v-bind:style="{height:demo_spilt_height}">
      <Split v-model="split1">
        <div slot="left" class="demo-split-pane">
          <collect_info ref="info"></collect_info>
          <p style="font-size: 10px;" v-bind:style="{'-webkit-text-fill-color':warn_color}">信息填写不完整</p>
          <Button type="primary" @click="search">查询结果</Button>
        </div>
        <div slot="right" class="demo-split-pane">
          <no_input ref="blank" v-bind:style="{display:ispage1}"></no_input>
          <display_result ref="result" v-bind:style="{display:ispage2}"></display_result>
        </div>
      </Split>
    </div>
  </div>

</template>

<script>
  import collect_info from "../components/Hedging/CollectInfo";
  import display_result from "../components/Hedging/DisplayResult"
  import no_input from "../components/Hedging/NoInput"
    export default {
        name: "Hedging",
      components:{collect_info, display_result, no_input},
      data () {
        return {
          split1: 0.3,
          demo_spilt_height:'500px',
          ispage1:'',
          ispage2:'none',
          warn_color:'#ffffff'
        }
      },
      methods:{
          search(){
            var array=this.$refs.info.$refs.formItem.fields
            var OpenInterest=array[0].fieldValue
            var rate=array[1].fieldValue
            var deadline=array[2].fieldValue
            var min_price=array[3].fieldValue
            if((OpenInterest=='')||(rate=='')||(deadline=='')||(min_price=='')||(min_price=='¥')){
              this.warn_color="#ed4014"
            }
            else{
              this.warn_color="#ffffff"
              this.ispage1="none"
              this.ispage2=""
              this.demo_spilt_height="700px"
              var deadline_value=this.$refs.info.formItem.month[deadline.substr(5)-1]

              //写入数据
              this.axios.post('/backend/recommend/hedging',{
                NO:OpenInterest,
                a:rate,
                s_exp:min_price,
                T:deadline_value
              }).then((response)=>{
                var data=response.data
                /************************还没添加内容***************************/
                this.$refs.result.data1[0].id=data.id
              })
            }
          }
      }
    }
</script>

<style scoped>
  .demo-split{
    border: 1px solid #dcdee2;
  }
  .demo-split-pane{
    padding: 10px;
  }
</style>
