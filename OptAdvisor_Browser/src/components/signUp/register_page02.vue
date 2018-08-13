<template>
  <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="0">
    <FormItem prop="question01">
      <label>1.	您的主要收入来源是：</label><br>
      <RadioGroup v-model="formValidate.question01">
        <Radio label="A">A. 工资、劳务报酬</Radio><br>
        <Radio label="B">B. 生产经营所得</Radio><br>
        <Radio label="C">C. 利息、股息、转让证券等金融性资产收入</Radio><br>
        <Radio label="D">D. 出租、出售房地产等非金融性资产收入</Radio><br>
        <Radio label="E">E. 无固定经济来源或个人或家庭人均收入低于当地城乡居民最低生活保障标准</Radio><br>
      </RadioGroup>
    </FormItem>
    <FormItem prop="question02">
      <label>2.	最近您家庭预计进行证券或者金融衍生品投资的资金占家庭现有总资产（不含自主、自用房产及汽车等固定资产）的比例是：</label><br>
      <RadioGroup v-model="formValidate.question02">
        <Radio label="A">A. 70%以上</Radio><br>
        <Radio label="B">B. 50%-70%</Radio><br>
        <Radio label="C">C. 30%-50%</Radio><br>
        <Radio label="D">D. 10%-30%</Radio><br>
        <Radio label="E">E. 10%以下</Radio><br>
      </RadioGroup>
    </FormItem>
    <FormItem prop="question03">
      <label>3.	您是否有尚未清偿的数额较大的债务，如有，其性质是：</label><br>
      <RadioGroup v-model="formValidate.question03">
        <Radio label="A">A. 没有</Radio><br>
        <Radio label="B">B. 有，住房抵押贷款等长期定额债务</Radio><br>
        <Radio label="C">C. 有，信用卡欠款、消费信贷等短期信用债务</Radio><br>
        <Radio label="D">D. 有，亲朋之间借款</Radio><br>
      </RadioGroup>
    </FormItem>
    <FormItem prop="question04">
      <label>4.	您的投资经验可以被概括为：</label><br>
      <RadioGroup v-model="formValidate.question04">
        <Radio label="A">A. 一般，尝试过衍生品交易但经验有限，需要进一步指导</Radio><br>
        <Radio label="B">B. 丰富，参与过权证、期货或创业板等高风险产品的交易，倾向于自己做出投资决策</Radio><br>
        <Radio label="C">C. 非常丰富，能够运用期权的多种策略，有数年进行期权交易的经验</Radio><br>
      </RadioGroup>
    </FormItem>
    <FormItem prop="question05">
      <label>5.	有一位投资者一个月内做了15笔交易（同一品种买卖各一次算一笔），您认为这样的交易频率：</label><br>
      <RadioGroup v-model="formValidate.question05">
        <Radio label="A">A. 太高了</Radio><br>
        <Radio label="B">B. 偏高</Radio><br>
        <Radio label="C">C. 正常</Radio><br>
        <Radio label="D">D. 偏低</Radio><br>
      </RadioGroup>
    </FormItem>
    <FormItem prop="question06">
      <label>6.	您过去的投资行为中重点投资于哪些种类的投资品种：</label><br>
      <label>①低风险产品或服务中的一项或多项。包括但不限于债券质押式报价回购业务、债券质押式逆回购业务、本金保障型收益凭证、低风险金融产品、低风险投资顾问服务产品等</label><br>
      <label>②中低风险产品或服务中的一项或多项。包括但不限于中低风险金融产品、中低风险投资顾问服务产品、网下申购服务等</label><br>
      <label>③中风险产品或服务中的一项或多项。包括但不限于股票交易、创业板股票交易、优先股交易、股票期权备兑开仓业务、股票期权保护性认沽开仓业务、贵金属现货实盘合约业务、中风险金融产品、中风险投资顾问服务产品等</label><br>
      <label>④中高风险产品或服务中的一项或多项。包括但不限于退市整理期股票交易、港股通股票(包括沪港通下的港股通和深港通下的港股通)交易、股票质押式回购(融入方)、约定购回式证券交易(融入方)、风险警示股票交易、融资融券业务、转融通证券出借业务、个股期权买入开仓业务、股票期权保证金卖出开仓业务、权证、分级基金子份额买入及基础份额分拆、全国中小企业股份转让系统(挂牌公司股票、两网退市股票、优先股)、优先股转让、债券市场合格投资者权限开通、暂停_上市提供转让服务债券合格投资者开通、债券基金类产品质押式回购交易(正回购)、贵金属现货延期交易业务、中高风险金融产品、中高风险投资顾问服务产品等</label><br>
      <label>⑤高风险产品或服务中的一项或多项。包括但不限于场外衍生产品及相关服务、复杂或高风险金融产品、高风险投资顾问服务产品等</label><br>
      <RadioGroup v-model="formValidate.question06">
        <Radio label="A">A. ①</Radio><br>
        <Radio label="B">B. ①②</Radio><br>
        <Radio label="C">C. ①②③</Radio><br>
        <Radio label="D">D. ①②③④</Radio><br>
        <Radio label="E">E. ①②③④⑤</Radio><br>
      </RadioGroup>
    </FormItem>
  </Form>
</template>

<script>
    export default {
        name: "register_page02",
      data () {
        return {
          formValidate: {
            question01:'',
            question02:'',
            question03:'',
            question04:'',
            question05:'',
            question06:'',
          },
          ruleValidate: {
            question01: [
              { required: true, message: '该题未作答', trigger: 'change' }
            ],
            question02: [
              { required: true, message: '该题未作答', trigger: 'change' }
            ],
            question03: [
              { required: true, message: '该题未作答', trigger: 'change' }
            ],
            question04: [
              { required: true, message: '该题未作答', trigger: 'change' }
            ],
            question05: [
              { required: true, message: '该题未作答', trigger: 'change' }
            ],
            question06: [
              { required: true, message: '该题未作答', trigger: 'change' }
            ]
          }
        }
      },
    }
</script>

<style scoped>
  label{
    font-size: larger;
  }
  Form{
    padding-left: 50px;
    padding-right: 50px;
  }
</style>
