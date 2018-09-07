<template xmlns:v-contextmenu="http://www.w3.org/1999/xhtml">
    <div>
      <!--右键菜单-->
      <v-contextmenu ref="contextmenu">
        <v-contextmenu-submenu title="新建组合">
          <v-contextmenu-item @click="newZiyuanGroup">新建资源配置组合</v-contextmenu-item>
          <v-contextmenu-item @click="newTaoqiGroup">新建套期保值组合</v-contextmenu-item>
          <v-contextmenu-item @click="newDiyGroup">新建我的DIY组合</v-contextmenu-item>
        </v-contextmenu-submenu>
        <v-contextmenu-item @click="deleteGroup">删除组合</v-contextmenu-item>
        <v-contextmenu-item @click="renameGroup">重命名组合</v-contextmenu-item>
      </v-contextmenu>

      <div style="float: left;width: 25%;min-height: 500px">
        <Menu  :open-names="['资产配置']" accordion @on-select="click_left" style="float: left">
          <Submenu name="资产配置" id="ZiChan">
            <template slot="title">
              <Icon type="logo-bitcoin" />
              资产配置组合
            </template>
            <div v-for="(zi,index) in zichan" :id=getName(1,index) @contextmenu.prevent="click_right">
              <MenuItem :name=getName(1,index) v-contextmenu:contextmenu>{{zi.name}}</MenuItem>
            </div>
          </Submenu>
          <Submenu name="套期保值" id="TaoQi">
            <template slot="title">
              <Icon type="logo-usd" />
              套期保值组合
            </template>
            <div v-for="(tao,index) in taoqi" :id=getName(2,index) @contextmenu.prevent="click_right">
              <MenuItem :name=getName(2,index) v-contextmenu:contextmenu>{{tao.name}}</MenuItem>
            </div>
          </Submenu>
          <Submenu name="我的DIY" id="DIY">
            <template slot="title">
              <Icon type="ios-bulb" />
              我的DIY组合
            </template>
            <div v-for="(d,index) in diy" :id=getName(3,index) @contextmenu.prevent="click_right">
              <MenuItem :name=getName(3,index) v-contextmenu:contextmenu>{{d.name}}</MenuItem>
            </div>
          </Submenu>
        </Menu>
        <div style="float: left;width: 1px;height: 800px;background-color: #E7E8EB"></div>
      </div>
      <div style="float: left;width: 75%;min-height: 500px;">
        <div class="demo-spin-container">
          <Right ref="portfolio" id="right" v-bind:style="{display:showRight}"></Right>
          <Spin v-if="showInfo" fix>
            <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
            <div>加载中</div>
          </Spin>
        </div>
      </div>
    </div>
</template>

