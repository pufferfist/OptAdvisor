<template>
  <div>
    <p>
      Name: {{ value }}
    </p>
    <p>
      <Button @click="handleRender">Custom content</Button>
    </p>
  </div>


</template>

<script>
  import logo from "../components/util/Logo"
  export default {
    name:'test',
    data () {
      return {
        value: ''
      }
    },
    methods: {
      handleRender () {
        this.$Modal.confirm({
          title: '重命名组合',
          okText:'确认',
          cancelText:'取消',
          render: (h) => {
            return h('Input', {
                props: {
                  value: this.value,
                  autofocus: true,
                  placeholder: 'Please enter your name...'
                },
                on: {
                  input: (val) => {
                    this.value = val;
                  }
                }
              })
          }
        })
      }
    }
  };
</script>
<style>
</style>
