<template>
    <Row>
      <Col class="box">
        <al-info-collect class="boxChild"></al-info-collect>
      </Col>
    </Row>
</template>

<script>
    import AlInfoCollect from "../components/allocation/al-infoCollect";
    export default {
        name: "Allocation",
      components: {AlInfoCollect}
    }
</script>

<style scoped>
  .box{
    display: table;
    height: 600px;
  }

  .boxChild{
    display: table-cell;
    vertical-align: middle;
  }
</style>
