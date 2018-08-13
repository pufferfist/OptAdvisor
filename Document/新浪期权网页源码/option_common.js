var A_price='',PRE='';
var doc_scrollTop=document.documentElement.scrollTop||document.body.scrollTop;
var hq_offset=$('.main_hq').offset().top;
function scrollTo(){

   document.documentElement.scrollTop+=80;
   document.body.scrollTop+=80;
   var hq_offset=$('.main_hq').offset().top-0;

  setTimeout(function(){

            if(document.documentElement.scrollTop+document.body.scrollTop<hq_offset)

                   scrollTo();
            else
            {
              document.documentElement.scrollTop=hq_offset;
              document.body.scrollTop=hq_offset;
              return;
            }
        },30);

 }

function inheritPrototype (sub,supera)
{
   var prototype = Object(supera.prototype);//\u5efa\u7acb\u5b9e\u4f8b\u5bf9\u8c61     \u8d85\u7c7b\u7684\u526f\u672c
  prototype.constructor=sub;   //\u589e\u5f3a\u5bf9\u8c61
  sub.prototype=prototype;    //\u6307\u5b9a\u5bf9\u8c61              \u526f\u672c\u8d4b\u503c\u7ed9\u5b50\u7c7b\u578b\u7684\u539f\u578b
}
cookie = {
        set : function(k,v,h,d){
            var hour = h || 24,
                now = new Date(),
                domain = d || document.domain,
                exp = new Date(now.getTime() + hour * 3600 * 1000);
                expires = exp.toGMTString();
            document.cookie = k + '=' + v + ';path=/;expires=' + expires + ';domain=' + domain;
        },
        get : function(k){
            var coo = unescape(document.cookie),
                s = coo.indexOf(k + '='),
                kl = s + k.length + 1,
                e = coo.indexOf(';',kl);
            if(s == -1){return '';}
            if(e == -1){return coo.substr(kl);}
            return coo.slice(kl,e);
        },
        remove : function(k){
            var now = new Date(),
                exp = new Date(now.getTime() - 3600 * 1000);
                expires = exp.toGMTString();
            document.cookie = k + '=' + ';expires=' + expires + '';
        }
    };

 function rdt(){

          var str=(''+Math.random()+new Date().getTime()).replace(/\./g,'');
          return str;
               }

 function unit(num,perci){
                    var per=perci||2;
                    per=per-0;
                    var number=Math.abs(num-0);
                    var a_num=(number/1e8)<1?((number/1e4)<1?number.toFixed(per):(number/1e4).toFixed(per)+' \u4e07'):((number/1e8)-0).toFixed(per)+' \u4ebf';
                    if(num<0)
                    return '-'+a_num;
                    else
                      return a_num;

                   }

function hand_data()
{

}
function jsonp_ajax(url,param,fn)  //\u8c03openapi\u63a5\u53e3\u51fd\u6570
{
  var self=this;
  var url=url||'';
  var param=param||'';
  if(!param)
    return;
   $.ajax({
            url:url,
      dataType:"jsonp",
      data:$.extend(param,{'dpc': 1}),
      jsonpCallback: 'johansen'+rdt(),
      cache:true,
      success:function(data){
        fn&&fn.call(self,data);
      }
      });

}

function script_stock(param_str,fn,format)  //\u8c03\u884c\u60c5\u4e32\u63a5\u53e3\u51fd\u6570
{   var self=this;

    var param_str=param_str||'';
    if(!param_str)
    return;
  if(format)
  {
    $.ajax({
              url: 'http://hq.sinajs.cn/ran='+rdt()+'&format='+format+'&list='+param_str,
              dataType: "script",
              cache:true,
              success: function(data){

                fn&&fn.call(self);
              }
      });
  }
  else
  {
    $.ajax({
              url: 'http://hq.sinajs.cn/list='+param_str,
              dataType: "script",
              cache:true,
              success: function(data){

                fn&&fn.call(self);
              }
      });
  }

}
function _visitedStock(num,name,dom,mark)
{
   this.num=num;
   this.name=name;
   this.dom=dom;
   this.data=[];
   this.stocklist;
   this.stocklist_str;
   this.mark=mark;
   this.no_hk_stocklist;

}
_visitedStock.prototype.getStock=function(){
               var s_stock=cookie.get(this.name);
               if(!s_stock)return;
               if(this.mark=='cn')
               {
                this.no_hk_stocklist=s_stock.split(',');
                 this.stocklist=s_stock.split(',');
               }

               if(this.mark=='hk')
               {
                 this.stocklist=s_stock.split('|');
                 this.no_hk_stocklist=s_stock.split('|');
                 for(var i=0;i<this.stocklist.length;i++)
                 {
                      this.stocklist[i]='rt_hk'+this.stocklist[i];
                 }
               }

               this.stocklist.reverse(this.stocklist);
               this.no_hk_stocklist.reverse(this.no_hk_stocklist);
               this.stocklist_str=this.stocklist.join(',');
               script_stock.call(this,this.stocklist_str,this.hand_hq_data);
}

_visitedStock.prototype.hand_hq_data=function(){

  if(this.mark=='cn')
  {
      for(var i=0;i<this.stocklist.length;i++)
      {
        var json={};
        if(!window['hq_str_'+this.stocklist[i]])continue;
        var arr=window['hq_str_'+this.stocklist[i]].split(/,|=/g);//hq\u5b57\u7b26\u4e32\u5207\u5272\u540e\u7684\u6570\u636e
         json.cname=arr[0];
         json.opening=arr[1];
         json.PRE=arr[2];
         json.curr_rate=arr[3];
         json.H_curr_rate=arr[4];
         json.L_curr_rate=arr[5];
         json.buying_rate=arr[6];
         json.selling_rate=arr[7];
         json.trading_volume=arr[8];
         json.Amount=arr[9];
         json.date=arr[30];
         json.hq_time=arr[31];
         //\u5bf9\u4e8e\u90a3\u4e9b\u975e\u4e0a\u5e02\u6b63\u5e38\u4ea4\u6613\u7684\u6211\u4eec\u8981\u8bf7\u6c42http://hq.sinajs.cn/list=symbol_i
         json.state=arr[32];
         this.data.push(json);
      }
  }
   if(this.mark=='hk')
  {
      for(var i=0;i<this.stocklist.length;i++)
      {
        var json={};
        if(!window['hq_str_'+this.stocklist[i]])continue;
        var arr=window['hq_str_'+this.stocklist[i]].split(/,|=/g);//hq\u5b57\u7b26\u4e32\u5207\u5272\u540e\u7684\u6570\u636e
                                json.ename=arr[0].substr(0,6);//\u82f1\u6587\u540dclipString('testtesttesttesttesttesttest', 0, 10);\u622a\u53d6\u5341\u4e2a\u534a\u89d2\u5b57\u7b26q
                                json.cname=arr[1],//\u4e2d\u6587\u540d
                                json.opening=arr[2],//\u5f00\u76d8
                                json.PRE=arr[3],//\u6628\u6536
                                json.H_curr_rate=arr[4],//\u6700\u9ad8
                                json.L_curr_rate=arr[5],//\u6700\u4f4e
                                json.zf=((arr[4]-arr[5])*100/(arr[3])).toFixed(3);
                                json.curr_rate=(arr[6]-0).toFixed(3),//\u5f53\u524d
                                json.zde=arr[7],//\u6da8\u8dcc\u989d
                                json.zdf=arr[8]+'%',//\u6da8\u8dcc\u5e45
                                json.sell1=arr[9],//\u53561
                                json.buy1=arr[10],//\u4e701
                                json.Amount=unit(arr[11],3),//(arr[11]/10e7).toFixed(3),//\u6210\u4ea4\u989d1
                                json.trading_volume=unit(arr[12],3),//\u6210\u4ea4\u91cf1

                                json.syl=arr[13],//\u5e02\u76c8\u7387
                                json.shouyl=arr[14]+'%',//\u6536\u76ca\u7387
                                json.zd52=arr[15],//52\u5468\u6700\u9ad8
                                json.zd52=arr[16],//52\u5468\u6700\u4f4e
                                json.date=arr[17],//\u884c\u60c5\u65e5\u671f
                                json.hq_time=arr[18],//\u884c\u60c5\u65f6\u95f4
                                this.data.push(json);
      }
  }

   this.dom_handle(this.data);

}

