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
        <Right></Right>
      </div>
    </div>
</template>

<script>
  import Right from "../components/myPortfolio/Option";
    export default {
        name: "MyPortfolio",
      components:{Right},
      data () {
        return{
          zichan:[
            {
              name:'555'
            },
            {
              name:'666'
            }
          ],
          taoqi:[
            {
              name:'111'
            },
            {
              name:'222'
            },
            {
              name:'555'
            },
            {
              name:'666'
            }
          ],
          diy:[],
          current_clicked_id:'',
          newName:''
        }
      },
      methods:{
        click_left(name){
            alert(name)
             //alert(this.zichan[name].name)
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
                //1.判断可不可以重命名
                //2.重命名
                if(id[0]=='1'){
                  this.zichan[id.substr(2)].name=newName
                }
                else if(id[0]=='2'){
                  this.taoqi[id.substr(2)].name=newName
                }
                else if(id[0]=='3'){
                  this.diy[id.substr(2)].name=newName
                }
                //3.修改后台数据
                this.current_clicked_id=''
                /***************************************************/
                this.$Message.info('重命名成功');
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

            var result=[]
           var index=id.substr(2)
           for(var i=0;i<newArray.length;i++){
             if(i==index){
               continue;
             }
             result.push(newArray[i]);
           }

           if(id[0]=='1'){
             this.zichan=result
           }
           else if(id[0]=='2'){
             this.taoqi=result
           }
           else{
             this.diy=result
           }
          },
      }
    }
</script>

<style scoped>

</style>
