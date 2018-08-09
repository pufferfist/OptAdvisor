<template>
  <ul>
    <template v-for="item in items">
      <li>
        <a :href="item.address" class="link">{{item.name}}</a>
      </li>
    </template>
  </ul>
</template>

<script>
    export default {
      name: "LinkGroup",
      props: ["items"],
    }
</script>

<style scoped>
  .link{
    color: #000000;
    text-decoration: none;
  }

  .link:hover{
    color: red;
  }

  .link:focus{
    outline: none;
  }

  ul,li{
    list-style: none;
  }
</style>