_visitedStock.prototype.dom_handle=function(data){
   var str='';//\u7c7b\u4f3c\u6a21\u677f\u7684\u90e8\u5206
   var str1=str2='';
   var num=this.num<data.length?this.num:data.length;
   for(var i=0;i<num;i++)
   {

    var color=data[i].curr_rate<0?' tex_down_color':data[i].curr_rate>0?' tex_up_color':'';
    if(this.mark=='cn')
     str1+='<tr><td><a href="http://finance.sina.com.cn/realstock/company/'+this.no_hk_stocklist[i]+'/nc.shtml" data="'+this.no_hk_stocklist[i]+'" target=_blank>'+data[i].cname+'</a></td><td class="Tex_r'+color+'">'+unit(data[i].curr_rate)+'</td></tr>';
    if(this.mark=='hk')
       str1+='<tr ><td><a href="http://stock.finance.sina.com.cn/hkstock/quotes/'+this.no_hk_stocklist[i]+'.html" data="'+this.no_hk_stocklist[i]+'" target=_blank>'+data[i].cname+'</a></td><td class="Tex_r'+color+'">'+unit(data[i].curr_rate)+'</td></tr>';

   }

   $(this.dom).find('tbody').html(str1);

}
/*\u677f\u5757\u6da8\u8dcc\u5e45*/
 var bk_change=function(param_str,time,dom)
 {
   this.param=param_str+'_';
   this.rank='up';
   this.time=time;
   this.dom=dom;
   //this.click_rank();

 }
bk_change.prototype.hqapi=function(){
   var self=this;
   var param_str=self.param+self.rank;
   script_stock.call(this,param_str,this.hand_hq_data);
   if(this.time)
   {
     self.timer=setInterval(function(){

            script_stock.call(self,param_str,self.hand_hq_data);

     },self.time);
   }
 }
bk_change.prototype.hand_hq_data=function(data){
    var rank = this.rank;
    var data = window['hq_str_sinaindustry_'+rank],
        str = '';
    var arr=eval(data);//\u677f\u5757\u6da8\u8dcc\u5e45\u6570\u7ec4

        if(!arr) return;
        for(var i=0;i<5;i++)//\u677f\u5757\u6da8\u8dcc\u5e45\u663e\u793a\u524d\u4e94\u6761
        {
          var arr_stock=arr[i].split(/,/g);
          for(var j=0;j<arr_stock.length;j++)
          {
              var bk_name=arr_stock[1],
                  bk_symbol=arr_stock[0],
                  bk_change=(arr_stock[5]-0).toFixed(2)+'%',
                  bk_stock=arr_stock[8],
                  bk_stock_name=arr_stock[12];
          }
          var color=arr_stock[5]<0?'tex_down_color':arr_stock[5]>0?'tex_up_color':'';
          str+='<tr><td ><a href="http://vip.stock.finance.sina.com.cn/mkt/#'+bk_symbol+'" target=_blank>'+bk_name+'</a></td><td class="Tex_r '+color+'">'+bk_change+'</td><td class="Tex_r"><a href="http://finance.sina.com.cn/realstock/company/'+bk_stock+'/nc.shtml" target=_blank>'+bk_stock_name+'</a></td></tr>';
        }
        $(this.dom).find('tbody').html(str);
}
bk_change.prototype.click_rank=function(){
   var self=this;

    if($(this.dom+' th a'))return;

   $(this.dom+' th a').on('click',function(){
       if(self.time)
        {
           clearInterval(self.timer);

        }
       if($(this).attr('data-rank')=='up')
       {
           $(this)[0].className='table_rank_desc' ;
           self.rank='down';
           $(this).attr('data-rank','down');
       }
       else{

             $(this)[0].className='table_rank_asc' ;
             self.rank='up';
             $(this).attr('data-rank','up');
        }
        self.hqapi();
   });
}

function detail_stock(url,dom,param,time) //\u80a1\u7968\u884c\u60c5\u8be6\u7ec6\u4fe1\u606f\u5904\u7406

{
   this.url=url||'';    //\u8bf7\u6c42\u7684\u63a5\u53e3
   this.dom=dom||'';    //\u9700\u8981\u6e32\u67d3\u7684dom
   this.param=param||'';//\u63a5\u53e3\u8bf7\u6c42\u53c2\u6570\uff0c\u5bf9\u4e8ehq\u7684param\u53ef\u4ee5\u5199\u6210\uff1a'[sh600000,sh600000_i]'
   this.data={};
   this.time=time;
   this.timer;
    /* this.data=json||null;//\u8bbe\u7f6ejson\u7684\u5b58\u5728\u6807\u8bb0

  //\u5bf9\u4e8e\u90a3\u4e9b\u975e\u4e0a\u5e02\u6b63\u5e38\u4ea4\u6613\u7684\u6211\u4eec\u8981\u8bf7\u6c42http://hq.sinajs.cn/list=symbol_i

  this.state=json.state;*/
 }

