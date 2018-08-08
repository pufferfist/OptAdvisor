<template>
      <Menu mode="horizontal" :theme="theme" active-name="1" class="nav">
        <div class="fl dib h-100 logo pa1 mr4">
          <router-link to="/home" class="h-100 dib">
            <Logo class="h-100"></Logo>
          </router-link>
        </div>
        <div class="fl dib">
          <p>个性化投顾</p>
        </div>
        <div class="fr dib">
          <Submenu name="1">
            <template slot="title">
              <Icon type="md-analytics" />
              市场行情
            </template>
              <MenuItem name="1-1">50ETF</MenuItem>
              <MenuItem name="1-2">50ETF期权</MenuItem>
          </Submenu>
          <MenuItem name="2">
            <Icon type="ios-people" />
            构建组合
          </MenuItem>
          <Submenu name="3">
            <template slot="title">
              <Icon type="ios-stats" />
              统计分析
            </template>
            <MenuGroup title="使用">
              <MenuItem name="3-1">新增和启动</MenuItem>
              <MenuItem name="3-2">活跃分析</MenuItem>
              <MenuItem name="3-3">时段分析</MenuItem>
            </MenuGroup>
            <MenuGroup title="留存">
              <MenuItem name="3-4">用户留存</MenuItem>
              <MenuItem name="3-5">流失用户</MenuItem>
            </MenuGroup>
          </Submenu>
          <MenuItem name="4" class="tr">
            <Icon type="ios-construct" />
            综合设置
          </MenuItem>
        </div>
      </Menu>
</template>

<script>
    import Logo from "./Logo";
    export default {
      name: "NavBar",
      components: {Logo},
      data () {
        return {
          theme: 'light'
        }
      }

    }
</script>

<style scoped>
  Logo{
    height: 100%;
  }

  .logo:after{
    content: "";
    display: block;
    width: 1px;
    height: 24px;
    background: #ebedee;
    position: absolute;
    top: 20px;
    left: 273px;
  }
</style>
