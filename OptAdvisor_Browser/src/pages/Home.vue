<template>

    <div>
      <!-- Header -->

        <router-link to="/login" class="loginButton" v-if="wheel<=600">
          <Button class="forwardButton">
            登录
          </Button>
        </router-link>


      <div class="flex masthead">
        <div class="container tc animated fadeIn" style="margin: auto">
          <h1 class="mb1"><span class="logo1">Opt</span><span class="logo2">Advisor</span></h1>
          <h3 class="mb5">
            <em>让期权投资变得简单</em>
          </h3>
          <a class="btn btn-primary btn-xl" @click="learnMore()">了解更多</a>
          <!--一个button-->
        </div>
      </div>

      <!-- About -->
      <section class="content-section bg-light" id="about" ref="about">
        <div class="container tc">
          <Row>
            <Col class="center">
              <h2>我们的产品</h2>
            </Col>
          </Row>
        </div>
      </section>

      <section class="content-section">
        <div class="container tc">
          <Carousel :autoplay="true" :autoplay-speed="4000">
            <CarouselItem class="h-100">
              <Row class="pv4 ph5 h-100 flex">
                <Col span="12">
                  <img class="img-fluid" src="../../static/graph/homeBack/1.jpg" alt="">
                </Col>
                <Col span="12" class="product">
                  <div class="productTitle"><span>资产配置</span><br/></div>
                  <div class="productContent">
                    <span>
                    为您的资产找出最合适的配置方式<br/>
                    由系统自动生成最佳期权组合
                    </span>
                  </div>
                </Col>
              </Row>
            </CarouselItem>
            <CarouselItem>
              <Row class="pv4 ph5 h-100 flex">
                <Col span="12">
                  <img class="img-fluid" src="../../static/graph/homeBack/3.jpg" alt="">
                </Col>
                <Col span="12" class="product">
                  <div class="productTitle"><span>套期保值</span><br/></div>
                  <div class="productContent">
                    <span>
                    合理选择对冲期权<br/>
                    让您的风险降到最低
                    </span>
                  </div>
                </Col>
              </Row>
            </CarouselItem>
            <CarouselItem>
              <Row class="pv4 ph5 h-100 flex">
                <Col span="12">
                  <img class="img-fluid" src="../../static/graph/homeBack/4.jpg" alt="">
                </Col>
                <Col span="12" class="product">
                  <div class="productTitle"><span>个性化组合</span><br/></div>
                  <div class="productContent">
                    <span>
                      由客户自己选定期权组合<br/>
                      我们提供组合评价<br/>
                      让您在选择组合时更加灵活
                    </span>
                  </div>
                </Col>
              </Row>
            </CarouselItem>
            <CarouselItem>
              <Row class="pv4 ph5 h-100 flex">
                <Col span="12">
                  <img class="img-fluid" src="../../static/graph/homeBack/5.jpg" alt="">
                </Col>
                <Col span="12" class="product">
                  <div class="productTitle"><span>风险跟踪</span><br/></div>
                  <div class="productContent">
                    <span>
                      时时刻刻替您跟踪收益<br/>
                      解放您的双手与大脑
                    </span>
                  </div>
                </Col>
              </Row>
            </CarouselItem>
          </Carousel>
        </div>
      </section>

      <!-- Services -->
      <section class="content-section bg-primary white tc" id="services">
        <div class="container">
          <div class="content-section-heading">
            <h3 class="text-secondary mb0">服务</h3>
            <h2 class="mb5">我们的优势</h2>
          </div>
          <Row>
            <Col class="mb0" span="8" offset="2">
            <span class="service-icon rounded-circle center mb3 verticalCenterFather">
              <Icon type="ios-construct" class="v-base"/>
            </span>
              <h4>
                <strong>个性化定制期权组合</strong>
              </h4>
              <p class="text-faded mb0">智能识别客户的投资偏好，匹配期权组合，轻松解决组合选择难题，千人千面，从客户实际需求出发，贴近客户预期</p>
            </Col>
            <Col class="mb5" span="8" offset="4">
            <span class="service-icon rounded-circle center mb3 verticalCenterFather">
              <Icon type="ios-timer-outline" class="v-base"/>
            </span>
              <h4>
                <strong>透明化展示期权表现</strong>
              </h4>
              <p class="text-faded mb0">组合内容透明公开，使客户投资一目了然，及时反映实时盈亏，步步为营，客户实时掌握资产收益情况</p>
            </Col>
          </Row>
          <Row>
            <Col class="mb5" span="8" offset="8">
            <span class="service-icon rounded-circle center mb3 verticalCenterFather">
              <Icon type="ios-lock" class="v-base"/>
            </span>
              <h4>
                <strong>强大的资产保值体系</strong>
              </h4>
              <p class="text-faded mb0">利用期权非线性收益所具有独特的保值优势，为客户资产的价值保驾护航，最大可能降低损失风险，
                每日跟踪组合收益与风险暴露因子，数据挖掘技术带来投资新价值，
                一键回测，快速获得组合表现评估</p>
            </Col>
          </Row>
        </div>
      </section>

      <!-- Callout -->
      <section class="callout">
        <div class="container tc">
          <h2 class="center mb5">现在就加入我们!</h2>
          <a class="btn btn-primary btn-xl" href="/signUp">注册</a>
        </div>
      </section>

      <!-- Portfolio -->
      <section class="content-section" id="portfolio">
        <div class="container">
          <div class="content-section-heading tc">
            <h3 class="text-secondary mb0">功能</h3>
            <h2 class="mb5">开始使用</h2>
          </div>
          <Row class="flex">
            <Col span="12">
              <a class="portfolio-item" href="/signUp">
              <span class="caption">
                <span class="caption-content">
                  <h2>组合推荐</h2>
                  <p class="mb0">点击使用</p>
                </span>
              </span>
                <img class="img-fluid" src="../../static/graph/homeBack/8.jpg" alt="">
              </a>
            </Col>
            <Col span="12">
              <a class="portfolio-item" href="/signUp">
              <span class="caption">
                <span class="caption-content">
                  <h2>自定义组合</h2>
                  <p class="mb0">点击使用</p>
                </span>
              </span>
                <img class="img-fluid" src="../../static/graph/homeBack/5.jpg" alt="">
              </a>
            </Col>
          </Row>
        </div>
      </section>

      <!-- Call to Action -->
      <section class="content-section bg-primary white">
        <div class="container tc">
          <h2 class="mb4">关于我们</h2>
          <a href="/signUp" class="btn btn-xl btn-dark mr4">注册</a>
          <a class="btn btn-xl btn-dark">了解更多</a>
        </div>
      </section>

      <!-- Footer -->
      <footer class="footer tc">
        <div class="container">
          <p class="text-muted small mb0">Copyright &copy; OptAdvisor loves you</p>
        </div>
      </footer>
      <BackTop></BackTop>
    </div>