detail_stock.prototype.hq_interface=function(){
  script_stock.call(this,this.param.join(','),this.hand_hq_data);
  var self=this;

    if(this.time)
    {
      clearInterval(self.timer);
      self.timer=setInterval(function(){
          script_stock.call(self,self.param.join(','),self.hand_hq_data);
      },self.time);
    }
}
detail_stock.prototype.hand_hq_data=function(){

   var arr = window['hq_str_'+this.param[0]].split(/,|=/g);//hq\u5b57\u7b26\u4e32\u5207\u5272\u540e\u7684\u6570\u636e
   var arr01=window['hq_str_'+this.param[1]].split(/,|=/g);
   this.data.zde=parseFloat(arr01[2]).toFixed(3)-0||'\uff0d\uff0d';
   this.data.zdf=arr01[3]-0||'-\uff0e-';
   this.data.cname=arr[0];
   this.data.opening=arr[1]-0||'--';
   this.data.PRE=arr[2]-0||'--';
   this.data.curr_rate=arr[3]-0||'--';
   this.data.H_curr_rate=arr[4]-0||'--';
   this.data.L_curr_rate=arr[5]-0||'--';
   this.data.buying_rate=arr[6]-0||'--';
   this.data.selling_rate=arr[7]-0||'--';
   this.data.trading_volume=arr[8]-0||'--';
   this.data.Amount=arr[9]-0||'--';
   this.data.buy_Five=[];//\u6570\u7ec4\u5b58\u5728
   this.data.sell_Five=[];//\u6570\u7ec4\u5b58\u5728
   this.data.date_time=arr[30]+'  '+arr[31];
   this.data.zf=arr[5]?((arr[4]-arr[5])*100/arr[5]).toFixed(2):'-.-';
   this.data.FPR=((arr[3]-IOPV)*100/(IOPV-0)).toFixed(2);
   for(var i=19;i>=10;i--)
   {
     var buyjson={};
     buyjson.buy_p=arr[i];
     buyjson.buy_a=arr[--i];
     this.data.buy_Five.push(buyjson);
   }
   for(var j=20;j<30;j++)
   {
     var selljson={};
     selljson.sell_a=arr[j];
     selljson.sell_p=arr[++j];
     this.data.sell_Five.push(selljson);
   }

   this.data.date=arr[30];
   this.data.hq_time=arr[31];
   //\u5bf9\u4e8e\u90a3\u4e9b\u975e\u4e0a\u5e02\u6b63\u5e38\u4ea4\u6613\u7684\u6211\u4eec\u8981\u8bf7\u6c42http://hq.sinajs.cn/list=symbol_i

   this.data.state=arr[32];

   this.dom_handle(this.data);//\u6570\u636e\u6e32\u67d3

}
detail_stock.prototype.dom_handle=function(data)
{
   var str='';//\u7c7b\u4f3c\u6a21\u677f\u7684\u90e8\u5206
   var str1=str2='<h4 style="margin:20px 150px;">\u4e94\u6863\u76d8\u53e3</h4>';
   for(var i in this.data)
   {

     if(i!='buy_Five'&&i!='sell_Five')
    {

    }

   }

       for(var j=0;j<this.data.buy_Five.length;j++)
         {
          str1+='<li style="margin:20px 150px;">\u4e70'+(5-j)+': '+this.data.buy_Five[j].buy_a+':  '+this.data.buy_Five[j].buy_p+'</li>';
         }
        for(var s=0;s<this.data.sell_Five.length;s++)
         {
            str2+='<li style="margin:20px 150px;">\u5356'+(s+1)+': '+this.data.sell_Five[s].sell_a+':  '+this.data.sell_Five[s].sell_p+'</li>';
         }
   var color=this.data.zdf<0?'tex_down_color':this.data.zdf>0?'tex_up_color':'';
   var marker=this.data.zdf<0?' down':this.data.zdf>0?' up_down':'';
   str='<div class="del-01"><span class="price '+color+'">'+this.data.curr_rate+'</span><span class="'+marker+'"> </span><span class="zdf"><em class="fl '+color+'">'+this.data.zde+'</em><em class="fl '+color+'">'+this.data.zdf+'%</em></span></div><div class="del-02">'+this.data.date_time+'</div><ul class="del-03 fl"><li><span>\u4eca\u3000\u5f00\uff1a<em class="'+color+'">'+this.data.opening+'</em></span>\u3000</li><li><span>\u6628\u3000\u6536\uff1a<em>'+this.data.PRE+'</em></span>\u3000</li> <li><span>\u6210\u4ea4\u91cf\uff1a<em>'+unit(this.data.trading_volume/100)+'\u624b</em></span></li><li><span>\u6210\u4ea4\u989d\uff1a<em>'+unit(this.data.Amount)+'</em></span><span class="tex_r_pos"></span></li><li><span>\u6298\u6ea2\u4ef7\u7387\uff1a<em>'+this.data.FPR+'%</em></span></li><li><span>\u5355\u4f4d\u51c0\u503c\uff1a<em>'+IOPV+'</em></span></li><li><span>\u7d2f\u8ba1\u51c0\u503c\uff1a<em>'+TP+'</em></span></li></ul><ul class="fr del-03-r"><li><span >\u6700\u9ad8\uff1a<em class="tex_up_color">'+ this.data.H_curr_rate+'</em></span></li><li><span >\u6700\u4f4e\uff1a<em class="tex_down_color">'+this.data.L_curr_rate+'</em></span></li><li><span >\u632f\u5e45\uff1a<em>'+this.data.zf+'%</em></span></li><li><span>\u51c0\u503c\u589e\u957f\uff1a<em>'+UGA+'%</em></span></li><li><span>\u6700\u65b0\u89c4\u6a21\uff1a<em>'+LS+'</em></span></li><li><span>\u7533\u8d2d\u72b6\u6001\uff1a<em>'+subscribe_state+'</em></span></li><li><span>\u8d4e\u56de\u72b6\u6001\uff1a<em>'+redemption_state+'</em></span></li></ul>';
   $(this.dom).html(str);
   /*$('#A50').html(' '+this.data.curr_rate);
   $('#A50')[0].className=color;*/
   //$('#mail').html(str1+str2);//\u4e94\u6863\u76d8\u53e3

}
/*\u671f\u6743\u8868\u683c\u7684\u5177\u4f53\u529f\u80fd*/
//\u521d\u59cb\u5316select

var _datas={
                    t1:{
                        dataformatter:function(arr_){
                            var myArr=[];
                            for(var i=0;i<arr_.length;i++){
                                var obj={
                                    m:arr_[i].i,
                                    p:arr_[i].p,
                                    avp:arr_[i].a,
                                    v:arr_[i].v,
                                    iy:arr_[i].t
                                };
                                if(i==0)obj.d=arr_[i].d;
                                myArr.push(obj);
                            }
                            return myArr;
                        },
                        url:''
                    },
                    t5:{
                        dataformatter:function(arr_){
                            var myArr=[];
                            for(var i=0;i<arr_.length;i++){
                                var obj={
                                    m:arr_[i].i,
                                    p:arr_[i].p,
                                    a:arr_[i].a,
                                    v:arr_[i].v,
                                    c:arr_[i].t
                                };
                                if(i==0)obj.d=arr_[i].d;
                                myArr.push(obj);
                            }
                            return myArr;
                        },
                        url:''
                    },
                  day:{
                  //dataformatter:function(){
                  //},
                  url:'http://stock.finance.sina.com.cn/futures/api/jsonp_v2.php/$cb/StockOptionDaylineService.getSymbolInfo?symbol=$symbol'
                  },
                  min:{
                  //url:'http://stock.finance.sina.com.cn/usstock/api/jsonp_v2.php/$cb/US_MinlineNService.getMinline?symbol=sina&day=1&_=$rn&___qn=3',
                  //dataformatter:function(){}
                  }
};

