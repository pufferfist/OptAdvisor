<template>
  <div class="fl">
    <Row>
      <div class="grid">a</div>
      <div class="grid">a</div>
      <div class="grid">a</div>
    </Row>
    <Row>
      <div class="grid">a</div>
      <div class="grid">a</div>
      <div class="grid">a</div>
    </Row>
    <Row>
      <div class="grid">a</div>
      <div class="grid">a</div>
      <div class="grid">a</div>
    </Row>
  </div>
</template>

<script>
    export default {
        name: "al-infoCollect"
    }
</script>

<style scoped>
  div{
    font-size: 0;
  }
  .grid{
    display: inline-block;
    height: 60px;
    width: 60px;
    background-color: #9eebcf;
    border: #F8F8FF;
    border-style: solid;
    border-width: 1px;
    border-radius: 0.25rem;
    }
</style>
