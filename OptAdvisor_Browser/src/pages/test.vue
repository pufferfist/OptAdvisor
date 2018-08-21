<template>
<button @click="test">XXXX</button>
</template>

<script>
  export default {
    name:'test',
methods:{
  a(callback) {
    setTimeout(function () {
      console.log("aaaaaaaaa");
      callback()//////////////
    }, 1000);

  },
  b() {
    console.log("bbbbbbbbbb");
  },
  test(){
    console.log("进来了");
    this.a(function () {////////
      console.log("ppppppppppp");
      this.b();
    });
  }
}
  };
</script>
<style>

</style>
