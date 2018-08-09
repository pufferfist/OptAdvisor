<template>
    <div class="layout">
      <Layout>
        <Header ref="header" class="header dib">
          <NavBar class="nav"></NavBar>
        </Header>
        <Content class="content">
          <router-view class="child"></router-view>
        </Content>
        <Footer class="dib">
          <my-footer></my-footer>
        </Footer>
        <BackTop></BackTop>
      </Layout>
    </div>
</template>

<script>
    import NavBar from "../components/util/NavBar";
    import MyFooter from "../components/util/myFooter";
    export default {
      name: "FatherPage",
      components: {MyFooter, NavBar}
    }
</script>

<style scoped>
  .nav{
    width:100%;
    height:60px;
    position:fixed;
    top:0;left:0;
  }
  .child{
    padding-top: 20px;
  }

  .header{
    background-color: #ffffff;
    padding: 0 50px;
    height: 60px;
    line-height: 60px;
  }

  .content{
    padding: 0 50px;
    background-color: #ffffff;
  }
</style>