// flash \u6e32\u67d3
window.HQ_kflash=null;//\u884c\u60c5\u56fe\u5bf9\u8c61(k\u7ebf)
window.HQ_diejia=false;//\u662f\u5426\u53e0\u52a0\u884c\u60c5\u56fe
window.conta_symbol='';//\u5408\u7ea6
window.HQ_tflash=null;//\u884c\u60c5\u56fe\u5bf9\u8c61(t\u7ebf)
window.B_symbol='sh510050';
function K_chart(symbol,com_symbol,fn){

      var op_symbol=symbol;

      KKE.api("chart.h5k.get",{
        domid:'flash',
        w:620,
        h:400,
        nfloat:4,
        symbol:symbol,//'sh600697'//symbol
        datas:_datas
      },function(_chart){
         _chart.tCharts([{name:'MACD'}]);
         window.HQ_kflash=_chart;

         fn&&fn.call(_chart);

      });
}
function T_chart(symbol,com_symbol,fn)
{

      var op_symbol=symbol;

      KKE.api("chart.h5t.get",{
        domid:'flash',
        datas:_datas,
        date:'',
        w:620,
        h:400,
        nfloat:4,
        symbol:symbol//'sh600697'//symbol
      },function(_chart){
        window.HQ_tflash=_chart;
        _chart.tCharts([
            //{name:'LB'},//\u91cf\u6bd4\uff08\u671f\u6743\u76ee\u524d\u6ca1\u6709\uff09
            //{name:'TVOLUME'},//\u6210\u4ea4\u91cf
            {name:'POSITION'}//\u6301\u4ed3\u91cf\uff08\u76ee\u524d\u671f\u6743\u4e13\u6709\uff09
    ]);

        fn&&fn.call(_chart);

      });

}
// \u53d6\u6d88\u53e0\u52a0
$('.compare_stock').toggle(
  function () {
    $(this).addClass("selected");
    window.HQ_diejia=true;
    HQ_tflash?HQ_tflash.compare({symbol:B_symbol}):null;
    HQ_kflash?HQ_kflash.compare({symbol:B_symbol}):null;
    if($(this).hasClass('selected'))
    {
        $(this).text('\u53d6\u6d88\u53e0\u52a0');
    }
    else
    {
        $(this).text('\u5bf9\u6bd4\u6807\u7684\u8d44\u4ea7');

    }

  },
  function () {
    $(this).removeClass("selected");
    window.HQ_diejia=false;
    HQ_tflash?HQ_tflash.compare({symbol:B_symbol},true):null;
    HQ_kflash?HQ_kflash.compare({symbol:B_symbol},true):null;
    if($(this).hasClass('selected'))
    {
        $(this).text('\u53d6\u6d88\u53e0\u52a0');
    }
    else
    {
        $(this).text('\u5bf9\u6bd4\u6807\u7684\u8d44\u4ea7');

    }

  }
);
$('#tab_H5').find('li[data-tabKey]').on('click',function(){
    var view=$(this).attr('data-tabKey');
    $('#tab_H5').find('li[data-tabKey]').removeClass('selected');
    $(this).addClass('selected');
    switch(view)
    {
      case 'ts':
      HQ_tflash?HQ_tflash.showView('ts'):null;
      HQ_kflash?HQ_kflash.hide():null;
      HQ_tflash.show();
      break;
      case 't5':
      HQ_tflash?HQ_tflash.showView('t5'):null;
      HQ_kflash?HQ_kflash.hide():null;
       HQ_tflash.show();
      break;
      case 'kd':
      HQ_tflash.hide();
      HQ_kflash?function(){HQ_kflash.show();HQ_kflash.showView('kd')}():K_chart(window.conta_symbol,false,function(){this.showView('kd');if(window.HQ_diejia)
      this.compare({symbol:B_symbol});});
      break;
      case'kw':
      HQ_tflash.hide();
      HQ_kflash?function(){HQ_kflash.show();HQ_kflash.showView('kw')}():K_chart(window.conta_symbol,false,function(){this.showView('kw');if(window.HQ_diejia)
      this.compare({symbol:B_symbol});});
      break;
      default:
      HQ_tflash.showView('ts');
      break;
    }
});

