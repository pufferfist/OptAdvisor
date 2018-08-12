<template>
  <Row class="pb3 row">
    <Col span="8" class="h-100">
      <div class="message-mainlist-con mw-100 h-100">
        <div>
          <Button @click="setCurrentMesType('unread')" size="large" long type="text">
            <transition name="mes-current-type-btn">
              <Icon v-show="currentMessageType === 'unread'" type="checkmark"></Icon>
            </transition>
            <span class="mes-type-btn-text">未读消息</span>
            <Badge class="message-count-badge-outer" class-name="message-count-badge" :count="unreadCount"></Badge>
          </Button>
        </div>
        <div>
          <Button @click="setCurrentMesType('hasread')" size="large" long type="text">
            <transition name="mes-current-type-btn">
              <Icon v-show="currentMessageType === 'hasread'" type="checkmark"></Icon>
            </transition>
            <span class="mes-type-btn-text">已读消息</span>
          </Button>
        </div>
      </div>
    </Col>
    <Col span="16" class="h-100  ">
      <div class="message-content-con mh3 h-100 min-h">
        <transition name="view-message">
          <div v-if="showMesTitleList" class="message-title-list-con">
            <Table ref="messageList" :columns="mesTitleColumns" :data="currentMesList" :no-data-text="noDataText"></Table>
          </div>
        </transition>
        <transition name="back-message-list">
          <div v-if="!showMesTitleList" class="message-view-content-con">
            <div class="message-content-top-bar">
             <Button class="mes-back-btn-con" type="text" @click="backMesTitleList"><Icon type="chevron-left"></Icon>&nbsp;&nbsp;返回</Button>
              <h3 class="mes-title">{{ mes.title }}</h3>
            </div>
            <p class="mes-time-con"><Icon type="android-time"></Icon>&nbsp;&nbsp;{{ mes.time }}</p>
            <div class="message-content-body">
              <p class="message-content">{{ mes.content }}</p>
            </div>
          </div>
        </transition>
      </div>
    </Col>
    <Modal
      v-model="deleteConfirm"
      title="删除已读"
      @on-ok='ok'>
      <p>确定要删除消息吗?</p>
    </Modal>
  </Row>
</template>

<script>
  export default {
    name: 'prompt',
    data () {
      const markAsreadBtn = (h, params) => {
        return h('Button', {
          props: {
            size: 'small'
          },
          on: {
            click: () => {
              this.hasreadMesList.unshift(this.currentMesList.splice(params.index, 1)[0]);
              this.$store.commit('setMessageCount', this.unreadMesList.length);
            }
          }
        }, '标为已读');
      };
      const deleteMesBtn = (h, params) => {
        return h('Button', {
          props: {
            size: 'small',
            type: 'error'
          },
          on: {
            click: () => {
              this.deleteConfirm = true;
              this.selectedRow = params;
            }
          }
        }, '删除');
      };
      return {
        currentMesList: [],
        unreadMesList: [],
        hasreadMesList: [],
        recyclebinList: [],
        currentMessageType: 'unread',
        showMesTitleList: true,
        unreadCount: 0,
        hasreadCount: 0,
        noDataText: '暂无未读消息',
        mes: {
          title: '',
          time: '',
          content: ''
        },
        deleteConfirm: false,
        selectedRow: null,
        mesTitleColumns: [
          {
            title: ' ',
            key: 'title',
            align: 'left',
            ellipsis: true,
            render: (h, params) => {
              if (this.currentMessageType === 'unread') {
                return h('a', {
                  on: {
                    click: () => {
                      this.showMesTitleList = false;
                      this.mes.title = params.row.title;
                      this.mes.time = this.formatDate(params.row.time);
                      this.getContent(params.index);
                      //添加到已读列表
                      this.hasreadMesList.unshift(this.currentMesList.splice(params.index, 1)[0]);
                      //请求服务器置为已读
                      let id = this.unreadMesList[this.selectedRow.index].id;
                      this.axios.post("/backend/message/setMessageRead",{id:id})
                        .then((res)=>{
                          if(res.data.code!==0){
                            this.$Message.error("something goes wrong")
                          }
                        });
                    }
                  }
                }, params.row.title);
              } else if(this.currentMessageType === 'hasread'){
                return h('a', {
                  on: {
                    click: () => {
                      this.showMesTitleList = false;
                      this.mes.title = params.row.title;
                      this.mes.time = this.formatDate(params.row.time);
                      this.getContent(params.index);
                    }
                  },
                  style:{
                    color: 'black'
                  }
                }, params.row.title);
              }
            }
          },
          {
            title: ' ',
            key: 'time',
            align: 'center',
            width: 180,
            render: (h, params) => {
              return h('span', [
                h('Icon', {
                  props: {
                    type: 'android-time',
                    size: 12
                  },
                  style: {
                    margin: '0 5px'
                  }
                }),
                h('span', {
                  props: {
                    type: 'android-time',
                    size: 12
                  }
                }, this.formatDate(params.row.time))
              ]);
            }
          },
          {
            title: ' ',
            key: 'asread',
            align: 'center',
            width: 100,
            render: (h, params) => {
              if (this.currentMessageType === 'unread') {
                return h('div', [
                  markAsreadBtn(h, params)
                ]);
              } else if (this.currentMessageType === 'hasread') {
                return h('div', [
                  deleteMesBtn(h, params)
                ]);
              }
            }
          }
        ]
      };
    },
    methods: {
      formatDate (time) {
        //待修改
        let date = new Date(time);
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();
        let hour = date.getHours();
        let minute = date.getMinutes();
        let second = date.getSeconds();
        return year + '/' + month + '/' + day + '  ' + hour + ':' + minute + ':' + second;
      },
      backMesTitleList () {
        this.showMesTitleList = true;
      },
      setCurrentMesType (type) {
        if (this.currentMessageType !== type) {
          this.showMesTitleList = true;
        }
        this.currentMessageType = type;
        if (type === 'unread') {
          this.noDataText = '暂无未读消息';
          this.currentMesList = this.unreadMesList;
        } else if (type === 'hasread') {
          this.noDataText = '暂无已读消息';
          this.currentMesList = this.hasreadMesList;
        }
      },
      getContent (index) {
        // you can write ajax request here to get message content
        let mesContent = '';
        switch (this.currentMessageType) {
          case 'unread': mesContent = this.unreadMesList[index].message; break;
          case 'hasread': mesContent = this.hasreadMesList[index].message; break;
        }
        this.mes.content = mesContent;
      },
      ok(){
        this.hasreadMesList.splice(this.selectedRow.index, 1);
        let id = this.hasreadMesList[this.selectedRow.index].id;
        this.axios.post("/backend/message/deleteMessage",{id:id})
          .then((res)=>{
            if(res.data.code!==0){
              this.$Message.error("something goes wrong")
            }
          });
      }
    },
    mounted () {
      /**
       * 请求消息列表
       */
      this.axios.post("/backend/message/getMessage")
        .then((res)=>{
          this.currentMesList = this.unreadMesList = res.data.data.unread;
          this.hasreadMesList = res.data.data.read;
        })
        .catch((error)=>{
          console.log(error)
        });
      this.unreadCount = this.unreadMesList.length;
      this.hasreadCount = this.hasreadMesList.length;
    },
    watch: {
      unreadMesList (arr) {
        this.unreadCount = arr.length;
      },
      hasreadMesList (arr) {
        this.hasreadCount = arr.length;
      }
    }
  };