</template>

<script>
    export default {
      name: "Home",
      data(){
        return {
          wheel:0
        }
      },
      mounted: function () {
        this.$nextTick(function () {
          window.addEventListener('scroll', this.onScroll)
        })
      },
      methods:{
        onScroll:function () {
          this.wheel = window.pageYOffset;
        },
        // 点击下滑函数
        learnMore() {
          const offsetTop = this.$refs.about.offsetTop; // div离最上方高度
          if(document.scrollingElement.scrollTop >= offsetTop) {
            return;
          }
          const speed = (offsetTop - document.scrollingElement.scrollTop)/12; // 每次滑动步长
          this.scroolInterval = setInterval(() => {
            if(document.scrollingElement.scrollTop >= offsetTop) {
              clearInterval(this.scroolInterval);
              return;
            };
            document.scrollingElement.scrollTop = document.scrollingElement.scrollTop + speed;
          },15); // 15为滑动频率
          
        }
      },
      destroyed () {
        window.removeEventListener('scroll', this.onScroll)
      },
    }
</script>

<style scoped>
  div{
    color: #111111;
  }

  .logo1{
    color: black;
  }
  .logo2{
    color: #245D90;
  }

  .product{
    margin: auto;
  }

  .productTitle{
    font-size: 22px;
    padding-bottom: 2rem;
  }

  .productContent{
    font-size: 18px;
    height: 40%;
    margin: auto;
  }

  .btn-xl {
    padding:1.25rem 2.5rem
  }
  .content-section {
    padding-top:7.5rem;
    padding-bottom:7.5rem
  }
  .content-section-heading h2 {
    font-size:3rem
  }
  .content-section-heading h3 {
    font-size:1rem;
    text-transform:uppercase
  }
  h1,h2,h3,h4,h5,h6 {
    font-weight:700
  }
  .text-faded {
    color:rgba(255,255,255,.7)
  }
  .map {
    height:30rem
  }
  @media (max-width:992px) {
    .map {
      height:75%
    }
  }.map iframe {
     pointer-events:none
   }
  .scroll-to-top {
    position:fixed;
    right:15px;
    bottom:15px;
    display:none;
    width:50px;
    height:50px;
    text-align:center;
    color:#fff;
    background:rgba(52,58,64,.5);
    line-height:45px
  }
  .scroll-to-top:focus,.scroll-to-top:hover {
    color:#fff
  }
  .scroll-to-top:hover {
    background:#343a40
  }
  .scroll-to-top i {
    font-weight:800
  }
  .masthead {
    min-height:30rem;
    position:relative;
    width:100%;
    height: 100%;
    padding-top:8rem;
    padding-bottom:8rem;
    background:linear-gradient(90deg,rgba(255,255,255,.1) 0,rgba(255,255,255,.1) 100%),url(../../static/graph/homeBack/7.jpg);
    background-position:center center;
    background-repeat:no-repeat;
    background-attachment: fixed;
    background-size:cover;
  }
  .masthead h1 {
    font-size:4rem;
    margin:0;
    padding:0;
  }
  @media (min-width:992px) {
    .masthead {
      height:100vh
    }
    .masthead h1 {
      font-size:5.5rem
    }
  }#sidebar-wrapper {
     position:fixed;
     z-index:2;
     right:0;
     width:250px;
     height:100%;
     -webkit-transition:all .4s ease 0s;
     -moz-transition:all .4s ease 0s;
     -ms-transition:all .4s ease 0s;
     -o-transition:all .4s ease 0s;
     transition:all .4s ease 0s;
     transform:translateX(250px);
     background:#1d809f;
     border-left:1px solid rgba(255,255,255,.1)
   }
  .sidebar-nav {
    position:absolute;
    top:0;
    width:250px;
    margin:0;
    padding:0;
    list-style:none
  }
  .sidebar-nav li.sidebar-nav-item a {
    display:block;
    text-decoration:none;
    color:#fff;
    padding:15px
  }
  .sidebar-nav li a:hover {
    text-decoration:none;
    color:#fff;
    background:rgba(255,255,255,.2)
  }
  .sidebar-nav li a:active,.sidebar-nav li a:focus {
    text-decoration:none
  }
  .sidebar-nav>.sidebar-brand {
    font-size:1.2rem;
    background:rgba(52,58,64,.1);
    height:80px;
    line-height:50px;
    padding-top:15px;
    padding-bottom:15px;
    padding-left:15px
  }
  .sidebar-nav>.sidebar-brand a {
    color:#fff
  }
  .sidebar-nav>.sidebar-brand a:hover {
    color:#fff;
    background:0 0
  }
  #sidebar-wrapper.active {
    right:250px;
    width:250px;
    -webkit-transition:all .4s ease 0s;
    -moz-transition:all .4s ease 0s;
    -ms-transition:all .4s ease 0s;
    -o-transition:all .4s ease 0s;
    transition:all .4s ease 0s
  }
  .menu-toggle {
    position:fixed;
    right:15px;
    top:15px;
    width:50px;
    height:50px;
    text-align:center;
    color:#fff;
    background:rgba(52,58,64,.5);
    line-height:50px;
    z-index:999
  }
  .menu-toggle:focus,.menu-toggle:hover {
    color:#fff
  }
  .menu-toggle:hover {
    background:#343a40
  }
  .service-icon {
    background-color:#fff;
    color:#1d809f;
    height:7rem;
    width:7rem;
    display:block;
    line-height:7.5rem;
    font-size:2.25rem;
    box-shadow:0 3px 3px 0 rgba(0,0,0,.1)
  }
  .callout {
    padding:15rem 0;
    background:linear-gradient(90deg,rgba(255,255,255,.1) 0,rgba(255,255,255,.1) 100%),url(../../static/graph/homeBack/2.jpg);
    background-position:center center;
    background-repeat:no-repeat;
    background-size:cover;
    background-attachment: fixed;
  }
  .callout h2 {
    font-size:3.5rem;
    font-weight:700;
    display:block;
    max-width:30rem
  }
  .portfolio-item {
    display:block;
    position:relative;
    overflow:hidden;
    max-width:530px;
    margin:auto auto 1rem
  }
  .portfolio-item .caption {
    display:flex;
    height:100%;
    width:100%;
    background-color:rgba(33,37,41,.2);
    position:absolute;
    top:0;
    bottom:0;
    z-index:1
  }
  .portfolio-item .caption .caption-content {
    color:#fff;
    margin:auto 2rem 2rem
  }
  .portfolio-item .caption .caption-content h2 {
    font-size:.8rem;
    text-transform:uppercase
  }
  .portfolio-item .caption .caption-content p {
    font-weight:300;
    font-size:1.2rem
  }
  @media (min-width:992px) {
    .portfolio-item {
      max-width:none;
      margin:0
    }
    .portfolio-item .caption {
      -webkit-transition:-webkit-clip-path .25s ease-out,background-color .7s;
      -webkit-clip-path:inset(0);
      clip-path:inset(0)
    }
    .portfolio-item .caption .caption-content {
      transition:opacity .25s;
      margin-left:5rem;
      margin-right:5rem;
      margin-bottom:5rem
    }
    .portfolio-item img {
      -webkit-transition:-webkit-clip-path .25s ease-out;
      -webkit-clip-path:inset(-1px);
      clip-path:inset(-1px)
    }
    .portfolio-item:hover img {
      -webkit-clip-path:inset(2rem);
      clip-path:inset(2rem)
    }
    .portfolio-item:hover .caption {
      background-color:rgba(29,128,159,.9);
      -webkit-clip-path:inset(2rem);
      clip-path:inset(2rem)
    }
  }footer.footer {
     padding-top:5rem;
     padding-bottom:5rem
   }
  footer.footer .social-link {
    display:block;
    height:4rem;
    width:4rem;
    line-height:4.3rem;
    font-size:1.5rem;
    background-color:#1d809f;
    transition:background-color .15s ease-in-out;
    box-shadow:0 3px 3px 0 rgba(0,0,0,.1)
  }
  footer.footer .social-link:hover {
    background-color:#155d74;
    text-decoration:none
  }
  a {
    color:#1d809f
  }
  a:active,a:focus,a:hover {
    color:#155d74
  }
  .btn-primary {
    background-color:#1d809f!important;
    border-color:#1d809f!important;
    color:#fff!important
  }
  .btn-primary:active,.btn-primary:focus,.btn-primary:hover {
    background-color:#155d74!important;
    border-color:#155d74!important
  }
  .btn-secondary {
    background-color:#ecb807!important;
    border-color:#ecb807!important;
    color:#fff!important
  }
  .btn-secondary:active,.btn-secondary:focus,.btn-secondary:hover {
    background-color:#ba9106!important;
    border-color:#ba9106!important
  }
  .btn-dark {
    color:#fff!important
  }
  .btn {
    box-shadow:0 3px 3px 0 rgba(0,0,0,.1);
    font-weight:700
  }
  .bg-primary {
    background-color:#1d809f!important
  }
  .text-primary {
    color:#1d809f!important
  }
  .text-secondary {
    color:#ecb807!important
  }
  .bg-light {
    background-color: #E3F2F3 !important;
  }
  .container {
    width: 100%;
    padding-right: 15px;
    padding-left: 15px;
    margin-right: auto;
    margin-left: auto;
  }

  @media (min-width: 576px) {
    .container {
      max-width: 540px;
    }
  }

  @media (min-width: 768px) {
    .container {
      max-width: 720px;
    }
  }

  @media (min-width: 992px) {
    .container {
      max-width: 960px;
    }
  }

  @media (min-width: 1200px) {
    .container {
      max-width: 1140px;
    }
  }
  .lead {
    font-size: 1.25rem;
    font-weight: 300;
  }
  .rounded-circle {
    border-radius: 50% !important;
  }
  .img-fluid {
    max-width: 100%;
    height: auto;
  }
  .list-inline {
    padding-left: 0;
    list-style: none;
  }

  .list-inline-item {
    display: inline-block;
  }

  .list-inline-item:not(:last-child) {
    margin-right: 0.5rem;
  }
  .forwardButton{
    color: #fff;
    background: 0 0;
    border-color: #fff;
    font-size: 20px;
    border-radius: 30px;
    width: 80px;
    height: 50px;
    text-align: center;
  }

  .forwardButton:hover {
    color: #000;
    background-color: #fff;
    border-color: #fff;
  }

  .loginButton{
    position: fixed;
    z-index: 100;
    top: 3%;
    left: 92%;
  }
</style>
