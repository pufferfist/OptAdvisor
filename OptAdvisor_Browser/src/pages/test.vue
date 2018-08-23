<template>
  <div>
    <span v-show="show" @click="getCode">获取验证码</span>
    <span v-show="!show" class="count">{{count}} s</span>
  </div>
</template>

<script>
  export default {
    name:'test',
    data(){
      return {
        show: true,
        count: '',
        timer: null,
      }
    },
    methods:{
      getCode(){
        const TIME_COUNT = 6;
        if (!this.timer) {
          this.count = TIME_COUNT;
          this.show = false;
          this.timer = setInterval(() => {
            if (this.count > 0 && this.count <= TIME_COUNT) {
              this.count--;
            } else {
              this.show = true;
              clearInterval(this.timer);
              this.timer = null;
            }
          }, 1000)
        }
      }
    }
  };
</script>
<style>

</style>