function Option_select(dom,url,url1,time){
  this.dom=dom||'';
  this.url=url||'';
  this.url1=url1||'';
  this.param={}||'';
  this.json={};
  this.curr_stock='';
  this.timer; //\u4e3btable\u5237\u65b0
  this.r_timer;//\u53f3\u4fa7\u5408\u7ea6\u4e0e\u4e94\u6863\u76d8\u53e3
  this.duration=time||'';
  this.domain_up_con='';
  this.domain_down_con='';
  this.init();
  this.click_sel();
  this.click_domain();
  this.click_tr();

}
Option_select.prototype = {
   init:function(){
        this.param.exchange=$(this.dom+' .exchange ul li a').eq(0).html();
        jsonp_ajax.call(this,this.url,{exchange:this.param.exchange,cate:''},this.dom_handle);
   },
   data_handle:function(){ //dom\u5217\u8868\u63a5\u53e3
       //Layerbox.pop({closeTime:2500,content:"\u6b63\u5728\u8f7d\u5165\u6570\u636e..."});
       jsonp_ajax.call(this,this.url,{exchange:this.param.exchange,cate:this.param.cate,date:this.param.date},this.dom_handle);
   },

   dom_handle:function(data){  //\u5217\u8868\u6e32\u67d3
      var dom=this.dom,json=data.result.data;
     // var exchange=json.;//\u4ea4\u6613\u6240
      var cate_list=json.cateList;
      var date=json.contractMonth;
      var str1='',str2='',str3='',str4='';
      $(dom+' .option_cate').find('span').eq(0).html(cate_list[0]);
      $(dom+' .date').find('span').eq(0).html(date[0]);
      var $sel_dom_cate=$(dom+' .option_cate').find('select');
      var $ul_dom_cate=$(dom+' .option_cate').find('ul');
      var $sel_dom_date=$(dom+' .date').find('select');
      var $ul_dom_date=$(dom+' .date').find('ul');
      for( var i=1;i<cate_list.length;i++)
      {

        str1+='<option value="'+cate_list[i]+'" selected>'+cate_list[i]+'</option>';
        str2+='<li><a href="javascript:;">'+cate_list[i]+'</a></li>';
      }
       $sel_dom_cate.html(str1);
       $ul_dom_cate.html(str2);
       for(var j=1;j<date.length;j++)
       {

        str3+='<option value="'+date[i]+'" selected>'+date[j]+'</option>';
        str4+='<li><a href="javascript:;">'+date[j]+'</a></li>';
       }
       $sel_dom_date.html(str3);
       $ul_dom_date.html(str4);

        this.param.cate=cate_list[0];
        this.param.date=date[0];
        this.confirm(this.param);//\u8bf7\u6c42\u5230\u671f\u65f6\u95f4\uff0c\u5269\u4f59\u65f6\u95f4\uff0c\u4e3b\u529b\u5408\u7ea6\u51fd\u6570

   },
   confirm:function(json){
       var json=json;
       jsonp_ajax.call(this,this.url1,json,this.table_handle);//\u65e5\u671f\uff0c\u671f\u6743\uff0c\u4e3b\u529b\u5408\u7ea6\u3002\u8bf7\u6c42\u63a5\u53e3
   },
   click_sel:function(){  //\u5217\u8868\u9009\u62e9
          var self=this;
          $(self.dom).find('#date').on('click',function(event){// $(self.dom+' .sel').find('i').on('click',function(event){
               var $target1 = $(event.target);
               while (!$target1.is('div')) {
                    if ($target1.get(0) == document.body)

                          return;
                    $target1 = $(($target1)[0].parentNode);
                }

                $target1.find('ul').css({display:'block'});
               return false;

          });
          $('body').on('click',function(){

                $('#date ul').hide();
          });
          $(self.dom).find('div').on('click',function(event){

                var $target = $(event.target);
                var ind=$(this).index();
                if($target[0].name=='sample')return;
                while (!($target.is('a'))) {
                    if ($target.get(0) == document.body)

                          return;

                    $target = $(($target)[0].parentNode);
                }

                var sel_value=$target.html();
                $(this).find('select').value=sel_value;
                $(this).find('ul').css({display:'none'});
                $(this).find('span').get(0).innerHTML=sel_value;
                switch(ind)
                {
                  case 0:
                  self.param.exchange=sel_value;
                  self.param.cate='';
                  self.param.date='';
                  break;
                  case 1:
                  self.param.cate=sel_value;
                  self.param.date='';
                  break;
                  case 2:
                  self.param.date=sel_value;
                  break;
                  default:
                  break;

                }
                self.curr_tr='';
                self.data_handle();
                return false;
      });

   },

   table_handle:function(data){
        /*  expireDay": "2015-03-15",
      "remainderDays": 53,
      "zhulikanzhang": "000300C1503M14500",
      "zhulikandie": "111300C1503M14500"*/
        var data=data.result.data;
        var hq_str='';
        var self=this;
        //由于合约规则变换修改bug(cateId后出现A20等)
        self.json.cateId=data.cateId.substring(0,11).replace(/[a-z]/ig,'');//'510050C1503'.replace(/[a-z]/ig,'');//data.cateId;   //\u671f\u6743\u4ee3\u7801  -M
        self.json.symbole='sh'+data.stockId;//'sh510050';//data.stockId;\u5bf9\u5e94\u80a1\u7968\u7684ID
        self.json.expireDay=data.expireDay;      //\u5230\u671f\u65f6\u95f4
        self.json.domain_up='';//data.zhulikanzhang;\u4e3b\u529b\u770b\u6da8\u5408\u7ea6\u4ee3\u7801  -M
        self.json.domain_down='';//data.zhulikandie;\u4e3b\u529b\u770b\u8dcc\u5408\u7ea6\u4ee3\u7801   -M
        self.json.remainderDays=data.remainderDays;//\u5269\u4f59\u65f6\u95f4
        //hq_str='CON_UP_LIST_'+self.json.cateId+',CON_DOWN_LIST_'+self.json.cateId+','+self.json.symbole;
        hq_str='OP_UP_'+self.json.cateId+',OP_DOWN_'+self.json.cateId+',s_sh510050,sh510050';
        $('.search-sel>span').eq(0).html(self.json.expireDay);
        $('.search-sel>span').eq(1).html(self.json.remainderDays+' \u5929');
        script_stock.call(self,hq_str,self.main_table);//\u5229\u7528\u671f\u6743\u4ee3\u7801\u8bf7\u6c42\u4ed6\u6240\u5bf9\u5e94\u7684\u5408\u7ea6HQ list
   },
   main_table:function(){
    var self=this;
    var str_up=window['hq_str_OP_UP_'+self.json.cateId];
    var str_down=window['hq_str_OP_DOWN_'+self.json.cateId];

    var arr_up  = str_up.split(',');
    var arr_down= str_down.split(',');

    script_stock.call(self,str_up+'s_'+self.json.symbole,function(){self.hand_hq_data(arr_up,'.table_up');});  //\u770b\u6da8\u5408\u7ea6
    script_stock.call(self,str_down,function(){self.hand_hq_data(arr_down,'.table_down')}); //\u770b\u8dcc\u5408\u7ea6

    //script_stock.call(self,'CON_ZL_'+self.json.domain_up+',CON_ZL_'+self.json.domain_down,function(){self.r02_table(self.json.domain_up)});//\u4e3b\u529b
    self.r01_table(arr_up[0].substring(7),'up');
    self.five_exchange(arr_up[0]);
    $('#tab_H5').find('li[data-tabKey]').removeClass('selected');
    $('#tab_H5 li').eq(0).addClass('selected');
    HQ_tflash?HQ_tflash.hide(true):null;
    HQ_kflash?HQ_kflash.hide(true):null;
    T_chart(arr_up[0]);
    HQ_kflash=null;
    if(window.HQ_diejia)
    {
      window.HQ_tflash.compare({symbol:B_symbol});

    }
    window.conta_symbol=arr_up[0];//'sh600697';//arr_up[0];

    if(self.duration)
        {
          clearInterval(self.timer);
          clearInterval(self.r_timer);
          self.timer=setInterval(function(){

            script_stock.call(self,str_up+'s_'+self.json.symbole,self.hand_hq_data(arr_up,'.table_up'));  //\u770b\u6da8\u5408\u7ea6
            script_stock.call(self,str_down,self.hand_hq_data(arr_down,'.table_down')); //\u770b\u8dcc\u5408\u7ea6
            script_stock.call(self,'CON_ZL_'+self.json.domain_up+',CON_ZL_'+self.json.domain_down,self.r02_table(self.json.domain_up));//\u4e3b\u529b

          },self.duration);
          self.r_timer=setInterval(function(){

           //self.r01_table(arr_up[0]);//\u9ed8\u8ba4\u7684\u4e94\u6863\u76d8\u53e3,\u4ee5\u53ca\u7b2c\u4e00\u4e2a\u5408\u7ea6

             self.r01_table(arr_up[0].substring(7),'up');
             self.five_exchange(arr_up[0]);
          },self.duration);

        }

   },

   /*****************************************************\u5408\u7ea6\u4e3b\u8868\u683c\u64cd\u4f5c******************************************************/
   /*
           var hq_str_CON_OP_\u4ee3\u7801=\u201c\u4e70\u91cf(0)\uff0c\u4e70\u4ef7\uff0c\u6700\u65b0\u4ef7\uff0c\u5356\u4ef7\uff0c\u5356\u91cf\uff0c\u6301\u4ed3\u91cf\uff0c\u6da8\u5e45\uff0c\u884c\u6743\u4ef7\uff0c\u6628\u6536\u4ef7\uff0c\u5f00\u76d8\u4ef7\uff0c\u6da8\u505c\u4ef7\uff0c\u8dcc\u505c\u4ef7(11),
           \u7533\u5356 \u4ef7\u4e94\uff0c\u7533\u5356\u91cf\u4e94\uff0c\u7533\u5356\u4ef7\u56db\uff0c\u7533\u5356\u91cf\u56db\uff0c\u7533\u5356\u4ef7\u4e09\uff0c\u7533\u5356\u91cf\u4e09\uff0c\u7533\u5356\u4ef7\u4e8c\uff0c\u7533\u5356\u91cf\u4e8c\uff0c\u7533\u5356\u4ef7\u4e00\uff0c\u7533\u5356\u91cf\u4e00\uff0c\u7533\u4e70\u4ef7\u4e00\uff0c\u7533\u4e70\u91cf\u4e00 \uff0c\u7533\u4e70\u4ef7\u4e8c\uff0c\u7533\u4e70\u91cf\u4e8c\uff0c\u7533\u4e70\u4ef7\u4e09\uff0c\u7533\u4e70\u91cf\u4e09\uff0c\u7533\u4e70\u4ef7\u56db\uff0c\u7533\u4e70\u91cf\u56db\uff0c\u7533\u4e70\u4ef7\u4e94\uff0c\u7533\u4e70\u91cf\u4e94\uff0c
           \u884c\u60c5\u65f6\u95f4\uff0c\u4e3b\u529b\u5408\u7ea6\u6807\u8bc6\uff0c\u72b6\u6001\u7801\uff0c \u6807\u7684\u8bc1\u5238\u7c7b\u578b\uff0c\u6807\u7684\u80a1\u7968\uff0c\u671f\u6743\u5408\u7ea6\u7b80\u79f0\uff0c\u632f\u5e45(38)\uff0c\u6700\u9ad8\u4ef7\uff0c\u6700\u4f4e\u4ef7\uff0c\u6210\u4ea4\u91cf\uff0c\u6210\u4ea4\u989d
   */
   hand_hq_data:function(arr,dom){
       var arr0=arr;
       var self=this;
       var dom=dom;

       A_price=window['hq_str_s_'+self.json.symbole].split(',')[1]-0;//\u5bf9\u5e94\u80a1\u7968\u6807\u7684\u4ef7\u683c
       PRE=window['hq_str_sh510050'].split(',')[2]-0;
       function main_tab_l(arr,dom){  //\u4e2d\u95f4\u4e3b\u8868\u683c\u64cd\u4f5c
             var arr=arr;
             var domain_arr=[];
             var str='<tr class="empty"></tr>';
             var str1='<tr class="empty"></tr>';
             for(var i=0;i<arr.length;i++) {
               var json={};

               if(!arr[i]) continue;
               var arr_hy=window['hq_str_'+arr[i]].split(',');
                var abs_XP=Math.abs(arr_hy[7]-PRE).toFixed(4);
                    json.hy_no=arr[i];//
                    json.buy_amount=arr_hy[0];//\u4e70\u91cf
                    json.buy=arr_hy[1];//\u4e70\u4ef7
                    json.curr=arr_hy[2];//\u6700\u65b0\u4ef7
                    json.sell=arr_hy[3];//\u5356\u91cf
                    json.sell_amount=arr_hy[4];//\u5356\u4ef7
                    json.amount=arr_hy[5];//\u6301\u4ed3\u91cf
                    json.amplitude=arr_hy[38]?(arr_hy[38]-0)+'%':'--';//\u632f\u5e45

                    json.change=arr_hy[6]?(arr_hy[6]-0)+'%':'--';//\u6da8\u5e45
                    json.xq=(arr_hy[7]-0).toFixed(4);//\u884c\u6743\u4ef7
                    json.domain_con=arr_hy[33]-0;//\u4e3b\u529b\u6807\u8bc6

                    /*\u5b9e\u503c,\u865a\u503c*/
                    json.shizhi=(A_price-arr_hy[7]);
                    var tr_color = json.shizhi>0?'first':'';
                    var color = arr_hy[6]>0?'tex_up_color':arr_hy[6]<0?'tex_down_color':'';
                    if (arr_hy[43] === 'A'||arr_hy[43] === 'B') {
                        json.xq = json.xq + arr_hy[43];
                    }

                    str1+='<tr><td>'+json.xq+'</td></tr>';
                    if(arr_hy[33]-0)//\u4e3b\u529b
                    {
                      domain_arr.push({no:json.hy_no,abs:abs_XP});
                      str+='<tr class="'+tr_color+'" data-symbole="'+json.hy_no+'" domain=0 data-updown="up"><td>'+json.buy_amount+'</td><td>'+json.buy+'</td><td>'+json.curr+'</td><td>'+json.sell+'</td><td>'+json.sell_amount+'</td><td>'+json.amount+'</td><td class="Tex_right">'+json.amplitude+'</td><td class="Tex_right last-l '+color+'"><div>'+json.change+'<span class="important-l domain" style="display:none;">\u4e3b</span></div></td></tr>';
                      $('#contract_up').attr('data-symbole',json.hy_no);
                       self.json.domain_up=json.hy_no.substring(7);
                    }
                    else
                    str+='<tr class="'+tr_color+'" data-symbole="'+json.hy_no+'" data-updown="up"><td>'+json.buy_amount+'</td><td>'+json.buy+'</td><td>'+json.curr+'</td><td>'+json.sell+'</td><td>'+json.sell_amount+'</td><td>'+json.amount+'</td><td class="Tex_right">'+json.amplitude+'</td><td class="Tex_right last-l '+color+'">'+json.change+'</tr>';
                }
                 $(dom+' tbody').html(str);
                 $('.table_xq tbody').html(str1);
                 domain_arr.sort(function(obj1,obj2){
                     return obj1.abs-obj2.abs;
                 });//\u4e3b\u529b\u6392\u5e8f
                 $(dom+' tbody').find('tr').each(function(index){
                      var sno=$(this).attr('data-symbole');
                      if(sno==domain_arr[0].no)
                      {
                         $(this).attr('domain','1');
                         $(this).find('span.domain').show();
                      }

                 });
                 script_stock.call(self,'CON_ZL_'+self.json.domain_up,function(){self.r02_table(self.json.domain_up)});//\u4e3b\u529b

         }
       function main_tab_r(arr,dom){  //\u4e2d\u95f4\u4e3b\u8868\u683c\u64cd\u4f5c
             var arr=arr;
              var domain_arr=[];
             var str='<tr class="empty"></tr>';
             for(var i=0;i<arr.length;i++) {
               var json={};
               if(!arr[i]) continue;
               var arr_hy=window['hq_str_'+arr[i]].split(',');
                var abs_XP=Math.abs(arr_hy[7]-PRE).toFixed(4);
                    json.hy_no=arr[i];//\u5408\u7ea6\u4ee3\u7801
                    json.buy_amount=arr_hy[0];//\u4e70\u91cf
                    json.buy=arr_hy[1];//\u4e70\u4ef7
                    json.curr=arr_hy[2];//\u6700\u65b0\u4ef7
                    json.sell=arr_hy[3];//\u5356\u91cf
                    json.sell_amount=arr_hy[4];//\u5356\u4ef7
                    json.amount=arr_hy[5];//\u6301\u4ed3\u91cf
                    json.amplitude=arr_hy[38]?(arr_hy[38]-0)+'%':'--';//\u632f\u5e45
                    json.change=arr_hy[6]?arr_hy[6]-0+'%':'--';//\u6da8\u5e45
                    json.xq=(arr_hy[7]-0).toFixed(4);//\u884c\u6743\u4ef7
                    json.domain_con=arr_hy[33]-0;//\u4e3b\u529b\u6807\u8bc6
                    /*\u5b9e\u503c,\u865a\u503c*/
                    json.shizhi=-(A_price-arr_hy[7]);//\u865a\u503c
                    /*\u4e94\u6863\u76d8\u53e3*/

                    /*json.=arr_hy[8];//
                    json.=arr_hy[9];//
                    json.=arr_hy[10];//
                    json.=arr_hy[11];//
                    json.=arr_hy[12];//
                    json.=arr_hy[13];//
                    json.=arr_hy[14];//
                    json.=arr_hy[15];//
                    json.=arr_hy[16];//
                    json.=arr_hy[17];//
                    json.=arr_hy[18];//
                    json.=arr_hy[19];//
                    json.=arr_hy[20];//
                    json.=arr_hy[21];//
                    json.=arr_hy[22];//
                    json.=arr_hy[23];//
                    json.=arr_hy[24];//
                    json.=arr_hy[25];//
                    json.=arr_hy[];//
                    */
                    var tr_color = json.shizhi>0?'first':'';
                     var color = arr_hy[6]>0?'tex_up_color':arr_hy[6]<0?'tex_down_color':'';
                    if(arr_hy[33]-0)//\u4e3b\u529b
                    {
                      domain_arr.push({no:json.hy_no,abs:abs_XP});
                      str+='<tr class="'+tr_color+'" data-symbole="'+json.hy_no+'" domain=0 data-updown="down"><td class="last-l"><div><span class="important-r domain" style="display:none;">\u4e3b</span>'+json.buy_amount+'</div></td><td>'+json.buy+'</td><td>'+json.curr+'</td><td>'+json.sell+'</td><td>'+json.sell_amount+'</td><td>'+json.amount+'</td><td class="Tex_right">'+json.amplitude+'</td><td class="Tex_right last-l '+color+'">'+json.change+'</td></tr>';
                       $('#contract_down').attr('data-symbole',json.hy_no);
                       self.json.domain_down=json.hy_no.substring(7);
                    }
                    else
                    str+='<tr class="'+tr_color+'" data-symbole="'+json.hy_no+'" data-updown="down"><td>'+json.buy_amount+'</td><td>'+json.buy+'</td><td>'+json.curr+'</td><td>'+json.sell+'</td><td>'+json.sell_amount+'</td><td>'+json.amount+'</td><td class="Tex_right">'+json.amplitude+'</td><td class="Tex_right last-l '+color+'">'+json.change+'</tr>';
                 }

                 $(dom+' tbody').html(str);
                 domain_arr.sort(function(obj1,obj2){
                     return obj1.abs-obj2.abs;
                 });
                 $(dom+' tbody').find('tr').each(function(index){
                      var sno=$(this).attr('data-symbole');
                      if(sno==domain_arr[0].no)
                      {
                         $(this).attr('domain','1');
                         $(this).find('span.domain').show();
                      }

                 });
                 script_stock.call(self,'CON_ZL_'+self.json.domain_down,function(){self.r02_table(self.json.domain_down)});//\u4e3b\u529b

         }
         switch(dom)
         {
           case '.table_up':
             main_tab_l(arr0,dom);
             var F_zdf=window['hq_str_s_'+self.json.symbole].split(',')[3]-0;
             var F_color=F_zdf>0?'tex_up_color':F_zdf<0?'tex_down_color':'';
             F_zdf=F_zdf>0?F_zdf='+'+F_zdf.toFixed(2):F_zdf.toFixed(2);
             $('#F50').html((A_price-0).toFixed(4)+' / '+F_zdf+'%');
             $('#F50')[0].className=F_color;
           break;
           case '.table_down':
             main_tab_r(arr0,dom);

           break;
         }
         //script_stock.call(self,'CON_ZL_'+self.json.domain_up+',CON_ZL_'+self.json.domain_down,function(){self.r02_table(self.json.domain_up)});//\u4e3b\u529b

         self.curr_tr?function(){  //\u662f\u5426\u9009\u62e9\u5230
                         $('.table tbody tr').each(function(){
                                var attr=$(this).attr('data-symbole');
                                if(attr==self.curr_tr)
                                {
                                   $(this).addClass('selected');
                                }
                         });

                 }()
                 :function(){
                                  $('.table_up tbody tr').eq(1).addClass('selected');
                 }();

   },

   /********************************************\u4e94\u6863\u76d8\u53e3**************************************************/

   five_exchange:function(symbole){
     var self=this;
     script_stock.call(self,symbole,function(){self.five_exchange_dom(symbole);});
   },
   five_exchange_dom:function(symbole){

      var arr=window['hq_str_'+symbole].split(',');
      var json={};
      var str='';
      var count1=0,count=0;
      var color=arr[6]>0?'tex_up_color':arr[6]<0?'tex_down_color':'';
      var state='',state_no=arr[34].substr(0,1).toUpperCase();
      for(var i=12;i<22;i+=2)
      {
         json.price=arr[i];
         json.amount=arr[i+1];
         str+='<tr><td>\u5356'+(5-count)+'</td><td class="'+color+'">'+json.price+'</td><td class="Tex_r">'+json.amount+'</td></tr>';
         count++;
      }
      for(var j=22;j<32;j+=2)
      {
         json.price=arr[j];
         json.amount=arr[j+1];
         if(j==22)
         str+='<tr class="buy"><td>\u4e70'+(count1+1)+'</td><td class="'+color+'">'+json.price+'</td><td class="Tex_r">'+json.amount+'</td></tr>';
         else
         str+='<tr><td>\u4e70'+(count1+1)+'</td><td class="'+color+'">'+json.price+'</td><td class="Tex_r">'+json.amount+'</td></tr>';
         count1++;
      }
      switch(state_no)
      {
        case 'S':
        state='\u542f\u52a8\uff08\u5f00\u5e02\u524d\uff09\u65f6\u6bb5';
        break;
        case 'C':
        state='\u96c6\u5408\u7ade\u4ef7\u65f6\u6bb5';
        break;
        case 'T':
        state='\u8fde\u7eed\u4ea4\u6613\u65f6\u6bb5';
        break;
        case 'B':
        state='\u4f11\u5e02\u65f6\u6bb5';
        break;
        case 'E':
        state='\u95ed\u5e02\u65f6\u6bb5';
        break;
        case 'V':
        state='\u6ce2\u52a8\u6027\u4e2d\u65ad';
        break;
        case 'P':
        state='\u4e34\u65f6\u505c\u724c';
        break;
        case 'U':
        state='\u6536\u76d8\u96c6\u5408\u7ade\u4ef7';
        break;
      }

       $('.five table tbody').html(str);
       $('.five p.time_state em').html(arr[32]);
         $('.five p.time_state span').html(state);
       $('.five em.PRE').html((arr[8]-0).toFixed(4));
       $('.five em.open').html((arr[9]-0).toFixed(4)||'--');
       $('.five em.zt').html((arr[10]-0).toFixed(4));
       $('.five em.dt').html((arr[11]-0).toFixed(4));
   },
   /**********************************************\u6269\u5c55\u6570\u636e******************************************************/
   r01_table:function(symbole,change){
     var self=this;
     var up_down=change;
     var contract_symbole=symbole;//symbole;//symbole
     if (!contract_symbole) return;
     script_stock.call(self,'CON_SO_'+contract_symbole,function(){self.r01_table_dom(contract_symbole,up_down);});
   },
   r01_table_dom:function(symbole,change){
           var contract_symbole=symbole;

           var arr=window['hq_str_CON_SO_'+contract_symbole].split(',');
           var json={};
           var str='';
           var up_down=change;
           var price_mark=(A_price-arr[13]).toFixed(4);
           json.symbole=contract_symbole;
           json.xq=arr[1];

           switch(up_down)
           {
             case 'up':
             json.price=price_mark>0?'实值':price_mark<0?'虚值':'平值';
             json.in_price=price_mark>0?price_mark:0;
             json.out_price=(arr[14]-json.in_price<=0)?'0.00':(arr[14]-json.in_price).toFixed(4);
             break;
             case 'down':
             json.price=price_mark<0?'实值':price_mark>0?'虚值':'平值';
             json.in_price=price_mark<0?-price_mark:0;
             json.out_price=(arr[14]-json.in_price).toFixed(4);
             break;
           }

          /* json.price=arr[1];                  //\u4ef7\u503c--\u5b9e\u503c\uff0c\u865a\u503c
           json.in_price=arr[2];               //\u5185\u5728\u4ef7\u503c
           json.out_price=arr[3]-0;             // \u65f6\u95f4\u4ef7\u503c*/
           json.amount=arr[4]-0||'--';                 //\u6210\u4ea4\u91cf
           json.Delta=arr[5]-0||'--';
           json.Gamma=arr[6]-0||'--';
           json.Theta=arr[7]-0||'--';
           json.Vega=arr[8]-0||'--';
           json.volatility=arr[9]?arr[9]-0:'--';            //\u9690\u542b\u6ce2\u52a8\u7387
           json.up_price=arr[10]-0||'--';               //\u6700\u9ad8\u4ef7
           json.low_price=arr[11]-0||'--';              //\u6700\u4f4e\u4ef7
           json.theoretical=arr[15]-0||'--';
           var color=(json.price=='\u5b9e\u503c')?'tex_up_color':'';
           str='<tr><td>\u4ea4\u6613\u4ee3\u7801</td><td>'+arr[12]+'</td></tr><tr><td>\u7406\u8bba\u4ef7\u503c</td><td>'+json.theoretical+'</td></tr><tr><td>\u4ef7\u503c\u72b6\u6001</td><td class="'+color+'">'+json.price+'</td></tr><tr><td class="Tex_r price">\u5185\u5728\u4ef7\u503c</td><td class="'+color+'">'+json.in_price.toFixed(4)+'</td></tr><tr><td class="Tex_r price">\u65f6\u95f4\u4ef7\u503c</td><td class="'+color+'">'+json.out_price+'</td></tr><tr><td>\u6210\u4ea4\u91cf</td><td>'+json.amount+'</td></tr><tr><td>Delta</td><td>'+json.Delta+'</td></tr><tr><td>Gamma</td><td>'+json.Gamma+'</td></tr><tr><td>Theta</td><td>'+json.Theta+'</td></tr><tr><td>Vega</td><td>'+json.Vega+'</td></tr><tr><td>\u9690\u542b\u6ce2\u52a8\u7387</td><td>'+json.volatility+'</td></tr><tr><td>\u6700\u9ad8\u4ef7</td><td>'+json.up_price+'</td></tr><tr><td>\u6700\u4f4e\u4ef7</td><td>'+json.low_price+'</td></tr>';
           $('.expend tbody').html(str);
           $('.five h4 span').html(arr[0]);

           $('.heyuefenxi_name').text(arr[0]);

   },
/**********************************************************\u4e3b\u529b****************************************************************/
   r02_table:function(){

     var self=this;
     function dom(arr,dom,str_stock){
       var json={};
       var str='';
           json.names=arr[0];
           json.symbole=str_stock;//\u5408\u7ea6\u4ee3\u7801
           var price_mark=(A_price-arr[19]).toFixed(4);
           switch(dom)
           {
             case '#tab_up':
             json.price=price_mark>0?'\u5b9e\u503c':price_mark<0?'\u865a\u503c':'\u5e73\u503c';
             json.in_price=price_mark>0?price_mark:0;
             json.out_price=(arr[6]-json.in_price<=0)?'0.00':(arr[6]-json.in_price).toFixed(4);
             break;
             case '#tab_down':
             json.price=price_mark<0?'\u5b9e\u503c':price_mark>0?'\u865a\u503c':'\u5e73\u503c';
             json.in_price=price_mark<0?-price_mark:0;
             json.out_price=(arr[6]-json.in_price).toFixed(4);
             break;
           }

/*
           json.price=arr[1];                  //\u4ef7\u503c--\u5b9e\u503c\uff0c\u865a\u503c
           json.in_price=arr[2]-0;               //\u5185\u5728\u4ef7\u503c
           json.out_price=arr[3]-0;             // \u65f6\u95f4\u4ef7\u503c*/
           json.amount=arr[12];                 //\u6210\u4ea4\u91cf
           json.Delta=arr[13];
           json.Gamma=arr[14]-0;
           json.Theta=arr[15];
           json.Vega=arr[16];
           json.volatility=arr[17];            //\u9690\u542b\u6ce2\u52a8\u7387
           json.up_price=arr[10];               //\u6700\u9ad8\u4ef7
           json.low_price=arr[11];              //\u6700\u4f4e\u4ef7
           json.hold=arr[4];                    //\u6301\u4ed3\u91cf
           json.percent=arr[5];                //\u5360\u6bd4
           json.buy=arr[8];                    //\u4e70\u4ef7
           json.sell=arr[9];                    //\u5356\u4ef7
           json.curr=arr[6];                  //\u6700\u65b0\u4ef7
           json.change=arr[7];                //\u6da8\u5e45
           json.theoretical=arr[20];
           var color=(json.price=='\u5b9e\u503c')?'tex_up_color':'';
           str+='<tr class="head"><th width="115px"></th><th></th></tr><tr><td>\u5408\u7ea6\u7b80\u79f0</td><td>'+json.names+'</td></tr><tr><td>\u4ea4\u6613\u4ee3\u7801</td><td>'+arr[18]+'</td></tr><tr><td>\u7406\u8bba\u4ef7\u503c</td><td>'+json.theoretical+'</td></tr><tr><td>\u4ef7\u503c\u72b6\u6001</td><td class="'+color+'">'+json.price+'</td></tr><tr><td class="Tex_r price">\u5185\u5728\u4ef7\u503c</td><td class="'+color+'">'+json.in_price.toFixed(4)+'</td></tr><tr><td class="Tex_r price">\u65f6\u95f4\u4ef7\u503c</td> <td class="'+color+'">'+json.out_price+'</td></tr><tr><td>\u6301\u4ed3\u91cf / \u5360\u3000\u6bd4</td><td>'+json.hold+' / '+json.percent+'</td></tr><tr><td>\u6700\u65b0\u4ef7 / \u6da8\u3000\u5e45</td><td>'+json.curr+' / '+json.change+'</td></tr><tr><td>\u4e70\u3000\u4ef7 / \u5356\u3000\u4ef7</td><td>'+json.buy+' / '+json.sell+'</td></tr><tr><td>\u6700\u9ad8\u4ef7 / \u6700\u4f4e\u4ef7</td><td>'+json.up_price+' / '+json.low_price+'</td></tr><tr><td>\u6210\u4ea4\u91cf</td><td>'+json.amount+'</td></tr><tr><td>Delta</td><td>'+json.Delta+'</td><tr><td>Gamma</td><td>'+json.Gamma+'</td></tr><tr><td>Theta</td><td>'+json.Theta+'</td></tr><tr><td>Vega</td><td>'+json.Vega+'</td></tr><tr><td>\u9690\u542b\u6ce2\u52a8\u7387</td><td>'+json.volatility+'</td></tr>';
           $(dom).html(str);

     }
     if(window['hq_str_CON_ZL_'+this.json.domain_up])
     {
      var arr_up=window['hq_str_CON_ZL_'+this.json.domain_up].split(',');
      dom(arr_up,'#tab_up',this.json.domain_up);
     }
    if(window['hq_str_CON_ZL_'+this.json.domain_down])
    {
      var arr_down=window['hq_str_CON_ZL_'+this.json.domain_down].split(',');
      dom(arr_down,'#tab_down',this.json.domain_down);
    }

   },

   /*********************************************\u70b9\u51fb\u53f3\u4fa7\u4e3b\u529b*********************************************************************/
   click_domain:function(){
      var self=this;
      var top=$('.search').offset().top;
       $('.contract_tab span').on('click',function(){
           var cate=$(this)[0].id;
           var contract_symbole=$(this).attr('data-symbole');
           self.curr_tr=$(this).attr('data-symbole');

           $('.table table tbody tr').removeClass('selected');
           switch(cate)
           {
             case 'contract_up':
               $('.table_up tbody').find('tr[domain="1"]').addClass('selected');
             break;
             case 'contract_down':
               $('.table_down tbody').find('tr[domain="1"]').addClass('selected');
             break;
           }
            document.body.scrollTop?document.body.scrollTop=top:document.documentElement.scrollTop=top;
       });
   },
   /***************************************************************\u70b9\u51fb\u4e3b\u8868\u884c*******************************************************************/
   click_tr:function(){
         var self=this;
         $('.table_up table tbody,.table_down table tbody').on('click',function(event){
             var $target=$(event.target);
             if(/empty/g.test($target[0].className))

             {
               return;
             }
             while(!$target.is('tr'))
             {
                if ($target.get(0) == document.body)
                {

                    return;
                }

                $target = $(($target)[0].parentNode);
             }

              $('.table table tbody').find('tr').removeClass('selected');
              $target.addClass('selected');
              var contract_symbole=$target.attr('data-symbole')||'';
              self.curr_tr=$target.attr('data-symbole');
              var up_down=$target.attr('data-updown');
              self.five_exchange(contract_symbole);
              self.r01_table(contract_symbole.substring(7),up_down);
              HQ_kflash?HQ_kflash.hide():null;
              window.HQ_tflash.newSymbol({symbol:contract_symbole,datas:_datas,nfloat:4});
              window.HQ_kflash?window.HQ_kflash.newSymbol({symbol:contract_symbole,datas:_datas,nfloat:4}):null;
              HQ_tflash.showView('ts');
              HQ_tflash.show();
              if(window.HQ_diejia)
              {
                window.HQ_tflash.compare({symbol:B_symbol});
                window.HQ_kflash?window.HQ_kflash.compare({symbol:B_symbol}):null;
              }
              $('#tab_H5').find('li[data-tabKey]').removeClass('selected');
              $('#tab_H5 li').eq(0).addClass('selected');
              window.conta_symbol=contract_symbole;
              var hq_offset=$('.main_hq').offset().top-0;
               if(document.documentElement.scrollTop+document.body.scrollTop<hq_offset)
               {
                 scrollTo();
               }

              if(self.duration)
              {
                clearInterval(self.r_timer);
                self.r_timer=setInterval(function(){
                self.r01_table(contract_symbole.substring(7),up_down);//\u5408\u7ea6
                self.five_exchange(contract_symbole);
                 },self.duration);
              }
              return false;
         });
   }
};
 var h5Test=document.createElement('canvas');
 if(h5Test.getContext && h5Test.getContext('2d'))$('.tip_ie').hide();
