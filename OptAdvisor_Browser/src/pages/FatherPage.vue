<template>
      <Layout class="h-100 bg-white tc">
        <Header ref="header" class="header dib">
          <NavBar class="nav"></NavBar>
        </Header>
        <Content class="content mh1">
          <router-view class="child"></router-view>
        </Content>
        <Footer class="dib footer">
          <my-footer></my-footer>
        </Footer>
        <BackTop></BackTop>
      </Layout>
</template>

<script>
    import NavBar from "../components/util/NavBar";
    import MyFooter from "../components/util/MyFooter";
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
    z-index: 1000;
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
    margin: auto;
    width: 65%;
    min-height: 1000px;
    background-color: #ffffff;
  }

  .footer{
    background-color: #ffffff;
  }
  @media screen and (max-width: 616px) {
    .content{
      width: 90%;
    }
  }
</style>