<script>
  import Right from "../components/myPortfolio/Option";
    export default {
        name: "MyPortfolio",
      components:{Right},
      mounted(){
          this.initial()
      },
      data () {
        return{
          zichan:[],
          taoqi:[],
          diy:[],
          allPortfolioData:[],
          current_clicked_id:'',
          newName:'',
          showRight:'none',
          showInfo:false
        }
      },
      methods:{
        //该方法还未集成
        click_left(name){
          this.showInfo=true
          var suffix=name.substr(2)
          var id
          if(name[0]=='1'){
            id=this.zichan[suffix].id
          }
          else if(name[0]=='2'){
            id=this.taoqi[suffix].id
          }
          else if(name[0]=='3'){
            id=this.diy[suffix].id
          }
          this.axios.get('/backend/portfolio/'+id)
            .then(re=>{
              this.$refs.portfolio.initial(re.data)
              this.showRight=''
              setTimeout(() => {
                this.showInfo = false;
              }, 2000);
            })
        },
        click_right(){
          this.current_clicked_id=event.currentTarget.id;
        },
        getName(order,index){
            return order+"-"+index
         },
        newZiyuanGroup(){
          this.$router.push("/allocation")
        },
        newTaoqiGroup(){
          this.$router.push("/hedging")
        },
        newDiyGroup(){
          this.$router.push("/diy")
        },
        renameGroup(){
            //1.获取原来的名称
            var id=this.current_clicked_id
            var html1=document.getElementById(id).innerHTML
            var name=html1.substr(html1.indexOf(">")+1)
            name=name.substr(0,name.indexOf("<"))
            var newName;

            this.$Modal.confirm({
              title: '重命名组合',
              okText:'确认',
              cancelText:'取消',
              render: (h) => {
                return h('Input', {
                  props: {
                    value: name,
                    autofocus: true,
                    placeholder: 'Please enter your name...'
                  },
                  on: {
                    input: (val) => {
                      newName= val;
                    }
                  }
                })
              },
              onOk: () => {
                if(id[0]=='1'){
                  this.axios.put('/backend/portfolio/'+this.diy[id.substr(2)].id+'/name',{name:newName})
                    .then(re=>{
                      if(re.data.msg=='Update portfolio name success'){
                        this.$Message.success("重命名成功")
                        this.zichan[id.substr(2)].name=newName
                        this.click_left(id)
                      }
                      else{
                        this.$Message.error("重命名失败")
                      }
                    })
                }
                else if(id[0]=='2'){
                  this.axios.put('/backend/portfolio/'+this.diy[id.substr(2)].id+'/name',{name:newName})
                    .then(re=>{
                      if(re.data.msg=='Update portfolio name success'){
                        this.$Message.success("重命名成功")
                        this.taoqi[id.substr(2)].name=newName
                        this.click_left(id)
                      }
                      else{
                        this.$Message.error("重命名失败")
                      }
                    })
                }
                else if(id[0]=='3'){
                  this.axios.put('/backend/portfolio/'+this.diy[id.substr(2)].id+'/name',{name:newName})
                    .then(re=>{
                      if(re.data.msg=='Update portfolio name success'){
                        this.$Message.success("重命名成功")
                        this.diy[id.substr(2)].name=newName
                        this.click_left(id)
                      }
                      else{
                        this.$Message.error("重命名失败")
                      }
                    })
                }
                this.current_clicked_id=''
              },
              onCancel: () => {
                this.current_clicked_id=''
              }
            });
           },
        deleteGroup(){
            var id=this.current_clicked_id;
            var newArray
            if(id[0]=='1'){
              newArray=this.zichan
            }
            else if(id[0]=='2'){
              newArray=this.taoqi
            }
            else{
              newArray=this.diy
            }

            //在界面上删除
            var result=[]
            var index=id.substr(2)
             for(var i=0;i<newArray.length;i++){
               if(i==index){
                continue;
              }
               result.push(newArray[i]);
            }

          //在后台删除并赋值
          var portfolioID
          if(id[0]=='1'){
            portfolioID= this.zichan[index].id
            this.zichan=result
           }
          else if(id[0]=='2'){
            portfolioID= this.taoqi[index].id
            this.taoqi=result
          }
          else{
            portfolioID= this.diy[index].id
            this.diy=result
          }
          this.axios.delete('/backend/portfolio/'+portfolioID)
            .then(re=>{
              if(re.data.msg !='Delete portfolio success'){
                alert("删除失败")
              }
            })
          },
        initial(){
          //1.获取所有的组合数据
          this.axios.get('/backend/portfolio')
            .then(re=>{
              this.allPortfolioData=re.data.data
              var tempData=re.data.data
              var zichan=[]
              var taoqi=[]
              var diy=[]
              for(var i=0;i<tempData.length;i++){
                var temp=tempData[i]
                var object={}
                object.name=temp.name
                object.id=temp.id
                if(temp.type==0){
                  zichan.push(object)
                }
                else if(temp.type==1){
                  taoqi.push(object)
                }
                else if(temp.type==2){
                  diy.push(object)
                }
              }
              this.zichan=zichan
              this.taoqi=taoqi
              this.diy=diy
            })
        }
      }
    }
</script>

<style scoped>

  .demo-spin-icon-load{
    animation: ani-demo-spin 1s linear infinite;
  }
  @keyframes ani-demo-spin {
    from { transform: rotate(0deg);}
    50%  { transform: rotate(180deg);}
    to   { transform: rotate(360deg);}
  }
  .demo-spin-container{
    display: inline-block;
    width: 100%;
    min-height: 500px;
    position: relative;
  }
</style>