</script>

<style scoped>
  .row{
    background-color: #f0f0f0;
  }
  .min-h{
    min-height: 600px;
  }
  .message-mainlist-con {
    bottom: 0;
    padding: 10px 0;
  }
  .message-mainlist-con div {
    padding: 10px;
    margin: 0 20px;
    border-bottom: 1px dashed #d2d3d2;
  }
  .message-mainlist-con div:last-child {
    border: none;
  }
  .message-mainlist-con div .message-count-badge-outer {
    float: right;
  }
  .message-mainlist-con div .message-count-badge {
    background: #d2d3d2;
  }
  .message-mainlist-con div:first-child .message-count-badge {
    background: #ed3f14;
  }
  .message-mainlist-con div .mes-type-btn-text {
    margin-left: 10px;
  }
  .message-content-con {
    background: white;
    border-radius: 3px;
    box-shadow: 2px 2px 10px 2px rgba(0, 0, 0, 0.1);
    overflow: auto;
  }
  .message-content-con .message-title-list-con {
    width: 100%;
    height: 100%;
  }
  .message-content-con .message-content-top-bar {
    height: 40px;
    width: 100%;
    background: white;
    border-bottom: 1px solid #dededb;
  }
  .message-content-con .message-content-top-bar .mes-back-btn-con {
    position: absolute;
    width: 70px;
    height: 30px;
    left: 20px;
    top: 5px;
  }
  .message-content-con .message-content-top-bar .mes-title {
    /*position: absolute;*/
    /*top: 0;*/
    /*right: 70px;*/
    /*bottom: 0;*/
    /*left: 70px;*/
    line-height: 40px;
    padding: 0 30px;
    margin: 0 40px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    text-align: center;
  }
  .message-content-con .mes-time-con {
    width: 100%;
    padding: 20px 0;
    text-align: center;
    font-size: 14px;
    color: #b7b7b5;
  }
  .message-content-con .message-content-body {
    overflow: auto;
  }
  .message-content-con .message-content-body .message-content {
    padding: 10px 20px;
  }
  .back-message-list-enter,
  .back-message-list-leave-to {
    opacity: 0;
  }
  .back-message-list-enter-active,
  .back-message-list-leave-active {
    transition: all 0.5s;
  }
  .back-message-list-enter-to,
  .back-message-list-leave {
    opacity: 1;
  }
  .view-message-enter,
  .view-message-leave-to {
    opacity: 0;
  }
  .view-message-enter-active,
  .view-message-leave-active {
    transition: all 0.5s;
  }
  .view-message-enter-to,
  .view-message-leave {
    opacity: 1;
  }
  .mes-current-type-btn-enter,
  .mes-current-type-btn-leave-to {
    opacity: 0;
    width: 0;
  }
  .mes-current-type-btn-enter-active,
  .mes-current-type-btn-leave-active {
    transition: all 0.3s;
  }
  .mes-current-type-btn-enter-to,
  .mes-current-type-btn-leave {
    opacity: 1;
    width: 12px;
  }

</style>
