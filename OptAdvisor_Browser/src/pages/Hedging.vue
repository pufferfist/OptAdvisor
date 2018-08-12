<template>
  <div style="height: 100%">
    <div class="demo-split">
      <Split v-model="split1">
        <div slot="left" class="demo-split-pane">
          <collect_info></collect_info>
        </div>
        <div slot="right" class="demo-split-pane">
          <display_result></display_result>
        </div>
      </Split>
    </div>
  </div>

</template>

<script>
  import collect_info from "../components/Hedging/collect_info";
  import display_result from "../components/Hedging/display_result"
    export default {
        name: "Hedging",
      components:{collect_info, display_result},
      data () {
        return {
          split1: 0.2
        }
      },
    }
</script>

<style scoped>
  .demo-split{
    height:100%;
    border: 1px solid #dcdee2;
  }
  .demo-split-pane{
    padding: 10px;
  }
</style>
