<template>
  <div>
    <p class="xx">XXX</p>
    <button onclick="test">XXX</button>
  </div>
</template>

<script>
    export default {
        name: "test",
      data () {

      },
      methods: {
        next(){
          alert($(".xx").val())
        }
      }

    }

    $(function () {
      $(".xx").click(function () {
        alert(1)
      })
    })

</script>

<style scoped>

</style>
